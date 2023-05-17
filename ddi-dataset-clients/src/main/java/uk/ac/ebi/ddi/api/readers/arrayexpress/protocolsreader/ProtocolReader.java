package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols.Protocols;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.xml.unmarshaller.ProtocolUnmarshallerFactory;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;


/**
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 18-Jul-2011
 * Time: 12:13:31
 */
public class ProtocolReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolReader.class);

    /**
     * internal unmashaller
     */
    private Unmarshaller unmarshaller = null;
    /**
     * internal xml extractor
     */

    private Protocols protocols = null;


    public ProtocolReader(InputStream inputStream) {
        try {
            // create unmarshaller
            this.unmarshaller = ProtocolUnmarshallerFactory.getInstance().initializeUnmarshaller();
            protocols = (Protocols) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Error unmarshalling InputStream: " + e.getMessage(), e);
        }
    }

    public ProtocolReader(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Xml file to be indexed must not be null");
        }
        // create extractor
        // this.extractor = new UnimodExtractor(xml);

        try {
            // create unmarshaller
            this.unmarshaller = ProtocolUnmarshallerFactory.getInstance().initializeUnmarshaller();
            protocols = (Protocols) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Error unmarshalling XML file: " + e.getMessage(), e);
        }
    }

    public Protocols getProtocols() {
        return protocols;
    }
}
