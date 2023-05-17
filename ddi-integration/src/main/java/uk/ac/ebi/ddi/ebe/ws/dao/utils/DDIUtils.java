package uk.ac.ebi.ddi.ebe.ws.dao.utils;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 */
public class DDIUtils {

    public static String getConcatenatedField(String[] fields) {

        String finalFields = "";
        if (fields != null && fields.length > 0) {
            int count = 0;
            for (String value : fields) {
                if (count == fields.length - 1) {
                    finalFields = finalFields + value;
                } else {
                    finalFields = finalFields + value + ",";
                }
                count++;
            }
        }
        return finalFields;
    }
}
