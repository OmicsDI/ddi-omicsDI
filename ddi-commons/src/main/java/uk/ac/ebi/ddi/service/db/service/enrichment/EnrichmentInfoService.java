package uk.ac.ebi.ddi.service.db.service.enrichment;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.enrichment.DatasetEnrichmentInfo;
import uk.ac.ebi.ddi.service.db.model.enrichment.Identifier;
import uk.ac.ebi.ddi.service.db.repo.enrichment.IEnrichmentInfoRepo;
import uk.ac.ebi.ddi.service.db.repo.enrichment.IIdentifierRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mingze on 30/07/15.
 */

//@Component
@Service
public class EnrichmentInfoService implements IEnrichmentInfoService {

    @Autowired
    private IEnrichmentInfoRepo accessRepo;

    @Autowired
    private IIdentifierRepo identifierRepo;


    @Override
    public DatasetEnrichmentInfo getLatest(String accession, String database) {
        return accessRepo.findByAccessionDatabaseStatusQuery(accession, database, "new");
    }


    @Override
    public DatasetEnrichmentInfo insert(DatasetEnrichmentInfo datasetEnrichmentInfo) {

        DatasetEnrichmentInfo oldDatasetEnrichmentInfo = getLatest(datasetEnrichmentInfo.getAccession(),
                datasetEnrichmentInfo.getDatabase());
        if (oldDatasetEnrichmentInfo != null){
            oldDatasetEnrichmentInfo.setStatus("old");
            accessRepo.save(oldDatasetEnrichmentInfo);
        }
        datasetEnrichmentInfo.setStatus("new");
        DatasetEnrichmentInfo insertedDataset = accessRepo.insert(datasetEnrichmentInfo);
        if ((insertedDataset.getId() == null))
            throw new DBWriteException("Inserting fail, no _id assigned");
        return insertedDataset;
    }


    @Override
    public DatasetEnrichmentInfo read(ObjectId id) {
        Optional<DatasetEnrichmentInfo> datasetEnrichmentInfo = accessRepo.findById(id);
        return datasetEnrichmentInfo == null ? null : datasetEnrichmentInfo.get();
    }

    @Override
    public Page<DatasetEnrichmentInfo> readAll(int pageStart, int size) {
        return accessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public DatasetEnrichmentInfo update(DatasetEnrichmentInfo datasetEnrichmentInfo) {
        return accessRepo.save(datasetEnrichmentInfo);
    }

    @Override
    public DatasetEnrichmentInfo delete(ObjectId id) {
        accessRepo.deleteById(id);
        return read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DatasetEnrichmentInfo readByAccession(String accession, String database) {
        if ((accession == null || database== null))
            throw new DBWriteException(" The accession/databaseName to the original resource should contain a string");
        return accessRepo.findByAccessionDatabaseStatusQuery(accession,database,"new");
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatasetEnrichmentInfo> readByAccessionDatabase(String accession, String database) {
        if ((accession == null || database== null))
            throw new DBWriteException(" The accession/databaseName to the original resource should contain a string");

        return accessRepo.findByAccessionDatabaseQuery(accession,database);

    }

    @Override
    public boolean isDatasetExist(String accession, String database) {
        DatasetEnrichmentInfo dataset = accessRepo.findByAccessionDatabaseStatusQuery(accession, database, "new");
        return (dataset != null);
    }

    @Override
    public void deleteAll() {
        accessRepo.deleteAll();
    }

    @Override
    public void updateIdentifiers(Iterable<Identifier> identifiers){
        identifierRepo.deleteAll();
        identifierRepo.insert(identifiers);
    }

    @Override
    public List<String> getAdditionalAccession(String accession){
        List<String> result = new ArrayList<String>();
        for(Identifier i : identifierRepo.getByAdditionalAccession(accession)){
            result.add(i.getAccession());
        }
        return result;
    }


}
