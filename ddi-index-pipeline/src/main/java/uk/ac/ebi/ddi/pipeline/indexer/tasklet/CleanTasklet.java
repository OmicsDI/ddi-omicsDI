package uk.ac.ebi.ddi.pipeline.indexer.tasklet;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.pipeline.indexer.io.FileSystemPersisterFactory;

import java.io.IOException;

/**
 * @author dani@ebi.ac.uk
 *         Date: 15/05/12
 */
@Getter
@Setter
public class CleanTasklet extends AbstractTasklet {
    public static final Logger LOGGER = LoggerFactory.getLogger(CleanTasklet.class);

    private FileSystemPersisterFactory fileSystemPersisterFactory;
    private String key;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOGGER.info("Cleaning up temporary files for key=" + key);

        //tasklet to delete temporary directory where all persistent files gets written
        try {
            fileSystemPersisterFactory.getInstance(key).clear();
        } catch (IOException e) {
            String msg = "Failed to clean file system persister for key: " + key + " because of: " + e.getMessage();
            LOGGER.error(msg);
//            throw new UnexpectedJobExecutionException(msg, e);
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fileSystemPersisterFactory, "FileSystemPersister cannot be empty");
        Assert.notNull(key, "Key cannot be empty");
    }
}
