package uk.ac.ebi.ddi.ecrin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.ddi.annotation.service.database.DDIDatabaseAnnotationService;
import uk.ac.ebi.ddi.annotation.service.dataset.DDIDatasetAnnotationService;
import uk.ac.ebi.ddi.annotation.service.publication.DDIPublicationAnnotationService;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.pipeline.indexer.utils.Constants;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class EcrinService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EcrinService.class);
    Set<String> omicsType = new HashSet<>();

    @Autowired
    DatasetService datasetService;

    @Autowired
    DDIDatabaseAnnotationService databaseAnnotationService;

    @Autowired
    DDIDatasetAnnotationService datasetAnnotationService;

    DDIPublicationAnnotationService ddiPublicationAnnotationService = DDIPublicationAnnotationService.getInstance();

    public void saveEntries(File file , String databaseName,  CopyOnWriteArrayList<Map.Entry<String, String>> threadSafeList) throws IOException {
        try {

            LOGGER.info("processing file:" + file);

            OmicsXMLFile omicsXMLFile = new OmicsXMLFile(file);

            List<Entry> entries = omicsXMLFile.getAllEntries();
            for (Entry dataEntry : entries) {
                String db = databaseName != null ? databaseName : omicsXMLFile.getDatabaseName() != null ? omicsXMLFile.getDatabaseName() : "NA";
                if ("".equals(db)) {
                    db = dataEntry.getRepository() != null ? dataEntry.getRepository() : "";
                }

                dataEntry.addAdditionalField(DSField.Additional.OMICS.key(),"Clinical");
                long submitterCount = dataEntry.getAdditionalFields() != null ?
                        dataEntry.getAdditionalFields().getField().parallelStream().
                                filter(fld -> fld.getName()
                                        .equals(DSField.Additional.SUBMITTER_KEYWORDS.key())).count() : 0;
                if (submitterCount > 0) {
                    List<String> keywordSet = dataEntry
                            .getAdditionalFieldValues(DSField.Additional.SUBMITTER_KEYWORDS.key());
                    if (keywordSet != null) {
                        Entry finalDataEntry = dataEntry;
                        keywordSet.parallelStream().flatMap(dt -> {
                                    if (dt.contains(Constants.SEMI_COLON_TOKEN)) {
                                        String[] newKeywords = dt.split(Constants.SEMI_COLON_TOKEN);
                                        return Arrays.stream(newKeywords);
                                    } else {
                                        return Stream.of(dt);
                                    }
                                }
                        ).distinct().forEach(tr -> finalDataEntry
                                .addAdditionalField(DSField.Additional.SUBMITTER_KEYWORDS.key(), tr));
                    }
                }
                if(!CollectionUtils.isEmpty(dataEntry.getAdditionalFieldValues(DSField.Additional.DATASET_FILE.key()))){
                    List<String> datasetFilesList = dataEntry.getAdditionalFieldValues(DSField.Additional.DATASET_FILE.key());

                }
                if(!CollectionUtils.isEmpty(dataEntry.getAdditionalFieldValues(DSField.Additional.CONDITION_FIELD.key()))){
                    dataEntry.getAdditionalFieldValues(DSField.Additional.CONDITION_FIELD.key()).stream().forEach(
                            confield -> {
                                dataEntry.addAdditionalField(DSField.Additional.DISEASE_FIELD.key(), confield);
                            }
                    );
                }
                datasetAnnotationService.insertDataset(dataEntry, db);
                threadSafeList.add(new AbstractMap.SimpleEntry<>(dataEntry.getId(), db));
                LOGGER.info("Dataset: " + dataEntry.getId() + " " + db + "has been added");
            }
        } catch (Exception e) {
            LOGGER.error("Error Reading file : {}, ", file.getAbsolutePath(), e);
        }
    }



}
