package uk.ac.ebi.ddi.pride.web.service.config;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class ArchiveWsConfigProd extends AbstractArchiveWsConfig {

    public ArchiveWsConfigProd() {
        super("https", "www.ebi.ac.uk");
    }
}
