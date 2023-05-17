package uk.ac.ebi.ddi.service.db.service.similarity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.similarity.EBISearchPubmedCount;
import uk.ac.ebi.ddi.service.db.repo.similarity.IEBISearchPubmedDataRepo;

/**
 * Created by gaur on 27/07/17.
 */
@Service
public class EBIPubmedSearchService implements IEBIPubmedSearchService{

    @Autowired
    public IEBISearchPubmedDataRepo ebiSearchPubmedRepo;

    public void saveEbiSearchPubmed(EBISearchPubmedCount ebiSearchPubmedCount){
        ebiSearchPubmedRepo.save(ebiSearchPubmedCount);
    }

    public EBISearchPubmedCount getSearchCount(String accession){
        return ebiSearchPubmedRepo.getSearchCount(accession);
    }
}
