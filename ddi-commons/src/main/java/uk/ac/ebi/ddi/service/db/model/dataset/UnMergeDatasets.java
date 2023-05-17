package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "unmerged")
public class UnMergeDatasets implements IDataset{

    private String masterAccession;

    private String masterDatabase;

    private MergedDataset data;

    public String getMasterAccession() {
        return masterAccession;
    }

    public void setMasterAccession(String masterAccession) {
        this.masterAccession = masterAccession;
    }

    public MergedDataset getDataset() {
        return data;
    }

    public void setDataset(MergedDataset dataset) {
        this.data = dataset;
    }

    public String getMasterDatabase() {
        return masterDatabase;
    }

    public void setMasterDatabase(String masterDatabase) {
        this.masterDatabase = masterDatabase;
    }


    @Override
    public String toString() {
        return "{" +
                "masterAccession='" + masterAccession + '\'' +
                ", masterDatabase='" + masterDatabase + '\'' +

                '}';
    }
}
