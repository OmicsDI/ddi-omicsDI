package uk.ac.ebi.ddi.gpmdb.extws.gpmdb.config;

/**
 * @author jadianes
 * @author ypriverol
 *
 */
public abstract class AbstractGPMDBWsConfig {

    private String hostName;
    private String protocol;

    protected AbstractGPMDBWsConfig(String protocol, String hostName) {
        this.hostName = hostName;
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }


    public String getProtocol() {
        return protocol;
    }

}
