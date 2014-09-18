package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.RecoveryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryMessage;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * Attention: Under construction
 * 
 * @author Tobias Brandt & Michael Brand
 *
 */
public class RecoveryCommunicator implements IRecoveryCommunicator,
		IPeerCommunicatorListener {

	//@formatter:off
	/**
	 * Plan (try to find some ideas) 
	 * 1. Someone (who?) tells us that a peer failed and which query this peer had (this should be part of LSOP-213)
	 * 2. We search for another peer who can install this query 
	 * 3. A new peer is found, we just take the first for now 
	 * 4. We tell this peer to install the query 
	 * 5. We connect the peers so that the new peer is known by the right
	 * peers
	 * 
	 * When we have this we add the second part (right side of diagram) 1. Tell
	 * the peers which are before the failed peer that the peer failed 2. Save
	 * the tuples there 3. When the new peer is there -> direct saved tuples to
	 * the new peer
	 * 
	 * And when we have this we add a third part 1. Add an error-protocol to
	 * handle errors during the recovery? (maybe like loadBalancing?)
	 * 
	 */
	//@formatter:on

	private static IPeerResourceUsageManager peerResourceUsageManager;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPeerCommunicator peerCommunicator;
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	// called by OSGi-DS
	public static void bindPeerResourceUsageManager(
			IPeerResourceUsageManager serv) {
		peerResourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerResourceUsageManager(
			IPeerResourceUsageManager serv) {
		if (peerResourceUsageManager == serv) {
			peerResourceUsageManager = null;
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
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Peer Communicator (registers all Messages and
	 * adds Listeners)
	 * 
	 * @param serv
	 *            PeerCommunicator to bind.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(RecoveryMessage.class);
		peerCommunicator.addListener(this, RecoveryMessage.class);
	}

	/**
	 * called by OSGi-DS to unbind Peer Communicator (unregisters all Messages
	 * and removes Listeners)
	 * 
	 * @param serv
	 *            Peer Communicator to unbind.
	 * 
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, RecoveryMessage.class);
			peerCommunicator.unregisterMessageType(RecoveryMessage.class);
			peerCommunicator = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			executor = null;
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

	/**
	 * Getter for Executor
	 * 
	 * @return
	 */
	public static IServerExecutor getExecutor() {
		return executor;
	}

	public void testAFewThings() {
		// Just something to begin with ...

		// All peers we know
		Collection<PeerID> peers = p2pDictionary.getRemotePeerIDs();

		// Take the first one
		PeerID theChosenOne = peers.iterator().hasNext() ? peers.iterator()
				.next() : null;

		// Send, that this peer has to do the hard work
		if (theChosenOne != null) {
			RecoveryMessage takeThis = new RecoveryMessage();
			try {
				peerCommunicator.send(theChosenOne, takeThis);
			} catch (PeerCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void takeOverTest(List<String> partsToTakeOver) {
		// We assume that we get the parts we have to get over as PQL strings
		for (String pql : partsToTakeOver) {
			RecoveryHelper.installAndRunQueryPartFromPql(pql);
		}

	}

	@Override
	public void recover(PeerID failedPeer,
			List<ILogicalQueryPart> queryPartsToRecover) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof RecoveryMessage) {
			// Take over the query part
		}

	}

}