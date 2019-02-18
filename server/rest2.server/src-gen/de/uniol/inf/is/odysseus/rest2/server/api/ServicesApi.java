package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ServicesApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
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

@Path("/services")


@io.swagger.annotations.Api(description = "the services API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-18T19:38:36.797Z[GMT]")
public class ServicesApi  {
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
    @POST
    @Path("/login")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Executes a login and returns a token.", notes = "", response = Object.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Object.class) })
    public Response servicesLoginPost(@Context Request request, @ApiParam(value = "" ,required=true) User user
) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.servicesLoginPost(session, user);
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
}
