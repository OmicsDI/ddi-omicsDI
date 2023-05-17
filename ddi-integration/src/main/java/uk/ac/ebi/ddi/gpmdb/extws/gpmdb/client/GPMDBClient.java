package uk.ac.ebi.ddi.gpmdb.extws.gpmdb.client;

import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.gpmdb.extws.gpmdb.config.AbstractGPMDBWsConfig;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */

public class GPMDBClient {

    protected RestTemplate restTemplate;

    protected AbstractGPMDBWsConfig config;

    public GPMDBClient(AbstractGPMDBWsConfig config){
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AbstractGPMDBWsConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractGPMDBWsConfig config) {
        this.config = config;
    }
}
