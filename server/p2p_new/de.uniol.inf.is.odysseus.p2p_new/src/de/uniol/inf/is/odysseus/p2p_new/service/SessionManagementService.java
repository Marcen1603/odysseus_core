package de.uniol.inf.is.odysseus.p2p_new.service;

import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class SessionManagementService {
	
	private static ISession currentSession;
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}

	public static ISessionManagement getSessionManagement() {
		return UserManagementProvider.getSessionmanagement();
	}
	
	public static ITenant getTenant() {
		return UserManagementProvider.getDefaultTenant();
	}
}
