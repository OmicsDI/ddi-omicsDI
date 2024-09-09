package uk.ac.ebi.ddi.ws.modules.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dictionary.DictionaryClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.domain.DomainWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.facet.FacetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.publication.PublicationWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.EbeyeWsConfigDev;
import uk.ac.ebi.ddi.ebe.ws.dao.config.EbeyeWsConfigProd;
import uk.ac.ebi.ddi.service.db.service.logger.DatasetResourceService;
import uk.ac.ebi.ddi.service.db.service.logger.HttpEventService;

import java.util.List;

@Configuration
@ComponentScan({"uk.ac.ebi.ddi.service.db.service","uk.ac.ebi.ddi.service.db.repo.facetsettings","uk.ac.ebi.ddi.service.db.repo.dataset","uk.ac.ebi.ddi.security.*"})
@EnableMongoRepositories(basePackages = "uk.ac.ebi.ddi.service.db.repo")
public class OpenAPIConfig {
    @Value("${omicsdi.openapi.dev-url}")
  private String devUrl;

  @Value("${omicsdi.openapi.prod-url}")
  private String prodUrl;

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

   @Value("${ddi.common.mongo.app.readPreference}")
   private String mongoAppReadPreference;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in Production environment");

    Contact contact = new Contact();
    contact.setEmail("omicsdi-support@ebi.ac.uk");
    contact.setName("omicsdi-support");
    contact.setUrl(prodUrl);

    License mitLicense = new License().name("APACHE LICENSE, VERSION 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html");

    Info info = new Info()
        .title("Data Discovery Index web service API")
        .version("openapi: 3.0.0")
        .contact(contact)
        .description("Programmatic access to the Data Discovery Index data via RESTful Web Services.").termsOfService("https://www.ebi.ac.uk/about/terms-of-use")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }

  @Bean("ebeyeWsConfigDev")
  public EbeyeWsConfigDev ebeyeWsConfigDev(){
    return new EbeyeWsConfigDev();
  }

  @Bean("ebeyeWsConfig")
 public EbeyeWsConfigProd ebeyeWsConfig(){
    return new EbeyeWsConfigProd();
 }

 @Bean("domainClient")
 public DomainWsClient domainClient(){
    return new DomainWsClient(ebeyeWsConfig());
 }

 @Bean("facetWsClient")
 public FacetWsClient facetWsClient(){
    return new FacetWsClient(ebeyeWsConfig());
 }

 @Bean("dataWsClient")
 public DatasetWsClient dataWsClient(){
    return new DatasetWsClient(ebeyeWsConfig());
 }

 @Bean("dictionaryClient")
 public DictionaryClient dictionaryClient(){
    return new DictionaryClient(ebeyeWsConfig());
 }

 @Bean("publicationWsClient")
 public PublicationWsClient publicationWsClient(){
    return new PublicationWsClient(ebeyeWsConfig());
 }

 @Bean("eventService")
 public HttpEventService eventService(){
    return new HttpEventService();
 }

 @Bean("resourceService")
 public DatasetResourceService resourceService(){
    return new DatasetResourceService();
 }

 @Bean("simpleMongoDbFactoryID")
 public MongoDatabaseFactory mongoDatabaseFactory(){
    return new SimpleMongoClientDatabaseFactory("mongodb://"+mongoAppUser+":"+mongoAppPassword+"@"+mongoPrimaryHost+":"+mongoPort+","+mongoSecondaryHost+":"+mongoPort+"/"+mongoAppDB+"?authSource="+mongoAppAuthDB+"&replicaSet="+mongoAppReplicaset+"&readPreference="+mongoAppReadPreference+"&serverSelectionTimeoutMS=200000&connectTimeoutMS=200000");
 }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/**");
    }
}
