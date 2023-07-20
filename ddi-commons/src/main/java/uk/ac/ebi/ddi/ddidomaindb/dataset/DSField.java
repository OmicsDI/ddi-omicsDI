package uk.ac.ebi.ddi.ddidomaindb.dataset;

import static uk.ac.ebi.ddi.ddidomaindb.dataset.FieldCategory.*;
import static uk.ac.ebi.ddi.ddidomaindb.dataset.FieldType.*;

public class DSField {
    public static final Field ID = new Field("id", MANDATORY, DATA, "Dataset Identifier");
    public static final Field NAME = new Field("name", MANDATORY, DATA, "Dataset Name");
    public static final Field DESCRIPTION = new Field("description", MANDATORY, DATA, "Dataset Description");
    public static final Field DATES = new Field("dates", OPTIONAL, DATA, "Dataset Dates");
    public static final Field IS_CLAIMABLE = new Field("isClaimable", OPTIONAL, DATA, "Is claimable");
    public static final Field DATABASE = new Field("database", OPTIONAL, DATA, "Database");
    public static final Field ACCESSION = new Field("accession", OPTIONAL, DATA, "Accession");


    public static class Date {
        public static final Field PUBLICATION = new Field("publication", MANDATORY, DATE, "Dataset Publication Date");
        public static final Field PUBLICATION_UPDATED = new Field("updated", UNKNOWN, DATE, "Dataset Updated Date");

        public static final Field CREATION = new Field("creation", FieldType.OPTIONAL, FieldCategory.DATE, "Dataset Creation Date");
        public static final Field SUBMISSION = new Field("submission", FieldType.OPTIONAL, FieldCategory.DATE, "Dataset Submission Date");
    }

    public static class CrossRef {
        public static final Field TAXONOMY = new Field("TAXONOMY", SUGGESTED, CROSSREF, "Dataset NCBI TAXONOMY");
        public static final Field PUBMED = new Field("pubmed", SUGGESTED, CROSSREF, "Dataset Pubmed Id");
        public static final Field MEDLINE = new Field("MEDLINE", UNKNOWN, CROSSREF, "MEDLINE Reference");
        public static final Field ENSEMBL_EXPRESSION_ATLAS = new Field("ensembl", UNKNOWN, CROSSREF, "Gene reference to ENSEMBL");
    }

    public static class Additional {

        public static final Field REPOSITORY = new Field("repository", MANDATORY, ADDITIONAL, "Dataset Repository");
        public static final Field OMICS = new Field("omics_type", MANDATORY, ADDITIONAL, "Dataset Omics Type");
        public static final Field LINK = new Field("full_dataset_link", MANDATORY, ADDITIONAL,
                "Full Dataset Link in the Original Database");
        public static final Field SUBMITTER = new Field("submitter", MANDATORY, ADDITIONAL, "Dataset Submitter information");
        public static final Field SUBMITTER_MAIL = new Field("submitter_mail", SUGGESTED, ADDITIONAL, "Dataset Submitter mail");
        public static final Field SUBMITTER_EMAIL = new Field("submitter_email", SUGGESTED, ADDITIONAL, "Dataset Submitter email");
        public static final Field SUBMITTER_AFFILIATION = new Field("submitter_affiliation", OPTIONAL, ADDITIONAL,
                "Dataset Submitter Affiliation");
        public static final Field INSTRUMENT = new Field("instrument_platform", SUGGESTED, ADDITIONAL, "Instrument Platform");
        public static final Field DATA = new Field("data_protocol", SUGGESTED, ADDITIONAL, "Dataset Abstract Data Protocol");
        public static final Field SAMPLE = new Field("sample_protocol", SUGGESTED, ADDITIONAL, "Dataset Abstract Sample Protocol");
        public static final Field ENRICH_TITLE = new Field("name_synonyms", UNKNOWN, ADDITIONAL, "Synonyms");
        public static final Field ENRICH_ABSTRACT = new Field("description_synonyms", UNKNOWN, ADDITIONAL, "Description Synonyms");
        public static final Field ENRICH_SAMPLE = new Field("sample_synonyms", UNKNOWN, ADDITIONAL, "Sample Synonyms");
        public static final Field ENRICH_DATA = new Field("data_synonyms", UNKNOWN, ADDITIONAL, "Data Synonyms");
        public static final Field PUBMED_ABSTRACT = new Field("pubmed_abstract", UNKNOWN, ADDITIONAL, "PUBMED Abstract");
        public static final Field PUBMED_TITLE = new Field("pubmed_title", UNKNOWN, ADDITIONAL, "Pubmed Title Synonyms");
        public static final Field PUBMED_AUTHORS = new Field("pubmed_authors", UNKNOWN, ADDITIONAL, "Pubmed Authors");
        public static final Field ENRICH_PUBMED_ABSTRACT = new Field("pubmed_abstract_synonyms", UNKNOWN, ADDITIONAL,
                "Pubmed Abstract Synonyms");
        public static final Field ENRICHE_PUBMED_TITLE = new Field("pubmed_title_synonyms", UNKNOWN, ADDITIONAL,
                "Pubmed title Synonyms");
        public static final Field GPMDB_MODEL = new Field("model", UNKNOWN, ADDITIONAL, "MODEL");
        public static final Field SUBMISSION_DATE = new Field("submission", UNKNOWN, ADDITIONAL, "Submission Date");
        public static final Field SOFTWARE_INFO = new Field("software", UNKNOWN, ADDITIONAL, "Software");
        public static final Field SPECIE_FIELD = new Field("species", UNKNOWN, ADDITIONAL, "Specie");
        public static final Field CELL_TYPE_FIELD = new Field("cell_type", UNKNOWN, ADDITIONAL, "Cell Type");
        public static final Field DISEASE_FIELD = new Field("disease", UNKNOWN, ADDITIONAL, "Disease");
        public static final Field TISSUE_FIELD = new Field("tissue", UNKNOWN, ADDITIONAL, "Tissue");
        public static final Field ADDITIONAL_ACCESSION = new Field("additional_accession", OPTIONAL, ADDITIONAL, "Additional Accession");
        public static final Field SECONDARY_ACCESSION = new Field("secondary_accession", OPTIONAL, ADDITIONAL, "Secondary Accession");

