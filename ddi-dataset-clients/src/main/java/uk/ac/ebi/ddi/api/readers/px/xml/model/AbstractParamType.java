
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * Abstract entity allowing either cvParam or userParam to be referenced in other schemas.
 *
 *
 * <p>Java class for AbstractParamType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AbstractParamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitAccession" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitCvRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractParamType")
@XmlSeeAlso({
        UserParamType.class,
        CvParamType.class
})
public abstract class AbstractParamType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected String value;
    @XmlAttribute
    protected String unitAccession;
    @XmlAttribute
    protected String unitName;
    @XmlAttribute
    protected String unitCvRef;

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

    /**
     * Gets the value of the unitAccession property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUnitAccession() {
        return unitAccession;
    }

    /**
     * Sets the value of the unitAccession property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUnitAccession(String value) {
        this.unitAccession = value;
    }

    /**
     * Gets the value of the unitName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets the value of the unitName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUnitName(String value) {
        this.unitName = value;
    }

    /**
     * Gets the value of the unitCvRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUnitCvRef() {
        return unitCvRef;
    }

    /**
     * Sets the value of the unitCvRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUnitCvRef(String value) {
        this.unitCvRef = value;
    }

}
