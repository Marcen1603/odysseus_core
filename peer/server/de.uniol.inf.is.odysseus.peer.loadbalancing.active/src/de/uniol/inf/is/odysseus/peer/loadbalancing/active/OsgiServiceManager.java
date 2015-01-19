package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingQueryManager;

/**
 * Bundle Activator for Active LoadBalancing Bundle.
 * Holds constants and references to severeal needed services.
 * @author Carsten Cordes
 *
 */
public class OsgiServiceManager {
	
	/**
	 * Size of Transport Buffer needed to transfer states in MovingState Strategy
	 */
	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);
	
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(OsgiServiceManager.class);

	private static ILoadBalancingQueryManager queryManager;

	
	private static IQueryPartController queryPartController;
	
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
	
	
	public static void bindQueryPartController(IQueryPartController controller) {
		LOG.debug("Boung Query Part Controller");
		queryPartController = controller;
	}
	
	public static void unbindQueryPartController(IQueryPartController controller) {
		LOG.debug("Unbinding Query Part Controller");
		if(queryPartController == controller) {
			queryPartController=null;
		}
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
	
	public static IQueryPartController getQueryPartController() {
		return queryPartController;
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
	
	public static void bindLBQueryManager(ILoadBalancingQueryManager serv) {
		LOG.debug("Bound LB Query Manager");
		queryManager = serv;
	}
	
	public static void unbindLBQueryManager(ILoadBalancingQueryManager serv) {
		LOG.debug("Unbound LB Query Manager");
		if(queryManager==serv) {
			queryManager = null;
		}
	}

	
	public static ILoadBalancingQueryManager getQueryManager() {
		return queryManager;
	}

}
