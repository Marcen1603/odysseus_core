package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class DatastreamsApiServiceImpl extends DatastreamsApiService {

	@Override
	public Response datastreamsGet(Optional<ISession> session) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response datastreamsNameDelete(Optional<ISession> session, String name) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response datastreamsNameGet(Optional<ISession> session, String name) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}
}
