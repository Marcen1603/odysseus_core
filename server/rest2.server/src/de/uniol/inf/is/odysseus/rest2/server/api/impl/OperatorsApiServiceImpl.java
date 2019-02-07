package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.rest2.server.api.ApiResponseMessage;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class OperatorsApiServiceImpl extends OperatorsApiService {
	
	@Override
	public Response operatorsGet() throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
