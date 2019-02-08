package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.SinksApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.SinksApiServiceFactory;

import io.swagger.annotations.ApiParam;
import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/sinks")


@io.swagger.annotations.Api(description = "the sinks API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class SinksApi  {
   private final SinksApiService delegate = SinksApiServiceFactory.getSinksApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available sinks.", notes = "", response = ResourceInformation.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ResourceInformation.class, responseContainer = "List") })
    public Response sinksGet()
    throws NotFoundException {
        return delegate.sinksGet();
    }
    @DELETE
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the sink with the given name.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response sinksNameDelete(@ApiParam(value = "The name of the sink.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.sinksNameDelete(name);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a sink by name.", notes = "", response = ResourceInformation.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ResourceInformation.class) })
    public Response sinksNameGet(@ApiParam(value = "The name of the sink.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.sinksNameGet(name);
    }
}
