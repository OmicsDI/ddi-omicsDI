package uk.ac.ebi.ddi.ebe.ws.dao.client;

import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;

import java.util.Collections;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */
public class EbeyeClient {

    protected RestTemplate restTemplate;
    protected AbstractEbeyeWsConfig config;

    private static final int RETRIES = 5;
    private RetryTemplate retryTemplate = new RetryTemplate();

    public EbeyeClient(AbstractEbeyeWsConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
        SimpleRetryPolicy policy =
                new SimpleRetryPolicy(RETRIES, Collections.singletonMap(Exception.class, true));
        retryTemplate.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000);
        backOffPolicy.setMultiplier(1.6);
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    protected RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AbstractEbeyeWsConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractEbeyeWsConfig config) {
        this.config = config;
    }
}
