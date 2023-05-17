package uk.ac.ebi.ddi.pride.web.service.client.peptide;

import uk.ac.ebi.ddi.pride.web.service.client.ArchiveClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.peptide.PsmDetailList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ypriverol
 *
 */
public class PeptideWsClient extends ArchiveClient{


    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public PeptideWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    /**
     * List of Peptides by Project Accession
     * @param projectAccession
     * @param page
     * @param show
     * @return
     * @throws IOException
     */
    public PsmDetailList getPsmsByProject(String projectAccession, int page, int show) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("page", "" + page);
        args.put("show", "" + show);

        String url = String.format("%s://%s/pride/ws/archive/peptide/list/project/%s?%s",
                config.getProtocol(), config.getHostName(), projectAccession, "&page={page}&show={show}");

        return this.restTemplate.getForObject(url, PsmDetailList.class, args);

    }

    /**
     * Number of Peptides by Project Accession
     * @param projectAccession
     * @return
     * @throws IOException
     */
    public int countPsmsByProject(String projectAccession) throws IOException {

        String url = String.format("%s://%s/pride/ws/archive/peptide/count/project/%s",
        config.getProtocol(), config.getHostName(), projectAccession);

        return this.restTemplate.getForObject(url, Integer.class);

    }

    /**
     * List of Psms by Project and Sequences
     * @param projectAccession
     * @param sequence
     * @return
     */
    public PsmDetailList getPsmsByProjectAndSequence(String projectAccession, String sequence){

        String url = String.format("%s://%s/pride/ws/archive/peptide/list/project/%s/sequence/%s",
                config.getProtocol(), config.getHostName(), projectAccession, sequence);

        return this.restTemplate.getForObject(url, PsmDetailList.class);
    }

    /**
     * Number of PSms by Project and Sequence
     * @param projectAccession
     * @param sequence
     * @return
     */
    public int countPsmsByProjectAndSequence(String projectAccession, String sequence){

        String url = String.format("%s://%s/pride/ws/archive/peptide/count/project/%s/sequence/%s",
                config.getProtocol(), config.getHostName(), projectAccession, sequence);

        return this.restTemplate.getForObject(url, Integer.class);
    }

    /**
     * List of Peptides by Assay Accession
     * @param assayAccession
     * @param page
     * @param show
     * @return
     * @throws IOException
     */
    public PsmDetailList getPsmsByAssay(String assayAccession, int page, int show) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("page", "" + page);
        args.put("show", "" + show);

        String url = String.format("%s://%s/pride/ws/archive/peptide/list/assay/%s?%s",
                config.getProtocol(), config.getHostName(), assayAccession, "&page={page}&show={show}");

        return this.restTemplate.getForObject(url, PsmDetailList.class, args);

    }

    /**
     * Number of Peptides by Assay Accession
     * @param assayAccession
     * @return
     * @throws IOException
     */
    public int countPsmsByAssay(String assayAccession) throws IOException {

        String url = String.format("%s://%s/pride/ws/archive/peptide/count/assay/%s",
                config.getProtocol(), config.getHostName(), assayAccession);

        return this.restTemplate.getForObject(url, Integer.class);

    }

    /**
     * List of Psms by Project and Sequences
     * @param assayAccession
     * @param sequence
     * @return
     */
    public PsmDetailList getPsmsByAssayAndSequence(String assayAccession, String sequence){

        String url = String.format("%s://%s/pride/ws/archive/peptide/list/assay/%s/sequence/%s",
                config.getProtocol(), config.getHostName(), assayAccession, sequence);

        return this.restTemplate.getForObject(url, PsmDetailList.class);
    }

    /**
     * Number of PSms by Project and Sequence
     * @param assayAccession
     * @param sequence
     * @return
     */
    public int countPsmsByAssayAndSequence(String assayAccession, String sequence){

        String url = String.format("%s://%s/pride/ws/archive/peptide/count/assay/%s/sequence/%s",
                config.getProtocol(), config.getHostName(), assayAccession, sequence);

        return this.restTemplate.getForObject(url, Integer.class);
    }
}
