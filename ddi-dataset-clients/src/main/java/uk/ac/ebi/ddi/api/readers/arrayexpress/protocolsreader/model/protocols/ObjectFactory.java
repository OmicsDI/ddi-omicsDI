
package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import java.math.BigInteger;


@XmlRegistry
public class ObjectFactory {

    private static final QName PERFORMER_QNAME = new QName("", "performer");
    private static final QName SOFTWARE_QNAME = new QName("", "software");
    private static final QName NAME_QNAME = new QName("", "name");
    private static final QName ACCESSION_QNAME = new QName("", "accession");
    private static final QName STANDARDPUBLICPROTOCOL_QNAME = new QName("", "standardpublicprotocol");
    private static final QName ID_QNAME = new QName("", "id");
    private static final QName TEXT_QNAME = new QName("", "text");
    private static final QName TYPE_QNAME = new QName("", "type");
    private static final QName USER_QNAME = new QName("", "user");
    private static final QName HARDWARE_QNAME = new QName("", "hardware");
    private static final QName ORDER_QNAME = new QName("", "order");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * uk.ac.ebi.ddi.arrayexpress.reader.model.protocols
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Parameter }
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link Protocols }
     */
    public Protocols createProtocols() {
        return new Protocols();
    }

    /**
     * Create an instance of {@link Protocol }
     */
    public Protocol createProtocol() {
        return new Protocol();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "performer")
    public JAXBElement<String> createPerformer(String value) {
        return new JAXBElement<String>(PERFORMER_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "software")
    public JAXBElement<String> createSoftware(String value) {
        return new JAXBElement<String>(SOFTWARE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(NAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "accession")
    public JAXBElement<String> createAccession(String value) {
        return new JAXBElement<String>(ACCESSION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "standardpublicprotocol")
    public JAXBElement<String> createStandardpublicprotocol(String value) {
        return new JAXBElement<String>(STANDARDPUBLICPROTOCOL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "id")
    public JAXBElement<BigInteger> createId(BigInteger value) {
        return new JAXBElement<BigInteger>(ID_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "text")
    public JAXBElement<String> createText(String value) {
        return new JAXBElement<String>(TEXT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "type")
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(TYPE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "user")
    public JAXBElement<BigInteger> createUser(BigInteger value) {
        return new JAXBElement<BigInteger>(USER_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "hardware")
    public JAXBElement<String> createHardware(String value) {
        return new JAXBElement<String>(HARDWARE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "order")
    public JAXBElement<BigInteger> createOrder(BigInteger value) {
        return new JAXBElement<BigInteger>(ORDER_QNAME, BigInteger.class, null, value);
    }

}
