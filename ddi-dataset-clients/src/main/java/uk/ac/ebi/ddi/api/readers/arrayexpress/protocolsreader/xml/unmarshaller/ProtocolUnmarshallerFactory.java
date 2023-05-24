package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.xml.unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.ModelConstants;

/**
 * yperez
 */
public class ProtocolUnmarshallerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolUnmarshallerFactory.class);

    private static ProtocolUnmarshallerFactory instance = new ProtocolUnmarshallerFactory();

    private static JAXBContext jc = null;

    private ProtocolUnmarshallerFactory() {
    }

    public static ProtocolUnmarshallerFactory getInstance() {
        return instance;
    }

    public Unmarshaller initializeUnmarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.PROTOCOL_MODEL_PKG);
            }

            //create unmarshaller

            return jc.createUnmarshaller();

        } catch (JAXBException e) {
            LOGGER.error("UnmarshallerFactory.initializeUnmarshaller", e);
            throw new IllegalStateException("Could not initialize unmarshaller", e);
        }
    }
}
