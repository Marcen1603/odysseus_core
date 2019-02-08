package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public abstract class SinksApiService {
    public abstract Response sinksGet() throws NotFoundException;
    public abstract Response sinksNameDelete(String name
 ) throws NotFoundException;
    public abstract Response sinksNameGet(String name
 ) throws NotFoundException;
}
