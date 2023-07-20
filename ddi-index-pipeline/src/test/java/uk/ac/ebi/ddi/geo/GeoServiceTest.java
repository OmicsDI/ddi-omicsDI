package uk.ac.ebi.ddi.geo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.geo.model.ESummaryResult;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})
public class GeoServiceTest {

    @Autowired
    GeoService geoService;

    @Test
    public void testGeoData() throws JAXBException, IOException, XMLStreamException {
        Resource[] xmlResources = getXMLResources();
        Arrays.stream(xmlResources).forEach(
                resource -> {
                    try {
                        geoService.saveEntries(resource.getFile().getAbsolutePath());
                    } catch (IOException  e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void ProcessFile(String path) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(ESummaryResult.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(path));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ESummaryResult eSummaryResult = (ESummaryResult) unmarshaller.unmarshal(xsr);
        System.out.println(eSummaryResult.getDocSum().get(0).getItem().get(0).getName());
    }

    private Resource[] getXMLResources() throws IOException
    {
        ClassLoader classLoader = MethodHandles.lookup().getClass().getClassLoader();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);

        return resolver.getResources("classpath:GDS_*.xml");
    }

}
