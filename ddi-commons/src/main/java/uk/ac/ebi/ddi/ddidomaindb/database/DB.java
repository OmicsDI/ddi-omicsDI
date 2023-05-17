package uk.ac.ebi.ddi.ddidomaindb.database;

public enum DB {
    PRIDE("Pride", "pride"),
    PEPTIDEATLAS("PeptideAtlas", "peptide_atlas"),
    MASSIVE_OLD("Massive", "massive"),
    MASSIVE("MassIVE", "massive"),
    METABOLIGHTS("MetaboLights", "metabolights_dataset"),
    EGA("EGA", "ega"),
    GPMDB("GPMDB",  "gpmdb"),
    GNPS("GNPS", "gnps"),
    ARRAY_EXPRESS("ArrayExpress", "arrayexpress-repository"),
    METABOLOMEEXPRESS("MetabolomeExpress", "metabolome_express"),
    EXPRESSION_ATLAS("ExpressionAtlas", "atlas-experiments"),
    METABOLOMICSWORKBENCH("MetabolomicsWorkbench", "metabolomics_workbench"),

    BIOMODELS("BioModels Database","biomodels"),
    BIOMODELS_SHORT("BioModels","biomodels"),

    DB_GAP("dbGaP", "dbgap"),

    LINCS("LINCS","lincs"),
    GEO("GEO", "geo"),
    PAXDB("PAXDB","paxdb"),
    JPOST("JPOST Repository","jpost"),
    EVA("EVA","eva"),
    ENA("ENA","ena"),
    NCBI("NCBI", "NCBI");

    String databaseName;
    String solarName;

    DB(String databaseName, String solrName) {
        this.databaseName = databaseName;
        this.solarName = solrName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDBName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSolarName() {
        return solarName;
    }

    public void setSolarName(String solarName) {
        this.solarName = solarName;
    }

    public static String retriveAnchorName(String name){
        for (DB database: values()) {
            if (database.solarName.equalsIgnoreCase(name)) {
                return database.getDatabaseName();
            }
        }
        return name;
    }

    public static String retriveSorlName(String name) {
        for (DB database: values()) {
            if (database.getDatabaseName().equalsIgnoreCase(name)) {
                return database.getSolarName();
            }
        }
        return name;
    }
}
