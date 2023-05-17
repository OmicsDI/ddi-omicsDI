
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;


import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id",
        "accession",
        "secondaryaccession",
        "name",
        "experimenttype",
        "releasedate",
        "submissiondate",
        "lastupdatedate",
        "samples",
        "hybs",
        "user",
        "sampleattribute",
        "experimentalfactor",
        "miamescore",
        "arraydesign",
        "bioassaydatagroup",
        "bibliography",
        "provider",
        "experimentdesign",
        "description",
        "protocol",
        "seqdatauri",
        "anonymousreview"
})
@XmlRootElement(name = "experiment")
public class Experiment
        implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected BigInteger id;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String accession;
    protected List<String> secondaryaccession;
    @XmlElement(required = true)
    protected String name;
    protected List<String> experimenttype;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar releasedate;
    @XmlElement(required = true)
    protected String submissiondate;
    @XmlElement(required = true)
    protected String lastupdatedate;
    @XmlElement(required = true)
    protected BigInteger samples;
    @XmlElement(required = true)
    protected BigInteger hybs;
    @XmlElement(required = true)
    protected List<BigInteger> user;
    protected List<Sampleattribute> sampleattribute;
    protected List<Experimentalfactor> experimentalfactor;
    protected List<Miamescore> miamescore;
    protected List<Arraydesign> arraydesign;
    protected List<Bioassaydatagroup> bioassaydatagroup;
    protected List<Bibliography> bibliography;
    protected List<Provider> provider;
    protected List<String> experimentdesign;
    @XmlElement(required = true)
    protected String description;
    protected List<Protocol> protocol;
    @XmlSchemaType(name = "anyURI")
    protected List<String> seqdatauri;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String anonymousreview;

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
        this.accession = value;
    }

    /**
     * Gets the value of the secondaryaccession property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secondaryaccession property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecondaryaccession().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getSecondaryaccession() {
        if (secondaryaccession == null) {
            secondaryaccession = new ArrayList<>();
        }
        return this.secondaryaccession;
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
     * Gets the value of the experimenttype property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the experimenttype property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExperimenttype().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * @return list string
     */
    public List<String> getExperimenttype() {
        if (experimenttype == null) {
            experimenttype = new ArrayList<String>();
        }
        return this.experimenttype;
    }

    /**
     * Gets the value of the releasedate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getReleasedate() {
        return releasedate;
    }

    /**
     * Sets the value of the releasedate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setReleasedate(XMLGregorianCalendar value) {
        this.releasedate = value;
    }

    /**
     * Gets the value of the submissiondate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSubmissiondate() {
        return submissiondate;
    }

    /**
     * Sets the value of the submissiondate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubmissiondate(String value) {
        this.submissiondate = value;
    }

    /**
     * Gets the value of the lastupdatedate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLastupdatedate() {
        return lastupdatedate;
    }

    /**
     * Sets the value of the lastupdatedate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastupdatedate(String value) {
        this.lastupdatedate = value;
    }

    /**
     * Gets the value of the samples property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getSamples() {
        return samples;
    }

    /**
     * Sets the value of the samples property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setSamples(BigInteger value) {
        this.samples = value;
    }

    /**
     * Gets the value of the hybs property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getHybs() {
        return hybs;
    }

    /**
     * Sets the value of the hybs property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setHybs(BigInteger value) {
        this.hybs = value;
    }

    /**
     * Gets the value of the user property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the user property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUser().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     */
    public List<BigInteger> getUser() {
        if (user == null) {
            user = new ArrayList<BigInteger>();
        }
        return this.user;
    }

    /**
     * Gets the value of the sampleattribute property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sampleattribute property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSampleattribute().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sampleattribute }
     */
    public List<Sampleattribute> getSampleattribute() {
        if (sampleattribute == null) {
            sampleattribute = new ArrayList<Sampleattribute>();
        }
        return this.sampleattribute;
    }

    /**
     * Gets the value of the experimentalfactor property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the experimentalfactor property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExperimentalfactor().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Experimentalfactor }
     */
    public List<Experimentalfactor> getExperimentalfactor() {
        if (experimentalfactor == null) {
            experimentalfactor = new ArrayList<Experimentalfactor>();
        }
        return this.experimentalfactor;
    }

    /**
     * Gets the value of the miamescore property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the miamescore property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMiamescore().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Miamescore }
     */
    public List<Miamescore> getMiamescore() {
        if (miamescore == null) {
            miamescore = new ArrayList<Miamescore>();
        }
        return this.miamescore;
    }

    /**
     * Gets the value of the arraydesign property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arraydesign property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArraydesign().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Arraydesign }
     */
    public List<Arraydesign> getArraydesign() {
        if (arraydesign == null) {
            arraydesign = new ArrayList<Arraydesign>();
        }
        return this.arraydesign;
    }

    /**
     * Gets the value of the bioassaydatagroup property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bioassaydatagroup property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBioassaydatagroup().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bioassaydatagroup }
     */
    public List<Bioassaydatagroup> getBioassaydatagroup() {
        if (bioassaydatagroup == null) {
            bioassaydatagroup = new ArrayList<Bioassaydatagroup>();
        }
        return this.bioassaydatagroup;
    }

    /**
     * Gets the value of the bibliography property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bibliography property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBibliography().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bibliography }
     */
    public List<Bibliography> getBibliography() {
        if (bibliography == null) {
            bibliography = new ArrayList<Bibliography>();
        }
        return this.bibliography;
    }

    /**
     * Gets the value of the provider property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the provider property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProvider().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Provider }
     */
    public List<Provider> getProvider() {
        if (provider == null) {
            provider = new ArrayList<Provider>();
        }
        return this.provider;
    }

    /**
     * Gets the value of the experimentdesign property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the experimentdesign property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExperimentdesign().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getExperimentdesign() {
        if (experimentdesign == null) {
            experimentdesign = new ArrayList<String>();
        }
        return this.experimentdesign;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the protocol property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the protocol property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocol().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Protocol }
     */
    public List<Protocol> getProtocol() {
        if (protocol == null) {
            protocol = new ArrayList<Protocol>();
        }
        return this.protocol;
    }

    /**
     * Gets the value of the seqdatauri property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the seqdatauri property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeqdatauri().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getSeqdatauri() {
        if (seqdatauri == null) {
            seqdatauri = new ArrayList<String>();
        }
        return this.seqdatauri;
    }

    /**
     * Gets the value of the anonymousreview property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAnonymousreview() {
        return anonymousreview;
    }

    /**
     * Sets the value of the anonymousreview property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAnonymousreview(String value) {
        this.anonymousreview = value;
    }

    @Override
    public String toString() {
        return "Experiment{" +
                "id=" + id +
                ", accession='" + accession + '\'' +
                ", secondaryaccession=" + secondaryaccession +
                ", name='" + name + '\'' +
                ", experimenttype=" + experimenttype +
                ", releasedate=" + releasedate +
                ", submissiondate='" + submissiondate + '\'' +
                ", lastupdatedate='" + lastupdatedate + '\'' +
                ", samples=" + samples +
                ", hybs=" + hybs +
                ", user=" + user +
                ", sampleattribute=" + sampleattribute +
                ", experimentalfactor=" + experimentalfactor +
                ", miamescore=" + miamescore +
                ", arraydesign=" + arraydesign +
                ", bioassaydatagroup=" + bioassaydatagroup +
                ", bibliography=" + bibliography +
                ", provider=" + provider +
                ", experimentdesign=" + experimentdesign +
                ", description='" + description + '\'' +
                ", protocol=" + protocol +
                ", seqdatauri=" + seqdatauri +
                ", anonymousreview='" + anonymousreview + '\'' +
                '}';
    }
}
