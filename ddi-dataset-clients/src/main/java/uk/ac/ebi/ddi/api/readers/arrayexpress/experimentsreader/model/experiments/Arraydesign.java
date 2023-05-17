
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;
import java.math.BigInteger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "arraydesign")
public class Arraydesign
        implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String accession;
    @XmlAttribute(required = true)
    protected BigInteger count;
    @XmlAttribute(required = true)
    protected BigInteger id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the value of the accession property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAccession() {
        return accession;
    }

    /**
     * Sets the value of the accession property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccession(String value) {
        this.accession = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

}
