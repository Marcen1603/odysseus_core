package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.UsersApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.UsersApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.common.model.User;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import org.wso2.msf4j.Request;
import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;

@Path("/users")


@io.swagger.annotations.Api(description = "the users API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class UsersApi  {
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
}
