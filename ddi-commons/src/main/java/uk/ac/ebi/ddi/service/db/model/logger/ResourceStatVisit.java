package uk.ac.ebi.ddi.service.db.model.logger;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2015
 */
public class ResourceStatVisit {

    private int total;

    private AbstractDocument abstractResource;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public AbstractDocument getAbstractResource() {
        return abstractResource;
    }

    public void setAbstractResource(AbstractDocument abstractResource) {
        this.abstractResource = abstractResource;
    }

    @Override
    public String toString() {
        return "ResourceStatVisit{" +
                "total=" + total +
                ", abstractResource=" + abstractResource +
                '}';
    }
}
