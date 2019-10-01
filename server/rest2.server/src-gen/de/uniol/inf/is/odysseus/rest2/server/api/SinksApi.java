package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Resource;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.SinksApiServiceFactory;
import io.swagger.annotations.ApiParam;

@Path("/sinks")


@io.swagger.annotations.Api(description = "the sinks API")
public class SinksApi  extends AbstractApi {
   private final SinksApiService delegate = SinksApiServiceFactory.getSinksApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available sinks.", notes = "", response = Resource.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Resource.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Access to resource requires authentication.", response = Resource.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Access to restricted resource is not allowed for current user.", response = Resource.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "A sink with the given name was not found.", response = Resource.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 423, message = "The sink could not be removed. This could happend, if the sink is part of a running query.", response = Resource.class, responseContainer = "List") })
    public Response sinksGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.sinksGet(session);
    }
    @DELETE
    @Path("/{name}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes the sink with the given name.", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Deleted", response = Void.class) })
    public Response sinksNameDelete(@Context Request request, @ApiParam(value = "The name of the sink.",required=true) @PathParam("name") String name
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.sinksNameDelete(session, name);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a sink by name.", notes = "", response = Resource.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Resource.class) })
    public Response sinksNameGet(@Context Request request, @ApiParam(value = "The name of the sink.",required=true) @PathParam("name") String name
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.sinksNameGet(session, name);
    }
}
