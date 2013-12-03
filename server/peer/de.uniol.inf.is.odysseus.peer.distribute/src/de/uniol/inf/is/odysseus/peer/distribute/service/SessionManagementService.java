package de.uniol.inf.is.odysseus.peer.distribute.service;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SessionManagementService {
	
	private static ISession activeSession;

	public static ISession getActiveSession() {
		
		if(activeSession == null)		
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		
		return activeSession;
		
	}
	
}