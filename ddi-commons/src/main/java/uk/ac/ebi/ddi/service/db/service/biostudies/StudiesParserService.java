package uk.ac.ebi.ddi.service.db.service.biostudies;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.biostudies.Attributes;
import uk.ac.ebi.ddi.service.db.model.biostudies.Links;
import uk.ac.ebi.ddi.service.db.model.biostudies.Submissions;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableMongoRepositories
public class StudiesParserService implements IBioStudiesParserService{


    public static final String SUBMISSIONS = "submissions";
    public static final String TITLE = "Title";
    public static final String ABSTRACT = "Abstract";
    public static final String EXPERIMENTTYPE = "Experiment type";
    public static final String ORGANISM = "Organism";
    public static final String PUBLICATION = "ReleaseDate";
    public static final String BIOSTUDIES_DATABASE = "BioStudies";
    public static final String BIOSTUDIES_DATASET_URL = "https://www.ebi.ac.uk/biostudies/studies/";


    //public static final String

    @Autowired
    DatasetService datasetService;

    Set<String> omicsType = new HashSet<String>();


    public void parseJson(InputStream is) throws IOException {

        List<Submissions> submissionsList = new LinkedList<Submissions>();
        // Create and configure an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Create a JsonParser instance
        try (JsonParser jsonParser = mapper.getFactory().createParser(is)) {
            JsonToken jsonToken = jsonParser.nextToken();

            while (jsonToken != JsonToken.END_OBJECT) {
                if (jsonToken == JsonToken.FIELD_NAME && SUBMISSIONS.equals(jsonParser.getCurrentName())) {


                    jsonToken = jsonParser.nextToken();

                    if (jsonToken == JsonToken.START_ARRAY) {
                        jsonToken = jsonParser.nextToken();
                    }
                    // Iterate over the tokens until the end of the array
                    while (jsonToken != JsonToken.END_ARRAY) {
                        // Read a contact instance using ObjectMapper and do something with it
                        Submissions submissions = mapper.readValue(jsonParser, Submissions.class);

                        if (submissions.getSection().getType().equals("Study")) {
                            Dataset dataset = transformSubmissionDataset(submissions);
                            if (!dataset.getAdditional().containsKey("additional_accession")) {
                                datasetService.save(dataset);
                            } else {
                                String accession = dataset.getAdditional()
                                        .get(DSField.Additional.ADDITIONAL_ACCESSION.key())
                                        .iterator().next();
                                List<Dataset> datasetList = datasetService.findByAccession(accession);
                                if (datasetList.size() > 0) {
                                    Dataset dataset1 =  datasetList.get(0);
                                    if (dataset1.getAdditional().containsKey(DSField.Additional.ADDITIONAL_ACCESSION.key())) {
                                        dataset1.getAdditional().get(DSField.Additional.ADDITIONAL_ACCESSION.key())
                                                .add(dataset.getAccession());
                                        datasetService.save(dataset1);
                                    }
                                } else {
                                    datasetService.save(dataset);
                                }
                            }
                            submissions.toString();
                        }
                        submissionsList.add(submissions);
                        jsonToken = jsonParser.nextToken();
                    }
                }
                jsonToken = jsonParser.nextToken();
            }
        }
    }

    /*public static void main(String[] args) throws IOException {
        File initialFile = new File("/media/gaur/Elements/biostudies/publicOnlyStudies.json");
        InputStream targetStream = new FileInputStream(initialFile);

        StudiesParserService studiesParser = new StudiesParserService();
        studiesParser.parseJson(targetStream);
    }*/

    public void saveStudies() throws IOException {
        omicsType.add("Unknown");
        File initialFile = new File("/media/gaur/Elements/biostudies/publicOnlyStudies.json");
        //File initialFile = new File("/media/gaur/Elements/teststudies.json");
        InputStream targetStream = new FileInputStream(initialFile);
        parseJson(targetStream);
        //StudiesParserService studiesParser = new StudiesParserService();
        //studiesParser.parseJson(targetStream);
    }
    public Dataset transformSubmissionDataset(Submissions submissions) {
        HashSet<String> datasetLink = new HashSet<String>();
        Map<String, String> subsections = null;
        datasetLink.add(BIOSTUDIES_DATASET_URL + submissions.getAccno());
        HashSet<String> authors = new HashSet<String>();
        Dataset dataset = new Dataset();
        dataset.setAccession(submissions.getAccno());
        dataset.setDatabase(BIOSTUDIES_DATABASE);
        dataset.setCurrentStatus(DatasetCategory.INSERTED.getType());
        dataset.addAdditional(DSField.Additional.OMICS.key(), omicsType);
        dataset.addAdditional(DSField.Additional.LINK.key(),datasetLink);
        Map<String, String> sectionMap = submissions.getSection().getAttributes().stream()
                .collect(Collectors.toMap(Attributes::getName, Attributes::getValue, (a1, a2) -> a1));
        Map<String, String> attributesMap = submissions.getAttributes().stream()
                .collect(Collectors.toMap(Attributes::getName, Attributes::getValue, (a1, a2) -> a1));
        Map<String, String> linksMap = null;
        List<Links> linksList = submissions.getSection().getLinks();
        if(submissions.getSection().getSubsections() != null) {
            subsections = submissions.getSection().getSubsections()
                    .stream().filter(r -> r.getType().equals("Author")).map(r -> r.getAttributes())
                    .flatMap(x -> x.stream()).collect(Collectors.toMap(Attributes::getName,
                            Attributes::getValue, (a1, a2) -> a1));
            authors.add(subsections.get("Name"));
            dataset.addAdditional(DSField.Additional.SUBMITTER.key(), authors);
        }

        if (linksList != null && linksList.size() > 0 && linksList.get(0).getAttributes() != null) {
            linksMap = linksList.stream().map(r -> r.getAttributes())
                    .flatMap(x -> x.stream()).collect(Collectors.toMap(Attributes::getName,
                            Attributes::getValue, (a1, a2) -> a1 ));

        }
        //.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(countInFirstMap, countInSecondMap)
        // -> countInFirstMap + countInSecondMap)));

        if (linksMap != null && linksMap.containsKey("Type")) {
            String accession = linksList.get(0).getUrl();
            HashSet<String> setSecAcc = new HashSet<String>();
            setSecAcc.add(accession);
            dataset.addAdditional(DSField.Additional.ADDITIONAL_ACCESSION.key(), setSecAcc);
        }

        dataset.setDescription(sectionMap.get(ABSTRACT.toString()));
        dataset.setName(sectionMap.get(TITLE.toString()));
        Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
        HashSet<String> setData = new HashSet<String>();
        HashSet<String> setOrganisms = new HashSet<String>();
        setData.add(attributesMap.get(PUBLICATION));
        //dates.put()
        dates.put(DSField.Date.PUBLICATION.key(), setData);

        HashSet<String> repository = new HashSet<String>();
        repository.add("biostudies");
        dataset.addAdditional(DSField.Additional.REPOSITORY.key(), repository);



        dataset.setDates(dates);
        //setData.clear();
        if (sectionMap.containsKey(ORGANISM.toString())) {
            setOrganisms.add(sectionMap.get(ORGANISM.toString()));
            dataset.getAdditional().put(DSField.Additional.SPECIE_FIELD.key(), setOrganisms);
        }

        //if(submissions.getSection().getLinks().)
        return dataset;
    }
}

