package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gaur on 25/06/17.
 */
@Document(collection = "mostaccessed")
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1}", unique = true)
})
public class MostAccessedDatasets extends Dataset{

    public MostAccessedDatasets(){

    }

    public MostAccessedDatasets(Dataset dataset,int count){
        total = count;
        name = dataset.getName();
        setAccession(dataset.getAccession());
        setDatabase(dataset.getDatabase());
        setAdditional(dataset.getAdditional());
        setCrossReferences(dataset.getCrossReferences());
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private int total;
}
