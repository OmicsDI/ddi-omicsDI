package uk.ac.ebi.ddi.retriever;

import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.io.IOException;
import java.util.Set;

public interface IDatasetFileUrlRetriever {

    Set<String> getDatasetFiles(String accession, String database) throws IOException;

    Set<String> getDatasetFiles(Dataset dataset) throws IOException;

}
