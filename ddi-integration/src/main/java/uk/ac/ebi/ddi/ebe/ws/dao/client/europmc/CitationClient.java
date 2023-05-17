package uk.ac.ebi.ddi.ebe.ws.dao.client.europmc;

import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ebe.ws.dao.client.EbeyeClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.europmc.CitationResponse;

import java.net.URI;

/**
 * Created by gaur on 13/07/17.
 */
public class CitationClient extends EbeyeClient {

    public CitationClient(AbstractEbeyeWsConfig config) {
        super(config);
    }

    public CitationResponse getCitations(String accession, int pageSize, String cursorMark) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/europepmc/webservices/rest/search")
                .queryParam("query", "\"" + accession + "\"")
                .queryParam("format", "JSON")
                .queryParam("resulttype", "idlist")
                .queryParam("pageSize", pageSize)
                .queryParam("cursorMark", cursorMark);
        URI uri = builder.build().encode().toUri();
        return restTemplate.getForObject(uri, CitationResponse.class);
    }
}
