package de.uniol.inf.is.odysseus.peer.loadbalancing.active.console;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingCommunicationListener;

/**
 * Console with debug commands for active LoadBalancing.
 * @author Carsten Cordes
 *
 */
public class ActiveLoadbalancingConsole implements CommandProvider {

	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveLoadbalancingConsole.class);

	/**
	 * Session Variable
	 */
	private static ISession activeSession;
	/**
	 * Used to store OSGi Services.
	 */
	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor executor;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Active Loadbalancing console activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Active Loadbalancing console deactivated");
	}

	@Override
	/**
	 * Outputs a simple Help.
	 */
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Active Loadbalancing commands---\n");
		sb.append("    lsParts		              		- Lists all queryParts installed on peer with ids\n");
		sb.append("    initLB <peername> <queryPartId>	- Initiate Loadbalancing with <peername> and query Part <queryPartId>\n");
		sb.append("    cpJxtaSender <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Sender");
		sb.append("    cpJxtaReceiver <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Receiver");
		return sb.toString();
	}

	/**
	 * Initiates Load Balancing.
	 * @param ci
	 */
	public void _initLB(CommandInterpreter ci) {
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			System.out.println("usage: initLB <peername> <queryPartId>");
			return;
		}
		String queryPartIDString = ci.nextArgument();
		if (Strings.isNullOrEmpty(queryPartIDString)) {
			System.out.println("usage: initLB <peername> <queryPartId>");
			return;
		}
		int queryPartID = 0;
		try {
			queryPartID = Integer.valueOf(queryPartIDString);
		} catch (Throwable t) {
			System.out.println("usage: initLB <peername> <queryPartId>");
			return;
		}
		Optional<PeerID> optPID = determinePeerID(peerName);
		if (optPID.isPresent()) {
			PeerID pid = optPID.get();
			initiateLoadBalancing(pid, queryPartID);
			System.out.println("Initiated Loadbalancing with peer '" + peerName
					+ "'");
		}
	}

	/**
	 * Lists installed QueryParts (logical)
	 * @param ci
	 */
	public void _lsParts(CommandInterpreter ci) {

		System.out.println("Query Parts on current Peer:");
		for (int queryID : executor.getLogicalQueryIds(getActiveSession())) {
			System.out.println("["
					+ queryID
					+ "] "
					+ executor.getLogicalQueryById(queryID, getActiveSession())
							.getQueryText());
		}
	}
	
	
	/**
	 * used by initLb command.
	 * @param destinationPeer
	 * @param queryPartID
	 */
	private void initiateLoadBalancing(PeerID destinationPeer, int queryPartID) {
		LoadBalancingCommunicationListener.getInstance().initiateLoadBalancing(
				destinationPeer, queryPartID);
	}

	/**
	 * Get PeerId from PeerName.
	 * @param peerName
	 * @return
	 */
	private static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : p2pDictionary.getRemotePeerIDs()) {
			if (p2pDictionary.getRemotePeerName(pid).equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

	/**
	 * Returns currently active Session.
	 * @return
	 */
	private static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}

		return activeSession;
	}

}
