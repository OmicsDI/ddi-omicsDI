package uk.ac.ebi.ddi.service.db.service.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.dataset.FileGroup;
import uk.ac.ebi.ddi.service.db.model.dataset.GalaxyFileExtension;
import uk.ac.ebi.ddi.service.db.repo.dataset.IFileGroupRepo;
import uk.ac.ebi.ddi.service.db.repo.dataset.IGalaxyFileExtensionRepo;

import java.util.List;

@Service
public class FileGroupService {

    @Autowired
    private IFileGroupRepo fileGroupRepo;

    @Autowired
    private IGalaxyFileExtensionRepo galaxyFileExtensionRepo;

    public List<FileGroup> findAll() {
        return fileGroupRepo.findAll();
    }

    public List<GalaxyFileExtension> findAllGalaxyExtensions() {
        return galaxyFileExtensionRepo.findAll();
    }
}
