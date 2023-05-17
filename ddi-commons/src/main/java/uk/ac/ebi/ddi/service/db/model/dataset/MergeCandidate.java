package uk.ac.ebi.ddi.service.db.model.dataset;

import java.util.List;

/**
 * Created by azorin on 09/02/2018.
 */
public class MergeCandidate extends DatasetShort {
    public List<DatasetShort> getSimilars() {
        return Similars;
    }

    public void setSimilars(List<DatasetShort> similars) {
        Similars = similars;
    }

    List<DatasetShort> Similars;
}
