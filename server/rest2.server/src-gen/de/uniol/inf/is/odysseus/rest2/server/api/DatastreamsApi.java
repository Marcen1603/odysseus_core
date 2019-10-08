package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.DatastreamsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import de.uniol.inf.is.odysseus.rest2.common.model.Resource;

import java.util.Optional;

import org.wso2.msf4j.Request;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;

@Path("/datastreams")


@io.swagger.annotations.Api(description = "the datastreams API")
public class DatastreamsApi extends AbstractApi{
   private final DatastreamsApiService delegate = DatastreamsApiServiceFactory.getDatastreamsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available data streams. A data stream is provided by a source or a query (as view).", notes = "", response = Resource.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Resource.class, responseContainer = "List") })
    public Response datastreamsGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.datastreamsGet(session);
    }
    @DELETE
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the data stream with the given name.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response datastreamsNameDelete(@Context Request request, @ApiParam(value = "The name of the data stream.",required=true) @PathParam("name") String name
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.datastreamsNameDelete(session, name);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a data stream by name.", notes = "", response = Resource.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Resource.class) })
    public Response datastreamsNameGet(@Context Request request, @ApiParam(value = "The name of the data stream.",required=true) @PathParam("name") String name
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.datastreamsNameGet(session, name);
    }
    
	@OPTIONS
    @Path("/{name}")
	public Response queriesOptions(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}
}
