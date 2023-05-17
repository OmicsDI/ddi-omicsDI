package uk.ac.ebi.ddi.pipeline.indexer.tasklet.io;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;
import uk.ac.ebi.ddi.pipeline.indexer.io.DDICleanDirectory;
import uk.ac.ebi.ddi.pipeline.indexer.tasklet.AbstractTasklet;
import uk.ac.ebi.ddi.pipeline.indexer.utils.FileUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 *  ==Overview==
 *
 *  This class
 *
 * Created by ypriverol (ypriverol@gmail.com) on 06/07/2016.
 */
@Setter
@Getter
public class DownloadPeptideAtlasSummaryFileTasklet extends AbstractTasklet {

    String originalFolder;

    String peptideAtlasSumamryFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadPeptideAtlasSummaryFileTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        DDICleanDirectory.cleanDirectory(originalFolder);

        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            LOGGER.error("Exception occurred, ", e);
        }

        URL url = new URL(peptideAtlasSumamryFile);
        URLConnection connection = url.openConnection();
        try (InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int count = 0;
            while ((line = in.readLine()) != null) {
                if (count != 0) {
                    if (line.split("\t").length > 4) {
                        String urlStr = line.split("\t")[3];
                        String fileName = urlStr.substring(urlStr.lastIndexOf('/') + 1);
                        String localPath = originalFolder + "/" + fileName;
                        FileUtil.downloadFileFromURL(urlStr, localPath);
                    }
                }
                count++;
            }
        }

        return RepeatStatus.FINISHED;

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(originalFolder, "The original URL Can't be null");
        Assert.notNull(peptideAtlasSumamryFile,  "The taget File Can be null");
    }
}
