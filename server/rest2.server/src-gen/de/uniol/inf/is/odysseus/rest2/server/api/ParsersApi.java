package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.ParsersApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ParsersApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.common.model.Query;

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

@Path("/parsers")


@io.swagger.annotations.Api(description = "the parsers API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-15T17:33:08.462Z[GMT]")
public class ParsersApi  {
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
}
