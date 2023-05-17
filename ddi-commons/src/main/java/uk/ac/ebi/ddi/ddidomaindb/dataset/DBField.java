package uk.ac.ebi.ddi.ddidomaindb.dataset;

import static uk.ac.ebi.ddi.ddidomaindb.dataset.FieldCategory.DATA;
import static uk.ac.ebi.ddi.ddidomaindb.dataset.FieldType.*;

public class DBField {
    public static final Field NAME = new Field("name", MANDATORY, DATA, "Database Name");
    public static final Field DESCRIPTION = new Field("description", SUGGESTED, DATA, "Database Description");
    public static final Field RELEASE = new Field("release", OPTIONAL, DATA, "Database release");
    public static final Field RELEASE_DATE = new Field("release_date", SUGGESTED, DATA, "Database Release Date");
    public static final Field ENTRY_COUNT = new Field("entry_count", SUGGESTED, DATA, "Database Entries Count");
    public static final Field KEYWORDS = new Field("keywords", SUGGESTED, DATA, "Database Keywords");
    public static final Field URL = new Field("url", SUGGESTED, DATA, "Database URL");
    public static final Field SEARCH_URL = new Field("search_url", OPTIONAL, DATA, "Database Prefix URL");
}
