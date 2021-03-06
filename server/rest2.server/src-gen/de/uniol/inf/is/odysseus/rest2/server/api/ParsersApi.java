package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ParsersApiServiceFactory;
import io.swagger.annotations.ApiParam;

@Path("/parsers")


@io.swagger.annotations.Api(description = "the parsers API")
public class ParsersApi  extends AbstractApi {
   private final ParsersApiService delegate = ParsersApiServiceFactory.getParsersApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available parsers.", notes = "", response = String.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = String.class, responseContainer = "List") })
    public Response parsersGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.parsersGet(session);
    }
    @POST
    @Path("/{name}")
    @Consumes({ "text/plain" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Executes a script with the adressed parser.", notes = "", response = Query.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "OK", response = Query.class),
        
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created", response = Query.class) })
    public Response parsersNamePost(@Context Request request, @ApiParam(value = "The name of the parser.",required=true) @PathParam("name") String name
,@ApiParam(value = "" ,required=true) String body
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.parsersNamePost(session, name,body);
    }
	@OPTIONS
    @Path("/{name}")
	public Response queriesOptions(@Context Request request) throws NotFoundException {
		return super.queriesOptions(request);
	}
}
