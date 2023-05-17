package uk.ac.ebi.ddi.pipeline.indexer.tasklet.annotation;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.annotation.service.dataset.DDIDatasetAnnotationService;
import uk.ac.ebi.ddi.annotation.service.dataset.DatasetAnnotationEnrichmentService;
import uk.ac.ebi.ddi.annotation.service.publication.DDIPublicationAnnotationService;
import uk.ac.ebi.ddi.annotation.service.taxonomy.NCBITaxonomyService;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;
import uk.ac.ebi.ddi.pipeline.indexer.utils.DatasetAnnotationFieldsUtils;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/10/15
 */
@Getter
@Setter
public class AnnotationXMLTasklet extends AbstractTasklet {

    public static final Logger LOGGER = LoggerFactory.getLogger(AnnotationXMLTasklet.class);

    String databaseName;

    DDIPublicationAnnotationService publicationService = DDIPublicationAnnotationService.getInstance();

    DDIDatasetAnnotationService datasetAnnotationService;

    NCBITaxonomyService taxonomyService = NCBITaxonomyService.getInstance();

    private static final int PARALLEL = Math.min(3, Runtime.getRuntime().availableProcessors());

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        List<Dataset> datasets = datasetAnnotationService.getAllDatasetsByDatabase(databaseName);
        ForkJoinPool customThreadPool = new ForkJoinPool(PARALLEL);

        customThreadPool.submit(() -> datasets.stream().parallel().forEach(this::process)).get();
        return RepeatStatus.FINISHED;
    }

    private void process(Dataset dataset) {
        try {
            Dataset exitingDataset = datasetAnnotationService.getDataset(dataset.getAccession(), dataset.getDatabase());
            exitingDataset = DatasetAnnotationFieldsUtils.addpublicationDate(exitingDataset);
            exitingDataset = DatasetAnnotationEnrichmentService.updatePubMedIds(publicationService, exitingDataset);
            exitingDataset = taxonomyService.annotateSpecies(exitingDataset);
            datasetAnnotationService.annotateDataset(exitingDataset);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred when processing dataset {}", dataset.getAccession(), ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(databaseName, "Database can not be null");
        Assert.notNull(datasetAnnotationService, "Annotation Service can't be null");

    }
}
