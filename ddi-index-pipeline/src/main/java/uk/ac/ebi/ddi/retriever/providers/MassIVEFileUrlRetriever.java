package uk.ac.ebi.ddi.retriever.providers;

import org.apache.commons.net.ftp.FTPClient;
import uk.ac.ebi.ddi.ddidomaindb.database.DB;
import uk.ac.ebi.ddi.extservices.net.FtpUtils;
import uk.ac.ebi.ddi.extservices.net.UriUtils;
import uk.ac.ebi.ddi.retriever.DatasetFileUrlRetriever;
import uk.ac.ebi.ddi.retriever.IDatasetFileUrlRetriever;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


public class MassIVEFileUrlRetriever extends DatasetFileUrlRetriever {

    private static final String FTP_MASSIVE = "ftp://massive.ucsd.edu";

    public MassIVEFileUrlRetriever(IDatasetFileUrlRetriever datasetDownloadingRetriever) {
        super(datasetDownloadingRetriever);
    }

    @Override
    public Set<String> getAllDatasetFiles(String accession, String database) throws IOException {
        Set<String> result = new HashSet<>();
        String url = String.format("%s/%s", FTP_MASSIVE, accession);
        URI uri = UriUtils.toUri(url);
        FTPClient ftpClient = createFtpClient();
        try {
            ftpClient.connect(uri.getHost());
            ftpClient.login("anonymous", "anonymous");
            FtpUtils.getListFiles(ftpClient, uri.getPath()).stream()
                    .map(x -> String.format("ftp://%s%s", uri.getHost(), x))
                    .forEach(result::add);
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        return result;
    }

    @Override
    protected boolean isSupported(String database) {
        return database.equals(DB.MASSIVE.getDBName()) || database.equals(DB.MASSIVE_OLD.getDBName());
    }

    @Override
    public Set<String> getDatasetFiles(Dataset dataset) throws IOException {
        return getDatasetFiles(dataset.getAccession(),dataset.getDatabase());
    }
}
