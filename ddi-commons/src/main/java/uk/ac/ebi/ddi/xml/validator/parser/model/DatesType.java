
package uk.ac.ebi.ddi.xml.validator.parser.model;

import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for datesType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="datesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date" type="{}dateType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datesType", propOrder = {
        "date"
})
public class DatesType
        implements Serializable, IDataObject {

    private static final long serialVersionUID = 105L;
    @XmlElement(required = true)
    protected List<Date> date;

    /**
     * Gets the value of the date property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the date property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDate().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Date }
     *
     * @return list of dates
     */
    public List<Date> getDate() {
        if (date == null) {
            date = new ArrayList<>();
        }
        return this.date;
    }

    public boolean isEmpty() {
        date = getDate();
        return date.isEmpty();
    }

    public boolean containsPublicationDate() {
        if (date != null && !date.isEmpty()) {
            for (Date dateField : date) {
                if (dateField.getType().equalsIgnoreCase(DSField.Date.PUBLICATION.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addDefaultPublicationDate() {
        String toAdd = null;
        if (date != null && !date.isEmpty()) {
            for (Date dateField : date) {
                if (dateField.getType().equalsIgnoreCase(DSField.Date.PUBLICATION_UPDATED.getName())) {
                    toAdd = dateField.getValue();
                }
            }
        }
        if (toAdd != null) {
            date.add(new Date(DSField.Date.PUBLICATION.getName(), toAdd));
        }
    }

    public Date getDateByKey(String key) {
        if (date != null && !date.isEmpty()) {
            for (Date dateField : date) {
                if (dateField.getType().equalsIgnoreCase(key)) {
                    return dateField;
                }
            }
        }
        return null;
    }

}
