package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.EventWebSocket;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ServicesApiServiceFactory;
import io.swagger.annotations.ApiParam;

@Path("/services")


@io.swagger.annotations.Api(description = "the services API")
public class ServicesApi  extends AbstractApi {
   private final ServicesApiService delegate = ServicesApiServiceFactory.getServicesApi();

    @GET
    @Path("/bundles")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of installed OSGi bundles.", notes = "", response = BundleInfo.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = BundleInfo.class, responseContainer = "List") })
    public Response servicesBundlesGet(@Context Request request, @ApiParam(value = "If filter is given only bundles whose symbolic name contains the given filter string are returned.") @QueryParam("filter") String filter
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesBundlesGet(session, filter);
    }
	@OPTIONS
    @Path("/bundles")
	public Response queriesOptions0(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}

    @GET
    @Path("/events")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of websockets that provides server events.", notes = "", response = EventWebSocket.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = EventWebSocket.class, responseContainer = "List") })
    public Response servicesEventsGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesEventsGet(session);
    }
	@OPTIONS
    @Path("/events")
	public Response queriesOptions1(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}
    @POST
    @Path("/login")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Executes a login and returns a token.", notes = "", response = Token.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Token.class) })
    public Response servicesLoginPost(@Context Request request, @ApiParam(value = "" ,required=true) User user
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesLoginPost(session, user);
    }
	@OPTIONS
    @Path("/login")
	public Response queriesOptions2(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}
    

    @POST
    @Path("/outputschema")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Determines the output schema of the given query.", notes = "", response = Schema.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Schema.class) })
    public Response servicesOutputschemaPost(@Context Request request, @ApiParam(value = "" ,required=true) Query query
,@ApiParam(value = "The port number of the output port of the root operator that should be used to determine the output schema.", defaultValue="0") @DefaultValue("0") @QueryParam("port") Integer port
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesOutputschemaPost(session, query,port);
    }
	@OPTIONS
    @Path("/outputschema")
	public Response queriesOptions3(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}

    @GET
    @Path("/session")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns the current token. Can be used to check if the current session is still valid.", notes = "", response = Token.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Token.class) })
    public Response servicesSessionGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesSessionGet(session);
    }
	@OPTIONS
    @Path("/session")
	public Response queriesOptions4(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}

}
