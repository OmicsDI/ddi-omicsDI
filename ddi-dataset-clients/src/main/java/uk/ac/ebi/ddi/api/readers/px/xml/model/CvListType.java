
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The list of controlled vocabularies used in the file.
 *
 * <p>Java class for CvListType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CvListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cv" type="{}CvType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CvListType", propOrder = {
        "cv"
})
public class CvListType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    @XmlElement(name = "Cv", required = true)
    protected List<CvType> cv;

    /**
     * Gets the value of the cv property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cv property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCv().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CvType }
     */
    public List<CvType> getCv() {
        if (cv == null) {
            cv = new ArrayList<>();
        }
        return this.cv;
    }

}
