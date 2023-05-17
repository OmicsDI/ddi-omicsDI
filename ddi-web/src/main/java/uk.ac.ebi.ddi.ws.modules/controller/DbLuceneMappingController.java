package uk.ac.ebi.ddi.ws.modules.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.service.db.model.dblucene.DbLuceneMapping;
import uk.ac.ebi.ddi.service.db.service.dblucene.IDbLuceneMappingService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by gaur on 29/3/17.
 */
@Tag(name = "dblucenemapping", description = "get feedback about search results")
@Controller
@RequestMapping(value = "/dblucene")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Hidden
public class DbLuceneMappingController {

    @Autowired
    IDbLuceneMappingService dbLuceneMappingService;

     @Operation(summary = "Retrieve all mappings", description = "Retrieve all mappings")
    @RequestMapping(value = "/getAllMappings", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Hidden
    public List<DbLuceneMapping> getAllMappings() {
        return dbLuceneMappingService.readAll();
    }

     @Operation(summary = "Retrieve lucene name", description = "Retrieve lucene name")
    @RequestMapping(value = "/getLuceneName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Hidden
    public List<DbLuceneMapping> getLuceneName(@Parameter(description = "database name, e.g : PRIDE")
                                                   @RequestParam(value = "dbName", required = true) String dbName) {
        return dbLuceneMappingService.getLuceneName(dbName);
    }

     @Operation(summary = "Retrieve database name", description = "Retrieve database name")
    @RequestMapping(value = "/getDbName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Hidden
    public List<DbLuceneMapping> getDbName(
             @Parameter(description = "lucene name, e.g : pride")
            @RequestParam(value = "luceneName", required = true) String luceneName) {
        return dbLuceneMappingService.getDatabase(luceneName);
    }

    @RequestMapping(value = "/saveMapping", method = PUT)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    @Hidden
    public void saveFeedback(@RequestBody DbLuceneMapping dbLuceneMapping,
                                    HttpServletRequest httpServletRequest) {
        dbLuceneMappingService.save(dbLuceneMapping);
    }
}
