package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.similarity.TermInDB;

import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */
@Repository
public interface ITermInDBService {

    /**
     * Create a new term in the MongoDB
     *
     * @param termInDB the term to be inserted in the database
     * @return the inserted term
     */
    TermInDB insert(TermInDB termInDB);

    List<TermInDB> insert(List<TermInDB> terms);

    /**
     * read a term from database
     *
     * @param id of the term entry in database
     * @return a term
     */
    TermInDB read(ObjectId id);

    /**
     * Read all the terms from the database
     *
     * @return A list of terms
     */
    Page<TermInDB> readAll(int pageStart, int size);

    /**
     * Update a term entry in the database using the new information
     *
     * @param termInDB the new datasetAccess information
     * @return the updated datasetAccess.
     */
    TermInDB update(TermInDB termInDB);

    /**
     * Remove a DatasetAccess in the Database using the id.
     *
     * @param id Identifier of the term to be removed from the database
     * @return the removed term
     */
    TermInDB delete(ObjectId id);

    TermInDB readByName(String termName);

    boolean isTermExist(String termName);

    long getNumberOfTermsInDB();

    void deleteAll();

    List<TermInDB> readAllInOneType(String dataType);

    List<TermInDB> readAllUncalculatedTermsInOneType(String dataType);

}
