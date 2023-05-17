
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The list of links to the complete ProteomeXchange dataset.
 *
 * <p>Java class for FullDatasetLinkListType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FullDatasetLinkListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FullDatasetLink" type="{}FullDatasetLinkType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FullDatasetLinkListType", propOrder = {
        "fullDatasetLink"
})
public class FullDatasetLinkListType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    @XmlElement(name = "FullDatasetLink", required = true)
    protected List<FullDatasetLinkType> fullDatasetLink;

    /**
     * Gets the value of the fullDatasetLink property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fullDatasetLink property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFullDatasetLink().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FullDatasetLinkType }
     */
    public List<FullDatasetLinkType> getFullDatasetLink() {
        if (fullDatasetLink == null) {
            fullDatasetLink = new ArrayList<>();
        }
        return this.fullDatasetLink;
    }

}
