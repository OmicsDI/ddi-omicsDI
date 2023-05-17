package uk.ac.ebi.ddi.api.readers.arrayexpress.arrayexpresscli.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 01/05/2016
 */
public class ArrayExpressUtils {

    public static boolean cotainsValue(String[] arr, String key) {
        if (arr != null && key != null) {
            for (String value: arr) {
                if (value.equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] refineValues(String value) {
        String[] values = value.split(";");
        List<String> resultValues = new ArrayList<>();
        for (String keyValue :values) {
            if (!keyValue.toUpperCase().contains("AVAILABLE") && !keyValue.toUpperCase().contains("APPLICABLE")
                    && !keyValue.toUpperCase().contains("NOT SPECIFIED")) {
                keyValue = keyValue.replace("_", " ");
                resultValues.add(keyValue);
            }
        }
        String[] arrValue = new String[resultValues.size()];
        for (int i = 0; i < resultValues.size(); i++) {
            arrValue[i] = resultValues.get(i);
        }
        return arrValue;

    }

    /**
     * Some editings to the current protocols in ArrayExpress
     * @param protocol
     * @return
     */
    public static String refineProtocol(String protocol) {
        if (protocol != null) {
            protocol = protocol.replace("Description:", "");
            protocol = protocol.replace("Title: ", "");
            return protocol;
        }
        return protocol;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
