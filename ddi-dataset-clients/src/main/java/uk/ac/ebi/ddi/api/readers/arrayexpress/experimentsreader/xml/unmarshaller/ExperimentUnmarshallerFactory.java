package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.xml.unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.ModelConstants;

/**
 * yperez
 */
public class ExperimentUnmarshallerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentUnmarshallerFactory.class);

    private static ExperimentUnmarshallerFactory instance = new ExperimentUnmarshallerFactory();

    private static JAXBContext jc = null;

    private ExperimentUnmarshallerFactory() {
    }

    public static ExperimentUnmarshallerFactory getInstance() {
        return instance;
    }

    public Unmarshaller initializeUnmarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.EXPERIMENT_MODEL_PKG);
            }

            //create unmarshaller
            return jc.createUnmarshaller();

        } catch (JAXBException e) {
            LOGGER.error("UnmarshallerFactory.initializeUnmarshaller", e);
            throw new IllegalStateException("Could not initialize unmarshaller", e);
        }
    }
}
