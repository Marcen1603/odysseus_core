package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class ServicesApiServiceImpl extends ServicesApiService {

	@Override
	public Response servicesLoginPost(Optional<ISession> ignore, User user) {
		ITenant tenant = UserManagementProvider.instance.getTenant(user.getTenant() == null ? "" : user.getTenant());
		ISession session = SessionManagement.instance.login(user.getUsername(), user.getPassword().getBytes(), tenant);
		if (session != null) {
			final Token token = new Token();
			token.setToken(session.getToken());
			return Response.ok().entity(token).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@Override
	public Response servicesOutputschemaPost(Optional<ISession> session, Integer port) {
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

}
