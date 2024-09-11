package uk.ac.ebi.ddi.ws.modules.controller;

/**
 * @author Yasset Perez-Riverol ypriverol
 */

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.record.Location;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.dictionary.DictionaryClient;
import uk.ac.ebi.ddi.ebe.ws.dao.client.domain.DomainWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.Entry;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.Facet;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.FacetValue;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.SimilarResult;
import uk.ac.ebi.ddi.service.db.model.dataset.*;
import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;
import uk.ac.ebi.ddi.service.db.repo.facetsettings.FacetSettingsRepository;
import uk.ac.ebi.ddi.service.db.service.database.DatabaseDetailService;
import uk.ac.ebi.ddi.service.db.service.dataset.*;
import uk.ac.ebi.ddi.service.db.service.enrichment.EnrichmentInfoService;
import uk.ac.ebi.ddi.service.db.service.logger.DatasetResourceService;
import uk.ac.ebi.ddi.service.db.service.logger.HttpEventService;
import uk.ac.ebi.ddi.service.db.service.similarity.CitationService;
import uk.ac.ebi.ddi.service.db.service.similarity.EBIPubmedSearchService;
import uk.ac.ebi.ddi.service.db.service.similarity.ReanalysisDataService;
import uk.ac.ebi.ddi.ws.modules.error.exception.OmicsCustomException;
import uk.ac.ebi.ddi.ws.modules.error.exception.ResourceNotFoundException;
import uk.ac.ebi.ddi.ws.modules.model.DataSetResult;
import uk.ac.ebi.ddi.ws.modules.model.DatasetDetail;
import uk.ac.ebi.ddi.ws.modules.model.DatasetSummary;
import uk.ac.ebi.ddi.ws.modules.model.Organism;
import uk.ac.ebi.ddi.ws.modules.model.Role;
import uk.ac.ebi.ddi.ws.modules.model.Content;
import uk.ac.ebi.ddi.ws.modules.service.LocationService;
import uk.ac.ebi.ddi.ws.modules.service.UserPermissionService;
import uk.ac.ebi.ddi.ws.modules.util.Constants;
import uk.ac.ebi.ddi.ws.modules.util.RepoDatasetMapper;
import uk.ac.ebi.ddi.ws.modules.util.WsUtilities;
import uk.ac.ebi.ddi.ws.modules.util.FacetViewAdapter;
import uk.ac.ebi.ddi.ws.modules.util.FileUtils;
import uk.ac.ebi.ddi.ws.modules.util.MapUtils;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Tag(name = "dataset", description = "Retrieve the information about the dataset including search functionalities")
@Controller
@RequestMapping(value = "/dataset")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DatasetController {

    @Autowired
    DatasetWsClient dataWsClient;

    @Autowired
    DomainWsClient domainWsClient;

    @Autowired
    private DatasetResourceService resourceService;

    @Autowired
    HttpEventService eventService;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    IDatasetSimilarsService datasetSimilarsService;

    IDatasetService datasetService;

    @Autowired
    IUnMergeDatasetService iUnMergeDatasetService;

    @Autowired
    FacetSettingsRepository facetSettingsRepository;

    @Autowired
    DatabaseDetailService databaseDetailService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private FileGroupService fileGroupService;

    //autowired by ctor param
    IMostAccessedDatasetService mostAccessedDatasetService;
    CitationService citationService;
    EBIPubmedSearchService ebiPubmedSearchService;
    ReanalysisDataService reanalysisDataService;
    EnrichmentInfoService enrichmentService;
    UnMergeDatasetService unMergeDatasetService;

    @Autowired
    UserPermissionService userPermissionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);

    @Autowired
    public DatasetController(CitationService citationService,
                             EBIPubmedSearchService ebiPubmedSearchService,
                             ReanalysisDataService reanalysisDataService,
                             IMostAccessedDatasetService mostAccessedDatasetService,
                             IDatasetService datasetService,
                             EnrichmentInfoService enrichmentService,
                             UnMergeDatasetService unMergeDatasetService,
                             UserPermissionService userPermissionService) {
        RepoDatasetMapper.ebiPubmedSearchService = ebiPubmedSearchService;
        RepoDatasetMapper.mostAccessedDatasetService = mostAccessedDatasetService;
        RepoDatasetMapper.citationService = citationService;
        RepoDatasetMapper.reanalysisDataService = reanalysisDataService;
        RepoDatasetMapper.datasetService = datasetService;


        this.userPermissionService = userPermissionService;
        this.mostAccessedDatasetService = mostAccessedDatasetService;
        this.citationService = citationService;
        this.ebiPubmedSearchService = ebiPubmedSearchService;
        this.reanalysisDataService = reanalysisDataService;
        this.datasetService = datasetService;
        this.enrichmentService = enrichmentService;
        this.unMergeDatasetService = unMergeDatasetService;
    }

    //@CrossOrigin
    @Operation(summary = "Search for datasets in the resource" ,
            description = "Retrieve datasets in the resource using different queries")
    @RequestMapping(value = "/search", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DataSetResult search(
             @Parameter(description = "General search term against multiple fields including, e.g: cancer human")
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
             @Parameter(description = "domain for which it will search for, e.g: omics")
            @RequestParam(value = "domain", required = false, defaultValue = "omics") String domain,
             @Parameter(description = "Field to sort the output of the search results, e.g:  id, publication_date")
            @RequestParam(value = "sortfield", required = false, defaultValue = "") String sortfield,
             @Parameter(description = "Type of sorting ascending or descending, e.g: ascending")
            @RequestParam(value = "order", required = false, defaultValue = "") String order,
             @Parameter(description = "The starting point for the search, e.g: 0")
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
             @Parameter(description = "The number of records to be retrieved, e.g: maximum 100")
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
             @Parameter(description = "The starting point for the search, e.g: 0")
            @RequestParam(value = "faceCount", required = false, defaultValue = "30") int facetCount) {
        try {
            query = (query == null || query.isEmpty()) ? "*:*" : query;

        query = WsUtilities.escapeSpecialCharacters(query);

        query = query + " NOT (isprivate:true)";
        query = modifyIfSearchByYear(query);

        QueryResult queryResult = null;

        if (!domain.equals(Constants.MAIN_DOMAIN)) {
            queryResult = dataWsClient.getDatasets(
                    Constants.MODELEXCHANGE_DOMAIN, query, Constants.DATASET_SUMMARY, sortfield, order, start, size,
                    facetCount);
        } else {
            queryResult = dataWsClient.getDatasets(
                    Constants.MAIN_DOMAIN, query, Constants.DATASET_SUMMARY, sortfield, order, start, size, facetCount);
        }

        QueryResult taxonomies = null;

        Set<String> taxonomyIds = RepoDatasetMapper.getTaxonomyIds(queryResult);

        // The number of queries should be controlled using the maximum QUERY threshold in this case 100 entries
        // for the EBE web service.
        if (taxonomyIds.size() > Constants.HIGH_QUERY_THRESHOLD) {
            List<QueryResult> results = new ArrayList<>();
            List<String> list = new ArrayList<>(taxonomyIds);
            int count = 0;
            for (int i = 0; i < taxonomyIds.size(); i += Constants.HIGH_QUERY_THRESHOLD) {
                Set<String> currentIds;
                if ((i + Constants.HIGH_QUERY_THRESHOLD) < taxonomyIds.size()) {
                    currentIds = new HashSet<>(list.subList(i, i + Constants.HIGH_QUERY_THRESHOLD));
                } else {
                    currentIds = new HashSet<>(list.subList(i, taxonomyIds.size() - 1));
                }
                try {
                    if (currentIds.size() > 0) {
                        QueryResult queryResult1 = dataWsClient.getDatasetsById(Constants.TAXONOMY_DOMAIN,
                                Constants.TAXONOMY_FIELDS, currentIds);
                        results.add(queryResult1);
                    }
                } catch (Exception ex) {
                    LOGGER.error("exception while getting taxonomy ids data", ex.getMessage());
                }
                count = i;
            }
            //Set<String> currentIds = new HashSet<>
            // (list.subList(count, taxonomyIds.size() - 1));
            //results.add(dataWsClient.getDatasetsById(Constants.TAXONOMY_DOMAIN,
            // Constants.TAXONOMY_FIELDS, currentIds));
            taxonomies = RepoDatasetMapper.mergeQueryResult(results);

        } else if (taxonomyIds.size() > 0) {
           taxonomies = dataWsClient.getDatasetsById(Constants.TAXONOMY_DOMAIN, Constants.TAXONOMY_FIELDS, taxonomyIds);
        }

        Facet[] facets = queryResult.getFacets();

        for (Facet facet : facets) {
            if (facet.getId().equals("omics_type") && domain.equals(Constants.MODELEXCHANGE_DOMAIN.toString())) {
                FacetValue[] nonMultiomics = Arrays.stream(facet.getFacetValues()).
                        filter(rt -> !rt.getLabel().equals("Multiomics")).toArray(FacetValue[]::new);
                facet.setFacetValues(nonMultiomics);
            }
        }


        if (queryResult.getCount() > 0) {
            queryResult.setFacets((new FacetViewAdapter(facetSettingsRepository)).process(queryResult.getFacets()));
        }

            return RepoDatasetMapper.asDataSummary(queryResult, taxonomies);
        } catch (Exception e) {
            LOGGER.error("Error in calling search method " + e.getMessage());
            throw new OmicsCustomException(e.getMessage());
        }
    }


    private String modifyIfSearchByYear(String query) {
        Pattern pattern = Pattern.compile("publication_date:\"\\s*(\\d{4})\"");
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            String matchedYear = matcher.group().replaceAll("publication_date:\"\\s*(\\d{4})\"", "$1");
            String searchByYear = "[" + matchedYear + "0000 TO " + matchedYear + "1231]";
            return query.replaceAll("publication_date:\"\\s*(\\d{4})\"", "publication_date:" + searchByYear);
        }
        return query;
    }


      @Operation(summary = "Retrieve the list of dataset's file using positions")
    @RequestMapping(value = "/{database}/{accession}/files", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<Map<String, String>> getFilesAt(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @PathVariable(value = "accession") String accession,
             @Parameter(description = "Database accession id, e.g: pride")
            @PathVariable(value = "database") String database,
            @RequestParam(value = "position") List<Integer> positions) {
        database = databaseDetailService.retriveAnchorName(database);
        Dataset dataset = datasetService.read(accession, database);
        List<String> files = new ArrayList<>();
        dataset.getFiles().keySet().forEach(x -> files.addAll(dataset.getFiles().get(x)));
        files.sort(Comparator.comparing(String::toString));
        List<GalaxyFileExtension> galaxyFileExtensions = fileGroupService.findAllGalaxyExtensions();
        galaxyFileExtensions.sort((x1, x2) -> x2.getExtension().length() - x1.getExtension().length());
        List<Map<String, String>> result = new ArrayList<>();
        for (int pos : positions) {
            Map<String, String> element = new HashMap<>();
            element.put("url", files.get(pos));
            String baseName = FileUtils.getFilenameFromUrl(files.get(pos));
            String type = "other";
            for (GalaxyFileExtension extension : galaxyFileExtensions) {
                if (baseName.toLowerCase().contains(extension.getExtension())) {
                    type = extension.getType();
                    break;
                }
            }
            element.put("type", type);
            result.add(element);
        }
        return result;
    }

      @Operation(summary = "Retrieve the latest datasets in the repository",
            description = "Retrieve the latest datasets in the repository")
    @RequestMapping(value = "/latest", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DataSetResult latest(
             @Parameter(description = "Number of terms to be retrieved, e.g : maximum 100, default 30")
            @RequestParam(value = "size", required = false, defaultValue = "30") int size,
             @Parameter(description = "domain for search results, e.g : ")
            @RequestParam(value = "domain", required = false, defaultValue = "omics") String domain) {

        String query = "*:*";
        Calendar calendar = Calendar.getInstance();
        Integer nextYear = calendar.get(Calendar.YEAR) + 1;

        DataSetResult dataSetResult = new DataSetResult();
        try {
            if (!domain.equals(Constants.MAIN_DOMAIN)) {
                dataSetResult = search(query, Constants.MODELEXCHANGE_DOMAIN, Constants.PUB_DATE_FIELD,
                        "descending", 0, size, 0);
            } else {
                dataSetResult = search(query, Constants.MAIN_DOMAIN, Constants.PUB_DATE_FIELD,
                        "descending", 0, size, 0);
            }
            dataSetResult.setDatasets(dataSetResult.getDatasets().stream().
                    filter(r -> !r.getPublicationDate().
                            contains(nextYear.toString())).collect(Collectors.toList()));
            //dataSetResult = dataSetResult.getDatasets().stream().
            // filter(r -> !r.getPublicationDate().contains("2022")).;
        } catch (Exception ex) {
            LOGGER.error("exception in latest api in data controller", ex.getMessage());
        }
        return dataSetResult;
    }

      @Operation(summary = "Retrieve an Specific Dataset", description = "Retrieve an specific dataset")
    @RequestMapping(value = "/{database}/{accession}", method = GET,
            produces = {APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public ResponseEntity<?> getDataset(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @PathVariable(value = "accession") String accession,
             @Parameter(description = "Database accession id, e.g: pride")
            @PathVariable(value = "database") String database,
            @RequestParam(value = "debug", defaultValue = "false", required = false) boolean debug,
            @RequestHeader HttpHeaders httpHeaders,
            HttpServletRequest request) throws OmicsCustomException {
        Map<String, Object> result = new HashMap<>();
        MediaType contentType = MediaType.APPLICATION_JSON;
          try {
            database = databaseDetailService.retriveAnchorName(database);
            if(accession.contains(".json") || accession.contains(".xml")){
                contentType = accession.contains(".json") ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML;
                accession = accession.substring(0, accession.lastIndexOf("."));
            }
            Dataset dataset = datasetService.read(accession, database);
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            ipAddress = ipAddress != null ? ipAddress : request.getHeader("X-Cluster-Client-IP");
            ipAddress = ipAddress != null ? ipAddress : request.getRemoteAddr();
            Map<String, Set<String>> additional = dataset.getAdditional();
            additional.remove(DSField.Additional.DATASET_FILE.getName());
            additional.put(DSField.Additional.ADDITIONAL_ACCESSION.key(), dataset.getAllSecondaryAccessions());
            additional.remove(DSField.Additional.SECONDARY_ACCESSION.key());
            result.put("accession", dataset.getAccession());
            result.put("name", dataset.getName());
            result.put("database", dataset.getDatabase());
            result.put("description", dataset.getDescription());
            result.put("dates", MapUtils.eliminateSet(dataset.getDates()));
            result.put("additional", additional);
            result.put("cross_references", dataset.getCrossReferences());
            result.put("is_claimable", dataset.isClaimable());
            result.put("scores", dataset.getScores());
            String primaryAccession = getPreferableAccession(dataset.getFiles(), ipAddress, dataset.getAccession());
            List<GalaxyFileExtension> galaxyFileExtensions = fileGroupService.findAllGalaxyExtensions();
            galaxyFileExtensions.sort((x1, x2) -> x2.getExtension().length() - x1.getExtension().length());
            MediaType finalContentType = contentType;
            List<Object> files = dataset.getFiles().keySet().stream().map(x -> {
                Map<String, Object> providers = new HashMap<>();
                Map<String, List<String>> fileGroups = new HashMap<>();
                dataset.getFiles().get(x).forEach(f -> {
                    String baseName = FileUtils.getFilenameFromUrl(f);
                    List<String> urls = new ArrayList<>(Collections.singleton(f));
                    String type = "Other";
                    for (GalaxyFileExtension extension : galaxyFileExtensions) {
                        if (baseName.toLowerCase().contains(extension.getExtension())) {
                            type = extension.getType().substring(0, 1).toUpperCase() + extension.getType().substring(1);
                            break;
                        }
                    }
                    if (fileGroups.containsKey(type)) {
                        urls.addAll(fileGroups.get(type));
                    }
                    fileGroups.put(type, urls);
                });
                providers.put("files", fileGroups);
                providers.put("type", x.equals(primaryAccession) ? "primary" : "mirror");
                if(finalContentType != null)
                    return  ResponseEntity.status(HttpStatus.OK).contentType(finalContentType).body(providers);
                else
                    return ResponseEntity.status(HttpStatus.OK).body(providers);
            }).collect(Collectors.toList());
            result.put("file_versions", files);
            if (debug) {
                result.put("ip_address", ipAddress);
                result.put("headers", httpHeaders.toSingleValueMap());
            }
        } catch (NullPointerException nlex) {
            throw new OmicsCustomException("Either Accession or Database is not available, " +
                    "Please provide correct data.");
        }
          if(contentType != null){
              return  ResponseEntity.status(HttpStatus.OK).contentType(contentType).body(result);
          } else {
              return ResponseEntity.status(HttpStatus.OK).body(result);
          }
    }

    private String getPreferableAccession(Map<String, Set<String>> files, String ipAddress, String defaultAccession) {
        if (files.size() == 1) {
            return files.keySet().iterator().next();
        }
        if (files.size() == 0) {
            return defaultAccession;
        }
        try {
            Map<String, Double> distances = new HashMap<>();
            Location userLocation = locationService.getLocation(ipAddress);
            for (String accession : files.keySet()) {
                URI uri = new URI(files.get(accession).iterator().next());
                Location serverLocation = locationService.getLocation(uri.getHost());
                distances.put(accession, LocationService.distance(userLocation, serverLocation));
            }
            distances = MapUtils.sortByValue(distances);
            return distances.keySet().iterator().next();
        } catch (GeoIp2Exception | IOException | URISyntaxException e) {
            return defaultAccession;
        }
    }

    private Map<String, String> getAvailableGroups() {
        List<FileGroup> fileGroups = fileGroupService.findAll();
        Map<String, String> result = new HashMap<>();
        for (FileGroup fileGroup : fileGroups) {
            for (String extension: fileGroup.getExtensions()) {
                result.put(extension, fileGroup.getGroup());
            }
        }
        return result;
    }

      @Operation(summary = "Retrieve a batch of datasets", description = "Retrieve an specific dataset")
    @RequestMapping(value = "/batch", method = GET, produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public Map<String, Object> getMultipleDatasets(
             @Parameter(description = "List of accessions, matching database by index")
            @RequestParam(value = "accession") String[] accessions,
             @Parameter(description = "List of databases, matching accession by index")
            @RequestParam(value = "database") String[] databases) {
        if (accessions.length != databases.length) {
            throw new IllegalArgumentException("The amounts of accessions and databases are not match");
        }

        Map<String, Object> result = new HashMap<>();

        List<DatasetDetail> datasetDetails = new ArrayList<>();
        List<DatasetShort> failure = new ArrayList<>();
        Map<DatasetShort, Boolean> datasetShorts = new HashMap<>();

        for (int i = 0; i < accessions.length; i++) {
            String domain = databaseDetailService.retriveAnchorName(databases[i]);
            datasetShorts.put(new DatasetShort(domain, accessions[i]), false);
        }
        List<Dataset> datasets = datasetService.findMultipleDatasets(datasetShorts.keySet());
        for (Dataset dataset : datasets) {
            try {
                DatasetDetail datasetDetail = new DatasetDetail();
                datasetDetail = getBasicDatasetInfo(datasetDetail, dataset);
                datasetDetails.add(datasetDetail);
                datasetShorts.put(new DatasetShort(dataset.getDatabase(), dataset.getAccession()), true);
            } catch (Exception e) {
                failure.add(new DatasetShort(dataset.getDatabase(), dataset.getAccession()));
            }
        }

        for (DatasetShort datasetShort : datasetShorts.keySet()) {
            if (!datasetShorts.get(datasetShort)) {
                failure.add(datasetShort);
            }
        }
        result.put("datasets", datasetDetails);
        result.put("failure", failure);
        return result;
    }

    //@Hidden
      @Operation(summary = "Retrieve an Specific Dataset", description = "Retrieve an specific dataset")
    @RequestMapping(value = "/get", method = GET, produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DatasetDetail get(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @RequestParam(value = "accession", required = true) String accession,
             @Parameter(description = "Database accession id, e.g: pride")
            @RequestParam(value = "database", required = true) String database,
            HttpServletRequest httpServletRequest, HttpServletResponse resp) {
        accession = accession.replaceAll("\\s", "");

        DatasetDetail datasetDetail = new DatasetDetail();
        Dataset dsResult = datasetService.read(accession, databaseDetailService.retriveAnchorName(database));

        datasetDetail = getBasicDatasetInfo(datasetDetail, dsResult);
        datasetDetail = getDatasetInfo(datasetDetail, dsResult);
        try {
        // Trace the access to the dataset
        DatasetResource resource = resourceService.read(accession, database);
        if (resource == null) {
            resource = new DatasetResource("http://www.omicsdi.org/" + database + "/" + accession, accession, database);
            resource = resourceService.save(resource);
        }

            HttpEvent event = WsUtilities.tranformServletResquestToEvent(httpServletRequest);
            event.setResource(resource);
            eventService.save(event);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage() + " in logger service");
        }

        if (datasetDetail.getId() == null) {
            throw new ResourceNotFoundException("Dataset not found");
        }

        DatasetSimilars similars = datasetSimilarsService.read(accession,
                databaseDetailService.retriveAnchorName(database));
        datasetDetail = WsUtilities.mapSimilarsToDatasetDetails(datasetDetail, similars);

        return datasetDetail;
    }


      @Operation(summary = "Retrieve an Specific Dataset", description = "Retrieve an specific dataset")
    @RequestMapping(value = "/mostAccessed", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DataSetResult getMostAccessed(
             @Parameter(description = "The most accessed datasets size, e.g: 20")
            @RequestParam(value = "size", required = true, defaultValue = "20") int size,
             @Parameter(description = "domain for search results, e.g : ")
            @RequestParam(value = "domain", required = false, defaultValue = "omics") String domain) {

            DataSetResult result = new DataSetResult();
            try {
                List<DatasetSummary> datasetSummaryList = new ArrayList<>();
                List<MostAccessedDatasets> datasets;
                if (!domain.equals(Constants.MAIN_DOMAIN)) {
                    List<String> database = Arrays.asList("FAIRDOMHub", "Physiome Model Repository",
                            "BioModels", "Cell Collective");
                    datasets = mostAccessedDatasetService.getMostAccessedByDatabase(database);
                } else {
                    datasets = mostAccessedDatasetService.readAll(0, size).getContent();
                }
                //datasets.map(r -> )
                for (MostAccessedDatasets dataset : datasets) {
                    //if(dataset.getDates().values().stream().filter(r-> r.contains("20")))
                    DatasetSummary datasetSummary = new DatasetSummary();
                    datasetSummary.setTitle(dataset.getName());
                    datasetSummary.setViewsCount(dataset.getTotal());
                    datasetSummary.setSource(databaseDetailService.retriveSolrName(dataset.getDatabase()));
                    datasetSummary.setId(dataset.getAccession());
                    if (dataset.getAdditional().containsKey(DSField.Additional.OMICS.key())) {
                        List<String> omicsType = Collections.list(
                                Collections.enumeration(dataset.getAdditional().get(DSField.Additional.OMICS.key())));
                        datasetSummary.setOmicsType(omicsType);
                    }
                    datasetSummaryList.add(datasetSummary);
                }
                result.setDatasets(datasetSummaryList);
                result.setCount(size);
            } catch (Exception ex) {
                LOGGER.error("error occured in mostaccessed", ex.getMessage());
            }
            return result;
    }


      @Operation(summary = "Retrieve the related datasets to one Dataset", description = "Retrieve the related datasets to one Dataset")
    @RequestMapping(value = "/getSimilar", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DataSetResult moreLikeThis(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @RequestParam(value = "accession", required = true) String accession,
             @Parameter(description = "Database accession id, e.g : pride")
            @RequestParam(value = "database", required = true) String database,
             @Parameter(description = "domain for search results, e.g : ")
            @RequestParam(value = "domain", required = false, defaultValue = "omics") String domain) {

        try {
            SimilarResult queryResult = dataWsClient.getSimilarProjectsByDomain(database, accession,
                    Constants.MORELIKE_FIELDS, domain);
            DataSetResult result = new DataSetResult();
            List<DatasetSummary> datasetSummaryList = new ArrayList<>();

        Map<String, Map<String, String>> currentIds = new HashMap<>();


        if (queryResult != null && queryResult.getEntries() != null && queryResult.getEntries().length > 0) {

            for (Entry entry: queryResult.getEntries()) {
                if (entry.getId() != null && entry.getSource() != null) {
                    Map<String, String> ids = currentIds.get(entry.getSource());
                    ids = (ids != null) ? ids : new HashMap<>();
                    if (!(entry.getId().equalsIgnoreCase(accession) && entry.getSource().equalsIgnoreCase(database))) {
                        ids.put(entry.getId(), entry.getScore());
                    }
                    if (!ids.isEmpty()) {
                        currentIds.put(entry.getSource(), ids);
                    }
                }
            }

            for (String currentDomain: currentIds.keySet()) {
                QueryResult datasetResult = dataWsClient.getDatasetsById(
                        currentDomain, Constants.DATASET_DETAIL, currentIds.get(currentDomain).keySet());
                datasetSummaryList.addAll(WsUtilities.transformSimilarDatasetSummary(
                        datasetResult, currentDomain, currentIds.get(currentDomain)));
            }

            datasetSummaryList.sort((o1, o2) -> {
                Double value1 = Double.valueOf(o1.getScore());
                Double value2 = Double.valueOf(o2.getScore());
                if (value1 < value2) {
                    return 1;
                } else if (Objects.equals(value1, value2)) {
                    return 0;
                } else {
                    return -1;
                }
            });

            result.setDatasets(datasetSummaryList);
            result.setCount(datasetSummaryList.size());

                return result;
            }
        } catch (Exception e) {
            LOGGER.error("Error in calling moreLikeThis method " + e.getMessage());
            throw  new OmicsCustomException(e.getMessage());
        }

        return null;
    }


      @Operation(summary = "Retrieve all file links for a given dataset", description = "Retrieve all file links for a given dataset")
    @RequestMapping(value = "/getFileLinks", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<String> getFileLinks(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @RequestParam(value = "accession", required = true) String accession,
             @Parameter(description = "Database accession id, e.g : pride")
            @RequestParam(value = "database", required = true) String database) {
        List<String> files = new ArrayList<>();

        String[] fields = {Constants.DATASET_FILE};

        Set<String> currentIds = Collections.singleton(accession);

        QueryResult datasetResult = dataWsClient.getDatasetsById(
                databaseDetailService.retriveAnchorName(database), fields, currentIds);

        if (datasetResult != null && datasetResult.getEntries() != null && datasetResult.getEntries().length > 0) {
            Entry entry = datasetResult.getEntries()[0];
            String[] fileNames = entry.getFields().get(Constants.DATASET_FILE);
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName: fileNames) {
                    if (fileName != null) {
                        files.add(fileName);
                    }
                }
            }
        }
        return files;
    }


    @Hidden
      @Operation(summary = "Get dataset by Url", description = "Retrieve dataset by source url")
    @RequestMapping(value = "/getDatasetByUrl", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public DatasetDetail getDatasetByUrl(
             @Parameter(description = "Url of the Dataset in the resource, "
                    + "e.g : https://www.ebi.ac.uk/arrayexpress/experiments/E-MTAB-5789")
            @RequestBody() String url) {
        DatasetDetail datasetDetail = new DatasetDetail();
        Dataset dsResult = datasetService.findByFullDatasetLink(url);

        datasetDetail = getBasicDatasetInfo(datasetDetail, dsResult);
        return getDatasetInfo(datasetDetail, dsResult);
    }

      @Operation(summary = "check file validaton", description = "check file validation url")
    @RequestMapping(value = "/checkfile", method = RequestMethod.POST, consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<String> checkFile(
             @Parameter(description = "small sample xml file to be validated for omicsdi or by covid portal, "
                    + "e.g :omicsdidata.xml")
            @RequestParam(value = "file", required = true) MultipartFile file,
             @Parameter(description = "domain of the validator , "
                    + "e.g :omicsdi, bycovid")
            @RequestParam(value = "validatorType", required = false, defaultValue = "omicsdi")
                    String validatorType,
             @Parameter(description = "errors or errors and warnings, "
                        + "e.g :true or false")
            @RequestParam(value = "isError", required = false, defaultValue = "true")
                    boolean isError) throws IOException {

        //System.out.println(file.getInputStream().read());
        //File targetFile = new File("/partition/tmp/up/temp.xml");
        File targetFile = File.createTempFile("temp", ".xml");

        java.nio.file.Files.copy(
                file.getInputStream(),
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        targetFile.isFile();
        //OmicsXMLFile.validateSchema(targetFile);
        List<String> errorList = new LinkedList<String>();


            List<Tuple<String,String>> errors = OmicsXMLFile.validateSemantic(targetFile);
            //errors.addAll(OmicsXMLFile.validateSchema(targetFile));
            errors.stream().map(Tuple::getValue).forEach(LOGGER::debug);

            if (!isError) {
                errorList = errors.stream().map(rt -> updateMessage(rt.getValue().toString(), validatorType)).
                        filter(r -> filterBycovidWarnings(r, validatorType)).distinct().collect(Collectors.toList());
            } else {
                errorList = errors.stream().filter(r -> (r.getKey().equals("Error") &&
                        //!r.getValue().toString().contains("Publication") &&
                        !r.getValue().toString().contains("cvc-complex") &&
                        !r.getValue().toString().contains("Experiment") &&
                        //!r.getValue().toString().contains("Omics Type") &&
                        //!r.getValue().toString().contains("Submitter") &&
                        filterBycovidMsg(r.getValue().toString(), validatorType))
                ).map(rt -> updateMessage(rt.getValue().toString(), validatorType))
                        .distinct().collect(Collectors.toList());
            }
         return errorList;
    }

    private boolean filterBycovidWarnings(String message, String validatorType) {
        if (message.contains("[Warning]") && validatorType.equals(Constants.BYCOVID)) {
            if (message.contains("Database Description") ||
                    message.contains("Database Release Date") || message.contains("Database Entries Count")
                || message.contains("Database Keywords") || message.contains("Database URL")
            ) {
                return true;
            }
            /*if (message.contains("Dataset Abstract Sample Protocol") ||
                    message.contains("Dataset Abstract Sample Protocol") || message.contains("Instrument Platform")) {
                return true;
            }*/
            return false;
        }
        return true;
    }

    private String updateMessage(String message, String validatorType) {

        if (validatorType.equals(Constants.BYCOVID)) {
            if (message.contains("Dataset Omics Type") || message.contains("Submitter")) {
                message = message.replaceAll("Error", "Warning");
            }
            /*if (message.contains("Submitter")) {
                message = message.replaceAll("Error", "Warn");
            }*/
        }
        return message;
    }

    private boolean filterBycovidMsg(String message, String validatorType) {
        if (validatorType.equals(Constants.BYCOVID)) {
            return (message.contains("Dataset Omics Type") || message.contains("Submitter")) ? false : true;
        } else {
            return true;
        }
    }
    private DatasetDetail getBasicDatasetInfo(DatasetDetail datasetDetail, Dataset argDataset) {
        if (argDataset == null) {
            return datasetDetail;
        }
        Map<String, Set<String>> datesField = argDataset.getDates();
        Map<String, Set<String>> fields = argDataset.getAdditional();
        Map<String, Set<String>> crossFields = argDataset.getCrossReferences();
        Set<String> omicsType = argDataset.getAdditional().get(DSField.Additional.OMICS.key());
        Set<String> publicationDates = argDataset.getDates().get(DSField.Date.PUBLICATION.key());

        datasetDetail.setId(argDataset.getAccession());
        datasetDetail.setSource(databaseDetailService.retriveSolrName(argDataset.getDatabase()));
        datasetDetail.setName(argDataset.getName());
        datasetDetail.setDescription(argDataset.getDescription());
        datasetDetail.setClaimable(argDataset.isClaimable());
        datasetDetail.setOmics_type(new ArrayList<>(omicsType));
        datasetDetail.setScores(argDataset.getScores());
        datasetDetail.setCurrentStatus(argDataset.getCurrentStatus());

        if (publicationDates != null && !publicationDates.isEmpty()) {
            datasetDetail.setPublicationDate(publicationDates.iterator().next());
        }

        Set<String> fullDatasetLinks = fields.get(DSField.Additional.LINK.key());
        if (fullDatasetLinks != null && fullDatasetLinks.size() > 0) {
            datasetDetail.setFull_dataset_link(fullDatasetLinks.iterator().next());
        }

        Set<String> diseases = fields.get(DSField.Additional.DISEASE_FIELD.key());
        if (diseases != null && diseases.size() > 0) {
            datasetDetail.setDiseases(diseases.toArray(new String[0]));
        }
        Set<String> viewCountScaled = fields.get(DSField.Additional.VIEW_COUNT_SCALED.key());
        if (viewCountScaled != null && viewCountScaled.size() > 0) {
            datasetDetail.setViewsCountScaled(Double.valueOf(viewCountScaled.iterator().next()));
        }

        Set<String> citationCountScaled = fields.get(DSField.Additional.CITATION_COUNT_SCALED.key());
        if (citationCountScaled != null && citationCountScaled.size() > 0) {
            datasetDetail.setCitationsCountScaled(Double.valueOf(citationCountScaled.iterator().next()));
        }

        Set<String> reanalysisCountScaled = fields.get(DSField.Additional.REANALYSIS_COUNT_SCALED.key());
        if (reanalysisCountScaled != null && reanalysisCountScaled.size() > 0) {
            datasetDetail.setReanalysisCountScaled(Double.valueOf(reanalysisCountScaled.iterator().next()));
        }

        Set<String> searchCountScaled = fields.get(DSField.Additional.SEARCH_COUNT_SCALED.key());
        if (searchCountScaled != null && searchCountScaled.size() > 0) {
            datasetDetail.setConnectionsCountScaled(Double.valueOf(searchCountScaled.iterator().next()));
        }

        Set<String> downloadCountScaled = fields.get(DSField.Additional.DOWNLOAD_COUNT_SCALED.key());
        if (downloadCountScaled != null && downloadCountScaled.size() > 0) {
            String downloadValue = downloadCountScaled.iterator().next();
            datasetDetail.setDownloadCountScaled(Double.valueOf(downloadValue.isEmpty() ? "0.0" : downloadValue));
        }

        Set<String> downloadCount = fields.get(DSField.Additional.DOWNLOAD_COUNT.key());
        if (downloadCount != null && downloadCount.size() > 0) {
            String downloadValue = downloadCount.iterator().next();
            datasetDetail.setDownloadCount(Integer.valueOf(downloadValue.isEmpty() ? "0.0" : downloadValue));
        }

        if (datesField != null && datesField.size() > 0) {
            datasetDetail.setDates(datesField);
        }

        Set<String> tissues = fields.get(DSField.Additional.TISSUE_FIELD.key());
        if (tissues != null && tissues.size() > 0) {
            datasetDetail.setTissues(setToArray(tissues, String.class));
        }

        Set<String> instruments = fields.get(DSField.Additional.INSTRUMENT.key());

        if (instruments != null && instruments.size() > 0) {
            datasetDetail.setArrayInstruments(setToArray(instruments, String.class));
        }

        Set<String> experimentType = fields.get(DSField.Additional.TECHNOLOGY_TYPE.key());
        if (experimentType != null && experimentType.size() > 0) {
            datasetDetail.setArrayExperimentType(setToArray(experimentType, String.class));
        }

        Set<String> pubmedids = crossFields.get(DSField.CrossRef.PUBMED.key());
        if ((pubmedids != null) && (pubmedids.size() > 0)) {
            datasetDetail.setArrayPublicationIds(setToArray(pubmedids, String.class));
        }

        Set<String> submitterKeys = fields.get(DSField.Additional.SUBMITTER_KEYWORDS.key());
        Set<String> curatorKeys = fields.get(DSField.Additional.CURATOR_KEYWORDS.key());

        if (submitterKeys != null && curatorKeys != null && submitterKeys.size() > 0 && curatorKeys.size() > 0) {
            datasetDetail.setKeywords(setToArray(submitterKeys, String.class), setToArray(curatorKeys, String.class));
        }

        Set<String> organization = fields.get(DSField.Additional.SUBMITTER_AFFILIATION.key());
        if ((organization != null) && (organization.size() > 0)) {
            datasetDetail.setOrganization(new ArrayList<>(organization));
        }

        Set<String> submitter = fields.get(DSField.Additional.SUBMITTER.key());
        if ((submitter != null) && (submitter.size() > 0)) {
            datasetDetail.setSubmitter(submitter);
        }

        Set<String> repositories = fields.get(DSField.Additional.REPOSITORY.key());

        if (repositories != null && repositories.size() > 0) {
            datasetDetail.setRepositories(repositories);
        }

        if (argDataset.getScores() != null) {
            Scores scores = argDataset.getScores();
            datasetDetail.setViewsCount(scores.getViewCount());
            datasetDetail.setCitationsCount(scores.getCitationCount());
            datasetDetail.setReanalysisCount(scores.getReanalysisCount());
            datasetDetail.setConnectionsCount(scores.getSearchCount());
        }

        return datasetDetail;
    }

    public DatasetDetail getDatasetInfo(DatasetDetail datasetDetail, Dataset argDataset) {
        if (argDataset == null) {
            return datasetDetail;
        }

        Map<String, Set<String>> fields = argDataset.getAdditional();


        Set<String> dataProtocols = fields.get(DSField.Additional.DATA.key());
        if (dataProtocols != null && dataProtocols.size() > 0) {
            datasetDetail.addProtocols(DSField.Additional.DATA.key(), dataProtocols.toArray(new String[0]));
        }

        Set<String> sampleProtocols = fields.get(DSField.Additional.SAMPLE.key());
        if (sampleProtocols != null && sampleProtocols.size() > 0) {
            datasetDetail.addProtocols(DSField.Additional.SAMPLE.key(), sampleProtocols.toArray(new String[0]));
        }

        Set<String> submitterMail = fields.get(DSField.Additional.SUBMITTER_MAIL.key());
        if ((submitterMail != null) && (submitterMail.size() > 0)) {
            datasetDetail.setSubmitterMail(submitterMail);
        }

        Set<String> submitterEmail = fields.get(DSField.Additional.SUBMITTER_EMAIL.key());
        if ((submitterEmail != null) && (submitterEmail.size() > 0)) {
            datasetDetail.setSubmitterMail(submitterEmail);
        }

        Set<String> labhead = fields.get(Constants.LAB_HEAD_FIELD);
        if ((labhead != null) && (labhead.size() > 0)) {
            datasetDetail.setLabHead(labhead);
        }

        Set<String> labHeadMail = fields.get(Constants.LAB_HEAD_MAIL_FIELD);
        if ((labHeadMail != null) && (labHeadMail.size() > 0)) {
            datasetDetail.setLabHeadMail(labHeadMail);
        }

        Set<String> organismsSet = fields.get(DSField.Additional.SPECIE_FIELD.getName());
        if ((organismsSet != null) && (organismsSet.size() > 0)) {
            List<Organism> organisms = fields.get(DSField.Additional.SPECIE_FIELD.getName()).
                    parallelStream().map(r -> new Organism("", r)).collect(Collectors.toList());
            datasetDetail.setOrganisms(organisms);
        }

        Set<String> taxonomyIds = argDataset.getCrossReferences().get(DSField.CrossRef.TAXONOMY.key());
        if (taxonomyIds != null && taxonomyIds.size() > 0) {
            ArrayList<String> ids = new ArrayList<>(taxonomyIds);
            QueryResult taxonomies = new QueryResult();

            if (ids.size() > 0) {
                if (ids.size() < 99) {
                    taxonomies = dataWsClient.getDatasetsById(
                            Constants.TAXONOMY_DOMAIN, Constants.TAXONOMY_FIELDS, new HashSet<>(ids));
                } else {
                    int i = 0;
                    while (i + 50 < ids.size()) {
                        List<String> idTemp = ids.subList(i, i + 50);
                        taxonomies.addResults(dataWsClient.getDatasetsById(
                                Constants.TAXONOMY_DOMAIN, Constants.TAXONOMY_FIELDS, new HashSet<>(idTemp)));
                        i = i + 50;
                    }
                    if (i < ids.size()) {
                        List<String> idTemp = ids.subList(i, ids.size());
                        taxonomies.addResults(dataWsClient.getDatasetsById(
                                Constants.TAXONOMY_DOMAIN, Constants.TAXONOMY_FIELDS, new HashSet<>(idTemp)));
                    }
                }
            }

            RepoDatasetMapper.addTaxonomy(datasetDetail, taxonomies);
        }

        //secondary accessions resolved via identifiers collection
        List<String> secondaryAccessionsPlus = new ArrayList<>();
        List<String> secondaryAccession1 = enrichmentService.getAdditionalAccession(argDataset.getAccession());
        if (null != secondaryAccession1) {
            secondaryAccessionsPlus.addAll(secondaryAccession1);
        }
        Set<String> secondaryAccession = fields.get(DSField.Additional.SECONDARY_ACCESSION.key());
        Set<String> additionalAccession = fields.get(DSField.Additional.ADDITIONAL_ACCESSION.key());

        if (additionalAccession != null && additionalAccession.size() > 0) {
            for (String acc : additionalAccession) {
                if (null == datasetDetail.getSecondary_accession()) {
                    datasetDetail.setSecondary_accession(new HashSet<>());
                }
                datasetDetail.getSecondary_accession().add(acc);
            }
        }
        if ((secondaryAccession != null) && (secondaryAccession.size() > 0)) {
            datasetDetail.setSecondary_accession(secondaryAccession);
            for (String s: secondaryAccession) {
                List<String> secondaryAccession2 = enrichmentService.getAdditionalAccession(s);
                if (null != secondaryAccession2) {
                    secondaryAccessionsPlus.addAll(secondaryAccession2);
                }
            }
        }

        for (String acc : secondaryAccessionsPlus) {
            if (null == datasetDetail.getSecondary_accession()) {
                datasetDetail.setSecondary_accession(new HashSet<>());
            }
            datasetDetail.getSecondary_accession().add(acc);
        }

        return datasetDetail;
    }

    public <T> T[] setToArray(Set<T> argSet, Class<T> type) {
        return argSet.toArray((T[]) Array.newInstance(type, argSet.size()));
    }

      @Operation(summary = "Retrieve all similar dataset based on pubmed id",
            description = "Retrieve all datasets which have same pubmed id")
    @RequestMapping(value = "/getSimilarByPubmed", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<Dataset> getSimilarDatasets(
             @Parameter(description = "Pubmed Id of the Dataset in the resource, e.g : 16585740")
            @RequestParam(value = "pubmed", required = true) String pubmed) {
        return datasetService.getSimilarByPubmed(pubmed);
    }

    @Hidden
      @Operation(summary = "Retrieve merge candidates", description = "Retrieve merge candidates")
    @RequestMapping(value = "/getMergeCandidates", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<MergeCandidate> getMergeCandidates(
             @Parameter(description = "The starting point for the search, e.g: 0")
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
             @Parameter(description = "The number of records to be retrieved, e.g: maximum 100")
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        return datasetService.getMergeCandidates(start, size);
    }

    @Hidden
      @Operation(summary = "Merge datasets", description = "Merge datasets")
    @RequestMapping(value = "/merge", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public void mergeDatasets(@RequestBody MergeCandidate mergeCandidate,
                              @RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        datasetService.mergeDatasets(mergeCandidate);
    }

    @Hidden
      @Operation(summary = "Skipping merge datasets", description = "Skip merge datasets")
    @RequestMapping(value = "/skipMerge", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public void skipDatasets(@RequestBody MergeCandidate mergeCandidate,
                             @RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        datasetService.skipMerge(mergeCandidate);
    }

    @Hidden
      @Operation(summary = "Multiomics merging datasets", description = "Multiomics merging datasets")
    @RequestMapping(value = "/multiomicsMerge", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public void multiomicsMergeDatasets(@RequestBody MergeCandidate candidate,
                                        @RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        datasetService.addMultiomics(candidate);
    }

    @Hidden
      @Operation(summary = "Retrieve merge candidate counts", description = "Retrieve merge candidate counts")
    @RequestMapping(value = "/getMergeCandidateCount", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public Integer getMergeCandidateCount(@RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        return datasetService.getMergeCandidateCount();
    }

    @Hidden
      @Operation(summary = "Retrieve all dataset counts by database", description = "Retrieve all datasets count by database")
    @RequestMapping(value = "/getDbDatasetCount", method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<DbDatasetCount> getDbDatasetsCount() {
        return datasetService.getDbDatasetsCount();
    }


    @Hidden
      @Operation(summary = "Unmerge datasets", description = "Un-merge datasets")
    @RequestMapping(value = "/unmerge", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    public void unMergeDatasets(@RequestBody List<UnMergeDatasets> mergeCandidate,
                                @RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        unMergeDatasetService.unmergeDataset(mergeCandidate);
    }

    @Hidden
      @Operation(summary = "Get all merged datasets", description = "Get all merged datasets")
    @RequestMapping(value = "/getAllmerged", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public List<UnMergeDatasets> getAllMergedDatasets(@RequestHeader("x-auth-token") String accessToken) {
        userPermissionService.hasRole(Role.ADMIN, accessToken);
        return unMergeDatasetService.findAll();
    }


    @Hidden
      @Operation(summary = "Get all datasets", description = "Get all datasets")
    @RequestMapping(value = "/getAll", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Stream<Dataset> getAllDatasets() {
        return datasetService.getAllData();
    }

    @Hidden
      @Operation(summary = "Get all datasets by pages", description = "Get all datasets by pages")
    @RequestMapping(value = "/getDatasetPage", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<Dataset> getAllDatasetPage(
             @Parameter(description = "The starting point for the search, e.g: 0")
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
             @Parameter(description = "The number of records to be retrieved, e.g: maximum 100")
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return datasetService.getDatasetPage(start, size);
    }


    @Hidden
    @SuppressWarnings("checkstyle:linelength")
      @Operation(summary = "Get all datasets by database", description = "Get all datasets by database")
    @RequestMapping(value = "/getDatasetByDB", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<Dataset> getAllDatasetsByDB(@Parameter(description = "The name of database of which " +
            "datasets need to be retrieved.")@RequestParam(value = "database", required = true) String database,
             @Parameter(description = "The starting point for the search, e.g: 0")
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
             @Parameter(description = "The number of records to be retrieved, e.g: maximum 100.")
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        Page<Dataset> data = datasetService.readDatasetsByDatabase(database, start, size);
        return data;
    }

      @Operation(summary = "Retrieve the related datasets to one Dataset", description = "Retrieve the related datasets to one Dataset")
    @RequestMapping(value = "/getDRS", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public Content[] getDRSID(
             @Parameter(description = "Accession of the Dataset in the resource, e.g : PXD000210")
            @RequestParam(value = "accession", required = true) String acc,
             @Parameter(description = "Database accession id, e.g : pride")
            @RequestParam(value = "database", required = true) String domain) {

        RestTemplate restTemplate = new RestTemplate();
        Content[] result = new Content[0];
        try {
            if (result.length == 0) {
                LOGGER.trace("drs is not continued");
            }
           /* result = restTemplate.getForObject(
                    "http://hx-rke-wp-webadmin-21-master-1.caas.ebi.ac.uk:30008/datasets/{database}/{accession}",
                    Content[].class, domain, acc
            );*/
        } catch (Exception ex) {
            LOGGER.error("exception caught in getdrs request", ex.getMessage());
        }

        return result;
    }

}
