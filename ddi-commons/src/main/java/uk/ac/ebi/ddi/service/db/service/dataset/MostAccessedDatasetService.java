package uk.ac.ebi.ddi.service.db.service.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.dataset.MostAccessedDatasets;
import uk.ac.ebi.ddi.service.db.repo.dataset.IMostAccessedRepo;


import java.util.List;

/**
 * Created by gaur on 25/06/17.
 */
@Service
public class MostAccessedDatasetService implements IMostAccessedDatasetService{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IMostAccessedRepo mostAccessedRepo;

    @Override
    public MostAccessedDatasets save(MostAccessedDatasets dataset) {
        return mostAccessedRepo.save(dataset);
    }

    @Override
    public Page<MostAccessedDatasets> readAll(int pageStart, int size) {
        return mostAccessedRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public void deleteAll() {
        mostAccessedRepo.deleteAll();
    }

    @Override
    public MostAccessedDatasets getDatasetView(String acc, String database){
        return mostAccessedRepo.getByAccessionAndDatabase(acc, database);
    }

    @Override
    public List<MostAccessedDatasets> getMostAccessedByDatabase(List<String> dbList){
        return mostAccessedRepo.getByDatabaseIn(dbList);
    }
}
