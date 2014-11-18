package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

/**
 * Bundle Activator for Active LoadBalancing Bundle.
 * Holds constants and references to severeal needed services.
 * @author Carsten Cordes
 *
 */
public class ActiveLoadBalancingActivator implements BundleActivator{
	
	/**
	 * Size of Transport Buffer needed to transfer states in MovingState Strategy
	 */
	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);
	
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveLoadBalancingActivator.class);

	/**
	 * OSGi-Start function.
	 * Does nothing.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		//Nothing to do here
		
	}

	/**
	 * OSGi-Stop Function
	 * does nothing.
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		//Nothing to do here
		
	}
	
	
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	
	/**
	 * Network Manager (needed to get GroupID, etc.)
	 */
	private static IP2PNetworkManager p2pNetworkManager;

	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;
	
	/**
	 * Getter for Executor
	 * @return
	 */
	public static IServerExecutor getExecutor() {
		return executor;
	}


	/**
	 * Getter for NetworkManager
	 * @return
	 */
	public static IP2PNetworkManager getP2pNetworkManager() {
		return p2pNetworkManager;
	}
	
	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		LOG.debug("Bound Executor.");
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		LOG.debug("Unbound Executor.");
		if (executor == exe) {
			executor = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to bind.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Bound network Manager");
		p2pNetworkManager = serv;
	}

	/**
	 * called by OSGi-DS to unbind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to unbind.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Unbound NetworkMananger");
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	
	

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}

	


}
