package uk.ac.ebi.ddi.geo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.ddi.annotation.service.publication.DDIPublicationAnnotationService;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.geo.model.DocSum;
import uk.ac.ebi.ddi.geo.model.ESummaryResult;
import uk.ac.ebi.ddi.geo.model.Item;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GeoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeoService.class);

    Set<String> omicsType = new HashSet<>();

    private static final Map<String, String> MAPPED_FIELDS = Map.of(
            "entryType", "entry_type",
            "gdsType", "gds_type",
            "valType", "val_type",
            "SeriesTitle", "series_title",
            "PlatformTitle", "platform_title",
            "PlatformTaxa", "platform_taxa",
            "SamplesTaxa", "samples_taxa"
    );

    @Autowired
    DatasetService datasetService;

    DDIPublicationAnnotationService ddiPublicationAnnotationService = DDIPublicationAnnotationService.getInstance();

    public void saveEntries(String filePath) throws IOException {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        parseXMLGEO(targetStream);
    }

    private void parseXMLGEO(InputStream targetStream) {
        try (InputStream is = targetStream) {
            processEntry(is);
        } catch (IOException e) {
            LOGGER.error("Caught IO Exception parsing document : {}", e.getMessage(), e);
        } catch (XMLStreamException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private void processEntry(InputStream is) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(ESummaryResult.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        XMLStreamReader xsr = xif.createXMLStreamReader(is);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ESummaryResult eSummaryResult = (ESummaryResult) unmarshaller.unmarshal(xsr);
        List<DocSum> docSumList = eSummaryResult.getDocSum();
        if(!CollectionUtils.isEmpty(docSumList)){
            docSumList.stream().forEach(
                    this::processDocSumEntry
            );
        }
    }

    private void processDocSumEntry(DocSum docSum){
        Dataset dataset = new Dataset();
        docSum.getItem().stream().forEach(
              item ->   {
                  dataset.setDatabase("GEO");
                  dataset.addAdditional("repository", new HashSet<>(Arrays.asList("GEO")));
                  dataset.addAdditional("omics_type", omicsType);
                  dataset.setCurrentStatus(DatasetCategory.INSERTED.getType());
                  if(item.getType().equalsIgnoreCase("String")) {
                      //Core fields
                      processStringData(dataset, item);
                  } else if (item.getType().equalsIgnoreCase("List")) {
                      processListData(dataset, item);
                  }
              }
        );
        updateDataset(dataset);
    }

    private static void processListData(Dataset dataset, Item item) {
        if(item.getName().equalsIgnoreCase("PubMedIds") && !item.getItem().isEmpty()){
            List<String> pubmedIds = new ArrayList<>();
            List<String> doiIds = new ArrayList<>();
            item.getItem().stream().forEach(
                    itemObj -> {
                        if(itemObj.getType().equalsIgnoreCase("Integer")){
                            pubmedIds.add(itemObj.getContent().toString());
                        } else if(itemObj.getType().equalsIgnoreCase("String")){
                            doiIds.add(itemObj.getContent().toString());
                        }
                    }
            );
            if(!pubmedIds.isEmpty()){
                dataset.addCrossReferences("PMID",new HashSet<>(pubmedIds));
            } else if (!doiIds.isEmpty()){
                dataset.addCrossReferences("DOI",new HashSet<>(doiIds));
            }
        } else if (item.getName().equalsIgnoreCase("ExtRelations") && !item.getItem().isEmpty()) {
            item.getItem().stream().forEach(
                    itemObj -> {
                        if(itemObj.getName().equalsIgnoreCase("ExtRelation") && !itemObj.getItem().isEmpty()){
                            itemObj.getItem().stream().forEach(
                                    itemObjChild -> {
                                        if(itemObjChild.getName().equalsIgnoreCase("TargetObject") && (!itemObjChild.getContent().isEmpty() && itemObjChild.getContent().get(0).startsWith("SR"))){
                                            dataset.addCrossReferenceValue("SRA",itemObjChild.getContent().get(0));
                                        }
                                    }
                            );
                        }
                    }
            );
        } else if (item.getName().equalsIgnoreCase("Samples") && !item.getItem().isEmpty()) {
            Set<String> gsmSet = new HashSet<>();
            item.getItem().stream().forEach(
                    itemObj -> {
                        if(itemObj.getName().equalsIgnoreCase("Sample") && !itemObj.getItem().isEmpty()){
                            itemObj.getItem().stream().forEach(
                                    itemObjChild -> {
                                        if(itemObjChild.getName().equalsIgnoreCase("Accession") && !CollectionUtils.isEmpty(itemObjChild.getContent())){
                                            gsmSet.add(itemObjChild.getContent().get(0));
                                        }
                                    }
                            );
                        }
                    }
            );
            if(!gsmSet.isEmpty())  {
                dataset.addCrossReferences("GSM",gsmSet);
            }
        }
    }

    private void processStringData(Dataset dataset, Item item) {
        if (item.getName().equalsIgnoreCase(DSField.ACCESSION.getName())) {
            if(!CollectionUtils.isEmpty(item.getContent())){
                dataset.setAccession(item.getContent().get(0));
                dataset.addAdditional("full_dataset_link",new HashSet<>(Arrays.asList("https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc="+ item.getContent().get(0))));
            }
        } else if (item.getName().equalsIgnoreCase(DSField.Additional.TITLE.getName())) {
            if(!CollectionUtils.isEmpty(item.getContent())) {
                dataset.setName(item.getContent().get(0));
            }
        } else if (item.getName().equalsIgnoreCase(DSField.Additional.SUMMARY.getName())) {
            if(!CollectionUtils.isEmpty(item.getContent())) {
                dataset.setDescription(item.getContent().get(0));
            }
            //We can also add DOI by parsing the free text
            List<String> dois = ddiPublicationAnnotationService.getDOIListFromText(item.getContent());
            if(!CollectionUtils.isEmpty(dois)){
                dataset.addCrossReferences("DOI",new HashSet<>(dois));
            }

        } else if (item.getName().equalsIgnoreCase(DSField.Additional.PDAT.getName())) {
            //Dates fields
            if(!CollectionUtils.isEmpty(item.getContent())) {
                HashSet<String> pubDateSet = new HashSet<>(Arrays.asList(item.getContent().get(0)));
                HashMap<String, Set<String>> pudDateMap = new HashMap<>();
                pudDateMap.put(DSField.Date.PUBLICATION.getName(), pubDateSet);
                dataset.setDates(pudDateMap);
            }
        } else if (item.getName().equalsIgnoreCase("GPL") || item.getName().equalsIgnoreCase("GSE")) {
            if(!CollectionUtils.isEmpty(item.getContent())) {
                if(item.getContent().get(0).contains(";")){
                    dataset.addCrossReferences(item.getName(), Arrays.stream(item.getContent().get(0).split(";")).collect(Collectors.toSet()));
                } else {
                    dataset.addCrossReferenceValue(item.getName(), item.getContent().get(0));
                }
            }
        } else if (item.getName().equalsIgnoreCase("taxon")) {
            if(!CollectionUtils.isEmpty(item.getContent())){
                if(item.getContent().get(0).contains(";")){
                    dataset.addAdditional(DSField.Additional.SPECIE_FIELD.getName(),Arrays.stream(item.getContent().get(0).split(";")).collect(Collectors.toSet()));
                    dataset.addCrossReferences(item.getName(), Arrays.stream(item.getContent().get(0).split(";")).collect(Collectors.toSet()));
                } else {
                    dataset.addAdditionalField(DSField.Additional.SPECIE_FIELD.getName(), item.getContent().get(0));
                    dataset.addCrossReferenceValue(item.getName(), item.getContent().get(0));
                }
            }
        } else if (item.getName().equalsIgnoreCase("FTPLink")) {
            if(!CollectionUtils.isEmpty(item.getContent())){
                dataset.addAdditionalField(DSField.Additional.DATASET_FILE.getName(), item.getContent().get(0));
            }
        } else if (MAPPED_FIELDS.containsKey(item.getName())) {
            if(!CollectionUtils.isEmpty(item.getContent())){
                dataset.addAdditionalField(MAPPED_FIELDS.get(item.getName()), item.getContent().get(0));
            }
        }
    }


    public void updateDataset(Dataset dataset) {
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
            LOGGER.error("exception while saving dataset {}", exception.getMessage());
        }
    }


}
