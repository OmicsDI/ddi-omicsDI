package uk.ac.ebi.ddi.gpmdb.extws.gpmdb.config;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class GPMDBWsConfigProd extends AbstractGPMDBWsConfig {

    public GPMDBWsConfigProd() {
        super("http", "rest.thegpm.org/1/");
    }
}
