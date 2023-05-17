package uk.ac.ebi.ddi.pipeline.indexer.tasklet.statistics;


import com.google.common.collect.Multiset;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.downloas.logs.ElasticSearchWsClient;
import uk.ac.ebi.ddi.downloas.logs.ElasticSearchWsConfigProd;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;
import uk.ac.ebi.ddi.pipeline.indexer.utils.TimeRanger;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

import static uk.ac.ebi.ddi.pipeline.indexer.utils.TimeRanger.START_TIME;

@Setter
@Getter
public class DatasetDownloadCountTasklet extends AbstractTasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetDownloadCountTasklet.class);

    private IDatasetService datasetService;

    private ElasticSearchWsClient elasticSearchClient;

    List<String> databases;

    private TimeRanger timeRanger;

    private boolean overwrite;

    private static final int PARALLEL = Math.min(9, Runtime.getRuntime().availableProcessors());

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeRanger.getToDate());
        cal.add(Calendar.DATE, -1); // Minus 1 day to avoid data haven't up-to-date yet.
        Date toDate = cal.getTime();
        elasticSearchClient.initialiseData(timeRanger.getFromDate(), toDate);
        for (String database : databases) {
            LOGGER.info("Processing database {}", database);
            Map<String, Map<String, Map<String, Multiset<String>>>> dbDownloadInfo
                    = elasticSearchClient.getDownloadsData(ElasticSearchWsConfigProd.DB.valueOf(database));
            List<Dataset> datasets = datasetService.readDatasetHashCode(database);

            ForkJoinPool customThreadPool = new ForkJoinPool(PARALLEL);
            customThreadPool.submit(() -> datasets.stream().parallel()
                    .filter(ds -> dbDownloadInfo.containsKey(ds.getAccession()))
                    .forEach(x -> process(x, dbDownloadInfo.get(x.getAccession()), toDate))
            ).get();
        }
        return RepeatStatus.FINISHED;
    }

    /**
     *
     * @param dt
     * @param dsDownloadInfo  // PERIOD_TO_ANONYMISED_IP_ADDRESS_TO_FILE_NAME
     * @param toDate
     */
    private void process(Dataset dt, Map<String, Map<String, Multiset<String>>> dsDownloadInfo, Date toDate) {
        try {
            Dataset dataset = datasetService.read(dt.getAccession(), dt.getDatabase());
            int downloadCurrValue = dataset.getAdditional().containsKey(DSField.Additional.DOWNLOAD_COUNT.key())
                    ? Integer.valueOf(dataset.getAdditional().get(DSField.Additional.DOWNLOAD_COUNT.key())
                                    .iterator().next())
                    : 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String lastUpdated = dataset.getConfigurations()
                    .get(DSField.Configurations.IGNORE_DATASET_FILE_RETRIEVER.key());
            Date lastUpdatedDate = lastUpdated == null || overwrite ? START_TIME : dateFormat.parse(lastUpdated);

            int newDownloadsCount = dsDownloadInfo.entrySet()
                    .stream()
                    .filter(period -> {
                        try {
                            return overwrite || dateFormat.parse(period.getKey()).after(lastUpdatedDate);
                        } catch (ParseException e) {
                            LOGGER.error("Unable to parse {}, ", period, e);
                            return false;
                        }
                    })
                    .mapToInt(period -> period.getValue().entrySet()
                            .stream()
                            .mapToInt(ipAddr -> ipAddr.getValue().size())
                            .sum()
                    ).sum();

            int totalDownloads = newDownloadsCount + downloadCurrValue;
            if (overwrite) {
                totalDownloads = newDownloadsCount;
            }

            LOGGER.info("Dataset {}: {} downloads", dataset.getAccession(), totalDownloads);
            dataset.getAdditional().put(DSField.Additional.DOWNLOAD_COUNT.key(),
                    Collections.singleton(String.valueOf(totalDownloads)));
            dataset.getConfigurations().put(DSField.Configurations.IGNORE_DATASET_FILE_RETRIEVER.key(),
                    dateFormat.format(toDate));
            datasetService.save(dataset);
        } catch (Exception e) {
            LOGGER.error("Exception occurred, dataset: {}, ", dt.getAccession(), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(datasetService, "The datasetService object can't be null");
        Assert.notNull(elasticSearchClient, "The elasticSearchClient object can't be null");
        Assert.notEmpty(databases, "The databases list can't be empty");
    }
}
