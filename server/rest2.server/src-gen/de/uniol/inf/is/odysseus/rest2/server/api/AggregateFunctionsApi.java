package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.common.model.*;
import de.uniol.inf.is.odysseus.rest2.server.api.AggregateFunctionsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.factories.AggregateFunctionsApiServiceFactory;

import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/aggregate_functions")


@io.swagger.annotations.Api(description = "the aggregate_functions API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class AggregateFunctionsApi  {
   private final AggregateFunctionsApiService delegate = AggregateFunctionsApiServiceFactory.getAggregateFunctionsApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Returns a list of all available aggregate functions.", notes = "Returns a list of functions that can be used in the [aggregate operator](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Aggregate+%28and+Group%29+operator).", response = AggregateFunction.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = AggregateFunction.class, responseContainer = "List") })
    public Response aggregateFunctionsGet()
    throws NotFoundException {
        return delegate.aggregateFunctionsGet();
    }
}
