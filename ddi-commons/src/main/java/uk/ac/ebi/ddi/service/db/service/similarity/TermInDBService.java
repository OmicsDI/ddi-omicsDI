package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.similarity.TermInDB;
import uk.ac.ebi.ddi.service.db.repo.similarity.ITermInDBRepo;

import java.util.List;
import java.util.Optional;

/**
 * Created by mingze on 30/07/15.
 */

@Service
public class TermInDBService implements ITermInDBService {


    @Autowired
    private ITermInDBRepo accessRepo;

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public TermInDB insert(TermInDB termInDB) {

        TermInDB insertedTerm = accessRepo.insert(termInDB);
        if ((insertedTerm.getId() == null))
            throw new DBWriteException("Insert failed, no _id assigned to this termInDB");
        return insertedTerm;
    }

    @Override
    public List<TermInDB> insert(List<TermInDB> terms) {
        return accessRepo.insert(terms);
    }

    @Override
    public TermInDB read(ObjectId id) {
        Optional<TermInDB> termInDB = accessRepo.findById(id);
        return termInDB == null ? null : termInDB.get();
    }

    @Override
    public Page<TermInDB> readAll(int pageStart, int size) {
        return accessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public TermInDB update(TermInDB termInDB) {
        return accessRepo.save(termInDB);
    }

    @Override
    public TermInDB delete(ObjectId id) {
        accessRepo.deleteById(id);
        return read(id);
    }

    @Override
    public TermInDB readByName(String termName) {
        if ((termName == null))
            throw new DBWriteException(" The reference to the original resource should contain a string");

        return accessRepo.findByNameQuery(termName);

    }


    @Override
    public boolean isTermExist(String termAccession) {
        TermInDB mainTerm = accessRepo.findByNameQuery(termAccession);
        return (mainTerm != null);
    }

    @Override
    public long getNumberOfTermsInDB() {
        return (accessRepo.count());
    }

    @Override
    public void deleteAll() {
        accessRepo.deleteAll();
    }

    @Override
    public List<TermInDB> readAllInOneType(String dataType){
        return accessRepo.findByDataType(dataType);
    }

    @Override
    public List<TermInDB> readAllUncalculatedTermsInOneType(String dataType){
        return accessRepo.findUncalculatedTermsByDataType(dataType);
    }
}
