package uk.ac.ebi.ddi.pipeline.indexer.tasklet.dataset;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.annotation.service.dataset.DDIDatasetAnnotationService;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;

/**
 * Created by gaur on 19/06/17.
 */
@Getter
@Setter
public class DatasetOperationTasklet extends AbstractTasklet {

    DDIDatasetAnnotationService datasetAnnotationService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED;
    }

//    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//        List<PublicationDataset> datasetList = datasetAnnotationService.getPublicationDatasets();
//        datasetList = datasetList.parallelStream()
//                .filter(x -> x.getOmicsType() != null && !x.getOmicsType().isEmpty())
//                .collect(Collectors.toList());
//        Map<String, Set<PublicationDataset>> publicationMap = datasetList.parallelStream()
//                .collect(Collectors.groupingBy(PublicationDataset::getPubmedId, Collectors.toSet()));
//        publicationMap.entrySet().parallelStream().forEach(publication -> {
//            boolean multiomics = publication.getValue().parallelStream()
//                    .collect(Collectors.groupingBy(PublicationDataset::getOmicsType, Collectors.toSet())).size() > 1;
//            if (multiomics) {
//                publication.getValue().parallelStream().forEach(x -> {
//                    Dataset dataset = datasetAnnotationService.getDataset(x.getDatasetID(), x.getDatabase());
//                    if (dataset != null) {
//                        dataset = DatasetUtils.addAdditionalField(
//                                dataset, Field.OMICS.getName(), Constants.MULTIOMICS_TYPE);
//                        datasetAnnotationService.updateDataset(dataset);
//                        datasetAnnotationService.addDatasetSimilars(dataset, publication.getValue(),
//                                DatasetSimilarsType.OTHER_OMICS_DATA.getType());
//                    }
//                });
//            }
//        });
//        return RepeatStatus.FINISHED;
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(datasetAnnotationService, "The dataset annotation object can't be null");
    }
}
