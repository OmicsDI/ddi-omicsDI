package uk.ac.ebi.ddi.service.db.repo.dataset;

import uk.ac.ebi.ddi.service.db.model.dataset.MergeCandidate;

import java.util.List;
import java.util.Set;

/**
 * Created by azorin on 11/01/2018.
 */
public interface IDatasetRepoExtension {
    Set<String> getSecondaryAccessions();

    List<MergeCandidate> getMergeCandidates(int start, int size);

    Integer getMergeCandidateCount();

    void mergeDataset(MergeCandidate mergeData);

    void delete(String database, String accession);

    void deleteMergeCandidte(String database, String accession);

    void addSecondaryAccession(String database, String accession, String secondaryAccession);

    long getMergedDatasetCount(String database, String accession);
}
