package uk.ac.ebi.ddi.ebe.ws.dao.utils;

/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class Constans {

    public static final String ASCENDING = "ascending";

    public static final String OR = "OR";

    public static final String AND = "AND";

    public enum Database {
        PRIDE("Pride", "pride"),
        PEPTIDEATLAS("PeptideAtlas", "peptide_atlas"),
        MASSIVE("Massive", "massive"),
        METABOLIGHTS("MetaboLights", "metabolights_dataset"),
        EGA("EGA", "ega"),
        GPMDB("GPMDB", "uk/ac/ebi/ddi/gpmdb"),
        GNPS("GNPS", "gnps"),
        ARRAY_EXPRESS("ArrayExpress", "arrayexpress-repository"),
        METABOLOMEEXPRESS("MetabolomeExpress", "metabolome_express"),
        EXPRESSION_ATLAS("ExpressionAtlas", "atlas-experiments"),
        METABOLOMICSWORKBENCH("MetabolomicsWorkbench", "metabolomics_workbench"),
        BIOMODELS("BioModels Database", "BioModels"),
        LINCS("LINCS", "lincs"),
        PAXDB("PAXDB", "paxdb"),
        JPOST("JPOST Repository", "jpost");

        String databaseName;
        String solarName;

        Database(String databaseName, String solrName) {
            this.databaseName = databaseName;
            this.solarName = solrName;
        }

        public String getDatabaseName() {
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

        public static String retriveAnchorName(String name) {
            for (Database database : values()) {
                if (database.solarName.equalsIgnoreCase(name)) {
                    return database.getDatabaseName();
                }
            }
            return name;
        }

        public static String retriveSorlName(String name) {
            for (Database database : values()) {
                if (database.getDatabaseName().equalsIgnoreCase(name)) {
                    return database.getSolarName();
                }
            }
            return name;
        }
    }
}
