package uk.ac.ebi.ddi.pride.web.service.client.protein;

import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.pride.web.service.client.ArchiveClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.protein.ProteinDetailList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ypriverol
 */
public class ProteinWsClient extends ArchiveClient{

    public ProteinWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    /**
     * List of Proteins by Project
     * @param projectAccession
     * @param page
     * @param show
     * @return
     * @throws IOException
     */
    public ProteinDetailList getProteinsByProject(String projectAccession, int page, int show) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("page", "" + page);
        args.put("show", "" + show);

        String url = String.format("%s://%s/pride/ws/archive/protein/list/project/%s?%s",
                config.getProtocol(), config.getHostName(), projectAccession, "&page={page}&show={show}");

        return this.restTemplate.getForObject(url, ProteinDetailList.class, args);

    }

    /**
     * Number of Protein by Projects
     * @param projectAccession
     * @return
     */
    public int countProteinsByProject(String projectAccession){

        String url = String.format("%s://%s/pride/ws/archive/protein/count/project/%s",
                config.getProtocol(), config.getHostName(), projectAccession);

        return this.restTemplate.getForObject(url, Integer.class);
    }

    /**
     * List of Proteins by Assay
     * @param projectAccession
     * @param page
     * @param show
     * @return
     * @throws IOException
     */
    public ProteinDetailList getProteinsByAssay(String projectAccession, int page, int show) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("page", "" + page);
        args.put("show", "" + show);

        String url = String.format("%s://%s/pride/ws/archive/protein/list/assay/%s?%s",
                config.getProtocol(), config.getHostName(), projectAccession, "&page={page}&show={show}");

        return this.restTemplate.getForObject(url, ProteinDetailList.class, args);
    }

    /**
     * Number of Proteins by Assay
     * @param assayAccession
     * @return
     */
    public int countProteinsByAssay(String assayAccession){

        String url = String.format("%s://%s/pride/ws/archive/protein/count/assay/%s",
                config.getProtocol(), config.getHostName(), assayAccession);

        return this.restTemplate.getForObject(url, Integer.class);
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AbstractArchiveWsConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractArchiveWsConfig config) {
        this.config = config;
    }
}
