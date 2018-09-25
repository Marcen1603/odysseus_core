package de.uniol.inf.is.odysseus.rest.serverresources;


import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public class AbstractSessionServerResource extends AbstractServerResource{
	
	protected ISession loginWithToken(String token) {	
		ISession session = SessionManagement.instance.login(token);	
		
		if (session != null) {
			return session;
		} 
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Security token unknown!");
	}
	

}
