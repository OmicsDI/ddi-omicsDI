package uk.ac.ebi.ddi.service.db.service.dataset;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetStatus;
import uk.ac.ebi.ddi.service.db.repo.dataset.IDatasetStatusRepo;

import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2016
 */
@Service
public class DatasetStatusService implements IDatasetStatusService{

    @Autowired
    private IDatasetStatusRepo datasetStatusAccessRepo;

    public DatasetStatus save(DatasetStatus datasetstatus) {
        return datasetStatusAccessRepo.save(datasetstatus);
    }

    @Override
    public DatasetStatus read(ObjectId id) {
        return null;
    }

    @Override
    public Page<DatasetStatus> readAll(int pageStart, int size) {
        return null;
    }

    @Override
    public DatasetStatus update(DatasetStatus datasetStatus) {
        return null;
    }

    @Override
    public DatasetStatus delete(ObjectId id) {
        return null;
    }

    @Override
    public DatasetStatus read(String acc, String database) {
        return null;
    }

    @Override
    public List<DatasetStatus> readDatasetHashCode(String database) {
        return null;
    }

    @Override
    public DatasetStatus updateCategory(DatasetStatus dataset) {
        return null;
    }
}
