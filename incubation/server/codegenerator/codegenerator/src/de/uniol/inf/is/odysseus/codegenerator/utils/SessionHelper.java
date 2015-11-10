package de.uniol.inf.is.odysseus.codegenerator.utils;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * SessionHelper to get easy a active session for a user
 * 
 * @author MarcPreuschaft
 *
 */
public class SessionHelper {
	
	private static ISession cActiveSession;
	
	/**
	 * Gets the currently active session.
	 */
	public static ISession getActiveSession() {

		if (cActiveSession == null || !cActiveSession.isValid()) {

			cActiveSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null,
					UserManagementProvider.getDefaultTenant().getName());

		}

		return cActiveSession;

	}

}
