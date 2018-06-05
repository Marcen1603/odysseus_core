package de.uniol.inf.is.odysseus.rest.serverresources;


import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;


public class AbstractLoginServerResource extends AbstractServerResource{
	
	protected ISession login(String tenant, String username, String password) {
		ITenant ten = UserManagementProvider.instance.getTenant(tenant);
		ISession user = SessionManagement.instance.login(username, password.getBytes(), ten);
		if (user != null) {
			return user;
		} 
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "User data are not correct");
	}	

}
