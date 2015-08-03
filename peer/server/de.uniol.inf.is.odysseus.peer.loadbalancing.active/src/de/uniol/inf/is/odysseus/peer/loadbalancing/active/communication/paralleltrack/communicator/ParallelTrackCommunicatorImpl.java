package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.AbortHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.InstructionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.ResponseHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackMasterStatus.LB_PHASES;

public class ParallelTrackCommunicatorImpl implements IPeerCommunicatorListener, ILoadBalancingCommunicator, IMessageDeliveryFailedListener {

	/**
	 * 
	 * @author Carsten Cordes Class to encapsulate a Connection to an operator
	 *         on another Peer.
	 * 
	 */

	/**
	 * Name of the Communicator
	 */
	private final String COMMUNICATOR_NAME = "ParallelTrack";

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ParallelTrackCommunicatorImpl.class);

	@Override
	public List<PeerID> getInvolvedPeers(int queryID) {
		return LoadBalancingHelper.getInvolvedPeers(queryID);
	}

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
	private static ParallelTrackCommunicatorImpl instance;

	/**
	 * Get Instance of Communication Listener
	 * 
	 * @return this.
	 */
	public static ParallelTrackCommunicatorImpl getInstance() {
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
		peerCommunicator.registerMessageType(ParallelTrackAbortMessage.class);
		peerCommunicator.addListener(this, ParallelTrackAbortMessage.class);

		peerCommunicator.registerMessageType(ParallelTrackInstructionMessage.class);
		peerCommunicator.addListener(this, ParallelTrackInstructionMessage.class);

		peerCommunicator.registerMessageType(ParallelTrackResponseMessage.class);
		peerCommunicator.addListener(this, ParallelTrackResponseMessage.class);

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
			peerCommunicator.removeListener(this, ParallelTrackAbortMessage.class);
			peerCommunicator.removeListener(this, ParallelTrackInstructionMessage.class);
			peerCommunicator.removeListener(this, ParallelTrackResponseMessage.class);

			peerCommunicator.unregisterMessageType(ParallelTrackAbortMessage.class);
			peerCommunicator.unregisterMessageType(ParallelTrackInstructionMessage.class);
			peerCommunicator.unregisterMessageType(ParallelTrackResponseMessage.class);

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
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {

		if (message instanceof ParallelTrackResponseMessage) {
			ParallelTrackResponseMessage response = (ParallelTrackResponseMessage) message;
			ResponseHandler.handlePeerResonse(response, senderPeer);
		}

		if (message instanceof ParallelTrackInstructionMessage) {
			ParallelTrackInstructionMessage instruction = (ParallelTrackInstructionMessage) message;
			InstructionHandler.handleInstruction(instruction, senderPeer);
		}

		if (message instanceof ParallelTrackAbortMessage) {
			ParallelTrackAbortMessage abortMessage = (ParallelTrackAbortMessage) message;
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
		ILogicalQueryPart partToCopy = LoadBalancingHelper.getInstalledQueryPart(queryId);
		ParallelTrackMasterStatus status = new ParallelTrackMasterStatus();
		status.setOriginalPart(partToCopy);
		int lbProcessIdentifier = LoadBalancingStatusCache.getInstance().storeLocalProcess(status);
		LOG.debug("New LoadBalancing Status created. LoadBalancing Process Id " + lbProcessIdentifier);
		status.setLogicalQuery(queryId);
		status.setMessageDispatcher(new ParallelTrackMessageDispatcher(peerCommunicator, lbProcessIdentifier));
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
	public String getName() {
		return COMMUNICATOR_NAME;
	}

	public void notifyFinished(boolean successful) {
		
		List<ILoadBalancingListener> listenersCopy = new ArrayList<ILoadBalancingListener>(listeners);
		
		for (ILoadBalancingListener listener : listenersCopy) {
			listener.notifyLoadBalancingFinished(successful);
		}

	}

	/**
	 * Called when Message delivery on Master Peer failed. Decides whether to
	 * abort or not.
	 */
	@Override
	public void update(IMessage message, PeerID peerId) {
		if (message instanceof ParallelTrackInstructionMessage) {
			ParallelTrackInstructionMessage instruction = (ParallelTrackInstructionMessage) message;
			handleTimeoutOnMasterPeer(instruction);
		}

		if (message instanceof ParallelTrackAbortMessage) {
			ParallelTrackAbortMessage abortMsg = (ParallelTrackAbortMessage) message;
			// If Abort Instruction could not be delivered... Bad luck. Stop
			// Sending it and try finishing up.
			if (abortMsg.getMsgType() == ParallelTrackAbortMessage.ABORT_INSTRUCTION) {
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
	 */
	private void handleTimeoutOnMasterPeer(ParallelTrackInstructionMessage instruction) {

		int lbProcessId = instruction.getLoadBalancingProcessId();
		ParallelTrackMasterStatus status = (ParallelTrackMasterStatus) LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(lbProcessId);

		if (status == null) {
			LOG.debug("Timeout or Failure occured. Current status: null");
			return;
		}

		LOG.debug("Timeout or Failure occured. Current status: " + status.getPhase());

		switch (instruction.getMsgType()) {
		case ParallelTrackInstructionMessage.INITIATE_LOADBALANCING:
			if (status.getPhase().equals(LB_PHASES.INITIATING)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case ParallelTrackInstructionMessage.ADD_QUERY:
			if (status.getPhase().equals(LB_PHASES.COPYING)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case ParallelTrackInstructionMessage.COPY_RECEIVER:
			if (status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case ParallelTrackInstructionMessage.COPY_SENDER:
			if (status.getPhase().equals(LB_PHASES.RELINKING_SENDERS)) {
				ResponseHandler.handleError(status, this);
			}
			break;
		case ParallelTrackInstructionMessage.DELETE_RECEIVER:
		case ParallelTrackInstructionMessage.DELETE_SENDER:
			if (status.getPhase().equals(LB_PHASES.INITIATING)) {
				ResponseHandler.handleError(status, this);
			}
			break;

		default:
			break;
		}

	}

}