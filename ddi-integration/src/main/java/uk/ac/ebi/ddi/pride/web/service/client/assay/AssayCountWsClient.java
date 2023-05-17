package uk.ac.ebi.ddi.pride.web.service.client.assay;

import uk.ac.ebi.ddi.pride.web.service.client.ArchiveClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;


/**
 * @author ypriverol
 */
public class AssayCountWsClient extends ArchiveClient{

    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public AssayCountWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    /**
     * Count number of Assays by Projects
     * @param projectAccession
     * @return
     */
    public int countByProjectAccession(String projectAccession){

        String url = String.format("%s://%s/pride/ws/archive/assay/count/project/%s",
                config.getProtocol(), config.getHostName(), projectAccession);


        return this.restTemplate.getForObject(url, Integer.class);
    }
}
