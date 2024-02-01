package uk.ac.ebi.ddi.api.readers.px.json.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Facets{
    @JsonIgnore
    public Object instrument;

    @JsonIgnore
    public Object keywords;

    public ArrayList<Repository> repository;
    @JsonIgnore
    public Object species;

    @JsonIgnore
    public Object year;

    public Object getInstrument() {
        return instrument;
    }

    public Object getKeywords() {
        return keywords;
    }

    public ArrayList<Repository> getRepository() {
        return repository;
    }

    public Object getSpecies() {
        return species;
    }

    public Object getYear() {
        return year;
    }
}