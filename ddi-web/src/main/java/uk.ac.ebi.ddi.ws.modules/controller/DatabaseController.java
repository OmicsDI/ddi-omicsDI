package uk.ac.ebi.ddi.ws.modules.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.service.db.model.database.DatabaseDetail;
import uk.ac.ebi.ddi.service.db.service.database.DatabaseDetailService;
import uk.ac.ebi.ddi.service.db.service.database.DatabaseService;
import uk.ac.ebi.ddi.ws.modules.error.exception.OmicsDatabaseException;
import uk.ac.ebi.ddi.ws.modules.error.exception.ResourceNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 16.05.17.
 */

@Tag(name = "database", description = "Retrieve information about databases")
@RestController
@RequestMapping("/database")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DatabaseController {

    @Autowired
    DatabaseDetailService databaseDetailService;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ServletContext servletContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public List<DatabaseDetail> getDatabaseList() {
        List<DatabaseDetail> databaseDetails = new ArrayList<>();
        try {
            databaseDetails = databaseDetailService.getDatabaseList();
        } catch (Exception ex) {
            LOGGER.error("error in all api of dataset controller", ex.getMessage());
        }
        return databaseDetails;
    }

    @RequestMapping(value = "/modelexchange", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public List<DatabaseDetail> getModelExchangeDatabaseList() {
        List<String> database = Arrays.asList("FAIRDOMHub", "Physiome Model Repository",
                "BioModels", "Cell Collective");
        return databaseDetailService.findModelExchangeDatabase(database);
    }

    @Operation(summary = "updateDb", description = "updateDb")
    @RequestMapping(value = "/updateDb", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 20
    @ResponseBody
    public DatabaseDetail updateDatabase(
            @Parameter(description = " example request {\"databaseName\":\"EVA\"" +
                     "lastUpdated\":\"2021-09-30\"}")
            @RequestBody() DatabaseDetail databaseDetail
            ) {
        DatabaseDetail databaseInfo = databaseDetailService.findDatabaseById(databaseDetail.getDatabaseName());
        if (databaseInfo.getDatabaseName() != null) {
            databaseInfo.setLastUpdated(databaseDetail.getLastUpdated());
            databaseDetailService.saveDatabase(databaseInfo);
        } else {
            throw new ResourceNotFoundException("Database not found");
        }
        return databaseInfo;
    }


    @Hidden
    @RequestMapping(value = "/{databaseName}/picture", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDatabasePicture(@PathVariable String databaseName, final HttpServletResponse response)
            throws IOException {
        byte[] image;
        response.setHeader("Cache-Control", "no-cache");
        try {
            DatabaseDetail databaseDetail = databaseDetailService.findDatabaseByName(databaseName);
            image = databaseDetail.getImage();
            if (null == image) {
                InputStream in = servletContext.getResourceAsStream("/home/gaur/Desktop/iprox.png");
                image = IOUtils.toByteArray(in);
            }
        } catch (NullPointerException ex) {
            throw new OmicsDatabaseException("Database is not available, " +
                    "Please provide correct database value.");
        }
        return image;
    }
    /**
     * Function for initial load, not used
     **/
    private void initLocalData() {

        InputStream databasesInputStream = this.getClass().getResourceAsStream("/databases.json");
        String databaseJSONString = null;
        try {
            databaseJSONString = IOUtils.toString(databasesInputStream);
            databasesInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray databaseArray = new JSONArray(databaseJSONString);
        for (int i = 0; i < databaseArray.length(); i++) {
            JSONObject database = databaseArray.getJSONObject(i);
            DatabaseDetail databaseDetail = new DatabaseDetail();
            databaseDetail.setDatabaseName(database.getString("databaseName"));
            databaseDetail.setTitle(database.getString("title"));
            String imageUrl = database.getString("image");
            InputStream imgInputStream = this.getClass().getResourceAsStream("/" + imageUrl);
            byte[] imgBytes = null;
            try {
                imgBytes = IOUtils.toByteArray(imgInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            databaseDetail.setImage(imgBytes);
            databaseDetail.setImgAlt(database.getString("imgAlt"));
            databaseDetail.setSourceUrl(database.getString("sourceUrl"));
            databaseDetail.setRepository(database.getString("repository"));
            databaseDetail.setDescription(database.getString("description"));
            databaseDetailService.saveDatabase(databaseDetail);
        }
    }

    @Hidden
    @RequestMapping(value = "/db/picturebyte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getStreamFromImage() throws IOException {
        DatabaseDetail databaseDetail = new DatabaseDetail();
        BufferedImage bi = ImageIO.read(new File("/home/gaur/Desktop/iprox.png"));
        databaseDetail.setDatabaseName("Test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        baos.flush();

        byte[] imageInByte = baos.toByteArray();
        baos.close();

        InputStream is = new ByteArrayInputStream(imageInByte);
        byte[] imgBytes = null;
        try {
            imgBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseDetail.setImage(imgBytes);
        databaseDetailService.saveDatabase(databaseDetail);
    }
}
