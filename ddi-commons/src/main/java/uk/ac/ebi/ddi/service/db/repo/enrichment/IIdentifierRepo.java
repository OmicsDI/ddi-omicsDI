package uk.ac.ebi.ddi.service.db.repo.enrichment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.enrichment.Identifier;

import java.util.List;

/**
 * Created by azorin on 22/01/2018.
 */
@Repository
public interface IIdentifierRepo extends MongoRepository<Identifier,String> {

    @Query("{additional_accessions:?0}")
    List<Identifier> getByAdditionalAccession(String accession);
}

