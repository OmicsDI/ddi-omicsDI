package uk.ac.ebi.ddi.pipeline.indexer.tasklet.dbgap;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.ddi.dbgap.DbgapService;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.biostudies.ImportBioStudiesTasklet;
import uk.ac.ebi.ddi.pipeline.indexer.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Getter
@Setter
public class ImportDbgapTasklet extends AbstractTasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBioStudiesTasklet.class);

    Resource inputDirectory;
    DbgapService dbgapService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        CopyOnWriteArrayList<Map.Entry<String, String>> threadSafeList = new CopyOnWriteArrayList<>();
        LOGGER.info("DataSet import, inputDirectory: {} ", inputDirectory.getURI());
        File[] files = inputDirectory.getFile().listFiles();
        if (files == null) {
            LOGGER.warn("Input directory is empty, {}", inputDirectory.getFile().getAbsolutePath());
            return RepeatStatus.FINISHED;
        }
        Collections.synchronizedList(Arrays.asList(files)).parallelStream().forEach(file -> {
            try {
                LOGGER.info("processing file:" + file);
                dbgapService.saveEntries(file.getPath());
            } catch (Exception e) {
                LOGGER.error("Error Reading file : {}, ", file.getAbsolutePath(), e);
            }
        });

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dbgapService, "The dbgapService service object can't be null");
    }
}
