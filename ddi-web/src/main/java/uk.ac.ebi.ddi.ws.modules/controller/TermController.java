package uk.ac.ebi.ddi.ws.modules.controller;

/**
 * @author Yasset Perez-Riverol ypriverol
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dictionary.DictionaryClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.domain.DomainWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.TermResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary.DictWord;
import uk.ac.ebi.ddi.service.db.service.logger.HttpEventService;
import uk.ac.ebi.ddi.ws.modules.error.exception.OmicsCustomException;
import uk.ac.ebi.ddi.ws.modules.model.Term;
import uk.ac.ebi.ddi.ws.modules.util.Constants;
import uk.ac.ebi.ddi.ws.modules.util.RepoDatasetMapper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "term", description = "Retrieve the information about the terms and words including search "
        + "functionalities")
@Controller
@RequestMapping(value = "/term")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TermController {

    @Autowired
    DatasetWsClient dataWsClient;

    @Autowired
    DomainWsClient domainWsClient;

    @Autowired
    HttpEventService eventService;

    @Autowired
    private DictionaryClient dictionaryClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(TermController.class);

     @Operation(summary = "Search dictionary Terms", description = "retrieve the Terms for a pattern")
    @RequestMapping(value = "/getTermByPattern", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DictWord getWords(
             @Parameter(description = "general pattern term to be search in the dictionary: hom")
            @RequestParam(value = "q", required = false, defaultValue = "") String q,
             @Parameter(description = "the number of records to be retrieved, e.g: maximum 100")
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        if (q.length() > 2) {
            try {
                return dictionaryClient.getWordsDomains(Constants.INITIAL_DOMAINS, q, size);
            } catch (Exception e) {
                LOGGER.error("Error while calling getTermByPattern " + e.getMessage());
                throw new OmicsCustomException(e.getMessage());
            }
        }
        return new DictWord();
    }

     @Operation(summary = "Retrieve frequently terms from the Repo", description = "Retrieve frequently terms from the Repo")
    @RequestMapping(value = "/frequentlyTerm/list", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<Term> frequentTerms(
             @Parameter(description = "Number of terms to be retrieved, e.g: maximum 100")
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
             @Parameter(description = "Repository to find the information, e.g: pride")
            @RequestParam(value = "domain", required = true, defaultValue = "pride") String domain,
             @Parameter(description = "Field to search for the specific Terms, e.g: description")
            @RequestParam(value = "field", required = true, defaultValue = "description") String field) {


        /*if (Constants.MAIN_DOMAIN.equalsIgnoreCase(domain)) {
            DomainList domainList = domainWsClient.getDomainByName(Constants.MAIN_DOMAIN);
            String[] subdomains = WsUtilities.getSubdomainList(domainList);
            domain = WsUtilities.validateDomain(subdomains, domain);
        }
        if (Constants.MODELEXCHANGE_DOMAIN.equalsIgnoreCase(domain)) {
            DomainList domainList = domainWsClient.getDomainByName(Constants.MODELEXCHANGE_DOMAIN);
            String[] subdomains = WsUtilities.getSubdomainList(domainList);
            domain = WsUtilities.validateDomain(subdomains, domain);
        }*/
        TermResult termResult = null;

        if (domain != null) {
            termResult = dataWsClient.getFrequentlyTerms(domain, field, Constants.SHORT_EXCLUSION_WORDS, size);
        }

        return RepoDatasetMapper.asTermResults(termResult);
    }
}
