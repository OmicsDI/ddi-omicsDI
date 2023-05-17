package uk.ac.ebi.ddi.service.db.model.logger;


import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.utils.CategoryType;

import java.io.Serializable;

/**
 * Information about the access of each dataset, Date of the access, the idea is to have a simple statistic about how many times
 * an specific dataset has been accessed.
 *
 * @author Yasset Perez-Riverol
 */

@Document(collection = "logger.resource")

public class DatasetResource extends AbstractResource implements Serializable{

    private static final long serialVersionUID = 1326887243102331826L;

//    @Indexed(background = true)
    private String accession;

//    @Indexed(background = true)
    private String database;

    /**
     * Default constructor
     */
    public DatasetResource(){
        this.category = CategoryType.DATASET_RESOURCE.getCategory();
    }

    /**
     * This Constructor generate an Id for the entity using the MongoDB driver
     * @param accession The access of the experiment in the repository
     * @param database  The id of the repository
     */
    public DatasetResource(String accession, String database) {
        this.category = CategoryType.DATASET_RESOURCE.getCategory();
        this.accession = accession;
        this.database = database;
    }

    public DatasetResource(String resourceUUID, String accession, String database) {
        super(resourceUUID);
        this.accession = accession;
        this.database  = database;
        this.category  = CategoryType.DATASET_RESOURCE.getCategory();
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasetResource)) return false;
        if (!super.equals(o)) return false;

        DatasetResource that = (DatasetResource) o;

        return accession.equals(that.accession) && database.equals(that.database);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + accession.hashCode();
        result = 31 * result + database.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DatasetAccess{" +
                "accession='" + accession + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
