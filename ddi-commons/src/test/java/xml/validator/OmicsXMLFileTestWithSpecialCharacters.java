package xml.validator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;

import java.io.File;
import java.net.URL;


public class OmicsXMLFileTestWithSpecialCharacters {

    File file;

    OmicsXMLFile reader;

    @Before
    public void setUp() throws Exception {

        URL fileURL = this.getClass().getClassLoader().getResource("PRIDE_EBEYE_PXD007896.xml");

        file = new File(fileURL.toURI());

        reader = new OmicsXMLFile(file);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEntryIds() throws Exception {
        Assert.assertEquals(reader.getEntryIds().size(),1);
        reader.getEntryById("PXD007896");
    }
}