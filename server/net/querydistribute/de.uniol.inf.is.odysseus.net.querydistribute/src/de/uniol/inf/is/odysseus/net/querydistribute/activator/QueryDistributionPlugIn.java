package de.uniol.inf.is.odysseus.net.querydistribute.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class QueryDistributionPlugIn implements BundleActivator {

	private static ISession activeSession;

	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

	public static ISession getActiveSession() {
		if(activeSession == null || !activeSession.isValid()) {	
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		
		return activeSession;
	}
}
