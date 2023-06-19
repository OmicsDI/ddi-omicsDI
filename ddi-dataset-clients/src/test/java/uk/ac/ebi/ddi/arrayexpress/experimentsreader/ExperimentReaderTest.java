package uk.ac.ebi.ddi.arrayexpress.experimentsreader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.ExperimentReader;

import java.io.File;
import java.net.URL;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 29/04/2016
 */
public class ExperimentReaderTest {

    URL url = ExperimentReaderTest.class.getClassLoader().getResource("example.xml");

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
        ExperimentReader experimentReader = new ExperimentReader(file);
    }
}