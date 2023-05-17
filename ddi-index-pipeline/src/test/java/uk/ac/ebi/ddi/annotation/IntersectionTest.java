package uk.ac.ebi.ddi.annotation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.annotation.service.synonyms.DDIExpDataImportService;
import uk.ac.ebi.ddi.annotation.utils.DataType;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.service.similarity.DatasetStatInfoService;
import uk.ac.ebi.ddi.service.db.service.similarity.ExpOutputDatasetService;
import uk.ac.ebi.ddi.service.db.service.similarity.TermInDBService;
import uk.ac.ebi.ddi.xml.validator.exception.DDIException;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Reference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class  IntersectionTest{

    @Autowired
    TermInDBService termInDBService = new TermInDBService();

    @Autowired
    ExpOutputDatasetService expOutputDatasetService = new ExpOutputDatasetService();

    @Autowired
    DDIExpDataImportService ddiExpDataImportService = new DDIExpDataImportService();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    DatasetStatInfoService datasetStatInfoService= new DatasetStatInfoService();

    private OmicsXMLFile reader;

    @Before
    public void setUp() throws Exception {

        URL fileURL = IntersectionTest.class.getClassLoader().getResource("pride-files/PRIDE_EBEYE_PRD000123.xml");

        assert fileURL != null;
        reader = new OmicsXMLFile(new File(fileURL.toURI()));

    }

    @Test
    public void testGetEntryIds() throws Exception {

        Assert.assertEquals(reader.getEntryIds().size(),1);

    }

    @Test
    public void testGetEntryById() throws Exception {

        Entry entry = reader.getEntryById("PRD000123");

        Assert.assertEquals(entry.getName().getValue(), "Large scale qualitative and quantitative profiling of tyrosine phosphorylation using a combination of phosphopeptide immuno-affinity purification and stable isotope dimethyl labeling");

        System.out.println(entry.toString());

    }

    @Test
    public void testMetabolomicsInsert() throws Exception {

        URL urlMetabolomics = IntersectionTest.class.getClassLoader().getResource("metabolites-files");

        assert urlMetabolomics != null;
        File folder = new File(urlMetabolomics.toURI());
        String dataType = DataType.METABOLOMICS_DATA.getName();

        File[] listOfFiles = folder.listFiles();

        if (termInDBService == null) {
            System.err.println("termInDBService is null");
            System.exit(1);
        }

        //delete all data in Mongodb
//        expOutputDatasetService.deleteAll();
//        termInDBService.deleteAll();
//        datasetStatInfoService.deleteAll();


//        int iterTime = 199;
        int index = 1;
        int fileindex = 1;
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().toLowerCase().endsWith("xml")) {
                    System.out.println("\n\n"+fileindex + "-" + file.getName()+":");
                    fileindex++;
                    reader = new OmicsXMLFile(file);
                    String database = reader.getName();
                    for (int i=0; i < reader.getEntryIds().size(); i++) {
                        System.out.println("deal the" + index + "entry in "+file.getName()+";");
                        index++;
                        Entry entry = reader.getEntryByIndex(i);
                        String entryAccession = entry.getId();
                        List<Reference> refs = entry.getCrossReferences().getRef();
                        ddiExpDataImportService.importDatasetTerms(dataType, entryAccession,
                                entry.getAdditionalFieldValue(DSField.Additional.REPOSITORY.getName()), refs);
                    }
                }
            }
        }

    }


    @Test
    public void testProteomicsImportMethod() throws Exception {

        URL urlProteomics = IntersectionTest.class.getClassLoader().getResource("pride-files");

        assert urlProteomics != null;
        File folder = new File(urlProteomics.toURI());
        String dataType = DataType.PROTEOMICS_DATA.getName();

        File[] listOfFiles = folder.listFiles();

        if (termInDBService == null) {
            System.err.println("termInDBService is null");
            System.exit(1);
        }

        //delete all data in Mongodb
//        expOutputDatasetService.deleteAll();
//        termInDBService.deleteAll();
//        datasetStatInfoService.deleteAll();


//        int iterTime = 199;
        int index = 1;
        int fileindex = 1;
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().toLowerCase().endsWith("xml")) {
                    System.out.println("\n\n"+fileindex + "-" + file.getName()+":");
                    fileindex++;
                    reader = new OmicsXMLFile(file);
                    String database = reader.getName();
                    for (int i=0; i<reader.getEntryIds().size(); i++) {
                        System.out.println("deal the" + index + "entry in "+file.getName()+";");
                        index++;
                        Entry entry = reader.getEntryByIndex(i);
                        String entryId = entry.getId();
                        List<Reference> refs = entry.getCrossReferences().getRef();
                        ddiExpDataImportService.importDatasetTerms(dataType, entryId,database,refs);
                    }
                }
            }
        }

    }


    @Test
     public void testImportMethod() throws Exception {

        URL urlFiles= IntersectionTest.class.getClassLoader().getResource("201512new/gpmdb");
        assert urlFiles!= null;
        File folder = new File(urlFiles.toURI());

//        File[] listOfFiles = folder.listFiles();
        List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        if (termInDBService == null) {
            System.err.println("termInDBService is null");
            System.exit(1);
        }

        //delete all data in Mongodb
//        expOutputDatasetService.deleteAll();
//        termInDBService.deleteAll();
//        datasetStatInfoService.deleteAll();


//        int iterTime = 199;
        int index = 1;
        int fileindex = 1;
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().toLowerCase().endsWith("xml")) {
                    System.out.println("\n\n"+fileindex + "-" + file.getName()+":");
                    fileindex++;
                    reader = new OmicsXMLFile(file);
                    String database = reader.getName();
                    for (int i=0; i<reader.getEntryIds().size(); i++) {
                        System.out.println("deal the " + index + " entry in "+file.getName()+";");
                        index++;
                        Entry entry = reader.getEntryByIndex(i);
                        String entryId = entry.getId();
                        String dataType = entry.getAdditionalFieldValue("omics_type");
                        List<Reference> refs = entry.getCrossReferences().getRef();
                        ddiExpDataImportService.importDatasetTerms(dataType, entryId,database,refs);
                    }
                }
            }
        }

    }






    @Test
    public void marshall() throws DDIException {

        FileWriter fw;
        File tmpFile;
        try {
            tmpFile = File.createTempFile("tmpMzML", ".xml");
            tmpFile.deleteOnExit();
            fw = new FileWriter(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create or write to temporary file for marshalling.");
        }

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Entry entry = reader.getEntryById("PRD000123");

        Database database = new Database();
        database.setDescription("new description");
        database.setEntryCount(10);
        database.setRelease("2010");
        Entries entries = new Entries();
        entries.addEntry(entry);
        database.setEntries(entries);
        mm.marshall(database, fw);

        OmicsXMLFile.isSchemaValid(tmpFile);


    }

    @Test
    public void testGetEntryByIndex() throws Exception {

        int index = 0;

        Entry entry = reader.getEntryByIndex(index);

        Assert.assertEquals(entry.getName().getValue(), "Large scale qualitative and quantitative profiling of tyrosine phosphorylation using a combination of phosphopeptide immuno-affinity purification and stable isotope dimethyl labeling");

        System.out.println(entry.toString());

    }

    public static int getIntersection(HashSet<String> set1, HashSet<String> set2) {
        boolean set1IsLarger = set1.size() > set2.size();
        Set<String> cloneSet = new HashSet<>(set1IsLarger ? set2 : set1);
        cloneSet.retainAll(set1IsLarger ? set1 : set2);
        return cloneSet.size();
    }
}