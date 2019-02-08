package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.api.ApiResponseMessage;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.api.UsersApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class UsersApiServiceImpl extends UsersApiService {
	
	@Override
	public Response usersGet() throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response usersNameGet(String name) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response usersNamePatch(String name, User user) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response usersNamePut(String name, User user) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response usersPost(User user) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
