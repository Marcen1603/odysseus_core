package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.UsersApiServiceFactory;
import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.PATCH;

@Path("/users")


@io.swagger.annotations.Api(description = "the users API")
public class UsersApi  extends AbstractApi {
   private final UsersApiService delegate = UsersApiServiceFactory.getUsersApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all users.", notes = "", response = User.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = User.class, responseContainer = "List") })
    public Response usersGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.usersGet(session);
    }
    @GET
    @Path("/{name}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a user by username.", notes = "", response = User.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = User.class) })
    public Response usersNameGet(@Context Request request, @ApiParam(value = "The username.",required=true) @PathParam("name") String name
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.usersNameGet(session, name);
    }
    @PATCH
    @Path("/{name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Updates the user with the given username. This action allows to send a partial user object.", notes = "", response = User.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = User.class) })
    public Response usersNamePatch(@Context Request request, @ApiParam(value = "The username.",required=true) @PathParam("name") String name
,@ApiParam(value = "" ,required=true) User user
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.usersNamePatch(session, name,user);
    }
    @PUT
    @Path("/{name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Updates the user with the given username. This actions needs a complete user object.", notes = "", response = User.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = User.class) })
    public Response usersNamePut(@Context Request request, @ApiParam(value = "The username.",required=true) @PathParam("name") String name
,@ApiParam(value = "" ,required=true) User user
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.usersNamePut(session, name,user);
    }
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Creates a new user.", notes = "", response = User.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created", response = User.class) })
    public Response usersPost(@Context Request request, @ApiParam(value = "" ,required=true) User user
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.usersPost(session, user);
    }
	@OPTIONS
    @Path("/{name}")
	public Response queriesOptions(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}
}
