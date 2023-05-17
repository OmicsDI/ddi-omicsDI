package uk.ac.ebi.ddi.service.db.model.facetsettings;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "settings")
public class FacetSettings {
        public int maxFacetCount;
        public FacetProperty[] facetProperties;
}
