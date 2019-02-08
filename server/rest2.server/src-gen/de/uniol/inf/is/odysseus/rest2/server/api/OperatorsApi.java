package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.OperatorsApiServiceFactory;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/operators")


@io.swagger.annotations.Api(description = "the operators API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class OperatorsApi  {
   private final OperatorsApiService delegate = OperatorsApiServiceFactory.getOperatorsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available operators.", notes = "", response = OperatorInfo.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = OperatorInfo.class, responseContainer = "List") })
    public Response operatorsGet()
    throws NotFoundException {
        return delegate.operatorsGet();
    }
}
