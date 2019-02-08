package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.ParsersApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ParsersApiServiceFactory;

import io.swagger.annotations.ApiParam;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/parsers")


@io.swagger.annotations.Api(description = "the parsers API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class ParsersApi  {
   private final ParsersApiService delegate = ParsersApiServiceFactory.getParsersApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available parsers.", notes = "", response = String.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = String.class, responseContainer = "List") })
    public Response parsersGet()
    throws NotFoundException {
        return delegate.parsersGet();
    }
    @POST
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Executes a script with the adressed parser.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "OK", response = Void.class) })
    public Response parsersNamePost(@ApiParam(value = "The name of the parser.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.parsersNamePost(name);
    }
}
