package uk.ac.ebi.ddi.retriever.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.xml.sax.InputSource;
import uk.ac.ebi.ddi.annotation.utils.Constants;
import uk.ac.ebi.ddi.ddidomaindb.database.DB;
import uk.ac.ebi.ddi.retriever.DatasetFileUrlRetriever;
import uk.ac.ebi.ddi.retriever.IDatasetFileUrlRetriever;
import uk.ac.ebi.ddi.similarityCalculator.SimilarityCounts;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ENAFileUrlRetriever extends DatasetFileUrlRetriever {

    private static final String ENA_ENDPOINT = "www.ebi.ac.uk/ena/portal/api";

    private static final Logger LOGGER = LoggerFactory.getLogger(SimilarityCounts.class);

    public ENAFileUrlRetriever(IDatasetFileUrlRetriever datasetDownloadingRetriever) {
        super(datasetDownloadingRetriever);
    }

    @Override
    public Set<String> getAllDatasetFiles(String accession, String database) throws IOException {
        Set<String> result = new HashSet<>();
        try {
            result.addAll(getReadRunFiles(accession));
            result.addAll(getAnalysisFiles(accession));
            result.addAll(getAssemblyFiles(accession));
            result.addAll(getWgsFiles(accession));
        } catch (URISyntaxException ex) {
            LOGGER.error("uri syntax exception in get all dataset " +
                    "files method of ena file retriever with acc {}, ", accession, ex);
        } catch (XPathException ex) {
            LOGGER.error("xpath exception in get all dataset " +
                    "files method of ena file retriever with acc {}, ", accession, ex);
        }
        return result;
    }

    public Set<String> getReadRunFiles(String accession) throws IOException, URISyntaxException {
        Set<String> result = new HashSet<>();
        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost(ENA_ENDPOINT)
                .setPath("/search")
                .setParameter("query", "(study_accession=" + accession + ")")
                .setParameter("fields", Constants.ENAREADRUNCOLUMN)
                .setParameter("result", "read_run")
                .setParameter("limit", "0")
                .setParameter("format", "json")
                .build();
        ResponseEntity<JsonNode> files = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));

        if (files.getBody() != null) {
            for (JsonNode node : files.getBody()) {
                result.addAll(Arrays.asList(node.get("fastq_ftp").asText().split(";")));
        /*String fastqAspera = node.get("fastq_aspera").asText();
        String fastqGalaxy = node.get("fastq_galaxy").asText();*/
            }
        }

        return result;
    }

    public Set<String> getAnalysisFiles(String accession) throws IOException, URISyntaxException {
        Set<String> result = new HashSet<>();

        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost(ENA_ENDPOINT)
                .setPath("/search")
                .setParameter("query", "(study_accession=" + accession + ")")
                .setParameter("fields", Constants.ENAANALYSISCOLUMN)
                .setParameter("result", "analysis")
                .setParameter("limit", "0")
                .setParameter("format", "json")
                .build();

        ResponseEntity<JsonNode> files = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));

        if (files.getBody() != null) {
            for (JsonNode node : files.getBody()) {
                result.addAll(Arrays.asList(node.get("submitted_ftp").asText().split(";")));
            }
        }

        return result;
    }

    public Set<String> getAssemblyFiles(String accession) throws IOException, URISyntaxException, XPathException {
        Set<String> result = new HashSet<>();
        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost(ENA_ENDPOINT)
                .setPath("/search")
                .setParameter("query", "(study_accession=" + accession + ")")
                .setParameter("fields", "accession")
                .setParameter("result", "assembly")
                .setParameter("limit", "0")
                .setParameter("format", "json")
                .build();
        ResponseEntity<JsonNode> files = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));

        if (files.getBody() != null) {
            for (JsonNode node : files.getBody()) {
                String acc = node.get("accession").textValue();
                URI viewUri = new URIBuilder()
                        .setScheme("https")
                        .setHost("www.ebi.ac.uk/ena/data/view")
                        .setPath("/" + acc + ".1&display=xml")
                        .build();
                ResponseEntity<String> assemblyFiles = execute(x -> restTemplate
                        .getForEntity(viewUri, String.class));
                XPath xPath = XPathFactory.newInstance().newXPath();
                result.add(xPath.evaluate("ROOT/ASSEMBLY/ASSEMBLY_LINKS/ASSEMBLY_LINK/URL_LINK/URL",
                        new InputSource(new StringReader(assemblyFiles.getBody()))));
                assemblyFiles.getStatusCode();
            }
        }

        return result;
    }

    public Set<String> getWgsFiles(String accession) throws IOException, URISyntaxException {
        Set<String> result = new HashSet<>();
        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost(ENA_ENDPOINT)
                .setPath("/search")
                .setParameter("query", "(study_accession=" + accession + ")")
                .setParameter("fields", Constants.ENAWSGFILECOLUMN)
                .setParameter("result", "wgs_set")
                .setParameter("limit", "0")
                .setParameter("format", "json")
                .build();
        ResponseEntity<JsonNode> files = execute(x -> restTemplate.getForEntity(uri, JsonNode.class));

        if (files.getBody() != null) {
            for (JsonNode node : files.getBody()) {
                result.add(node.get("embl_file").textValue());
                result.add(node.get("fasta_file").textValue());
                result.add(node.get("master_file").textValue());
            }
        }
        return result;
    }
    @Override
    protected boolean isSupported(String database) {
        return DB.ENA.getDBName().equals(database);
    }

}
