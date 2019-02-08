package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.DatatypesApiServiceFactory;

import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import org.wso2.msf4j.Request;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/datatypes")


@io.swagger.annotations.Api(description = "the datatypes API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class DatatypesApi  {
   private final DatatypesApiService delegate = DatatypesApiServiceFactory.getDatatypesApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available data types.", notes = "", response = Datatype.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Datatype.class, responseContainer = "List") })
    public Response datatypesGet(@Context Request request)
    throws NotFoundException {
        return delegate.datatypesGet(request);
    }
}
