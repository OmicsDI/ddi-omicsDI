package uk.ac.ebi.ddi.api.readers.lincs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.lincs.ws.client.LINCSClient;
import uk.ac.ebi.ddi.api.readers.lincs.ws.client.LINCSConfigProd;
import uk.ac.ebi.ddi.api.readers.lincs.ws.model.DatasetList;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.util.Arrays;


/**
 * This project takes class Retrieve information from GPMDB, it allows to retrieve
 * the proteins ids for an specific model, etc.
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateLINCSOmicsXML implements IGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateLINCSOmicsXML.class);

    public AbstractWsConfig config;

    String outputFolder;

    String releaseDate;


    public GenerateLINCSOmicsXML(AbstractWsConfig config, String folder, String releaseDate) {
        this.config = config;
        this.outputFolder = folder;
        this.releaseDate = releaseDate;
    }

    public void generate() throws Exception {

        LINCSClient datasetWsClient = new LINCSClient(config);
        DatasetList datasetList = datasetWsClient.getAllDatasets();

        if (datasetList != null && datasetList.getDatasets() != null && datasetList.getDatasets().length > 0) {
            Entries lincsEntries = new Entries();
            Arrays.asList(datasetList.getDatasets()).parallelStream().forEach(dataset -> {
                Entry entry = Transformers.transformAPIDatasetToEntry(dataset);
                lincsEntries.addEntry(entry);
                LOGGER.info(dataset.getIdentifier());
            });

            FileWriter lincsFile = new FileWriter(outputFolder + "/lincs_data.xml");
            OmicsDataMarshaller mm = new OmicsDataMarshaller();

            Database database = new Database();
            database.setDescription(Constants.LINCS_DESCRIPTION);
            database.setName(Constants.LINCS);
            database.setRelease(releaseDate);
            database.setEntries(lincsEntries);
            database.setEntryCount(lincsEntries.getEntry().size());
            mm.marshall(database, lincsFile);

        }
    }

    /**
     * This program generate the massive files in two different type of files MASSIVE and GNPS Files. The MASSIVE
     * files correspond to proteomics datasets and the GNPS correspond to metabolomics datasets.
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate = null;

        if (args != null && args.length > 1 && args[0] != null) {
            outputFolder = args[0];
            releaseDate = args[1];
        } else {
            System.exit(-1);
        }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        LINCSConfigProd lincsConfigProd = (LINCSConfigProd) ctx.getBean("lincsProd");

        try {
            GenerateLINCSOmicsXML lincsGenerator =
                    new GenerateLINCSOmicsXML(lincsConfigProd, outputFolder, releaseDate);
            lincsGenerator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
