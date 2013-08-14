package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Michael Brand
 */
public class SessionManagementService {
	
	/**
	 * The active {@link ISession}.
	 */
	private static ISession activeSession;

	/**
	 * Returns the active {@link ISession}.
	 */
	public static ISession getActiveSession() {
		
		if(activeSession == null)		
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		
		return activeSession;
		
	}
	
}