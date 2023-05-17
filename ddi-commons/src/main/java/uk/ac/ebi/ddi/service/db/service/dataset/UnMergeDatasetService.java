package uk.ac.ebi.ddi.service.db.service.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.UnMergeDatasets;
import uk.ac.ebi.ddi.service.db.repo.dataset.IUnMergeDatasetsRepo;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.util.List;

@Service
public class UnMergeDatasetService implements IUnMergeDatasetService{

    @Autowired
    private IUnMergeDatasetsRepo iUnMergeDatasetsRepo;

    @Autowired
    private DatasetService datasetService;

    public void save(UnMergeDatasets unMergeDatasets){
        iUnMergeDatasetsRepo.save(unMergeDatasets);
    }

    public List<UnMergeDatasets> findAll(){
        List<UnMergeDatasets> datasets = iUnMergeDatasetsRepo.findAll();
        return datasets;
    }

    public void unmergeDataset(List<UnMergeDatasets> unMergeDatasets) {
        unMergeDatasets.forEach(mp -> {
            Dataset dataset = new Dataset(mp.getDataset().getAccession(),mp.getDataset().getDatabase(),
                    mp.getDataset().getName(),mp.getDataset().getDescription(),mp.getDataset().getDates(),
                    mp.getDataset().getAdditional(),mp.getDataset().getCrossReferences(), DatasetCategory.UPDATED);
            datasetService.save(dataset);
            Dataset masterDataset = datasetService.read(mp.getMasterAccession(), mp.getMasterDatabase());
            masterDataset.getAdditional()
                    .get(DSField.Additional.SECONDARY_ACCESSION.key())
                    .remove(mp.getDataset().getAccession() + "~" + mp.getDataset().getAdditional().get(DSField.Additional.LINK.key()));
            datasetService.save(masterDataset);
            iUnMergeDatasetsRepo.deleteByDataset(mp.getDataset().getAccession(), mp.getDataset().getDatabase());
        });
    }
}
