package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.OperatorsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.common.model.LogicalOperatorTypeInfo;

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

@Path("/operators")


@io.swagger.annotations.Api(description = "the operators API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class OperatorsApi  {
   private final OperatorsApiService delegate = OperatorsApiServiceFactory.getOperatorsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available operators.", notes = "", response = LogicalOperatorTypeInfo.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = LogicalOperatorTypeInfo.class, responseContainer = "List") })
    public Response operatorsGet(@Context Request request) {
    	final String securityToken = (String) request.getSession()
				.getAttribute(SecurityAuthInterceptor.SESSION_ATTRIBUTE_NAME);
		final Optional<ISession> session = Optional.ofNullable(SessionManagement.instance.login(securityToken));
        return delegate.operatorsGet(session);
    }
}
