package uk.ac.ebi.ddi.annotation.service.dataset;

import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.xml.validator.parser.model.Date;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/11/2015
 */
public class DatasetAnnotationFieldsService {

    public static Entry addpublicationDate(Entry dataset) {
        if (dataset.getDates() != null && !dataset.getDates().isEmpty()
                && !dataset.getDates().containsPublicationDate()) {
            dataset.getDates().addDefaultPublicationDate();
        }
        return dataset;
    }

    public static Dataset addpublicationDate(Dataset dataset) {
        if (dataset.getDates() != null && !dataset.getDates().isEmpty()
                && containsPublicationDate(dataset.getDates())) {
            dataset = addDefaultPublicationDate(dataset);
        }
        return dataset;
    }

    private static Dataset addDefaultPublicationDate(Dataset dataset) {
        Set<String> toAdd = null;
        if (dataset.getDates() != null && !dataset.getDates().isEmpty()) {
            for (String dateField: dataset.getDates().keySet()) {
                if (dateField.equalsIgnoreCase(DSField.Date.PUBLICATION_UPDATED.getName())) {
                    toAdd = dataset.getDates().get(dateField);
                }
            }
        }
        if (toAdd != null) {
            dataset.getDates().put(DSField.Date.PUBLICATION.getName(), toAdd);
        }
        return dataset;
    }

    private static boolean containsPublicationDate(Map<String, Set<String>> dates) {
        if (dates != null && !dates.isEmpty()) {
            for (String dateField : dates.keySet()) {
                if (dateField.equalsIgnoreCase(DSField.Date.PUBLICATION.getName())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static Entry addPublicationDateFromSubmission(Entry dataset) {
        if (dataset.getDates() != null && !dataset.getDates().isEmpty()
                && !dataset.getDates().containsPublicationDate()) {
            Date date = dataset.getDates().getDateByKey(DSField.Additional.SUBMISSION_DATE.getName());
            if (date != null) {
                dataset.addDate(new Date(DSField.Date.PUBLICATION.getName(), date.getValue()));
            }
        }
        return dataset;
    }

    public static Entry cleanDescription(Entry dataset) {
        if (dataset != null && dataset.getDescription() != null) {
            String finalDescription;
            String[] descriptionArray = dataset.getDescription().split("\\(\\[\\[");
            if (descriptionArray.length > 1) {
                String[] descriptionArraySecond = descriptionArray[1].split("\\]\\]\\)");
                if (descriptionArraySecond.length > 1) {
                    finalDescription = descriptionArray[0] + " " + descriptionArraySecond[1];
                    dataset.setDescription(finalDescription);
                }
            }
        }
        return dataset;
    }

    public static Entry replaceMEDLINEPubmed(Entry dataset) {

        if (dataset.getCrossReferences() != null
                && !dataset.getCrossReferenceFieldValue(DSField.CrossRef.MEDLINE.getName()).isEmpty()) {
            dataset.getCrossReferenceFieldValue(DSField.CrossRef.MEDLINE.getName()).stream()
                    .filter(value -> value != null && !value.isEmpty())
                    .forEach(value -> dataset.addCrossReferenceValue(DSField.CrossRef.PUBMED.getName(), value));
            dataset.removeCrossReferences(DSField.CrossRef.MEDLINE.getName());
        }
        return dataset;
    }

    public static Entry replaceAuthorField(Entry dataset) {
        if (dataset.getAuthors() != null && !dataset.getAuthors().isEmpty()) {
            dataset.addAdditionalField(DSField.Additional.SUBMITTER.getName(), dataset.getAuthors());
            dataset.setAuthors(null);
        }
        return dataset;
    }

    public static Entry replaceKeywords(Entry dataset) {
        if (dataset.getKeywords() != null && !dataset.getKeywords().isEmpty()) {
            dataset.addAdditionalField(DSField.Additional.SUBMITTER_KEYWORDS.getName(), dataset.getKeywords());
            dataset.setKeywords(null);
        }
        return dataset;
    }
}
