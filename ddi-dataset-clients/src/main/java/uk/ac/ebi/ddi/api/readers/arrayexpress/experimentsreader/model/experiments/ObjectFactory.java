
package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the uk.ac.ebi.ddi.arrayexpress.reader.model.experiments package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * uk.ac.ebi.ddi.arrayexpress.reader.model.experiments
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Miamescore }
     */
    public Miamescore createMiamescore() {
        return new Miamescore();
    }

    /**
     * Create an instance of {@link Experiments }
     */
    public Experiments createExperiments() {
        return new Experiments();
    }

    /**
     * Create an instance of {@link Bibliography }
     */
    public Bibliography createBibliography() {
        return new Bibliography();
    }

    /**
     * Create an instance of {@link Arraydesign }
     */
    public Arraydesign createArraydesign() {
        return new Arraydesign();
    }

    /**
     * Create an instance of {@link Bioassaydatagroup }
     */
    public Bioassaydatagroup createBioassaydatagroup() {
        return new Bioassaydatagroup();
    }

    /**
     * Create an instance of {@link Experimentalfactor }
     */
    public Experimentalfactor createExperimentalfactor() {
        return new Experimentalfactor();
    }

    /**
     * Create an instance of {@link Experiment }
     */
    public Experiment createExperiment() {
        return new Experiment();
    }

    /**
     * Create an instance of {@link Sampleattribute }
     */
    public Sampleattribute createSampleattribute() {
        return new Sampleattribute();
    }

    /**
     * Create an instance of {@link Provider }
     */
    public Provider createProvider() {
        return new Provider();
    }

    /**
     * Create an instance of {@link Protocol }
     */
    public Protocol createProtocol() {
        return new Protocol();
    }

}
