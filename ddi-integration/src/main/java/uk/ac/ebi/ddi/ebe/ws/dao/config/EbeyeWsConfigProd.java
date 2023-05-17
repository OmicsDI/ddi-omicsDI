package uk.ac.ebi.ddi.ebe.ws.dao.config;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class EbeyeWsConfigProd extends AbstractEbeyeWsConfig {

    public EbeyeWsConfigProd() {
        super("https", "www.ebi.ac.uk");
    }
}
