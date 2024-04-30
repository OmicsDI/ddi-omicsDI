package uk.ac.ebi.ddi.pipeline.indexer.tasklet.biostudies;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.biostudies.BioStudiesCoreService;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;

import java.io.File;

@Getter
@Setter
public class ImportBioStudiesCoreTasklet extends AbstractTasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBioStudiesCoreTasklet.class);

    Resource inputDirectory;

    BioStudiesCoreService bioStudiesCoreService;

    String repository;

    String omics_type;
    String pattern;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        try {
            LOGGER.info("DataSet import, inputDirectory: {} ", inputDirectory.getURI());
            System.out.println("DataSet import, inputDirectory:"+ inputDirectory.getURI());
            File inputFile = inputDirectory.getFile();
            if (inputFile == null) {
                System.out.println("Input directory is empty"+inputDirectory.getFile().getAbsolutePath());
                LOGGER.warn("Input directory is empty, {}", inputDirectory.getFile().getAbsolutePath());
                return RepeatStatus.FINISHED;
            }
            bioStudiesCoreService.saveStudies(inputFile.getPath(), repository, omics_type);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred, ", ex);
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(bioStudiesCoreService, "The biostudies service object can't be null");
    }
}
