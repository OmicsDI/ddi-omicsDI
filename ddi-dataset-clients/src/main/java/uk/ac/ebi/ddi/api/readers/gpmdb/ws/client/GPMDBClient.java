package uk.ac.ebi.ddi.api.readers.gpmdb.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.model.Model;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class GPMDBClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GPMDBClient.class);

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public GPMDBClient(AbstractWsConfig config) {
        super(config);

    }

    /**
     * Returns the Datasets from MtabolomeWorbench
     *
     * @return A list of entries and the facets included
     */
    public String[] getAllProteins(String model) {
        String url = String.format("%s://%s/model/proteins/gpm=%s",
                config.getProtocol(), config.getHostName(), model);
        //Todo: Needs to be removed in the future, this is for debugging
        LOGGER.debug(url);
        try {
            return this.restTemplate.getForObject(url, String[].class);
        } catch (Exception e) {
            LOGGER.error("Exception occurred when fetching protern for model {}, ", model, e);
        }
        return null;
    }

    /**
     * Get the information from the Web services in the Model
     *
     * @param modelId model Identifier
     * @return GPMDB Model information
     */
    public Model getModelInformation(String modelId) {

        String url = String.format("%s://%s/model/metadata/gpm=%s",
                config.getProtocol(), config.getHostName(), modelId);

        try {
            return this.restTemplate.getForObject(url, Model.class);
        } catch (Exception e) {
            try {
                String responseModel = parseFromString(url);
                responseModel = responseModel.replace("\\", "");
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseModel, Model.class);
            } catch (Exception e1) {
                LOGGER.error("Exception occurred when fetching model {}, ", modelId, e1);
            }
        }
        return null;

    }

    private String parseFromString(String url) throws IOException {

        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

}
