package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.xml.marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.ModelConstants;

/**
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 13-Aug-2010
 * Time: 14:15:35
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolMarshallerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolMarshallerFactory.class);

    private static ProtocolMarshallerFactory instance = new ProtocolMarshallerFactory();

    private static JAXBContext jc = null;

    private ProtocolMarshallerFactory() {
    }

    public static ProtocolMarshallerFactory getInstance() {
        return instance;
    }

    public Marshaller initializeMarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.PROTOCOL_MODEL_PKG);
            }

            //create unmarshaller

            return jc.createMarshaller();

        } catch (JAXBException e) {
            LOGGER.error("UnimodMarshaller.initializeMarshaller", e);
            throw new IllegalStateException("Could not initialize marshaller", e);
        }
    }
}
