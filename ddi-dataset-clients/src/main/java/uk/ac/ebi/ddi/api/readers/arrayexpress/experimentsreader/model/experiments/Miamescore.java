
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;


import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;
import java.math.BigInteger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "miamescore")
public class Miamescore
        implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(required = true)
    protected BigInteger value;

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

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setValue(BigInteger value) {
        this.value = value;
    }

}
