package uk.ac.ebi.ddi.ebe.ws.dao.client.dictionary;


import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ebe.ws.dao.client.EbeyeClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary.DictWord;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary.Item;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary.Suggestion;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary.Suggestions;

import java.net.URI;
import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 26/06/2015
 */

public class DictionaryClient extends EbeyeClient {

    public DictionaryClient(AbstractEbeyeWsConfig config) {
        super(config);
    }

    /**
     * Returns the Datasets for a domain with an specific Query
     * @param domainName Domain to retrieve the information
     * @param pattern pattern
     * @param size size to retrieve
     * @return A list of entries and the facets included
     */
    public DictWord getWordsDomains(String[] domainName, String pattern, int size) {

        Map<String, Integer> resultWords = new TreeMap<>();

        for (String domain: domainName) {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                    .scheme(config.getProtocol())
                    .host(config.getHostName())
                    .path("/ebisearch/ws/rest")
                    .path("/" + domain)
                    .path("/autocomplete")
                    .queryParam("term", pattern)
                    .queryParam("format", "JSON");
            URI uri = builder.build().encode().toUri();
            Suggestions results = restTemplate.getForObject(uri, Suggestions.class);
            if (results != null && results.getEntries() != null && results.getEntries().length > 0) {
                for (Suggestion word: results.getEntries()) {
                    int count = 1;
                    if (resultWords.containsKey(word.getSuggestion())) {
                        count = resultWords.get(word.getSuggestion()) + 1;
                    }
                    resultWords.put(word.getSuggestion(), count);
                }

            }
        }
        resultWords = sortByValues(resultWords);

        List<Item> items = new ArrayList<>();

        int count = 0;
        Iterator<String> word = resultWords.keySet().iterator();
        while (count < size && word.hasNext()) {
            items.add(new Item(word.next()));
            count++;
        }

        return new DictWord(items.size(), items);
    }

    private static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = (k1, k2) -> {
            int compare = map.get(k2).compareTo(map.get(k1));
            if (compare == 0) {
                return 1;
            } else {
                return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }


}

