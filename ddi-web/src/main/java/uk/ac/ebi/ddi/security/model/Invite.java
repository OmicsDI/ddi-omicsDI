package uk.ac.ebi.ddi.security.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by azorin on 22/11/2017.
 */


@Document(collection = "invites")
public class Invite {
    @Id
    public String id;

    public String userName;

    public String email;

    public DataSetShort[] dataSets;
}
