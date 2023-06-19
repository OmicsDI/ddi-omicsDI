package uk.ac.ebi.ddi.api.readers.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.api.readers.utils.RetryClient;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */
public abstract class AbstractClient extends RetryClient {

    protected RestTemplate restTemplate;

    protected AbstractWsConfig config;

    protected ObjectMapper objectMapper;

    /**
     * Default constructor for All clients
     * @param config
     */
    public AbstractClient(AbstractWsConfig config) {
        this.config = config;
        restTemplate = new RestTemplate(clientHttpRequestFactory());
        objectMapper = new ObjectMapper();
    }

    /**
     * Create Default HttpRequestFactory using default TimeOut
     * @return ClientHttpRequestFactory
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        /*HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        int timeOut = 1000000;

        //factory.setReadTimeout(timeOut);
        factory.setConnectionRequestTimeout(timeOut);
        factory.setConnectTimeout(timeOut);
        return factory;*/
        int connectionTimeout = 1000000; // milliseconds
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectionTimeout))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(connectionTimeout))
                .build();
        /*PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(5);
        connectionManager.setDefaultMaxPerRoute(5);*/
        CloseableHttpClient client = HttpClientBuilder
                .create()
                //.setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

}
