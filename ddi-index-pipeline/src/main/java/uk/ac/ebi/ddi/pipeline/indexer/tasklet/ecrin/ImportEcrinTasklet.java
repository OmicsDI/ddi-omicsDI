package uk.ac.ebi.ddi.pipeline.indexer.tasklet.ecrin;

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
import uk.ac.ebi.ddi.annotation.service.database.DDIDatabaseAnnotationService;
import uk.ac.ebi.ddi.annotation.service.dataset.DDIDatasetAnnotationService;
import uk.ac.ebi.ddi.ecrin.EcrinService;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
public class ImportEcrinTasklet extends AbstractTasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportEcrinTasklet.class);

    Resource inputDirectory;

    String databaseName;

    DDIDatasetAnnotationService datasetAnnotationService;

    DDIDatabaseAnnotationService databaseAnnotationService;

    Boolean updateStatus = true;
    EcrinService ecrinService;

    String pattern;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        CopyOnWriteArrayList<Map.Entry<String, String>> threadSafeList = new CopyOnWriteArrayList<>();
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
                ecrinService.saveEntries(file,databaseName,threadSafeList);
            } catch (Exception e) {
                LOGGER.error("Error Reading file : {}, ", file.getAbsolutePath(), e);
            }
        });

        if (files.length > 0) {
            OmicsXMLFile file = new OmicsXMLFile(files[0]);
            databaseAnnotationService.updateDatabase(
                    databaseName, file.getDescription(), file.getReleaseDate(), file.getRelease(), null, null);
        }

        Set<String> databases = threadSafeList.parallelStream().map(Map.Entry::getValue).collect(Collectors.toSet());
        CopyOnWriteArrayList<Map.Entry<List<Dataset>, String>> datasets = new CopyOnWriteArrayList<>();
        databases.parallelStream().forEach(database -> datasets.add(
                new AbstractMap.SimpleEntry<>(datasetAnnotationService.getAllDatasetsByDatabase(database), database)));

        CopyOnWriteArrayList<Dataset> removed = new CopyOnWriteArrayList<>();
        datasets.parallelStream().forEach(x -> x.getKey().parallelStream().forEach(dataset -> {
            Map.Entry<String, String> pair = new AbstractMap.SimpleEntry<>(dataset.getAccession(), dataset.getDatabase());
            if (!threadSafeList.contains(pair)) {
                removed.add(dataset);
            }
        }));

        if (!updateStatus) {
            removed.forEach(x -> datasetAnnotationService.updateDeleteStatus(x));
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ecrinService, "The ecrinService service object can't be null");
    }
}
