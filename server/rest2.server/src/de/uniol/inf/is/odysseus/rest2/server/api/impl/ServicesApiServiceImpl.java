package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest2.server.api.ApiResponseMessage;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;
import de.uniol.inf.is.odysseus.rest2.server.model.User;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class ServicesApiServiceImpl extends ServicesApiService {

	@Override
	public Response servicesLoginPost(User user) throws NotFoundException {
		ITenant tenant = UserManagementProvider.instance.getTenant(user.getTenant() == null ? "" : user.getTenant());
		ISession session = SessionManagement.instance.login(user.getUsername(), user.getPassword().getBytes(), tenant);
		if (session != null) {
			return Response.ok().entity(session.getToken()).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@Override
	public Response servicesOutputschemaPost(Integer port) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

}
