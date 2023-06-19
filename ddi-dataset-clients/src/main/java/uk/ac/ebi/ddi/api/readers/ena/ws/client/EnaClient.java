package uk.ac.ebi.ddi.api.readers.ena.ws.client;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import uk.ac.ebi.ddi.api.readers.ena.ws.model.EnaDataset;
import uk.ac.ebi.ddi.api.readers.ena.ws.model.EnaDatasetIndexInfo;
import uk.ac.ebi.ddi.api.readers.model.NcbiDataset;
import uk.ac.ebi.ddi.api.readers.utils.XMLUtils;
import uk.ac.ebi.ddi.api.readers.ws.NcbiClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/12/2017
 */

public class EnaClient {

    private String filePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnaClient.class);

    private NcbiClient ncbiClient;

    public EnaClient(String ncbiFilePath, String filePath) {
        this.filePath = filePath;
        this.ncbiClient = new NcbiClient(ncbiFilePath);
    }

    private File getEnaFile(String id) throws Exception {
        File f = new File(filePath + "/" + id + ".xml");
        if (!f.exists()) {
            URL website = new URL("https://www.ebi.ac.uk/ena/data/view/" + id + "&display=xml");
            try (InputStream in = website.openStream()) {
                Path targetPath = f.toPath();
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return f;
    }

    public EnaDataset readFile(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        String organismName = XMLUtils.readFirstElement(doc,
                "ProjectType/ProjectTypeSubmission/Target/Organism/OrganismName");

        EnaDataset dataset = new EnaDataset();

        //TODO:read dataset

        return dataset;
    }

    public Collection<EnaDataset> getAllDatasets() throws Exception {

        Map<String, EnaDataset> outDatasets = new HashMap<>();

        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("*.xml");

        //TODO: foreach
        String prnjid = "PRJNA17297"; //enaIndexInfo.id;
        try {
            // read dataset info
            EnaDatasetIndexInfo enaIndexInfo = new EnaDatasetIndexInfo();

            NcbiDataset ncbiDataset = this.ncbiClient.getNcbiDataset(prnjid);

            // EnaDataset dataset = readFile(f);

            // outDatasets.put(PRNJID, dataset);

        } catch (Exception ex) {
            LOGGER.error("Error processing " + prnjid + " : " + ex);
        }
        return outDatasets.values();
    }

}
