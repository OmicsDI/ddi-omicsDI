package uk.ac.ebi.ddi.service.db.repo.dataset;

import uk.ac.ebi.ddi.service.db.model.dataset.DatasetSimilars;

/**
 * Created by azorin on 14/02/2018.
 */
public interface IDatasetSimilarsRepoExtension {

    DatasetSimilars findByAccessionDatabaseQuery(String acc, String database);


}
