package uk.ac.ebi.ddi.pride.web.service.client.assay;

import uk.ac.ebi.ddi.pride.web.service.client.ArchiveClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.assay.AssayList;

/**
 * @author ypriverol
 */
public class ProjectAssaysWsClient extends ArchiveClient{


    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public ProjectAssaysWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    /**
     * Return a lis of Assays for an Specific Project Accession
     * @param projectAccession
     * @return
     */
    public AssayList findAllByProjectAccession(String projectAccession){

       String url = String.format("%s://%s/pride/ws/archive/assay/list/project/%s",
                config.getProtocol(), config.getHostName(), projectAccession);

        return this.restTemplate.getForObject(url, AssayList.class);
    }
}
