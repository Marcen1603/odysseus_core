package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.api.UsersApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class UsersApiServiceImpl extends UsersApiService {

	@Override
	public Response usersGet(Optional<ISession> session) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response usersNameGet(Optional<ISession> session, String name) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response usersNamePatch(Optional<ISession> session, String name, User user) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response usersNamePut(Optional<ISession> session, String name, User user) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response usersPost(Optional<ISession> session, User user) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}
}
