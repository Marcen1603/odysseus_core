package de.uniol.inf.is.odysseus.query.transformation.utils;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class QueryTransformationHelper {
	
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
