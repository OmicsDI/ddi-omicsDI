package uk.ac.ebi.ddi.service.db.service.publication;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.model.publication.PublicationDataset;
import uk.ac.ebi.ddi.service.db.repo.publication.IPublicationDatasetRepo;

import java.util.List;
import java.util.Optional;


/**
 * The DatasetAccess reader that implements all the methods to retrieve a dataset, remove it. or find them.
 *
 * @author Yasset Perez-Riverol
 */
@Component
public class PublicationDatasetService implements IPublicationDatasetService {

    @Autowired
    private IPublicationDatasetRepo datasetAccessRepo;

    @Override
    public PublicationDataset save(PublicationDataset dataset) {
        return datasetAccessRepo.save(dataset);
    }

    @Override
    public PublicationDataset read(ObjectId id) {
        Optional<PublicationDataset> publicationDataset = datasetAccessRepo.findById(id);
        return publicationDataset == null ? null : publicationDataset.get();
    }

    @Override
    public Page<PublicationDataset> readAll(int pageStart, int size) {
        return datasetAccessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public PublicationDataset update(PublicationDataset dataset) {

        PublicationDataset existingDataset = read(dataset.getId());

        existingDataset.setDatasetID(dataset.getDatasetID());
        existingDataset.setDatabaseID(dataset.getDatabase());

        return datasetAccessRepo.save(existingDataset);
    }

    @Override
    public PublicationDataset delete(ObjectId id) {
        datasetAccessRepo.deleteById(id);
        return read(id);
    }


    @Override
    public PublicationDataset read(String acc, String database) {
        return datasetAccessRepo.findByAccessionDatabaseQuery(acc, database);
    }

    @Override
    public List<PublicationDataset> findByPubmedId(String pubmedID) {
        return datasetAccessRepo.findByPubmedId(pubmedID);
    }

    @Override
    public List<PublicationDataset> readAll() {
        return datasetAccessRepo.findAll();
    }
}
