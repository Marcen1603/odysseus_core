package de.uniol.inf.is.odysseus.net.querydistribute.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.querydistribute.transmit.ServerPortReserver;

public class QueryDistributionPlugIn implements BundleActivator {

	private static ISession activeSession;
	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static IOdysseusNodeManager nodeManager;
	
	private static ServerPortReserver portReserver;

	@Override
	public void start(BundleContext context) throws Exception {

	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = SessionManagement.instance.loginSuperUser(null, UserManagementProvider.instance.getDefaultTenant().getName());
		}

		return activeSession;
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;
		
		portReserver = new ServerPortReserver();
		portReserver.start(nodeCommunicator);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			portReserver.stop();
			portReserver = null;
			
			nodeCommunicator = null;
		}
	}

	public static IOdysseusNodeCommunicator getNodeCommunicator() {
		return nodeCommunicator;
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}
	
	public static IOdysseusNodeManager getNodeManager() {
		return nodeManager;
	}
}
