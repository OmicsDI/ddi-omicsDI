package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.xml.marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.ModelConstants;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 13-Aug-2010
 * Time: 14:15:35
 * To change this template use File | Settings | File Templates.
 */
public class ExperimentMarshallerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentMarshallerFactory.class);

    private static ExperimentMarshallerFactory instance = new ExperimentMarshallerFactory();

    private static JAXBContext jc = null;

    private ExperimentMarshallerFactory() {
    }

    public static ExperimentMarshallerFactory getInstance() {
        return instance;
    }

    public Marshaller initializeMarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.EXPERIMENT_MODEL_PKG);
            }

            //create unmarshaller

            return jc.createMarshaller();

        } catch (JAXBException e) {
            LOGGER.error("UnimodMarshaller.initializeMarshaller", e);
            throw new IllegalStateException("Could not initialize marshaller", e);
        }
    }
}
