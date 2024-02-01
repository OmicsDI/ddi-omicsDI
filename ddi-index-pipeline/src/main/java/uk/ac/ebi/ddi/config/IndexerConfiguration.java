package uk.ac.ebi.ddi.config;

import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.domain.DomainWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.europmc.CitationClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.EbeyeWsConfigProd;
import uk.ac.ebi.ddi.pipeline.indexer.pipeline.listener.ExecutionContextThrowablePromotionListener;
import uk.ac.ebi.ddi.pipeline.indexer.pipeline.listener.PipelineJobStatusListener;
import uk.ac.ebi.ddi.pipeline.indexer.pipeline.listener.StepExecutionPeriodListener;
import uk.ac.ebi.ddi.similarityCalculator.SimilarityCounts;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@ComponentScan({"uk.ac.ebi.ddi.annotation.service","uk.ac.ebi.ddi.service.db.service","uk.ac.ebi.ddi.annotation","uk.ac.ebi.ddi.service.db.repo.facetsettings","uk.ac.ebi.ddi.service.db.repo.dataset","uk.ac.ebi.ddi.pipeline.indexer.pipeline.listener"})
@EnableMongoRepositories(basePackages = "uk.ac.ebi.ddi.service.db.repo")
@PropertySources({
        @PropertySource("classpath:common.properties"),
        @PropertySource("classpath:application.properties")
})
public class IndexerConfiguration {
    @Value("${ddi.common.mongo.app.user}")
    private String mongoAppUser;

    @Value("${ddi.common.mongo.app.password}")
    private String mongoAppPassword;

    @Value("${ddi.common.mongo.app.machine.one}")
    private String mongoPrimaryHost;

    @Value("${ddi.common.mongo.app.machine.two}")
    private String mongoSecondaryHost;

    @Value("${ddi.common.mongo.app.port}")
    private String mongoPort;

    @Value("${ddi.common.mongo.app.db}")
    private String mongoAppDB;

    @Value("${ddi.common.mongo.app.authenticationDatabase}")
    private String mongoAppAuthDB;

    @Value("${ddi.common.mongo.app.replicaset}")
    private String mongoAppReplicaset;
/*

    @Value("${ddi.common.elastic.app.host}")
    private String host;

    @Value("${ddi.common.elastic.app.port}")
    private String port;

    @Value("${ddi.common.elastic.app.user}")
    private String username;

    @Value("${ddi.common.elastic.app.password}")
    private String password;
*/

    @Bean("throwablePromotionListener")
    public ExecutionContextThrowablePromotionListener throwablePromotionListener(){
        return new ExecutionContextThrowablePromotionListener();
    }

    @Bean("stepExecutionPeriodListener")
    public StepExecutionPeriodListener stepExecutionPeriodListener(){
        return new StepExecutionPeriodListener();
    }

    @Bean("jobListener")
    public PipelineJobStatusListener jobListener(){
        return new PipelineJobStatusListener();
    }

    @Bean("pxpeptideAtlas")
    public List<String> pxpeptideAtlas(){
        return Arrays.asList("PeptideAtlas");
    }

    @Bean("fastDateFormat")
    public FastDateFormat fastDateFormat(){
        return FastDateFormat.getInstance("yyyyMMdd");
    }

    @Bean("currentDate")
    public String currentDate(){
            return fastDateFormat().format(new Date());
    }


    @Bean("simpleMongoDbFactoryID")
    public MongoDatabaseFactory mongoDatabaseFactory(){
        return new SimpleMongoClientDatabaseFactory("mongodb://"+mongoAppUser+":"+mongoAppPassword+"@"+mongoPrimaryHost+":"+mongoPort+","+mongoSecondaryHost+":"+mongoPort+"/"+mongoAppDB+"?authSource="+mongoAppAuthDB+"&replicaSet="+mongoAppReplicaset+"&serverSelectionTimeoutMS=200000&connectTimeoutMS=200000");
    }

   /* @Bean("elasticSearchWsConfig")
    public ElasticSearchWsConfigProd elasticSearchWsConfig(){
        if(!isStringInt(port)){
            port = "9200";
        }
        return new ElasticSearchWsConfigProd(Integer.valueOf(port), host, username, password);
    }

    @Bean("elasticSearchWsClient")
    public ElasticSearchWsClient elasticSearchWsClient(){
        return new ElasticSearchWsClient(elasticSearchWsConfig());
    }*/

    @Bean("similarityCounts")
    public SimilarityCounts similarityCounts(){
        return new SimilarityCounts();
    }

    @Bean("ebeyeWsConfig")
    public EbeyeWsConfigProd ebeyeWsConfig(){
        return new EbeyeWsConfigProd();
    }

    @Bean("citationClient")
    public CitationClient citationClient(){
        return new CitationClient(ebeyeWsConfig());
    }

    @Bean("datasetWsClient")
    public DatasetWsClient datasetWsClient(){
        return new DatasetWsClient(ebeyeWsConfig());
    }

    @Bean("domainWsClient")
    public DomainWsClient domainWsClient(){
        return new DomainWsClient(ebeyeWsConfig());
    }

    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoDatabaseFactory());
    }

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}