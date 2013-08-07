package de.uniol.inf.is.odysseus.p2p_new.service;

import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SessionManagementService {
	public static ISession getActiveSession() {
		return getSessionManagement().loginSuperUser(null, "");
	}

	public static ISessionManagement getSessionManagement() {
		return UserManagement.getSessionmanagement();
	}
}
