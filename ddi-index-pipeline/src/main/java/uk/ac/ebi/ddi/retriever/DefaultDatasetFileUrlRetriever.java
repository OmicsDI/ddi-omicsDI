package uk.ac.ebi.ddi.retriever;

import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DefaultDatasetFileUrlRetriever implements IDatasetFileUrlRetriever {

    @Override
    public Set<String> getDatasetFiles(String accession, String database) {
        return new HashSet<>();
    }

    @Override
    public Set<String> getDatasetFiles(Dataset dataset) throws IOException {
        return new HashSet<>();
    }
}
