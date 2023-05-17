package uk.ac.ebi.ddi.api.readers.expressionatlas.utils;

import org.codehaus.stax2.XMLInputFactory2;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yperez on 24/06/2016.
 */
public class FastOmicsDIReader {

    static FastOmicsDIReader instance;

    File source;

    private static final String ENTRY_ELEMENT = "entry";
    private static final String NAME_ELEMENT  = "name";
    private static final String CROSS_ELEMENT = "ref";
    private static final String DBNAME_PROPERTY_ELEMENT = "dbname";
    private static final String DBKEY_PROPERTY_ELEMENT  = "dbkey";
    private static final String DB_ATLAS                = "atlas";

    FastOmicsDIReader(File xmlFile) {
        this.source = xmlFile;
    }

    FastOmicsDIReader() {

    }

    public static FastOmicsDIReader getInstance(File xmlFile){
        if(instance == null)
            instance = new FastOmicsDIReader(xmlFile);
        return instance;
    }

    public static FastOmicsDIReader getInstance(){
        if(instance == null)
            instance = new FastOmicsDIReader();
        return instance;
    }

    public List<Entry> read(File xmlFile) {
        this.source = xmlFile;
        return read();
    }

    public List<Entry> read() {

        List<Entry> elements = new ArrayList<>();

        FileInputStream fileInputStream;
        try {

            fileInputStream = new FileInputStream(source);
            XMLStreamReader xmlStreamReader = XMLInputFactory2.newInstance().createXMLStreamReader(fileInputStream);

            // reading the data
            Entry entry = null;
            while (xmlStreamReader.hasNext()) {

                int eventCode = xmlStreamReader.next();

                // this triggers _users records_ logic
                if ((XMLStreamConstants.START_ELEMENT == eventCode) && xmlStreamReader.getLocalName().equalsIgnoreCase(ENTRY_ELEMENT)) {
                    // read and parse the user data rows
                    entry = new Entry();
                    int attributesCount = xmlStreamReader.getAttributeCount();
                    for (int i = 0; i < attributesCount; i++) {
                        entry.setId(xmlStreamReader.getAttributeValue(i));
                    }
                }else if ((XMLStreamConstants.END_ELEMENT == eventCode) && xmlStreamReader.getLocalName().equalsIgnoreCase(ENTRY_ELEMENT)) {
                    elements.add(entry);
                    entry = null;
                }else if ((XMLStreamConstants.START_ELEMENT == eventCode) && xmlStreamReader.getLocalName().equalsIgnoreCase(CROSS_ELEMENT)) {
                    String dbName = null;
                    String dbKey  = null;
                    int attributesCount = xmlStreamReader.getAttributeCount();
                    for (int i = 0; i < attributesCount; i++)
                        if(xmlStreamReader.getAttributeLocalName(i).equalsIgnoreCase(DBNAME_PROPERTY_ELEMENT))
                            dbName = xmlStreamReader.getAttributeValue(i);
                        else if(xmlStreamReader.getAttributeLocalName(i).equalsIgnoreCase(DBKEY_PROPERTY_ELEMENT))
                            dbKey = xmlStreamReader.getAttributeValue(i);
                    if(dbName != null && dbName.equalsIgnoreCase(DB_ATLAS)){
                        entry.addCrossReferenceValue(dbName, dbKey);
                    }
                }else if ((XMLStreamConstants.START_ELEMENT == eventCode) && xmlStreamReader.getLocalName().equalsIgnoreCase(NAME_ELEMENT) && entry != null) {
                    entry.setName(xmlStreamReader.getElementText());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return elements;

    }

}