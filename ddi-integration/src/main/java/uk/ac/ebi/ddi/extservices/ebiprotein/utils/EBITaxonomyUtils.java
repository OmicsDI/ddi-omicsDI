package uk.ac.ebi.ddi.extservices.ebiprotein.utils;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * == General Description ==
 * <p>
 * This class Provides a general information or functionalities for
 * <p>
 * ==Overview==
 * <p>
 * How to used
 * <p>
 * Created by yperez (ypriverol@gmail.com) on 20/10/2016.
 */
public class EBITaxonomyUtils {

    public enum EbiTaxRank {
        SPECIES("species"),
        GENUES("genues"),
        NO_RANK("no rank");

        private final String name;

        EbiTaxRank(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static boolean isSpeciesOrGenues(String term) {
            return term.equalsIgnoreCase(SPECIES.getName()) || term.equalsIgnoreCase(GENUES.getName());
        }
    }
}
