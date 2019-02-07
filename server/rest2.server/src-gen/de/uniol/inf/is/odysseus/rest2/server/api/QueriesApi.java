package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.QueriesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.QueriesApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.server.model.Query;

import java.util.List;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import java.io.InputStream;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/queries")


@io.swagger.annotations.Api(description = "the queries API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class QueriesApi  {
   private final QueriesApiService delegate = QueriesApiServiceFactory.getQueriesApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all installed queries.", notes = "", response = Query.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Query.class, responseContainer = "List") })
    public Response queriesGet()
    throws NotFoundException {
        return delegate.queriesGet();
    }
    @DELETE
    @Path("/{id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the query with the given ID.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response queriesIdDelete(@ApiParam(value = "The ID of the query.",required=true) @PathParam("id") Integer id
)
    throws NotFoundException {
        return delegate.queriesIdDelete(id);
    }
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a query by ID.", notes = "", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Query.class) })
    public Response queriesIdGet(@ApiParam(value = "The ID of the query.",required=true) @PathParam("id") Integer id
)
    throws NotFoundException {
        return delegate.queriesIdGet(id);
    }
    @PATCH
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Updates the query with the given ID.", notes = "With this operation you can update the state of the query (e.g. to start or stop a query). Updating the query text is not allowed. Remove this query and add a new one instead.", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Query.class) })
    public Response queriesIdPatch(@ApiParam(value = "The ID of the query.",required=true) @PathParam("id") Integer id
,@ApiParam(value = "" ,required=true) Query query
)
    throws NotFoundException {
        return delegate.queriesIdPatch(id,query);
    }
    @DELETE
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the query with the given name.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response queriesNameDelete(@ApiParam(value = "The name of the query.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.queriesNameDelete(name);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a query by name.", notes = "", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Query.class) })
    public Response queriesNameGet(@ApiParam(value = "The name of the query.",required=true) @PathParam("name") String name
)
    throws NotFoundException {
        return delegate.queriesNameGet(name);
    }
    @PATCH
    @Path("/{name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Updates the query with the given name.", notes = "With this operation you can update the state of the query (e.g. to start or stop a query). Updating the query text is not allowed. Remove this query and add a new one instead.", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Query.class) })
    public Response queriesNamePatch(@ApiParam(value = "The name of the query.",required=true) @PathParam("name") String name
,@ApiParam(value = "" ,required=true) Query query
)
    throws NotFoundException {
        return delegate.queriesNamePatch(name,query);
    }
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Installs a new query.", notes = "", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created", response = Query.class) })
    public Response queriesPost(@ApiParam(value = "" ,required=true) Query query
)
    throws NotFoundException {
        return delegate.queriesPost(query);
    }
}
