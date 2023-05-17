package uk.ac.ebi.ddi.service.db.repo.logger;

import uk.ac.ebi.ddi.service.db.model.logger.ResourceStatVisit;

import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2015
 */
public interface IHttpEventCustom {

    List<ResourceStatVisit> getHttpEventByDatasetResource(int size);

}
