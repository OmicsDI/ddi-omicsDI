package uk.ac.ebi.ddi.pipeline.indexer.tasklet.dbgap;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.dbgap.DbgapService;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

@Getter
@Setter
public class ImportDbgapTasklet extends AbstractTasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportDbgapTasklet.class);

    Resource inputDirectory;
    DbgapService dbgapService;

    String pattern;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOGGER.info("DataSet import, inputDirectory: {} ", inputDirectory.getURI());
        FileFilter fileFilter = new WildcardFileFilter(pattern);
        File[] files = inputDirectory.getFile().listFiles(fileFilter);
        if (files == null) {
            LOGGER.warn("Input directory is empty, {}", inputDirectory.getFile().getAbsolutePath());
            return RepeatStatus.FINISHED;
        }
        Collections.synchronizedList(Arrays.asList(files)).stream().forEach(file -> {
            try {
                    LOGGER.info("processing file:{}",file);
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
