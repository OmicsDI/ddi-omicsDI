package uk.ac.ebi.ddi.retriever.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ddidomaindb.database.DB;
import uk.ac.ebi.ddi.retriever.DatasetFileUrlRetriever;
import uk.ac.ebi.ddi.retriever.IDatasetFileUrlRetriever;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


public class JPostFileUrlRetriever extends DatasetFileUrlRetriever {

    private static final String JPOST_ENDPOINT = "https://repository.jpostdb.org";

    private static final  String JPOST_STORAGE_ENDPOINT = "https://storage.jpostdb.org";

    private static final int DEFAULT_ELEMENTS_PER_REQUEST = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(JPostFileUrlRetriever.class);

    public JPostFileUrlRetriever(IDatasetFileUrlRetriever datasetDownloadingRetriever) {
        super(datasetDownloadingRetriever);
    }

    @Override
    public Set<String> getAllDatasetFiles(String accession, String database) throws IOException {
        Set<String> result = new HashSet<>();
        String jpostProject = getJpostProject(accession);
        if (jpostProject == null) {
            return result;
        }
        result.addAll(fetchListDatasetFiles(0, DEFAULT_ELEMENTS_PER_REQUEST, jpostProject));
        return result;
    }

    @Override
    protected boolean isSupported(String database) {
        return database.equals(DB.JPOST.getDBName());
    }


    private String getJpostProject(String accession) {
        /*UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(JPOST_ENDPOINT)
                .path("/_api/project")
                .queryParam("sortKey", "announcementDate")
                .queryParam("offset", 0)
                .queryParam("num", 1)
                .queryParam("target", "public")
                .queryParam("keyword", accession)
                .queryParam("searchKey", "");*/
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(JPOST_ENDPOINT)
                .path("/open-api/projects/"+accession);
        URI uri = builder.build().encode().toUri();
        try{
            System.out.println("Resulting url "+uri);
            ResponseEntity<JsonNode> response = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));
            for (JsonNode node : response.getBody()) {
                System.out.println(node.asText());
                return  (node.get("jpostId") != null) ? node.get("jpostId").asText() :   null;
            }
        } catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        return null;
    }

    private List<String> fetchListDatasetFiles(int offset, int numberElement, String accession) {
        /*UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(JPOST_ENDPOINT)
                .path("/_api/file")
                .queryParam("sortKey", "id")
                .queryParam("offset", offset)
                .queryParam("num", numberElement)
                .queryParam("target", "server")
                .queryParam("projectId", accession)
                .queryParam("searchKey", "");*/
        List<String> result = new ArrayList<>();
        if(StringUtils.hasLength(accession)){
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(JPOST_ENDPOINT)
                    .path("/open-api/projects/"+accession+"/files");
            URI uri = builder.build().encode().toUri();
            System.out.println("URL for listfiles "+uri);
            ResponseEntity<JsonNode> response = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Exception occurred when fetching dataset's files of {}", accession);
                return result;
            }

            for (JsonNode item : response.getBody().get("list")) {
                String fileName = item.get("name").asText();
                String projectId = accession;
            /*UriComponentsBuilder fileUrlBuilder = UriComponentsBuilder.fromHttpUrl(JPOST_ENDPOINT)
                    .path("/data")
                    .path("/" + projectId)
                    .path("/" + fileName);*/
                UriComponentsBuilder fileUrlBuilder = UriComponentsBuilder.fromHttpUrl(JPOST_STORAGE_ENDPOINT)
                        .path("/"+accession+"/files/"+fileName);
                result.add(fileUrlBuilder.build().encode().toUriString());
            }
            if(response.getBody().get("meta") != null && response.getBody().get("meta").get("total") != null){
                int totalFiles = response.getBody().get("meta").get("total").intValue();
                int fetched = offset + numberElement;
                if (offset + numberElement < totalFiles) {
                    result.addAll(fetchListDatasetFiles(fetched, totalFiles - fetched, accession));
                }
            }

            //int totalFiles = response.getBody().get("total").intValue();

        }
        return result;
    }

    @Override
    public Set<String> getDatasetFiles(Dataset dataset) throws IOException {
        Set<String> result = new HashSet<>();
       //String jpostProject = getJpostProject(dataset.getAccession());
        //if (jpostProject == null) {
            if(CollectionUtils.isEmpty(result)){
                System.out.println("Inside empty result case block "+dataset.getAccession()+" contains link "+dataset.getAdditional().get("full_dataset_link"));
                if(dataset.getAdditional().containsKey("full_dataset_link")){
                    String fullDatasetLink = dataset.getAdditional().get("full_dataset_link").stream().toList().get(0);
                    String datasetAccession = null;
                    if(fullDatasetLink.contains("//?")){
                        try{
                            datasetAccession = getParamValue(fullDatasetLink,"ID");
                            System.out.println("Dataset accession for "+ dataset.getAccession() + " is "+datasetAccession);

                        } catch (URISyntaxException e){
                            e.printStackTrace();
                        }
                    } else {
                        datasetAccession = fullDatasetLink.substring(fullDatasetLink.lastIndexOf('/') + 1);
                    }
                    if(StringUtils.hasLength(datasetAccession)){
                        //String jpostProject = getJpostProject(datasetAccession);
                        System.out.println(dataset.getAccession() +"" +datasetAccession+ " list of files "+fetchListDatasetFiles(0, DEFAULT_ELEMENTS_PER_REQUEST, datasetAccession));
                        result.addAll(fetchListDatasetFiles(0, DEFAULT_ELEMENTS_PER_REQUEST, datasetAccession));
                    }
                }
            }
        //}
        return result;
    }
    private String getParamValue(String url, String paramName) throws URISyntaxException {
        return Stream.of(url.split("//?")[1].split("&"))
                .map(kv -> kv.split("="))
                .filter(kv -> "paramName".equalsIgnoreCase(kv[0]))
                .map(kv -> kv[1])
                .findFirst()
                .orElse("");
    }
}
