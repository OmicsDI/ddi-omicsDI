package uk.ac.ebi.ddi.gpmdb;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 03/12/2015
 */
public class GetGPMDBInformationTest {

    GetGPMDBInformation instance = null;

    @Before
    public void setUp() throws Exception {
        instance = GetGPMDBInformation.getInstance();

    }

    @Test
    public void testGetListProteinIds() throws Exception {

    }

    @Test
    public void testGetUniqueProteinList() throws Exception {

        List<String> models = new ArrayList<String>();
        models.add("GPM32320002708");
        models.add("GPM32320002697");
        List<String> proteins = instance.getUniqueProteinList(models);

        System.out.println(proteins);
    }
}