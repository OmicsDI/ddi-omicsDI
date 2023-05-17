package uk.ac.ebi.ddi.pride.web.service.client.project;

import uk.ac.ebi.ddi.pride.web.service.client.ArchiveClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ypriverol
 * @author jadianes
 */
public class ProjectCountWsClient extends ArchiveClient{


    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public ProjectCountWsClient(AbstractArchiveWsConfig config) {
        super(config);
    }

    public int getProjectCount(String q) throws IOException {
        Map<String, String> args = new HashMap<String, String>();
        args.put("q", q);

        String url = String.format("%s://%s/pride/ws/archive/project/count?%s",
                    config.getProtocol(), config.getHostName(), "project/list?q={q}");


        return this.restTemplate.getForObject(url, Integer.class, args);
    }


    /**
     * Number of Projects for specific query
     * @param q Retrieve the information for a particular query trough all the fields
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
    public Integer getProjectCount(String q,
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
        args.put("speciesFilter", constructComplexFieldFilter(speciesFilter));
        args.put("ptmsFilter", constructComplexFieldFilter(ptmsFilter));
        args.put("tissueFilter", constructComplexFieldFilter(tissueFilter));
        args.put("diseaseFilter", constructComplexFieldFilter(diseaseFilter));
        args.put("titleFilter", constructComplexFieldFilter(titleFilter));
        args.put("instrumentFilter", constructComplexFieldFilter(instrumentFilter));
        args.put("quantificationFilter", constructComplexFieldFilter(quantificationFilter));
        args.put("projectTagFilter", constructComplexFieldFilter(projectTagFilter));

        String url = String.format("%s://%s/pride/ws/archive/project/count?%s",
                config.getProtocol(),
                config.getHostName(),
                "project/list?q={q}&speciesFilter={speciesFilter}&ptmsFilter={ptmsFilter}&tissueFilter={tissueFilter}&diseaseFilter={diseaseFilter}&titleFilter={titleFilter}&instrumentFilter={instrumentFilter}&quantificationFilter={quantificationFilter}&projectTagFilter={projectTagFilter}");

        return this.restTemplate.getForObject(url, Integer.class, args);

    }

    protected String constructComplexFieldFilter(String[] complexFilter) {
        String res = "";
        if(complexFilter != null && complexFilter.length > 0){
            for (int i = 0; i < complexFilter.length -1; i ++)
                if(complexFilter[i] != null)
                    res = res + complexFilter[i] + ",";
            if(complexFilter[complexFilter.length -1] != null)
                res = res + complexFilter[complexFilter.length -1];
        }
        return res;
    }
}
