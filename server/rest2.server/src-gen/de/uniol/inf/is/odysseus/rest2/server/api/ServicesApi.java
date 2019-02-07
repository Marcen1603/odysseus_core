package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.ServicesApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.server.model.Schema;
import de.uniol.inf.is.odysseus.rest2.server.model.User;

import java.util.List;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import java.io.InputStream;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/services")


@io.swagger.annotations.Api(description = "the services API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class ServicesApi  {
   private final ServicesApiService delegate = ServicesApiServiceFactory.getServicesApi();

    @POST
    @Path("/login")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Executes a login and returns a token.", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = String.class) })
    public Response servicesLoginPost(@ApiParam(value = "" ,required=true) User user
)
    throws NotFoundException {
        return delegate.servicesLoginPost(user);
    }
    @POST
    @Path("/outputschema")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Determines the output schema of the given query.", notes = "", response = Schema.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Schema.class) })
    public Response servicesOutputschemaPost(@ApiParam(value = "The port number of the output port of the root operator that should be used to determine the output schema.", defaultValue="0") @DefaultValue("0") @QueryParam("port") Integer port
)
    throws NotFoundException {
        return delegate.servicesOutputschemaPost(port);
    }
}
