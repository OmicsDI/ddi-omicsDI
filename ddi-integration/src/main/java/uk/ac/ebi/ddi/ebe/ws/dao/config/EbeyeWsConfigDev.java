package uk.ac.ebi.ddi.ebe.ws.dao.config;

/**
 * This class is used when the service is in dev.
 *
 * @author ypriverol
 */
public class EbeyeWsConfigDev extends AbstractEbeyeWsConfig {

    public EbeyeWsConfigDev() {
        super("http", "wwwdev.ebi.ac.uk");
    }
}
