package uk.ac.ebi.ddi.dbgap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

public class DbgapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbgapService.class);
    Set<String> omicsType = new HashSet<>();

    @Autowired
    DatasetService datasetService;

    public void saveEntries(String filePath) throws IOException {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        parseXMLDbgap(targetStream);
    }

    private void parseXMLDbgap(InputStream targetStream) {
        try (InputStream is = targetStream) {
            Document document = Jsoup.parse(is, "UTF-8", "");

            Elements studies = document.getElementsByTag("study");
            Element study = studies.first();
            if (study != null) {
                saveStudy(study);
            }
        } catch (IOException e) {
            LOGGER.error("Caught IO Exception parsing document : {}", e.getMessage(), e);
        }
    }

    private void saveStudy(Element study) {
        Dataset dataset = new Dataset();

        String acc = study.attr(DSField.ACCESSION.getName());
        acc = acc.substring(0, acc.indexOf('.'));

        dataset.setAccession(acc);
        dataset.setDatabase("dbGaP");
        dataset.setCurrentStatus(DatasetCategory.INSERTED.getType());
        Map<String, Set<String>> dates = new HashMap<>();
        dates.put(DSField.Date.CREATION.getName(), new HashSet<>(Arrays.asList(study.attr("createDate"))));
        dates.put("last_modification", new HashSet<>(Arrays.asList(study.attr("modDate"))));
        dataset.setDates(dates);

        String parentStudy = study.attr("parentStudy");
        parentStudy = parentStudy.substring(0, parentStudy.indexOf('.'));
        if (StringUtils.isNotBlank(parentStudy) && !parentStudy.equals(acc)) {
            dataset.addCrossReferences("DBGAP", new HashSet<>(Arrays.asList(study.attr("parentStudy"))));
        }

        dataset.addAdditional("omics_type", omicsType);
        dataset.addAdditional("full_dataset_link",  new HashSet<>(Arrays.asList("https://www.ncbi.nlm.nih.gov/projects/gap/cgi-bin/study.cgi?study_id=" + acc)));
        dataset.addAdditional("repository", new HashSet<>(Arrays.asList("dbGaP")));

        Element configuration = getFirstElementByTagName(study, "configuration");
        if (configuration != null) {
            // Two name fields - the one from the summary document (StudyNameEntrez) and one from the
            // main report page (StudyNameReportPage)
            dataset.setName(getFirstElementText(configuration, "StudyNameEntrez"));
            // StudyNameReportPage may contain raw HTML
            Element nameSynonyms = getFirstElementByTagName(configuration, "StudyNameReportPage");
            if (nameSynonyms != null) {
                dataset.addAdditional(DSField.Additional.ENRICH_TITLE.getName(), new HashSet<>(Arrays.asList(nameSynonyms.html())));
            }
            dataset.setDescription(getFirstElementText(configuration, "description"));
            dataset.addAdditional("study_inc_exc", new HashSet<>(Arrays.asList(getFirstElementText(configuration, "StudyInEx"))));
            dataset.addAdditional("study_history", new HashSet<>(Arrays.asList(getFirstElementText(configuration, "StudyHistory"))));
            addStudyTypes(getFirstElementByTagName(configuration, "StudyTypes"),dataset);
            addPublications(getFirstElementByTagName(configuration, "Publications"),dataset);

            addDiseases(getFirstElementByTagName(configuration, "Diseases"),dataset);
            addAttributions(getFirstElementByTagName(configuration, "Attributions"),dataset);
            addStudyUrls(getFirstElementByTagName(configuration, "StudyURLs"),dataset);
        }
        updateDataset(dataset);
    }

    public void updateDataset(Dataset dataset) {

        try {
            Dataset inDataset = datasetService.
                    read(dataset.getAccession(), dataset.getDatabase());
            if (inDataset != null) {
                datasetService.update(inDataset.getId(), dataset);
            } else {
                datasetService.save(dataset);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error("exception while saving dataset {}", exception.getMessage());
        }
    }

    private void addStudyUrls(Element studyUrls, Dataset dataset) {
        if (studyUrls != null) {
            studyUrls.getElementsByTag("Url").forEach(el ->
                dataset.addAdditionalField(el.attr("name"),el.attr("url"))
            );
        }
    }

    private void addAttributions(Element attributions, Dataset dataset) {
        if (attributions != null) {
            HashSet<String> attributionSet = new HashSet<>();
            for (Element header : attributions.getElementsByTag("Header")) {
                String title = header.attr("title");
                String attName = getFirstElementText(header, "AttName");
                String institution = getFirstElementText(header, "Institution");
                attributionSet.add(title +
                        (attName == null ? "" : " - " + attName) +
                        (institution == null ? "" : " - " + institution));
            }
            if(!CollectionUtils.isEmpty(attributionSet)){
                dataset.addAdditional("attribution" , attributionSet);
            }
        }
    }

    private void addDiseases(Element diseases, Dataset  dataset) {
        if (diseases != null) {
            for (Element disease : diseases.getElementsByTag("Disease")) {
                String sourceDb = disease.attr("vocab_source");
                String term = disease.attr("vocab_term");
                if (StringUtils.isNotBlank(sourceDb) && StringUtils.isNotBlank(term)) {
                    dataset.addCrossReferences(sourceDb,  new HashSet<>(Arrays.asList(term)));
                }
            }
        }
    }

    private void addPublications(Element publications,Dataset dataset) {
        if (publications != null) {
            // Publication may contain Journal details instead of Pubmed refs - need to ignore these
            HashSet<String> pubMedSet = new HashSet<>();
            publications.getElementsByTag("Publication").stream()
                    .map(el -> getFirstElementByTagName(el, "Pubmed"))
                    .filter(Objects::nonNull)
                    .map(pubmed -> pubmed.attr("pmid"))
                    .forEach(pubMedSet::add);
            if(!CollectionUtils.isEmpty(pubMedSet)){
                dataset.addCrossReferences("PMID",pubMedSet);
            }
        }
    }
    private void addStudyTypes(Element studyTypes, Dataset dataset) {
        if (studyTypes != null) {
            HashSet<String> studyTypeSet = new HashSet<>();
            studyTypes.getElementsByTag("StudyType").stream()
                    .map(Element::text)
                    .forEach(studyTypeSet::add);

            if(!CollectionUtils.isEmpty(studyTypeSet)){
                dataset.addAdditional("study_type",studyTypeSet);
            }
        }
    }

    private Element getFirstElementByTagName(Element element, String tag) {
        Elements elements = element.getElementsByTag(tag);
        return elements.first();
    }

    private String getFirstElementText(Element parent, String tag) {
        Element element = getFirstElementByTagName(parent, tag);
        if (element != null) {
            return element.text();
        }
        return null;
    }

}
