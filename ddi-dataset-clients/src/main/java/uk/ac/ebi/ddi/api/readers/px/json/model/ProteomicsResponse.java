package uk.ac.ebi.ddi.api.readers.px.json.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class ProteomicsResponse {
    public ArrayList<ArrayList<String>> datasets;


    public Facets facets;

    @JsonIgnore
    public Object query;
    @JsonIgnore
    public Object result_set;
    @JsonIgnore
    public Object status;

    public ArrayList<ArrayList<String>> getDatasets() {
        return datasets;
    }

    public Facets getFacets() {
        return facets;
    }

    public Object getQuery() {
        return query;
    }

    public Object getResult_set() {
        return result_set;
    }

    public Object getStatus() {
        return status;
    }
}