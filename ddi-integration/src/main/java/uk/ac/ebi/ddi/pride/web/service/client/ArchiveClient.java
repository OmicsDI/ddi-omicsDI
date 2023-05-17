package uk.ac.ebi.ddi.pride.web.service.client;

import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;

/**
 * @author ypriverol
 */
public class ArchiveClient {

    protected RestTemplate restTemplate;
    protected AbstractArchiveWsConfig config;

    /**
     * Default constructor for Archive clients
     * @param config
     */
    public ArchiveClient(AbstractArchiveWsConfig config){
        this.config = config;
        this.restTemplate = new RestTemplate();
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
