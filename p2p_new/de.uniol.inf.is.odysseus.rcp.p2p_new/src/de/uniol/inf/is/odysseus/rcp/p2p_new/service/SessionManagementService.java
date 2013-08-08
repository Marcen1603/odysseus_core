package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SessionManagementService {
	
	public static ISession getActiveSession() {
		return getSessionManagement().loginSuperUser(null, "");
	}

	public static ISessionManagement getSessionManagement() {
		return UserManagementProvider.getSessionmanagement();
	}
}