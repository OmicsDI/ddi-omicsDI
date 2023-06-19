package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.similarity.ExpOutputDataset;
import uk.ac.ebi.ddi.service.db.repo.similarity.IExpOutputDatasetRepo;


import java.util.List;
import java.util.Optional;

/**
 * Created by mingze on 30/07/15.
 */

@Service
public class ExpOutputDatasetService implements IExpOutputDatasetService {


    @Autowired
    private IExpOutputDatasetRepo accessRepo;

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public ExpOutputDataset insert(ExpOutputDataset expOutputDataset) {

        if (accessRepo.findByAccessionQuery(expOutputDataset.getAccession(), expOutputDataset.getDatabase()) != null) {
            return expOutputDataset;
        }

        ExpOutputDataset insertedDataset = accessRepo.insert(expOutputDataset);
        if ((insertedDataset.getId() == null))
            throw new DBWriteException("Inserting fail, no _id assigned");
        return insertedDataset;
    }


    @Override
    public ExpOutputDataset read(ObjectId id) {
        Optional<ExpOutputDataset> outputDataset = accessRepo.findById(id);
        return outputDataset == null ? null : outputDataset.get();
    }

    @Override
    public Page<ExpOutputDataset> readAll(int pageStart, int size) {
        if (size<1) {
            return null;
        }
        return accessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public List<ExpOutputDataset> readAllInOneType(String dataType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("expDataType").is(dataType));
        return mongoOperation.find(query, ExpOutputDataset.class);
    }

    @Override
    public ExpOutputDataset update(ExpOutputDataset expOutputDataset) {
        return accessRepo.save(expOutputDataset);
    }

    @Override
    public ExpOutputDataset delete(ObjectId id) {
        accessRepo.deleteById(id);
        return read(id);
    }

    @Override
    public ExpOutputDataset readByAccession(String accession, String database) {
        if ((accession == null || database == null))
            throw new DBWriteException(" The accession/database to the original resource should contain a string");
        return accessRepo.findByAccessionQuery(accession,database);
    }

    @Override

    public boolean isDatasetExist(String accession, String database) {
        ExpOutputDataset dataset = accessRepo.findByAccessionQuery(accession, database);
        return (dataset != null);
    }

    @Override
    public long getNumberOfDatasets() {
        return (accessRepo.count());
    }

    @Override
    public void deleteAll() {
        accessRepo.deleteAll();
    }
}