        public static final Field SUBMITTER_KEYWORDS = new Field("submitter_keywords", UNKNOWN, ADDITIONAL, "Submitter Keywords");
        public static final Field DATASET_FILE = new Field("dataset_file", OPTIONAL, ADDITIONAL, "Dataset File Link");
        public static final Field FILE_SIZE = new Field("file_size", OPTIONAL, ADDITIONAL, "Total File Size for the Dataset");
        public static final Field FILE_COUNT = new Field("file_count", OPTIONAL, ADDITIONAL, "Number of Files by Dataset");
        public static final Field PTM_MODIFICATIONS = new Field("ptm_modification", OPTIONAL, ADDITIONAL,
                "Posttranslations Modifications");
        public static final Field STUDY_FACTORS = new Field("study_factor", OPTIONAL, ADDITIONAL, "General Study Factor");
        public static final Field TECHNOLOGY_TYPE = new Field("technology_type", OPTIONAL, ADDITIONAL,
                "Additional Tags of the Technology");
        public static final Field PROTEOMEXCHANGE_TYPE_SUBMISSION = new Field("proteomexchange_type_submission",
                OPTIONAL, ADDITIONAL, "Complete or Partial Submission");
        public static final Field PUBCHEM_ID = new Field("pubchem_id", OPTIONAL, ADDITIONAL, "Pubchem Metabolite Identifier");
        public static final Field METABOLITE_NAME = new Field("metabolite_name", OPTIONAL, ADDITIONAL, "Metabolite Name");
        public static final Field PROTEIN_NAME = new Field("protein_name", OPTIONAL, ADDITIONAL, "Protein Names for the Database");
        public static final Field FUNDING = new Field("funding", OPTIONAL, ADDITIONAL, "Funding agency or grant");
        public static final Field CURATOR_KEYWORDS = new Field("curator_keywords", UNKNOWN, ADDITIONAL, "Submitter Keywords");
        public static final Field DATASET_TYPE = new Field("dataset_type", SUGGESTED, ADDITIONAL, "Type of Experiment");
        public static final Field GENE_NAME = new Field("gene_name", OPTIONAL, ADDITIONAL, "Additional Gene Name");
        public static final Field DOWNLOAD_COUNT = new Field("download_count", UNKNOWN, ADDITIONAL, "Download count");
        public static final Field DOWNLOAD_COUNT_SCALED = new Field("download_count_scaled", UNKNOWN, ADDITIONAL, "Download count scaled");
        public static final Field SEARCH_COUNT = new Field("search_count", UNKNOWN, ADDITIONAL, "Search Count");
        public static final Field SEARCH_COUNT_SCALED = new Field("normalized_connections", UNKNOWN, ADDITIONAL, "Search Count scaled");
        public static final Field CITATION_COUNT = new Field("citation_count", UNKNOWN, ADDITIONAL, "Citation Count");
        public static final Field CITATION_COUNT_SCALED = new Field("citation_count_scaled", UNKNOWN, ADDITIONAL, "Citation Count Scaled");
        public static final Field VIEW_COUNT = new Field("view_count", UNKNOWN, ADDITIONAL, "View Count");
        public static final Field VIEW_COUNT_SCALED = new Field("view_count_scaled", UNKNOWN, ADDITIONAL, "View Count Scaled");
        public static final Field REANALYSIS_COUNT = new Field("reanalysis_count", UNKNOWN, ADDITIONAL, "Reanalysis Count");
        public static final Field REANALYSIS_COUNT_SCALED = new Field("reanalysis_count_scaled", UNKNOWN, ADDITIONAL, "Reanalysis Count Scaled");
        public static final Field IS_PRIVATE = new Field("isPrivate", UNKNOWN, ADDITIONAL, "IsPrivate");
        public static final Field SEARCH_DOMAIN = new Field("search_domains", UNKNOWN, ADDITIONAL, "Search Domains");
        public static final Field TITLE = new Field("title", UNKNOWN, ADDITIONAL, "Title");
        public static final Field SUMMARY = new Field("summary", UNKNOWN, ADDITIONAL, "Summary");
        public static final Field PDAT = new Field("PDAT", UNKNOWN, ADDITIONAL, "PDAT");
        public static final Field PMID = new Field("PDAT", UNKNOWN, ADDITIONAL, "PDAT");

    }

    public static class Configurations {
        public static final Field DOWNLOAD_LAST_UPDATED = new Field("download_last_updated", OPTIONAL, CONFIGURATION,
                "Last time updated download count");
        public static final Field IGNORE_DATASET_FILE_RETRIEVER = new Field("ignore_dataset_file_retriever", OPTIONAL,
                CONFIGURATION, "Don't fetch dataset's file urls for this dataset");
    }
}
