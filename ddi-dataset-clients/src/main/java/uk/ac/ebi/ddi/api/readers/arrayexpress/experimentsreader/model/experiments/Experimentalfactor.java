
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;


import jakarta.xml.bind.annotation.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "experimentalfactor")
public class Experimentalfactor implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String value;

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
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Experimentalfactor{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
