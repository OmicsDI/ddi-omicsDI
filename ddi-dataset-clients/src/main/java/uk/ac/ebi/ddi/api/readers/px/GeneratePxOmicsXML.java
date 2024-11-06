package uk.ac.ebi.ddi.api.readers.px;

import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.px.json.model.ProteomicsResponse;
import uk.ac.ebi.ddi.api.readers.px.json.model.Repository;
import uk.ac.ebi.ddi.api.readers.px.utils.ReaderPxXML;
import uk.ac.ebi.ddi.api.readers.px.xml.io.PxReader;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */
public class GeneratePxOmicsXML implements IGenerator {

    private static HashMap<String, String> pageBuffer = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePxOmicsXML.class);

    private static final String PXSUBMISSION_PATTERN = "<ProteomeXchangeDataset";

    private List<String> databases = Arrays.asList("jPOST", "iProX", "PanoramaPublic");

    public String pxURL;
    public String pxAPIURL;

    public String repository;

    public String outputFolder;

    public String releaseDate;

    @Autowired
    RestTemplate restTemplate;


    public GeneratePxOmicsXML(String pxURL, String pxAPIURL, String outputFolder, String repository, String releaseDate) {
        this.pxURL = pxURL;
        this.pxAPIURL = pxAPIURL;
        this.outputFolder = outputFolder;
        this.repository = repository;
        this.releaseDate = releaseDate;
    }
    @Override
    public void generate() throws Exception {
        ProteomicsResponse proteomicsResponse = restTemplate.getForObject(pxAPIURL, ProteomicsResponse.class);
        AtomicInteger pagecount = new AtomicInteger(10000);
        if(proteomicsResponse != null && proteomicsResponse.getFacets() != null){
            ArrayList<Repository> repositories = proteomicsResponse.getFacets().getRepository();
            if(!CollectionUtils.isEmpty(repositories)){
                repositories.stream().forEach(
                        repository -> {
                            if(repository.getName().equalsIgnoreCase(this.repository)) {
                                pagecount.set(repository.getCount());
                            }
                        }
                );
            }
        }
        proteomicsResponse = restTemplate.getForObject(pxAPIURL+ "?src=PCUI&pageNumber=1&pageSize="+pagecount.get()+"&repository="+repository+"&resultType=compact", ProteomicsResponse.class);
        if(!CollectionUtils.isEmpty(proteomicsResponse.getDatasets())){
            List<Entry> entries = new ArrayList<>();
            proteomicsResponse.getDatasets().forEach( datasetObj -> {
                if(datasetObj != null){
                    String accession = datasetObj.get(0);
                    String pxURLProject = String.format(pxURL, accession);
                    String page = getPage(pxURLProject);
                    if (page != null && isDataset(page)) {
                        PxReader dataset = null;
                        try{
                            dataset = ReaderPxXML.parseDocument(page);
                            if (dataset != null && dataset.getRepository() != null && databases.contains(dataset.getRepository())) {
                                dataset.setAccession(accession);
                                Entry transFormedEntry = Transformers.transformAPIDatasetToEntry(dataset);
                                if(!entries.contains(transFormedEntry)){
                                    entries.add(transFormedEntry);
                                }
                            }

                        } catch (JAXBException e){
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            if(!CollectionUtils.isEmpty(entries)){
                WriteEntriesToOutputFile(entries, outputFolder);
            }
        }
    }

    private void WriteEntriesToOutputFile(List<Entry> entries, String outputFolder) throws IOException {
        FileWriter pxdbFile = new FileWriter(outputFolder + "/px_data.xml");

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(Constants.PX_DESCRIPTION);
        if (repository != null) {
            database.setName(repository);
        } else {
            database.setName(Constants.PX_DATABASE);
        }
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, pxdbFile);
        LOGGER.info("Search for Files has been FINISHED!!");
    }

    /**
     * Gets the page from the given address. Returns the
     * retrieved page as a string.
     *
     * @param urlString The address of the resource to retrieve.
     * @return The page as a String
     * @throws Exception Thrown on any problem.
     */
    public static String getPage(String urlString) {
        // check if the page is cached

        try {
            if (pageBuffer.containsKey(urlString)) {
                return pageBuffer.get(urlString);
            }
            LOGGER.warn(urlString);
            // create the url
            URL url = new URL(urlString);

            // send the request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(60000); //set timeout to 10 seconds

            connection.setReadTimeout(300000); // set timeout to 30 seconds

            connection.connect();

            // get the page
            BufferedReader in;
            StringBuilder page = new StringBuilder();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                page.append(line);
                page.append("\n");
            }

            return page.toString();
        } catch (Exception ioe) {
            LOGGER.warn("Failed to read web page");
        }
        LOGGER.debug(urlString);
        return null;
    }

    private static boolean isDataset(String pxSubmission) {
        return pxSubmission.contains(PXSUBMISSION_PATTERN);
    }


}
