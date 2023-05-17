package uk.ac.ebi.ddi.retriever;

import java.io.IOException;
import java.util.Set;

public interface IDatasetFileUrlRetriever {

    Set<String> getDatasetFiles(String accession, String database) throws IOException;
}
