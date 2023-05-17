package uk.ac.ebi.ddi.service.db.model.enrichment;

/**
 * Created by mingze on 22/07/15.
 * word has been found by recomender in each field.
 */
public class WordInField implements Comparable<WordInField>{

    private String text;
    private int from;
    private int to;

    public WordInField(String text, int from, int to) {
        this.text = text;
        this.from = from;
        this.to = to;
    }

    public int compareTo(WordInField anotherInstance) {
        return this.from - anotherInstance.from;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" +
                "\"text\" : \"" + text + '\"' +
                ", \"from\" : \"" + from + '\"' +
                ", \"to\" : \"" + to + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordInField that = (WordInField) o;

        if (from != that.from) return false;
        return to == that.to && text.equals(that.text);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + from;
        result = 31 * result + to;
        return result;
    }
}
