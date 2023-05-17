package uk.ac.ebi.ddi.pride.web.service.client.project;

import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.projectsummary.ProjectSummaryList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jadianes
 * @author ypriverol
 */
public class ProjectSummaryWsClient extends ProjectCountWsClient{

    public ProjectSummaryWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    /**
     * Retrieve the information for a particular query trough all the fields
     * @param q Query to search in all the terms in the metadata.
     * @param page Page for the specific query.
     * @param show Number of Projects to be retrieved
     * @return
     * @throws IOException
     */

    public ProjectSummaryList list(String q, int page, int show) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("q", q);
        args.put("page", "" + page);
        args.put("show", "" + show);

        String url = String.format("%s://%s/pride/ws/archive/%s",
                config.getProtocol(), config.getHostName(), "project/list?q={q}&page={page}&show={show}");

        return this.restTemplate.getForObject(url, ProjectSummaryList.class, args);

    }

    /**
     *
     * @param q Retrieve the information for a particular query trough all the fields
     * @param page
     * @param show
     * @param speciesFilter
     * @param ptmsFilter
     * @param tissueFilter
     * @param diseaseFilter
     * @param titleFilter
     * @param instrumentFilter
     * @param quantificationFilter
     * @param projectTagFilter
     * @return
     * @throws IOException
     */
    public ProjectSummaryList list(String q, int page, int show,
                                   String[] speciesFilter,
                                   String[] ptmsFilter,
                                   String[] tissueFilter,
                                   String[] diseaseFilter,
                                   String[] titleFilter,
                                   String[] instrumentFilter,
                                   String[] quantificationFilter,
                                   String[] projectTagFilter) throws IOException {

        Map<String, String> args = new HashMap<String, String>();
        args.put("q", q);
        args.put("page", "" + page);
        args.put("show", "" + show);
        args.put("speciesFilter", constructComplexFieldFilter(speciesFilter));
        args.put("ptmsFilter", constructComplexFieldFilter(ptmsFilter));
        args.put("tissueFilter", constructComplexFieldFilter(tissueFilter));
        args.put("diseaseFilter", constructComplexFieldFilter(diseaseFilter));
        args.put("titleFilter", constructComplexFieldFilter(titleFilter));
        args.put("instrumentFilter", constructComplexFieldFilter(instrumentFilter));
        args.put("quantificationFilter", constructComplexFieldFilter(quantificationFilter));
        args.put("projectTagFilter", constructComplexFieldFilter(projectTagFilter));

        String url = String.format("%s://%s/pride/ws/archive/%s",
                config.getProtocol(),
                config.getHostName(),
                "project/list?q={q}&page={page}&show={show}&speciesFilter={speciesFilter}&ptmsFilter={ptmsFilter}&tissueFilter={tissueFilter}&diseaseFilter={diseaseFilter}&titleFilter={titleFilter}&instrumentFilter={instrumentFilter}&quantificationFilter={quantificationFilter}&projectTagFilter={projectTagFilter}");

        return this.restTemplate.getForObject(url, ProjectSummaryList.class, args);

    }

}
