package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.DatatypesApiServiceFactory;

import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;

import java.util.Optional;

import org.wso2.msf4j.Request;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.SecurityAuthInterceptor;

@Path("/datatypes")


@io.swagger.annotations.Api(description = "the datatypes API")
public class DatatypesApi  extends AbstractApi  {
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
