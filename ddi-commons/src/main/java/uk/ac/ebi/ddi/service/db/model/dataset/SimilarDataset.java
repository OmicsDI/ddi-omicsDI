package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Objects;

/**
 *
 * Created by yperez on 20/06/2016.
 */
public class SimilarDataset {

    @DBRef
    Dataset similarDataset;

    String relationType;

    public SimilarDataset() {
    }

    public SimilarDataset(Dataset similarDataset, String relationType) {
        this.similarDataset = similarDataset;
        this.relationType = relationType;
    }

    public Dataset getSimilarDataset() {
        return similarDataset;
    }

    public void setSimilarDataset(Dataset similarDataset) {
        this.similarDataset = similarDataset;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarDataset that = (SimilarDataset) o;
        return Objects.equals(similarDataset, that.similarDataset) &&
                Objects.equals(relationType, that.relationType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(similarDataset, relationType);
    }
}
