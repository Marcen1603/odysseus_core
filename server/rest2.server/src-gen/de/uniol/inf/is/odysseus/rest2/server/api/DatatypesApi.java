package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.DatatypesApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;

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

@Path("/datatypes")


@io.swagger.annotations.Api(description = "the datatypes API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-14T10:51:57.707Z[GMT]")
public class DatatypesApi  {
   private final DatatypesApiService delegate = DatatypesApiServiceFactory.getDatatypesApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available data types.", notes = "", response = Datatype.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Datatype.class, responseContainer = "List") })
    public Response datatypesGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.datatypesGet(session);
    }
}
