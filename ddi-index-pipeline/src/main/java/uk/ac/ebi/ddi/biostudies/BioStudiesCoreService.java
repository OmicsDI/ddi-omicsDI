package uk.ac.ebi.ddi.biostudies;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ddi.annotation.utils.DatasetUtils;
import uk.ac.ebi.ddi.biostudies.model.*;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BioStudiesCoreService {

    public static final String SUBMISSIONS = "submissions";
    public static final String TITLE = "Title";
    public static final String ABSTRACT = "Abstract";
    public static final String EXPERIMENTTYPE = "Experiment type";
    public static final String ORGANISM = "Organism";
    public static final String DISEASESTATE = "Diseasestate";
    public static final String PUBLICATION = "ReleaseDate";
    public static final String DESCRIPTION = "Description";
    //public static final String

    private static final String       PUBLICATION_LOOKUP_DOMAIN         = "europepmc";
    private static final String       PUBLICATION_LOOKUP_ABSTRACT_FIELD = "description";
    private static final List<String> PUBLICATION_LOOKUP_FIELDS         = Collections.singletonList(PUBLICATION_LOOKUP_ABSTRACT_FIELD);

    @Autowired
    DatasetService datasetService;

    private final EbiSearchHttpLookup httpLookup = new EbiSearchHttpLookup("https://www.ebi.ac.uk/ebisearch/ws/rest");;

    Set<String> omicsType = new HashSet<String>();

    private static final Set<String> LINKS_TO_SKIP = Set.of("External link",    // Non-specific external links
                                                            "HipSci FTP site",  // FTP links, not xrefs
                                                            "kegg.pathway",     // no useful content, just "https"
                                                            "NA",               // Not applicable?
                                                            "Copyright",        // Plain text
                                                            "Disclaimer",       // Plain text
                                                            "Keywords",         // Plain text
                                                            "Received"          // Plain text
    );

    public static final Logger LOGGER = LoggerFactory.getLogger(BioStudiesCoreService.class);




    public void saveStudies(String filePath, String repository, String omics_type) throws IOException {
        omicsType.add(omics_type);
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        parseJsonBioStudies(targetStream, repository, omicsType);
    }

    private void parseJsonBioStudies(InputStream is, String repository, Set<String> omicsType) throws IOException {
        // Create and configure an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Create a JsonParser instance
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String line;
            while ((line = br.readLine()) != null) {
                final DocSubmission submission = objectMapper.readValue(line, DocSubmission.class);
                saveEntry(submission, repository, omicsType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }


    private void saveEntry(DocSubmission submission, String repository, Set<String> omicsType){
        if (submission.getSection().getType().equals("Study")) {
            Dataset dataset = transformSubmissionDataset(submission, repository, omicsType);
            saveMetadataDetails(submission, repository, dataset);
            if (!dataset.getAdditional().containsKey("additional_accession")) {
                updateDataset(dataset);
            } else {
                String accession = dataset.getAdditional().get("additional_accession")
                                          .iterator().next();
                List<Dataset> datasetList = datasetService.findByAccession(accession);
                if (datasetList.size() > 0) {
                    Dataset dataset1 =  datasetList.get(0);
                    if (dataset1.getAdditional().containsKey("additional_accession")) {
                        dataset1.getAdditional().get("additional_accession")
                                .add(dataset.getAccession());
                        updateDataset(dataset1);
                    }
                    updateDataset(dataset);
                } else {
                    updateDataset(dataset);
                }
            }
        }
    }

    private void saveMetadataDetails(DocSubmission submission, String repository, Dataset dataset) {
        DocSection topLevelSection = submission.getSection();
        if(repository.equalsIgnoreCase("biostudies-literature")){
            dataset.addAdditionalField(DSField.Additional.PUBMED_ABSTRACT.key(),findAttributeByName(topLevelSection.getAttributes(), "Abstract").getValue());
            dataset.addAdditionalField(DSField.Additional.PUBMED_TITLE.key(),findAttributeByName(topLevelSection.getAttributes(), "Title").getValue());
            for (DocSection section : topLevelSection.getSections()) {
                switch (section.getType()) {
                case "Funding" -> saveFundingFields(section,dataset);
                case "Publication" -> savePublicationFields(section,dataset);
                case "Author" -> saveAuthor(section,dataset);
                case "Organization" -> saveOrganisation(section,dataset);
                default -> { /* Do nothing */ }
                }
            }
        } else if (repository.equalsIgnoreCase("bioimages")){
            if(attributeHasValue(findAttributeByName(submission.getAttributes(), "AttachTo"))) {
                dataset.addAdditionalField(DSField.Additional.ATTACH_TO.key(),findAttributeByName(submission.getAttributes(), "AttachTo").getValue());
            }
            if(attributeHasValue(findAttributeByName(submission.getAttributes(), "License"))) {
                dataset.addAdditionalField(DSField.Additional.LICENSE.key(), findAttributeByName(topLevelSection.getAttributes(), "License").getValue());
            }
            if(topLevelSection != null){
                for (DocSection section : topLevelSection.getSections()) {
                    if(section.getType() != null){
                        switch (section.getType()) {
                        case "Author":
                            saveAuthor(section,dataset);
                            break;
                        case "author":
                            saveAuthor(section,dataset);
                            break;
                        case "Publication":
                            savePublicationFields(section,dataset);
                            break;
                        default:
                            saveFigure(section,dataset);
                        }
                    }
                }
            }
        } else if (repository.equalsIgnoreCase("biostudies-arrayexpress")){
            final String acc = submission.getAccNo();
            DocAttribute studyTypeAttr = findAttributeByName(topLevelSection.getAttributes(), "Study type");
            if (attributeHasValue(studyTypeAttr)) {
                dataset.addAdditionalField(DSField.Additional.OMICS.key(),DSField.ArrayExpressType.getByType(studyTypeAttr.getValue()).getOmicsType());
                dataset.addAdditionalField(DSField.Additional.STUDY_TYPE.key(),studyTypeAttr.getValue());

                // Add the top-level EFO cross-reference, if available
                addEfoCrossReference(studyTypeAttr,dataset);
            }
            addAttributeValueIfNotNull(findAttributeByName(topLevelSection.getAttributes(), "Description"), DSField.DESCRIPTION.key(), dataset);
            addAttributeValueIfNotNull(findAttributeByName(topLevelSection.getAttributes(), "Organism"), DSField.Additional.ORGANISM.key(), dataset);

            if(topLevelSection != null){
                for (DocSection section : topLevelSection.getSections()) {
                    if(section.getType() != null){
                        switch (section.getType()) {
                        case "Author":
                            saveAuthor(section,dataset);
                            break;
                        case "author":
                            saveAuthor(section,dataset);
                            break;
                        case "Publication":
                            processPublicationSection(section,dataset);
                            break;
                        case "Samples":
                            saveDiseaseField(section,dataset);
                            break;
                        default:
                            saveFigure(section,dataset);
                        }
                    }
                }
            }


            //addAttributeValueIfNotNull(findAttributeByName(topLevelSection.getAttributes(), "DiseaseState"), DSField.Additional.DISEASE_FIELD.key(), dataset);


            for (DocSection section : topLevelSection.getSections()) {
                if (section.getMetaClass().equals("ac.uk.ebi.biostd.persistence.doc.model.DocSectionTable")) {
                    // Probably the Protocols table - loop through each sub-section
                    processDocSectionTable(section,dataset);
                }
                else if ("Author".equals(section.getType())) {
                    // Handle the author
                    saveAuthor(section,dataset);
                } /*else if ("Title".equalsIgnoreCase(section.getType())) {
                    dataset.addAdditionalField(DSField.Additional.PUBMED_TITLE.key(),findAttributeByName(section.getAttributes(), "Title").getValue());
                }*/
                else if ("Publication".equals(section.getType())) {
                    // Handle the publication data
                    processPublicationSection(section,dataset);
                }
            }

            // Deal with any links
            if (topLevelSection.getLinks() != null && !topLevelSection.getLinks().isEmpty()) {
                processLinks(topLevelSection.getLinks(), acc, dataset);
            }
        } else if (repository.equalsIgnoreCase("biostudies-other")) {
            // Get the top-level section
            DocSection topLevel = submission.getSection();
            // Process the top-level attributes
            for (DocAttribute attr : topLevel.getAttributes()) {
                if (attributeHasValue(attr)) {
                    switch (attr.getName()) {
                    case "Abstract" -> processField("abstract", attr.getValue(),dataset);
                    case "Project" -> processField("project", attr.getValue(),dataset);
                    case "Experiment type" -> processField("experiment_type", attr.getValue(),dataset);
                    case "Organism" -> processField("species", attr.getValue(),dataset);
                    default -> addEfoCrossReference(attr,dataset);
                    }
                }
            }

            // Loop through the top-level sub-sections
            for (DocSection section : topLevel.getSections()) {
                if (section.getType() == null) {
                    continue;
                }

                switch (section.getType()) {
                case "Author" -> saveAuthor(section,dataset);
                case "Organisation", "Organization" -> saveOrganisation(section,dataset);
                case "Publication" -> savePublicationFields(section,dataset);
                case "Funding" -> saveFundingFields(section,dataset);
                default -> { /* Do nothing */ }
                }
            }

            // Process the top-level links
            processLinks(topLevel.getLinks(), submission.getAccNo(), dataset);

            // Loop through the top-level attributes
            for (DocAttribute attribute : submission.getAttributes()) {
                if (attributeHasValue(attribute) && "DataSource".equals(attribute.getName())) {
                    processField("data_source", attribute.getValue(), dataset);
                }
            }
        }
    }

    private void processPublicationSection(DocSection publicationSection, Dataset dataset) {
        // Add the PMID, if present
        String acc = null;
        if (publicationSection.getAccNo() != null && !publicationSection.getAccNo().isEmpty()) {
            acc = publicationSection.getAccNo();
            DatasetUtils.addCrossReferenceValue(dataset, DSField.CrossRef.PUBMED.getName(), acc);
        }
        // Add the title and authors, if present
        addAttributeValueIfNotNull(findAttributeByName(publicationSection.getAttributes(), "Title"), "pubmed_title", dataset);
        addAttributeValueIfNotNull(findAttributeByName(publicationSection.getAttributes(), "Authors"), "pubmed_authors", dataset);
        // Add the DOI xref, if present
        DocAttribute doiAttr = findAttributeByName(publicationSection.getAttributes(), "DOI");
        String doi = null;
        if (attributeHasValue(doiAttr)) {
            // Trim down to just the DOI, not the full URL
            doi = doiAttr.getValue().replaceAll("^https?://doi\\.org/", "");
            DatasetUtils.addCrossReferenceValue(dataset, DSField.CrossRef.DOI.getName(), doi);
        }

        // Fill in abstract, if available
        String query = null;
        if (acc != null) {
            query = "id:" + acc;
        }
        else if (doi != null) {
            query = String.format("DOI:\"%s\"", doi);
        }

        if (query != null) {
            List<Map<String, Object>> response = httpLookup.search(PUBLICATION_LOOKUP_DOMAIN, query, PUBLICATION_LOOKUP_FIELDS, 1);
            if (!response.isEmpty()) {
                final String abstractValue = extractAbstractFromResult(response.get(0));
                if (abstractValue != null && !abstractValue.isBlank()) {
                    processField("pubmed_abstract", abstractValue,dataset);
                }
            }
        }
    }

    private String extractAbstractFromResult(Map<String, Object> result) {
        final String abstractString;
        final Object abstractObject = result.get(PUBLICATION_LOOKUP_ABSTRACT_FIELD);
        if (abstractObject instanceof List<?> abstractList && !abstractList.isEmpty()) {
            abstractString = abstractList.stream().map(Object::toString).filter(s -> !s.isBlank()).collect(Collectors.joining("; "));
        }
        else if (abstractObject instanceof String absString) {
            abstractString = absString;
        }
        else {
            abstractString = null;
        }
        return abstractString;
    }

    private void processField(String fieldName , String fieldValue , Dataset dataset) {
        dataset.addAdditionalField(fieldName,fieldValue);
    }

    private void processLinks(List<DocLink> links, String acc, Dataset dataset) {
        for (DocLink link : links) {
            if (link.getLinks() != null) {
                processLinks(link.getLinks(), acc, dataset);
            }

            if (link.getAttributes() != null) {
                DocAttribute typeAttr = findAttributeByName(link.getAttributes(), "Type");
                // Skip anything where the reference/URL == the current acc
                if (attributeHasValue(typeAttr) && link.getUrl() != null && !acc.equals(link.getUrl())) {
                    dataset.addCrossReferenceValue(typeAttr.getValue(), link.getUrl());
                }
            }
        }
    }

    private void processDocSectionTable(DocSection tableSection,Dataset dataset) {
        for (DocSection rowSection : tableSection.getSections()) {
            if (rowSection.getType() == null) {
                continue;
            }

            if (rowSection.getType().equals("Protocols")) {
                processProtocolSectionRow(rowSection,dataset);
            }
        }
    }

    private void processProtocolSectionRow(DocSection protocolSection,Dataset dataset) {
        DocAttribute typeAttr = findAttributeByName(protocolSection.getAttributes(), "Type");
        DocAttribute descAttr = findAttributeByName(protocolSection.getAttributes(), "Description");
        DocAttribute hardwareAttr = findAttributeByName(protocolSection.getAttributes(), "Hardware");
        DocAttribute softwareAttr = findAttributeByName(protocolSection.getAttributes(), "Software");

        if (attributeHasValue(typeAttr) && attributeHasValue(descAttr)) {
            DSField.Protocols protocol = DSField.Protocols.getByType(typeAttr.getValue());
            if (protocol == null) {
                LOGGER.warn("Unrecognized protocol type {}", typeAttr.getValue());
            }
            else {
                // Some Type entries have an EFO ontology reference
                addEfoCrossReference(typeAttr,dataset);

                final String protocolText = protocol.getName() + " - " + descAttr.getValue();
                if (DSField.DATA_PROTOCOLS_FIELD.equals(protocol.getField())) {
                    dataset.addAdditionalField(DSField.DATA_PROTOCOLS_FIELD, protocolText);
                }
                else if (DSField.SAMPLE_PROTOCOLS_FIELD.equals(protocol.getField())) {
                    dataset.addAdditionalField(DSField.SAMPLE_PROTOCOLS_FIELD, protocolText);
                }

                // Add the hardware and software values, if available
                addAttributeValueIfNotNull(hardwareAttr, "instrument_platform",dataset);
                addAttributeValueIfNotNull(softwareAttr, "software",dataset);
            }
        }
    }

    private void addEfoCrossReference(DocAttribute attribute,Dataset dataset) {
        boolean foundOntology = false;
        String termId = null;

        for (Attr attr : attribute.getValueAttrs()) {
            if ("Ontology".equals(attr.getName()) && "EFO".equals(attr.getValue())) {
                foundOntology = true;
            }
            else if ("TermId".equals(attr.getName())) {
                termId = attr.getValue();
            }
        }

        if (foundOntology && termId != null) {
            dataset.addCrossReferenceValue("EFO", termId);
        }
    }

    private void saveFigure(DocSection figureSection,Dataset dataset) {

        String figureType = figureSection.getType();
        if (figureType != null) {
            dataset.addAdditionalField(DSField.Additional.FIGURE_TYPE.key(),figureType);
        }
        // Loop over third Level section
        for (DocSection figureSubSection : figureSection.getSections()) {
            saveSubFigure(figureSubSection,dataset);
        }
    }

    private void saveSubFigure(DocSection figureSubSection, Dataset dataset) {

        String figureSubSectionType = figureSubSection.getType();
        if (figureSubSectionType != null) {
            dataset.addAdditionalField(DSField.Additional.FIGURE_SUB.key(),figureSubSectionType);
        }
        // Loop over Fourth Level Section
        for (DocSection ImageSection : figureSubSection.getSections()) {
            saveImage(ImageSection,dataset);
        }
    }

    private void saveImage(DocSection ImageSection, Dataset dataset) {
        String ImageSectionType = ImageSection.getType();
        if (ImageSectionType != null) {
            dataset.addAdditionalField(DSField.Additional.FIGURE_SUB.key(),ImageSectionType);
        }
        if(ImageSection.getAttributes() != null){
            DocAttribute legend = findAttributeByName(ImageSection.getAttributes(), "Legend");
            if(attributeHasValue(legend)){
                dataset.addAdditionalField(DSField.Additional.LEGEND.key(),legend.getValue());
            }
        }
    }

    private void saveAuthor(DocSection authorSection, Dataset dataset) {
        addAttributeValueIfNotNull(findAttributeByName(authorSection.getAttributes(), "Name"), DSField.Additional.PUBMED_AUTHORS.key(), dataset);
    }

    private void saveDiseaseField(DocSection sampleSection, Dataset dataset) {
        for (DocSection sampleChildSection : sampleSection.getSections()) {
            if(sampleChildSection.getType().equalsIgnoreCase("Source Characteristics")) {
                addAttributeValueIfNotNull(findAttributeByName(sampleChildSection.getAttributes(), DISEASESTATE), DSField.Additional.DISEASE_FIELD.key(), dataset);
            }
        }
    }



    private void saveOrganisation(DocSection diseaseSection,  Dataset dataset) {
        addAttributeValueIfNotNull(findAttributeByName(diseaseSection.getAttributes(), "DiseaseState"), DSField.Additional.DISEASE_FIELD.key(), dataset);
    }

    protected void addAttributeValueIfNotNull(DocAttribute attr, String fieldName, Dataset dataset) {
        if (attributeHasValue(attr)) {
            dataset.addAdditionalField(fieldName,attr.getValue());
        }
    }
    private void saveFundingFields(DocSection fundingSection,Dataset dataset) {
        for (DocAttribute attr : fundingSection.getAttributes()) {
            if (attr.getName() == null) {
                continue;
            }
            switch (attr.getName()) {
            case "Agency" -> dataset.addAdditionalField(DSField.Additional.FUNDING.key(),attr.getValue());
            case "grant_id" -> dataset.addAdditionalField(DSField.Additional.FUNDING_GRANT_ID.key(),attr.getValue());
            default -> { /* Do nothing */ }
            }
        }
    }

    private void savePublicationFields(DocSection publicationSection,Dataset dataset) {
        // Handle the attributes
        for (DocAttribute attr : publicationSection.getAttributes()) {
            if (attr.getName() == null) {
                continue;
            }
            Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
            switch (attr.getName()) {
            case "Journal" -> dataset.addAdditionalField(DSField.Additional.JOURNAL.key(),attr.getValue());
            case "Volume" -> dataset.addAdditionalField(DSField.Additional.VOLUME.key(),attr.getValue());
            case "Pages" -> dataset.addAdditionalField(DSField.Additional.PAGINATION.key(),attr.getValue());
            case "Publication date" -> {
                dates.put(DSField.Date.PUBLICATION.key(), Collections.singleton(attr.getValue()));
                dataset.setDates(dates);
            }
            default -> { /* Do nothing */ }
            }
        }

        // Extract links
        savePublicationLinks(publicationSection,dataset);
    }

    private void savePublicationLinks(DocSection publicationSection, Dataset dataset) {
        if (publicationSection.getLinks() != null && !publicationSection.getLinks().isEmpty()) {
            DocLink linkTable = publicationSection.getLinks().get(0);
            if(linkTable != null && linkTable.getLinks() != null){
                for (DocLink link : linkTable.getLinks()) {
                    DocAttribute linkType = findAttributeByName(link.getAttributes(), "Type");
                    if (!attributeHasValue(linkType)) {
                        continue;
                    }
                    String type = linkType.getValue();
                    if ("PMC".equals(type)) {
                        dataset.addAdditionalField(DSField.Additional.PMCID.key(), link.getUrl());
                    }
                    else if ("PMID".equals(type)) {
                        DatasetUtils.addCrossReferenceValue(dataset, DSField.CrossRef.PUBMED.getName(),  link.getUrl());
                    } else if ("DOI".equals(type)) {
                        DatasetUtils.addCrossReferenceValue(dataset, DSField.CrossRef.DOI.getName(),  link.getUrl());
                    }
                }
            }
        }
    }

    protected static boolean attributeHasValue(DocAttribute attribute) {
        return attribute != null && attribute.getValue() != null;
    }

    public Dataset transformSubmissionDataset(DocSubmission submissions, String repository, Set<String> omicsType) {
        Dataset dataset = new Dataset();
        try {
            HashSet<String> datasetLink = new HashSet<String>();
            Map<String, String> subsections = null;
            datasetLink.add("https://www.ebi.ac.uk/biostudies/studies/" + submissions.getAccNo());
            HashSet<String> authors = new HashSet<String>();
            dataset.setAccession(submissions.getAccNo());
            dataset.setDatabase(repository);
            dataset.setCurrentStatus(DatasetCategory.INSERTED.getType());
            dataset.addAdditional("omics_type", omicsType);
            dataset.addAdditional("full_dataset_link", datasetLink);
            Map<String, String> sectionMap = submissions.getSection().getAttributes() != null ?
                  submissions.getSection().getAttributes().stream().filter(attribute -> attribute.getName() != null && attribute.getValue() != null)
                             .collect(Collectors.toMap(DocAttribute::getName, DocAttribute::getValue, (a1, a2) -> a1)) : null;
            Map<String, String> attributesMap = submissions.getAttributes() != null ?
                  submissions.getAttributes().stream().filter(attribute -> attribute.getName() != null && attribute.getValue() != null)
                             .collect(Collectors.toMap(DocAttribute::getName, DocAttribute::getValue, (a1, a2) -> a1)) : null;

            Map<String, String> linksMap = null;
            List<DocLink> linksList = submissions.getSection().getLinks();
            if (submissions.getSection() != null && submissions.getSection().getSections() != null) {
                subsections = submissions.getSection().getSections()
                                         .stream().filter(r -> r.getType() != null && r.getType().equals("Author")).map(r -> r.getAttributes())
                                         .flatMap(x -> x.stream()).filter(attribute -> attribute.getName() != null && attribute.getValue() != null).collect(Collectors.toMap(DocAttribute::getName,
                                                                                                                                                                             DocAttribute::getValue, (a1, a2) -> a1));
                authors.add(subsections.get("Name"));
                dataset.addAdditional("submitter", authors);
            }
            if (linksList != null && linksList.size() > 0 && linksList.get(0).getAttributes() != null) {
                linksMap = linksList.stream().filter(l -> l.getAttributes() != null && l.getAttributes().size() > 0).map(DocLink::getAttributes)
                                    .flatMap(x -> x.stream()).filter(attribute -> attribute.getName() != null && attribute.getValue() != null).collect(Collectors.toMap(DocAttribute::getName,
                                                                                                                                                                        DocAttribute::getValue, (a1, a2) -> a1));
            }
            if (linksMap != null && linksMap.containsKey("Type")) {
                String accession = linksList.get(0).getUrl();
                HashSet<String> setSecAcc = new HashSet<String>();
                setSecAcc.add(accession);
                dataset.addAdditional("additional_accession", setSecAcc);
            }
            if (sectionMap != null && sectionMap.containsKey(ABSTRACT.toString())) {
                dataset.setDescription(sectionMap.get(ABSTRACT.toString()));
            } else if (sectionMap != null && sectionMap.containsKey(DESCRIPTION.toString())) {
                dataset.setDescription(sectionMap.get(DESCRIPTION.toString()));
            }

            //Based on request from biostudies team
            if (submissions.getTitle() != null && !submissions.getTitle().isEmpty()) {
                dataset.setName(submissions.getTitle());
            } else if (attributesMap != null && attributesMap.containsKey(TITLE.toString())) {
                dataset.setName(attributesMap.get(TITLE.toString()));
            } else if (sectionMap != null && sectionMap.containsKey(TITLE.toString())) {
                dataset.setName(sectionMap.get(TITLE.toString()));
            }

            HashSet<String> setData = new HashSet<String>();
            HashSet<String> setOrganisms = new HashSet<String>();

            if(!dataset.getDates().containsKey(DSField.Date.PUBLICATION.key())){
                Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
                if (attributesMap != null && attributesMap.containsKey(PUBLICATION.toString())) {
                    setData.add(attributesMap.get(PUBLICATION.toString()));
                }
                //dates.put()
                dates.put("publication", setData);
                dataset.setDates(dates);
            }

            dataset.addAdditional("repository", new HashSet<>(Arrays.asList(repository)));

            //setData.clear();
            if (sectionMap != null && sectionMap.containsKey(ORGANISM.toString())) {
                setOrganisms.add(sectionMap.get(ORGANISM.toString()));
                dataset.getAdditional().put("species", setOrganisms);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("exception while parsing submission into dataset in transformsubmission", ex.getMessage());
        }
        return dataset;
    }

    protected static DocAttribute findAttributeByName(List<DocAttribute> attributes, String name) {
        return attributes.stream()
                         .filter(a -> name.equals(a.getName()))
                         .findFirst()
                         .orElse(null);
    }

    private void updateDataset(Dataset dataset) {
        try {
            Dataset inDataset = datasetService.
                  read(dataset.getAccession(), dataset.getDatabase());
            if (inDataset != null) {
                datasetService.update(inDataset.getId(), dataset);
            } else {
                datasetService.save(dataset);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error("exception while saving dataset", exception.getMessage());
        }
    }
}
