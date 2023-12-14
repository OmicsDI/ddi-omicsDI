package uk.ac.ebi.ddi.biostudies;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ddi.biostudies.model.DocAttribute;
import uk.ac.ebi.ddi.biostudies.model.DocLink;
import uk.ac.ebi.ddi.biostudies.model.DocSubmission;
import uk.ac.ebi.ddi.biostudies.model.Submissions;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BioStudiesCoreService {

    public static final String SUBMISSIONS = "submissions";
    public static final String TITLE = "Title";
    public static final String ABSTRACT = "Abstract";
    public static final String EXPERIMENTTYPE = "Experiment type";
    public static final String ORGANISM = "Organism";
    public static final String PUBLICATION = "ReleaseDate";
    public static final String DESCRIPTION = "Description";
    //public static final String

    @Autowired
    DatasetService datasetService;

    Set<String> omicsType = new HashSet<String>();

    public static final Logger LOGGER = LoggerFactory.getLogger(BioStudiesCoreService.class);




    public void saveStudies(String filePath, String repository, String omics_type) throws IOException {
        omicsType.add(omics_type);
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        parseJsonBioStudies(targetStream, repository, omicsType);
    }

    private void parseJsonBioStudies(InputStream is, String repository, Set<String> omicsType) throws IOException {

        List<DocSubmission> submissionsList = new LinkedList<DocSubmission>();
        // Create and configure an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Create a JsonParser instance
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String line;
            while ((line = br.readLine()) != null) {
                final DocSubmission submission = objectMapper.readValue(line, DocSubmission.class);
                saveEntry(submission, repository, omicsType);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }


    private void saveEntry(DocSubmission submission, String repository, Set<String> omicsType){
        if (submission.getSection().getType().equals("Study")) {
            Dataset dataset = transformSubmissionDataset(submission, repository, omicsType);
            if (!dataset.getAdditional().containsKey("additional_accession")) {
                updateDataset(dataset);
            } else {
                String accession = dataset.getAdditional().get("additional_accession")
                        .iterator().next();
                List<Dataset> datasetList = datasetService.findByAccession(accession);
                if (datasetList.size() > 0) {
                    Dataset dataset1 =  datasetList.get(0);
                    if (dataset1.getAdditional().containsKey("additional_accession")) {
                        dataset1.getAdditional().get("additional_accession")
                                .add(dataset.getAccession());
                        updateDataset(dataset1);
                    }
                } else {
                    updateDataset(dataset);
                }
            }
        }
    }

    public Dataset transformSubmissionDataset(DocSubmission submissions, String repository, Set<String> omicsType) {
        Dataset dataset = new Dataset();
        try {
            HashSet<String> datasetLink = new HashSet<String>();
            Map<String, String> subsections = null;
            datasetLink.add("https://www.ebi.ac.uk/biostudies/studies/" + submissions.getAccNo());
            HashSet<String> authors = new HashSet<String>();
            dataset.setAccession(submissions.getAccNo());
            dataset.setDatabase("biostudies-literature");
            dataset.setCurrentStatus(DatasetCategory.INSERTED.getType());
            dataset.addAdditional("omics_type", omicsType);
            dataset.addAdditional("full_dataset_link", datasetLink);
            Map<String, String> sectionMap = submissions.getSection().getAttributes() != null ?
                    submissions.getSection().getAttributes().stream().filter(attribute -> attribute.getName() != null && attribute.getValue() != null)
                            .collect(Collectors.toMap(DocAttribute::getName, DocAttribute::getValue, (a1, a2) -> a1)) : null;
            Map<String, String> attributesMap = submissions.getAttributes() != null ?
                    submissions.getAttributes().stream().filter(attribute -> attribute.getName() != null && attribute.getValue() != null)
                            .collect(Collectors.toMap(DocAttribute::getName, DocAttribute::getValue, (a1, a2) -> a1)) : null;

            Map<String, String> linksMap = null;
            List<DocLink> linksList = submissions.getSection().getLinks();
            if (submissions.getSection() != null && submissions.getSection().getSections() != null) {
                subsections = submissions.getSection().getSections()
                        .stream().filter(r -> r.getType() != null && r.getType().equals("Author")).map(r -> r.getAttributes())
                        .flatMap(x -> x.stream()).filter(attribute -> attribute.getName() != null && attribute.getValue() != null).collect(Collectors.toMap(DocAttribute::getName,
                                DocAttribute::getValue, (a1, a2) -> a1));
                authors.add(subsections.get("Name"));
                dataset.addAdditional("submitter", authors);
            }
            if (linksList != null && linksList.size() > 0 && linksList.get(0).getAttributes() != null) {
                linksMap = linksList.stream().filter(l -> l.getAttributes() != null && l.getAttributes().size() > 0).map(DocLink::getAttributes)
                        .flatMap(x -> x.stream()).filter(attribute -> attribute.getName() != null && attribute.getValue() != null).collect(Collectors.toMap(DocAttribute::getName,
                                DocAttribute::getValue, (a1, a2) -> a1));
            }
            if (linksMap != null && linksMap.containsKey("Type")) {
                String accession = linksList.get(0).getUrl();
                HashSet<String> setSecAcc = new HashSet<String>();
                setSecAcc.add(accession);
                dataset.addAdditional("additional_accession", setSecAcc);
            }
            if (sectionMap != null && sectionMap.containsKey(ABSTRACT.toString())) {
                dataset.setDescription(sectionMap.get(ABSTRACT.toString()));
            } else if (sectionMap != null && sectionMap.containsKey(DESCRIPTION.toString())) {
                dataset.setDescription(sectionMap.get(DESCRIPTION.toString()));
            }

            //Based on request from biostudies team
            if (submissions.getTitle() != null && !submissions.getTitle().isEmpty()) {
                dataset.setName(submissions.getTitle());
            } else if (attributesMap != null && attributesMap.containsKey(TITLE.toString())) {
                dataset.setName(attributesMap.get(TITLE.toString()));
            } else if (sectionMap != null && sectionMap.containsKey(TITLE.toString())) {
                dataset.setName(sectionMap.get(TITLE.toString()));
            }

            Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
            HashSet<String> setData = new HashSet<String>();
            HashSet<String> setOrganisms = new HashSet<String>();
            if (attributesMap != null && attributesMap.containsKey(PUBLICATION.toString())) {
                setData.add(attributesMap.get(PUBLICATION.toString()));
            }
            //dates.put()
            dates.put("publication", setData);
            dataset.addAdditional("repository", new HashSet<>(Arrays.asList(repository)));
            dataset.setDates(dates);
            //setData.clear();
            if (sectionMap != null && sectionMap.containsKey(ORGANISM.toString())) {
                setOrganisms.add(sectionMap.get(ORGANISM.toString()));
                dataset.getAdditional().put("species", setOrganisms);
            }
        } catch (Exception ex) {
            LOGGER.error("exception while parsing submission into dataset in transformsubmission", ex.getMessage());
        }
        return dataset;
    }



    private void updateDataset(Dataset dataset) {
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
            LOGGER.error("exception while saving dataset", exception.getMessage());
        }
    }
}
