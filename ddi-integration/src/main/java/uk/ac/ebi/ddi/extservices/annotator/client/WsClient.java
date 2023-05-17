package uk.ac.ebi.ddi.extservices.annotator.client;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.http.client.config.CookieSpecs;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.extservices.annotator.config.BioOntologyWsConfigProd;
import uk.ac.ebi.ddi.extservices.utils.RetryClient;


/**
 * Abstract client to query The Tax NCBI to get the Id
 *
 * @author ypriverol
 */

public class WsClient extends RetryClient {

    protected RestTemplate restTemplate;
    protected BioOntologyWsConfigProd config;

    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public WsClient(BioOntologyWsConfigProd config) {
        this.config = config;
        this.restTemplate = new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //factory.clearsetReadTimeout(2000);
        factory.setConnectTimeout(2000);
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        factory.setHttpClient(httpClient);
        return factory;
    }

    public BioOntologyWsConfigProd getConfig() {
        return config;
    }

    public void setConfig(BioOntologyWsConfigProd config) {
        this.config = config;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
