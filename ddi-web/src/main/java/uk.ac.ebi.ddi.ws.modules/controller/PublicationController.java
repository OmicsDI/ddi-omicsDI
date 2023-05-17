package uk.ac.ebi.ddi.ws.modules.controller;

/**
 * @author Yasset Perez-Riverol ypriverol
 */

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.ebe.ws.dao.client.publication.PublicationWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;
import uk.ac.ebi.ddi.ws.modules.model.PublicationDetail;
import uk.ac.ebi.ddi.ws.modules.model.PublicationResult;
import uk.ac.ebi.ddi.ws.modules.util.Constants;
import uk.ac.ebi.ddi.ws.modules.util.PubmedUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Hidden
@Tag(name = "publication",
        description = "Retrieve the information about the publication including search functionalities")
@Controller
@RequestMapping(value = "/publication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PublicationController {

    @Autowired
    PublicationWsClient publicationWsClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationController.class);

     @Operation(summary = "Retrieve a set of publications by Ids", description = "Retrieve a set of publications by Ids")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public PublicationResult list(
             @Parameter(description = "Accession of the publications to be retrieved, e.g: 25347964,23851314")
            @RequestParam(value = "acc", required = true) String[] accs) {

        PublicationResult publicationResult = new PublicationResult();
        try {
        if (accs != null && accs.length > 0) {
            Set<String> ids = new HashSet<>();
            for (String acc: accs) {
                if (acc != null && acc.length() > 0) {
                    ids.add(acc);
                }
            }

            QueryResult queryResult = publicationWsClient.getPublications(Constants.PUBLICATION_SUMMARY, ids);

            List<PublicationDetail> publications = PubmedUtils.transformPublication(queryResult);
            publicationResult.setPublications(publications);
            publicationResult.setCount(publications.size());
        }
        } catch (Exception ex) {
            LOGGER.error("error in list api of publication controller", ex.getMessage());
        }
        return publicationResult;
    }
}
