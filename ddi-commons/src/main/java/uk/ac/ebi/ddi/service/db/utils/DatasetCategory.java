package uk.ac.ebi.ddi.service.db.utils;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */
public enum DatasetCategory {

    INSERTED("Inserted"),
    ENRICHED("Enriched"),
    ANNOTATED("Annotated"),
    FILES_FETCHED("FilesFetched"),
    DELETED("Deleted"),
    UPDATED("Updated");

    private final String type;

    DatasetCategory(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
