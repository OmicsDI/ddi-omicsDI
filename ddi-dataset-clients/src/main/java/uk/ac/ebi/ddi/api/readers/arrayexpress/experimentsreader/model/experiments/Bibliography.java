
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;


import jakarta.xml.bind.annotation.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.IArrayExpress;

import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bibliography")
public class Bibliography
        implements Serializable, IArrayExpress {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String accession;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String authors;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String doi;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String edition;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String issue;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String pages;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String publication;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String publisher;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String status;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String title;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String uri;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String volume;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String year;

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
     * Gets the value of the authors property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAuthors(String value) {
        this.authors = value;
    }

    /**
     * Gets the value of the doi property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the value of the doi property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDoi(String value) {
        this.doi = value;
    }

    /**
     * Gets the value of the edition property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the value of the edition property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEdition(String value) {
        this.edition = value;
    }

    /**
     * Gets the value of the issue property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Sets the value of the issue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIssue(String value) {
        this.issue = value;
    }

    /**
     * Gets the value of the pages property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPages() {
        return pages;
    }

    /**
     * Sets the value of the pages property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPages(String value) {
        this.pages = value;
    }

    /**
     * Gets the value of the publication property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPublication() {
        return publication;
    }

    /**
     * Sets the value of the publication property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublication(String value) {
        this.publication = value;
    }

    /**
     * Gets the value of the publisher property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the uri property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUri(String value) {
        this.uri = value;
    }

    /**
     * Gets the value of the volume property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVolume() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVolume(String value) {
        this.volume = value;
    }

    /**
     * Gets the value of the year property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setYear(String value) {
        this.year = value;
    }

}
