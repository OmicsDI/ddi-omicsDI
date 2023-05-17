package uk.ac.ebi.ddi.service.db.service.dataset;

import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.dataset.MostAccessedDatasets;

import java.util.List;

/**
 * Created by gaur on 25/06/17.
 */
public interface IMostAccessedDatasetService {

    public MostAccessedDatasets save(MostAccessedDatasets dataset);

    public Page<MostAccessedDatasets> readAll(int pageStart, int size);

    public void deleteAll();

    public MostAccessedDatasets getDatasetView(String acc, String database);

    public List<MostAccessedDatasets> getMostAccessedByDatabase(List<String> DbList);
}
