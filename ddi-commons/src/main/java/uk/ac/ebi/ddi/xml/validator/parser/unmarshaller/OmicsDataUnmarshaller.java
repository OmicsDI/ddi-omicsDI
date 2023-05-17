package uk.ac.ebi.ddi.xml.validator.parser.unmarshaller;

import uk.ac.ebi.ddi.xml.validator.parser.model.IDataObject;
import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;


public interface OmicsDataUnmarshaller {
    <T extends IDataObject> T unmarshal(String xmlSnippet, DataElement element) throws Exception;
}
