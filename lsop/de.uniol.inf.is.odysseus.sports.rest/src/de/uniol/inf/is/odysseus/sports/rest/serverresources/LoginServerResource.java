package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.sports.rest.dto.LoginInformation;
import de.uniol.inf.is.odysseus.sports.rest.resources.ILoginResource;

public class LoginServerResource extends ServerResource implements
		ILoginResource {

	@Override
	public void login(LoginInformation loginInfo) {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ISession user = UserManagementProvider.getSessionmanagement().login(loginInfo.getUsername(), loginInfo.getPassword().getBytes(), tenant);
		if (user != null) {
			String token = user.getToken();
		    this.getResponse().getCookieSettings().add("securityToken", token);
		} else {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, "login data are not correct!");
		}					
	}

	

}
