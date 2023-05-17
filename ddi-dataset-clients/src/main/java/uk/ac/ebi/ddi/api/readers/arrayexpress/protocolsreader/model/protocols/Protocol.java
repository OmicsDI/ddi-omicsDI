
package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols;


import jakarta.xml.bind.annotation.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.IArrayExpress;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id",
        "accession",
        "name",
        "user",
        "text",
        "type",
        "performer",
        "hardware",
        "software",
        "standardpublicprotocol",
        "parameter"
})
@XmlRootElement(name = "protocol")
public class Protocol implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected BigInteger id;
    @XmlElement(required = true)
    protected String accession;
    @XmlElement(required = true)
    protected String name;
    protected BigInteger user;
    @XmlElement(required = true)
    protected String text;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String performer;
    @XmlElement(required = true)
    protected String hardware;
    @XmlElement(required = true)
    protected String software;
    @XmlElement(required = true)
    protected String standardpublicprotocol;
    protected List<Parameter> parameter;

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
        this.accession = value.trim();
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

    /**
     * Gets the value of the user property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setUser(BigInteger value) {
        this.user = value;
    }

    /**
     * Gets the value of the text property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the performer property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPerformer() {
        return performer;
    }

    /**
     * Sets the value of the performer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerformer(String value) {
        this.performer = value;
    }

    /**
     * Gets the value of the hardware property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHardware() {
        return hardware;
    }

    /**
     * Sets the value of the hardware property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHardware(String value) {
        this.hardware = value;
    }

    /**
     * Gets the value of the software property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSoftware() {
        return software;
    }

    /**
     * Sets the value of the software property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSoftware(String value) {
        this.software = value;
    }

    /**
     * Gets the value of the standardpublicprotocol property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStandardpublicprotocol() {
        return standardpublicprotocol;
    }

    /**
     * Sets the value of the standardpublicprotocol property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStandardpublicprotocol(String value) {
        this.standardpublicprotocol = value;
    }

    /**
     * Gets the value of the parameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     */
    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "id=" + id +
                ", accession='" + accession + '\'' +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", performer='" + performer + '\'' +
                ", hardware='" + hardware + '\'' +
                ", software='" + software + '\'' +
                ", standardpublicprotocol='" + standardpublicprotocol + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
