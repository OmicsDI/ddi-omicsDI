package uk.ac.ebi.ddi.service.db.utils;

/**
 * Created by yperez on 20/06/2016.
 */
public enum DatasetSimilarsType {

    REANALYZED_BY("Reanalyzed by"),
    REANALYSIS_OF("Reanalysis of"),
    UNKNOWN("Unknown"),
    OTHER_OMICS_DATA("Other Omics Data in:");

    private final String type;

    DatasetSimilarsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    static public String getReverseRelationType(String relationType) {
        if(relationType.equalsIgnoreCase(DatasetSimilarsType.OTHER_OMICS_DATA.getType()))
            return relationType;
        if(relationType.equalsIgnoreCase(REANALYSIS_OF.getType()))
            return REANALYZED_BY.getType();
        if(relationType.equalsIgnoreCase(REANALYZED_BY.getType()))
            return REANALYSIS_OF.getType();
        return UNKNOWN.getType();
    }
}
