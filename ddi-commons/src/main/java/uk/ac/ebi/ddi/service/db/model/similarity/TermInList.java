package uk.ac.ebi.ddi.service.db.model.similarity;

/**
 * Created by mingze on 11/09/15.
 */
@Deprecated
public class TermInList implements Comparable<TermInList>{

    private String termName;

    public TermInList(String termName) {
        this.termName = termName;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public int compareTo(TermInList anotherTerm) {
        return this.termName.compareTo(anotherTerm.getTermName());
    }


}
