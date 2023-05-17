package uk.ac.ebi.ddi.service.db.model.enrichment;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractDocument;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mingze on 23/07/15.
 * annotated word which has its own synonym list
 *
 * label is also the label of the word
 */
@Document(collection = "enrichment.synonyms")

public class Synonym extends AbstractDocument implements Serializable {

    private static final long serialVersionUID = 1326887243102331825L;

    @Field
    @Indexed(unique = true)
    private String label;

    @Field
    private List<String> synonyms;

    @PersistenceConstructor
    public Synonym(String label, List<String> synonyms ) {
        this.label = label;
        this.synonyms = synonyms;

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getlabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Synonyms{" +
                "id='" + _id + '\'' +
                "label='" + label + '\'' +
                ", nextSynonym=" + synonyms +
                '}';
    }
}
