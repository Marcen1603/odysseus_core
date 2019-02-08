package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public abstract class QueriesApiService {
    public abstract Response queriesGet() throws NotFoundException;
    public abstract Response queriesIdDelete(Integer id
 ) throws NotFoundException;
    public abstract Response queriesIdGet(Integer id
 ) throws NotFoundException;
    public abstract Response queriesIdPatch(Integer id
 ,Query query
 ) throws NotFoundException;
    public abstract Response queriesNameDelete(String name
 ) throws NotFoundException;
    public abstract Response queriesNameGet(String name
 ) throws NotFoundException;
    public abstract Response queriesNamePatch(String name
 ,Query query
 ) throws NotFoundException;
    public abstract Response queriesPost(Query query
 ) throws NotFoundException;
}
