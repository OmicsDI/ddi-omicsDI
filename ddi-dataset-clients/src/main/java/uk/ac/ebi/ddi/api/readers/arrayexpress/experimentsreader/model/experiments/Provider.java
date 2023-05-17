
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;


import jakarta.xml.bind.annotation.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "provider")
public class Provider implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String contact;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String email;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String role;

    /**
     * Gets the value of the contact property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the role property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRole(String value) {
        this.role = value;
    }

}
