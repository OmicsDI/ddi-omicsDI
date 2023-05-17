package uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yperez
 * Date: 18-Julio-2011
 */
public class ModelConstants {

    public static final String MODEL_PKG = "uk.ac.ebi.ddi.arrayexpress.protocolsreader.model.protocols";
    public static final String ARRAY_EXPRESS_PROTOCOLS = "";

    private static Map<Class, QName> modelQNames = new HashMap<Class, QName>();

    static {

        modelQNames.put(Protocols.class, new QName(ARRAY_EXPRESS_PROTOCOLS,     "protocols"));
        modelQNames.put(Protocol.class, new QName(ARRAY_EXPRESS_PROTOCOLS,      "protocol"));
        modelQNames.put(Parameter.class, new QName(ARRAY_EXPRESS_PROTOCOLS,     "parameter"));

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
