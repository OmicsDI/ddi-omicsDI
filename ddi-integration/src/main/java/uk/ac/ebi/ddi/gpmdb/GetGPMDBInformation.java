package uk.ac.ebi.ddi.gpmdb;

import uk.ac.ebi.ddi.gpmdb.extws.gpmdb.client.ModelGPMDBClient;
import uk.ac.ebi.ddi.gpmdb.extws.gpmdb.config.GPMDBWsConfigProd;

import java.util.*;


/**
 * This project takes class Retrieve information from GPMDB, it allows to retrieve
 * the proteins ids for an specific model, etc.
 *
 *
 * @author Yasset Perez-Riverol
 */

public class GetGPMDBInformation {

    private static GetGPMDBInformation instance;

    private ModelGPMDBClient gpmdbClient;

    private GetGPMDBInformation(){
        gpmdbClient = new ModelGPMDBClient(new GPMDBWsConfigProd());
    }

    public static GetGPMDBInformation getInstance(){
        if(instance == null)
            instance = new GetGPMDBInformation();
        return instance;
    }


    /**
     * Retrieve the List of Proteins for a GPMDBB model
     * @param modelID model id
     * @return list of proteins
     */
    public List<String> getListProteinIds(String modelID){
        if(modelID != null && !modelID.isEmpty()){
            String[] proteinIds = gpmdbClient.getAllProteins(modelID);
            if(proteinIds != null){
                List<String> proteinsID = new ArrayList<String>();
                for(String proteinID: proteinIds){
                    proteinsID.add(proteinID);
                }
                return proteinsID;
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Retrieve the non-redundant list of protein ids for a list of models
     * @param modelIds model ids
     * @return List of protein ids.
     */
    public List<String> getUniqueProteinList(List<String> modelIds){
        if(modelIds != null && !modelIds.isEmpty()){
            Set<String> ids = new TreeSet<String>();
            for(String modelID: modelIds)
                ids.addAll(getListProteinIds(modelID));
            return new ArrayList<String>(ids);
        }
        return Collections.EMPTY_LIST;
    }



}
