package uk.ac.ebi.ddi.service.db.service.similarity;

import uk.ac.ebi.ddi.service.db.model.similarity.EBISearchPubmedCount;

/**
 * Created by gaur on 27/07/17.
 */
public interface IEBIPubmedSearchService {

    void saveEbiSearchPubmed(EBISearchPubmedCount ebiSearchPubmedCount);

    public EBISearchPubmedCount getSearchCount(String accession);
}
