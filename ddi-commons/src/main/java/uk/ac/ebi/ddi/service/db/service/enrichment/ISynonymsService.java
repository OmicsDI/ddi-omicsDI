package uk.ac.ebi.ddi.service.db.service.enrichment;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.enrichment.Synonym;

import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */
public interface ISynonymsService {

    /**
     * Create a new word in the MongoDB
     *
     * @param word the word to be inserted in the database
     * @return the inserted word
     */
    Synonym insert(String word, List<String> synonyms);

    /**
     * @param mainWord of all synonyms (basically, every word in synonyms cycle could be the mainWord now)
     * @param synonymLabel  the new synonym which need to be inserted in the synonyms cycle
     * @return
     */
//    Synonym insertAsSynonym(Synonym mainWord, String synonymLabel);

    /**
     * read a word from database
     *
     * @param id of the word entry in database
     * @return a word
     */
    Synonym read(ObjectId id);

    /**
     * Read all the words from the database
     *
     * @return A list of words
     */
    Page<Synonym> readAll(int pageStart, int size);

    /**
     * Update a word entry in the database using the new information
     *
     * @param synonym the new datasetAccess information
     * @return the updated datasetAccess.
     */
    Synonym update(Synonym synonym);

    /**
     * Remove a DatasetAccess in the Database using the id.
     *
     * @param id Identifier of the word to be removed from the database
     * @return the removed word
     */
    Synonym delete(ObjectId id);

    /**
     * read one synonym word in DB by label
     * @param label
     * @return
     */
    Synonym readByLabel(String label);

    /**
     * read all synonyms' labels of one word
     * @param wordLabel
     * @return
     */

    List<String> getAllSynonyms(String wordLabel);

    /**
     * check if one word exist or not
     * @param wordLabel
     * @return
     */
    boolean isWordExist(String wordLabel);
}
