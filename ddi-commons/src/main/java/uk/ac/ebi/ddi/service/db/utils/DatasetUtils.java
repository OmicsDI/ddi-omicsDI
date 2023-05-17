package uk.ac.ebi.ddi.service.db.utils;

import uk.ac.ebi.ddi.ddidomaindb.dataset.Field;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static uk.ac.ebi.ddi.ddidomaindb.dataset.DSField.Additional.IS_PRIVATE;

public class DatasetUtils {

    private DatasetUtils() {
    }

    public static boolean hasDateType(Dataset dataset, Field dateType) {
        Map<String, Set<String>> dates = dataset.getDates();

        if (dates != null) {
            return dates.containsKey(dateType.getName());
        }
        return false;
    }

    public static Set<String> getDateType(Dataset dataset, Field dateType) {
        Map<String, Set<String>> dates = dataset.getDates();
        return dates.get(dateType.getName());
    }

    public static String getFirstAdditional(Dataset dataset, String key) {
        if (dataset.getAdditional() != null && !dataset.getAdditional().isEmpty()) {
            if (dataset.getAdditional().containsKey(key) && !dataset.getAdditional().get(key).isEmpty()) {
                return new ArrayList<>(dataset.getAdditional().get(key)).get(0);
            }
        }
        return null;
    }

    public static boolean isPrivateDataset(Dataset dataset) {
        if (!dataset.getAdditionalField(IS_PRIVATE.key()).isEmpty()) {
            return dataset.getAdditionalField(IS_PRIVATE.key()).iterator().next().equals("true");
        }
        return false;
    }

    /**
     * Get configuration field of dataset
     * Return null if not found
     * @param dataset
     * @param configurationField
     * @return
     */
    public static String getConfiguration(Dataset dataset, Field configurationField) {
        if (dataset.getConfigurations().containsKey(configurationField.key())) {
            return dataset.getConfigurations().get(configurationField.key());
        }
        return null;
    }
}
