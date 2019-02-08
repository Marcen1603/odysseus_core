package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.DatastreamsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/datastreams")


@io.swagger.annotations.Api(description = "the datastreams API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class DatastreamsApi  {
   private final DatastreamsApiService delegate = DatastreamsApiServiceFactory.getDatastreamsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available data streams. A data stream is provided by a source or a query (as view).", notes = "", response = ResourceInformation.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ResourceInformation.class, responseContainer = "List") })
    public Response datastreamsGet()
    throws NotFoundException {
        return delegate.datastreamsGet();
    }
    @DELETE
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the data stream with the given name.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response datastreamsNameDelete(@ApiParam(value = "The name of the data stream.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.datastreamsNameDelete(name);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a data stream by name.", notes = "", response = ResourceInformation.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ResourceInformation.class) })
    public Response datastreamsNameGet(@ApiParam(value = "The name of the data stream.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.datastreamsNameGet(name);
    }
}
