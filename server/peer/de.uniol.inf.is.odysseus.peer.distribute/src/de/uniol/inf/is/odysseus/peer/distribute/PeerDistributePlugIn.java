package de.uniol.inf.is.odysseus.peer.distribute;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PeerDistributePlugIn implements BundleActivator {

	private static ISession activeSession;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public static ISession getActiveSession() {
		if(activeSession == null || !activeSession.isValid()) {	
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		
		return activeSession;
	}
}
