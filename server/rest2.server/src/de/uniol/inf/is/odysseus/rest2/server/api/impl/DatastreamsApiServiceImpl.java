package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.rest2.server.api.ApiResponseMessage;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class DatastreamsApiServiceImpl extends DatastreamsApiService {
	
	@Override
	public Response datastreamsGet() throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response datastreamsNameDelete(String name) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response datastreamsNameGet(String name) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
