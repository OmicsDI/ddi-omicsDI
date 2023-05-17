package uk.ac.ebi.ddi.service.db.service.similarity;

import uk.ac.ebi.ddi.service.db.model.similarity.Citations;

/**
 * Created by gaur on 20/07/17.
 */
public interface ICitationService {

    void saveCitation(Citations citations);


    Citations read(String accession,String database);
}
