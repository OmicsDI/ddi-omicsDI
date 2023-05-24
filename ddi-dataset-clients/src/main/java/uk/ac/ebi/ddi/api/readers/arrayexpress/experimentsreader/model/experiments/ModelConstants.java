package uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yperez
 * Date: 18-Julio-2011
 */
public class ModelConstants {

    //public static final String MODEL_PKG = "uk.ac.ebi.ddi.arrayexpress.experimentsreader.model.experiments";
//    public static final String MODEL_PKG = "uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments";
//    public static final String MODEL_PKG = "uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols";

    public static final String EXPERIMENT_MODEL_PKG = "uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments";

     public static final String PROTOCOL_MODEL_PKG = "uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols";
    public static final String ARRAY_EXPRESS_EXPERIMENTS = "";

    private static Map<Class, QName> modelQNames = new HashMap<Class, QName>();

    static {

        modelQNames.put(Experiments.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,     "experiments"));
        modelQNames.put(Experiment.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,      "experiment"));
        modelQNames.put(Arraydesign.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,     "arraydesign"));
        modelQNames.put(Bibliography.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,    "bibliography"));
        modelQNames.put(Experimentalfactor.class, new QName(ARRAY_EXPRESS_EXPERIMENTS, "experimentfactor"));
        modelQNames.put(Miamescore.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,         "miamescore"));
        modelQNames.put(Protocol.class, new QName(ARRAY_EXPRESS_EXPERIMENTS, "protocol"));

        modelQNames.put(Provider.class, new QName(ARRAY_EXPRESS_EXPERIMENTS, "provider"));
        modelQNames.put(Sampleattribute.class, new QName(ARRAY_EXPRESS_EXPERIMENTS,         "sampleattribute"));

        //now make set unmodifiable
        modelQNames = Collections.unmodifiableMap(modelQNames);

    }


    public static boolean isRegisteredClass(Class cls) {
        return modelQNames.containsKey(cls);
    }

    public static QName getQNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return modelQNames.get(cls);
        } else {
            throw new IllegalStateException("No QName registered for class: " + cls);
        }
    }

    public static String getElementNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return modelQNames.get(cls).getLocalPart();
        } else {
            throw new IllegalStateException("No QName registered for class: " + cls);
        }
    }

    public static Class getClassForElementName(String name) {
        for (Map.Entry<Class, QName> entry : modelQNames.entrySet()) {
            if (entry.getValue().getLocalPart().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }


}
