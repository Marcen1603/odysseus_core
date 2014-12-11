package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator;

import java.util.ArrayList;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol.AbortHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol.InstructionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol.ResponseHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus.LB_PHASES;

/***
 * Implements a MovingState Communicator
 * 
 * @author Carsten Cordes
 *
 */
public class InactiveQueryCommunicatorImpl implements IPeerCommunicatorListener,
		ILoadBalancingCommunicator, IMessageDeliveryFailedListener {

	/**
	 * Name of the Communicator
	 */
	private final String COMMUNICATOR_NAME = "InactiveQuery";

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(InactiveQueryCommunicatorImpl.class);

	/**
	 * Peer Communicator
	 */
	private static IPeerCommunicator peerCommunicator;

	/**
	 * List of registered Listeners.
	 */
	private ArrayList<ILoadBalancingListener> listeners = new ArrayList<ILoadBalancingListener>();

	/**
	 * Instance of Communication Listener.
	 */
	private static InactiveQueryCommunicatorImpl instance;

	/**
	 * Get Instance of Communication Listener
	 * 
	 * @return this.
	 */
	public static InactiveQueryCommunicatorImpl getInstance() {
		return instance;
	}

	/**
	 * Getter for PeerCommunicator
	 * 
	 * @return
	 */
	public static IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}

	/**
	 * called by OSGi-DS to bind Peer Communicator (registers all Messages and
	 * adds Listeners)
	 * 
	 * @param serv
	 *            PeerCommunicator to bind.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(InactiveQueryAbortMessage.class);
		peerCommunicator.addListener(this, InactiveQueryAbortMessage.class);

		peerCommunicator
				.registerMessageType(InactiveQueryInstructionMessage.class);
		peerCommunicator.addListener(this, InactiveQueryInstructionMessage.class);

		peerCommunicator.registerMessageType(InactiveQueryResponseMessage.class);
		peerCommunicator.addListener(this, InactiveQueryResponseMessage.class);

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
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator
					.removeListener(this, InactiveQueryAbortMessage.class);
			peerCommunicator.removeListener(this,
					InactiveQueryInstructionMessage.class);
			peerCommunicator.removeListener(this,
					InactiveQueryResponseMessage.class);

			peerCommunicator
					.unregisterMessageType(InactiveQueryAbortMessage.class);
			peerCommunicator
					.unregisterMessageType(InactiveQueryInstructionMessage.class);
			peerCommunicator
					.unregisterMessageType(InactiveQueryResponseMessage.class);

			peerCommunicator = null;
		}
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		instance = this;
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		instance = null;
	}

	@Override
	/**
	 * called when a registered Message is received.
	 * Delegates Message to according message handler.
	 * @param communicator Peer Communicator
	 * @param senderPeer Peer sending the Message
	 * @param message Received Message.
	 */
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {

		if (message instanceof InactiveQueryResponseMessage) {
			InactiveQueryResponseMessage response = (InactiveQueryResponseMessage) message;
			ResponseHandler.handlePeerResonse(response, senderPeer);
		}

		if (message instanceof InactiveQueryInstructionMessage) {
			InactiveQueryInstructionMessage instruction = (InactiveQueryInstructionMessage) message;
			InstructionHandler.handleInstruction(instruction, senderPeer);
		}

		if (message instanceof InactiveQueryAbortMessage) {
			InactiveQueryAbortMessage abortMessage = (InactiveQueryAbortMessage) message;
			AbortHandler.handleAbort(abortMessage, senderPeer);
		}

	}

	/**
	 * Initiates the copy Process between a Peer and another Peer (after
	 * Allocation)
	 * 
	 * @param otherPeer
	 *            Peer which should receive a QueryPart
	 * @param queryPartID
	 *            queryId of QueryPart to copy.
	 */
	@Override
	public void initiateLoadBalancing(PeerID otherPeer, int queryId) {
		ILogicalQueryPart partToCopy = LoadBalancingHelper
				.getInstalledQueryPart(queryId);

		InactiveQueryMasterStatus status = new InactiveQueryMasterStatus();
		status.setOriginalPart(partToCopy);

		int lbProcessIdentifier = LoadBalancingStatusCache.getInstance()
				.storeLocalProcess(status);

		LOG.debug("New LoadBalancing Status created. LoadBalancing Process Id "
				+ lbProcessIdentifier);
		status.setLogicalQuery(queryId);
		status.setMessageDispatcher(new InactiveQueryMessageDispatcher(
				peerCommunicator, lbProcessIdentifier));
		status.getMessageDispatcher().sendInitiate(otherPeer, this);
	}

	/**
	 * Adds a LoadBalancing Listener
	 */
	@Override
	public void registerLoadBalancingListener(ILoadBalancingListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a Load Balancing Listener
	 */
	@Override
	public void removeLoadBalancingListener(ILoadBalancingListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	@Override
	/***
	 * Used to get Communcator-name
	 */
	public String getName() {
		return COMMUNICATOR_NAME;
	}

	/**
	 * Notfies Strategy that LoadBalancing is finished.
	 */
	public void notifyFinished() {
		for (ILoadBalancingListener listener : listeners) {
			listener.notifyLoadBalancingFinished();
		}

	}

	/**
	 * Called when Message delivery on Master Peer failed. Decides whether to
	 * abort or not.
	 */
	@Override
	public void update(IMessage message, PeerID peerId) {
		if (message instanceof InactiveQueryInstructionMessage) {
			InactiveQueryInstructionMessage instruction = (InactiveQueryInstructionMessage) message;
			handleTimeoutOnMasterPeer(instruction);
		}

		if (message instanceof InactiveQueryAbortMessage) {
			InactiveQueryAbortMessage abortMsg = (InactiveQueryAbortMessage) message;
			// If Abort Instruction could not be delivered... Bad luck. Stop
			// Sending it and try finishing up.
			if (abortMsg.getMsgType() == InactiveQueryAbortMessage.ABORT_INSTRUCTION) {
				AbortHandler.stopSendingAbort(abortMsg, peerId);
			}
		}

		// No need to process Messages from slave Peers, as the master Peer will
		// notice that they don't answer and initiate Abort.

	}

	/**
	 * When Timeout happens and peer is in corresponding Phase -> Handle Error.
	 * 
	 * @param instruction
	 *            InstructionMessage that caused timeout.
	 */
	private void handleTimeoutOnMasterPeer(
			InactiveQueryInstructionMessage instruction) {
		

		int lbProcessId = instruction.getLoadBalancingProcessId();
		InactiveQueryMasterStatus status = (InactiveQueryMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessId);

		if (status == null) {
			LOG.debug("Timeout or Failure occured. Current status: null");
			return;
		}

		LOG.debug("Timeout or Failure occured. Current status: "
				+ status.getPhase());

		switch (instruction.getMsgType()) {
		case InactiveQueryInstructionMessage.INITIATE_LOADBALANCING:
			if (status.getPhase().equals(LB_PHASES.INITIATING)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case InactiveQueryInstructionMessage.ADD_QUERY:
			if (status.getPhase().equals(LB_PHASES.COPYING_QUERY)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case InactiveQueryInstructionMessage.REPLACE_RECEIVER:
			if (status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		}

	}

}