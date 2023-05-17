package uk.ac.ebi.ddi.ebe.ws.dao.config;

/**
 * @author jadianes
 * @author ypriverol
 *
 */
public abstract class AbstractEbeyeWsConfig {

    private String hostName;
    private String protocol;

    protected AbstractEbeyeWsConfig(String protocol, String hostName) {
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
