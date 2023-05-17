package uk.ac.ebi.ddi.service.db.service.dataset;

import uk.ac.ebi.ddi.service.db.model.dataset.UnMergeDatasets;

import java.util.List;

public interface IUnMergeDatasetService {

    public void save(UnMergeDatasets unMergeDatasets);

    public List<UnMergeDatasets> findAll();

    void unmergeDataset(List<UnMergeDatasets> unMergeDatasets);
}
