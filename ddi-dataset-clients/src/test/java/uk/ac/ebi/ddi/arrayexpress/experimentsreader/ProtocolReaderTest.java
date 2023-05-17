package uk.ac.ebi.ddi.arrayexpress.experimentsreader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.ProtocolReader;

import java.io.File;
import java.net.URL;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 29/04/2016
 */
public class ProtocolReaderTest {

    URL url = ProtocolReaderTest.class.getClassLoader().getResource("example.xml");

    File file = null;

    @Before
    public void setUp() throws Exception {
        file = new File(url.toURI());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetExperiments() throws Exception {

        ProtocolReader experimentReader = new ProtocolReader(file);
        System.out.println(experimentReader.getProtocols().getProtocol());

    }


    @Test
    public void testGetUniqueProtocols() throws Exception{
//        Todo: Never use a local file for testing
//        file = new File("/Users/yperez/work/EBI-work/BD2K-Datasets/databases/ArrayExpress/protocols.xml");
//        ProtocolReader experimentReader = new ProtocolReader(file);
//        Map<String, List<Protocol>> protocolMap = experimentReader.getProtocols()
//                .getProtocol().parallelStream().collect(Collectors.groupingBy(Protocol::getType));
//
//
//        protocolMap = protocolMap.entrySet().stream()
//                .sorted(Comparator.<Map.Entry<String, List<Protocol>>>comparingInt(e->e.getValue().size()).reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (a,b) -> {throw new AssertionError();},
//                        LinkedHashMap::new
//                ));
//
//        protocolMap.entrySet().forEach( protocol -> {
//            System.out.println(protocol.getKey() + " : " +  protocol.getValue().size());
//        });
//


    }
}