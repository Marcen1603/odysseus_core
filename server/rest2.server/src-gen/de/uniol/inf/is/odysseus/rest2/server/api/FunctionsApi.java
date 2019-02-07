package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.FunctionsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.FunctionsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import de.uniol.inf.is.odysseus.rest2.server.model.Function;

import java.util.List;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import java.io.InputStream;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/functions")


@io.swagger.annotations.Api(description = "the functions API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class FunctionsApi  {
   private final FunctionsApiService delegate = FunctionsApiServiceFactory.getFunctionsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available functions.", notes = "Returns a list of [functions and operations (MEP)](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/MEP%3A+Functions+and+Operators) Odysseus provides.", response = Function.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Function.class, responseContainer = "List") })
    public Response functionsGet()
    throws NotFoundException {
        return delegate.functionsGet();
    }
}
