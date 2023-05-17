
package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols;


import jakarta.xml.bind.annotation.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "name",
        "order"
})
@XmlRootElement(name = "parameter")
public class Parameter implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger order;

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
     * Gets the value of the order property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setOrder(BigInteger value) {
        this.order = value;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
