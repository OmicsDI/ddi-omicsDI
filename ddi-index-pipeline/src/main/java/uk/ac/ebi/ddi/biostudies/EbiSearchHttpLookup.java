package uk.ac.ebi.ddi.biostudies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to fetch results from EBI Search over HTTP during indexing.
 *
 * @author mpearce
 */
public class EbiSearchHttpLookup {

   private static final Logger LOG = LoggerFactory.getLogger(EbiSearchHttpLookup.class);

   private final String baseUrl;

   public EbiSearchHttpLookup(String baseUrl) {
      this.baseUrl = baseUrl;
   }

   /**
    * Execute a search using an HTTP request.
    *
    * @param domain the domain to search on.
    * @param query  the query to search.
    * @param fields the fields to be returned.
    * @param size   the number of entries to return.
    * @return a list of results, converted into a Map of fields, or an empty list if an
    * error occurs with the request.
    * @throws IllegalArgumentException if the domain or query parameters are missing or blank.
    */
   public List<Map<String, Object>> search(final String domain, final String query, List<String> fields, int size) {
      if (StringUtils.isBlank(domain)) {
         throw new IllegalArgumentException("Domain missing from HTTP search");
      }
      if (StringUtils.isBlank(query)) {
         throw new IllegalArgumentException("Query missing from HTTP search");
      }

      try {
         URIBuilder uriBuilder = new URIBuilder(String.format("%s/%s", baseUrl, domain));
         uriBuilder.setParameter("format", "json");
         uriBuilder.setParameter("query", query);
         if (fields != null && !fields.isEmpty()) {
            uriBuilder.setParameter("fields", StringUtils.join(fields, ','));
         }
         if (size != 0) {
            uriBuilder.setParameter("size", "" + size);
         }
         SearchResponse response = fetchResultsFromUrl(uriBuilder.build().toURL());

         // Convert the response into a list of mappings
         if (response != null) {
            return response.entries
                    .stream()
                    .map(Entry::getFields)
                    .toList();
         }
      } catch (URISyntaxException e) {
         LOG.error("URI syntax exception building search request: {}", e.getMessage());
      } catch (MalformedURLException e) {
         LOG.error("URL exception building search request: {}", e.getMessage());
      }
      return Collections.emptyList();
   }

   /**
    * Fetch search results from a given URL.
    * @param url the search URL.
    * @return a response object, or {@code null} if an error occurs making the
    * request.
    */
   private SearchResponse fetchResultsFromUrl(final URL url) {
      try {
         ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         return mapper.readValue(url, SearchResponse.class);
      } catch (IOException e) {
         LOG.error("Caught IOException reading search results from {}: {}", url, e.getMessage());
         return null;
      }
   }


   /*
    * Response objects for deserializing from JSON.
    */
   static class SearchResponse {

      private final int         hitCount;
      private final List<Entry> entries;

      @JsonCreator
      public SearchResponse(@JsonProperty("hitCount") int hitCount, @JsonProperty("entries") List<Entry> entries) {
         this.hitCount = hitCount;
         this.entries = entries;
      }
   }

   static class Entry {
      private final String              source;
      private final Map<String, Object> fields = new HashMap<>();

      @JsonCreator
      public Entry(@JsonProperty("id") String id,
                   @JsonProperty("acc") String acc,
                   @JsonProperty("source") String source,
                   @JsonProperty("fields") Map<String, Object> fields) {
         this.source = source;
         if (fields != null) {
            this.fields.putAll(fields);
         }
         if (stringIsNotBlank(id)) {
            this.fields.put("id", id);
         }
         if (stringIsNotBlank(acc)) {
            this.fields.put("acc", acc);
         }
      }

      public Map<String, Object> getFields() {
         return fields;
      }

      private static boolean stringIsNotBlank(String str) {
         return str != null && !str.isBlank();
      }
   }
}
