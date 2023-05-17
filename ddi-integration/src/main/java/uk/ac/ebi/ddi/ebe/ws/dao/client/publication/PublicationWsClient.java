package uk.ac.ebi.ddi.ebe.ws.dao.client.publication;

import com.google.common.collect.Lists;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ebe.ws.dao.client.EbeyeClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;
import uk.ac.ebi.ddi.ebe.ws.dao.utils.DDIUtils;

import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 11/06/2015
 */
public class PublicationWsClient extends EbeyeClient {

    private static final int MAX_IDENTIFIER_PER_REQUEST = 100;

    public PublicationWsClient(AbstractEbeyeWsConfig config) {
        super(config);
    }

    /**
     * This function retrieve a set of publications by Ids and the corresponding fields
     * @param fields The fields to be retrieved
     * @param ids The pubmed ids
     * @return A set of publications
     */
    public QueryResult getPublications(String[] fields, Set<String> ids) {

        QueryResult queryResult = new QueryResult();
        List<List<String>> parts = Lists.partition(Lists.newArrayList(ids), MAX_IDENTIFIER_PER_REQUEST);

        for (List<String> part : parts) {
            String finalIds = String.join(",", part);
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                    .scheme(config.getProtocol())
                    .host(config.getHostName())
                    .path("/ebisearch/ws/rest/pubmed/entry")
                    .path("/" + finalIds)
                    .queryParam("fields", DDIUtils.getConcatenatedField(fields))
                    .queryParam("format", "JSON");

            URI uri = builder.build().encode().toUri();

            QueryResult tmp = getRetryTemplate().execute(context -> restTemplate.getForObject(uri, QueryResult.class));
            queryResult.addResults(tmp);
        }

        return queryResult;
    }
}
