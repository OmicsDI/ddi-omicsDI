package uk.ac.ebi.ddi.xml.validator.parser.marshaller;

import com.ctc.wstx.api.EmptyElementHandler;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import org.codehaus.stax2.XMLOutputFactory2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.IDataObject;
import uk.ac.ebi.ddi.xml.validator.parser.model.ModelConstants;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;


public class OmicsDataMarshaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmicsDataMarshaller.class);

    public <T extends IDataObject> String marshall(T object) {
        StringWriter sw = new StringWriter();
        this.marshall(object, sw);
        return sw.toString();
    }

    public <T extends IDataObject> void marshall(T object, OutputStream os) {
        this.marshall(object, new OutputStreamWriter(os));
    }

    public <T extends IDataObject> void marshall(T object, Writer out) {

        if (object == null) {
            throw new IllegalArgumentException("Cannot marshall a NULL object");
        }

        try {
            MarshallerFactory marshallerFactory = MarshallerFactory.getInstance();

            Marshaller marshaller = marshallerFactory.initializeMarshaller();

            // Set JAXB_FRAGMENT_PROPERTY to true for all objects that do not have
            // a @XmlRootElement annotation
            // ToDo: add handling of omicsDI (-> add flag to control treatment as fragment or not)
            if (!(object instanceof Database)) {
                marshaller.setProperty(ModelConstants.JAXB_FRAGMENT_PROPERTY, true);
                marshaller.setProperty(ModelConstants.JAXB_FORMATTING_PROPERTY, false);
                String encoding = (String) marshaller.getProperty(Marshaller.JAXB_ENCODING);

                marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

                // Specify the new header
                marshaller.setProperty(
                        "com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.1\" encoding=\"" + encoding + "\">");

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Object '" + object.getClass().getName() +
                            "' will be treated as root element.");
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Object '" + object.getClass().getName() + "' will be treated as fragment.");
                }
            }

            QName aQName = ModelConstants.getQNameForClass(object.getClass());

            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");

            XMLOutputFactory2 factory = new WstxOutputFactory();

            factory.setProperty(WstxOutputProperties.P_OUTPUT_EMPTY_ELEMENT_HANDLER,
                    new EmptyElementHandler.SetEmptyElementHandler(marshallerFactory.emptyElements));

            XMLStreamWriter xmlStreamWriter = factory.createXMLStreamWriter(out);

            xmlStreamWriter = new IndentingXMLStreamWriter(xmlStreamWriter);


            marshaller.marshal(new JAXBElement(aQName, object.getClass(), object), xmlStreamWriter);

        } catch (JAXBException | XMLStreamException e) {
            LOGGER.error("Marshaller.marshall", e);
            throw new IllegalStateException("Error while marshalling object:" + object.toString());
        }

    }

    public void close() {

    }
}
