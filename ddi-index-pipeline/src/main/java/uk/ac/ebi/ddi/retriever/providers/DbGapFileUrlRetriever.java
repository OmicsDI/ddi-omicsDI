package uk.ac.ebi.ddi.retriever.providers;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


public class DbGapFileUrlRetriever extends DatasetFileUrlRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbGapFileUrlRetriever.class);
    private static final String FTP_DBGAP = "ftp://ftp.ncbi.nlm.nih.gov/dbgap/studies";

    public DbGapFileUrlRetriever(IDatasetFileUrlRetriever datasetDownloadingRetriever) {
        super(datasetDownloadingRetriever);
    }

    @Override
    public Set<String> getAllDatasetFiles(String accession, String database) throws IOException {
        Set<String> result = new HashSet<>();
        String url = String.format("%s/%s%s", FTP_DBGAP, accession, "?disconnect=true");
        URI uri = UriUtils.toUri(url);
        FTPClient ftpClient = createFtpClient();
        try {
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.connect(uri.getHost());
            ftpClient.login("anonymous", "anonymous");
            FtpUtils.getListFiles(ftpClient, uri.getPath()).stream()
                    .map(x -> String.format("ftp://%s%s", uri.getHost(), x))
                    .forEach(result::add);
        } catch (FTPConnectionClosedException e) {
            LOGGER.error("Error while retrieving dataset files for {} {}",accession, e.getMessage() );
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.quit();
                ftpClient.disconnect();
            }
        }
        return result;
    }

    @Override
    protected boolean isSupported(String database) {
        return database.equals(DB.DB_GAP.getDBName());
    }

    @Override
    public Set<String> getDatasetFiles(Dataset dataset) throws IOException {
        return getDatasetFiles(dataset.getAccession(),dataset.getDatabase());
    }
}
