package uk.ac.ebi.ddi.dbgap;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})
public class DbgapServiceTest {

    @Autowired
    DbgapService dbgapService;

    @Test
    public void testDbgap() throws IOException {
        String resourceName = "GapExchange_phs001826.v1.p1.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        dbgapService.saveEntries(file.getAbsolutePath());
    }
}
