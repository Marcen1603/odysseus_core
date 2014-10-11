package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.LoadBalancingResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.AbortHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.InstructionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol.ResponseHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingMasterStatus.LB_PHASES;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingStatusCache;

public class ParallelTrackCommunicatorImpl implements
		IPeerCommunicatorListener, ILoadBalancingCommunicator, IMessageDeliveryFailedListener{

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
	private static final Logger LOG = LoggerFactory
			.getLogger(ParallelTrackCommunicatorImpl.class);


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
		peerCommunicator.registerMessageType(LoadBalancingAbortMessage.class);
		peerCommunicator.addListener(this, LoadBalancingAbortMessage.class);

		peerCommunicator
				.registerMessageType(LoadBalancingInstructionMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingInstructionMessage.class);

		peerCommunicator
				.registerMessageType(LoadBalancingResponseMessage.class);
		peerCommunicator.addListener(this, LoadBalancingResponseMessage.class);

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
			peerCommunicator.removeListener(this,
					LoadBalancingAbortMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingInstructionMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingResponseMessage.class);

			peerCommunicator
					.unregisterMessageType(LoadBalancingAbortMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInstructionMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingResponseMessage.class);

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

		if (message instanceof LoadBalancingResponseMessage) {
			LoadBalancingResponseMessage response = (LoadBalancingResponseMessage) message;
			ResponseHandler.handlePeerResonse(response, senderPeer);
		}

		if (message instanceof LoadBalancingInstructionMessage) {
			LoadBalancingInstructionMessage instruction = (LoadBalancingInstructionMessage) message;
			InstructionHandler.handleInstruction(instruction, senderPeer);
		}

		if (message instanceof LoadBalancingAbortMessage) {
			LoadBalancingAbortMessage abortMessage = (LoadBalancingAbortMessage) message;
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
		int lbProcessIdentifier = LoadBalancingStatusCache.getInstance()
				.createNewLocalProcess(partToCopy);
		LoadBalancingMasterStatus status = LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessIdentifier);
		LOG.debug("New LoadBalancing Status created. LoadBalancing Process Id " + lbProcessIdentifier);
		status.setLogicalQuery(queryId);
		status.setMessageDispatcher(new ParallelTrackMessageDispatcher(peerCommunicator, lbProcessIdentifier));
		status.getMessageDispatcher().sendInitiate(otherPeer,this);
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
	

	public void notifyFinished() {
		for(ILoadBalancingListener listener : listeners) {
			listener.notifyLoadBalancingFinished();
		}
		
	}

	/**
	 * Called when Message delivery on Master Peer failed. Decides whether to abort or not.
	 */
	@Override
	public void update(IMessage message, PeerID peerId) {
		if(message instanceof LoadBalancingInstructionMessage) {
			LoadBalancingInstructionMessage instruction = (LoadBalancingInstructionMessage)message;
			handleTimeoutOnMasterPeer(instruction);
		}
		
		if(message instanceof LoadBalancingAbortMessage) {
			LoadBalancingAbortMessage abortMsg = (LoadBalancingAbortMessage) message;
			//If Abort Instruction could not be delivered... Bad luck. Stop Sending it and try finishing up.
			if(abortMsg.getMsgType()==LoadBalancingAbortMessage.ABORT_INSTRUCTION) {
				AbortHandler.stopSendingAbort(abortMsg, peerId);
			}
		}
		
		//No need to process Messages from slave Peers, as the master Peer will notice that they don't answer and initiate Abort.
		
	}


	/**
	 * When Timeout happens and peer is in corresponding Phase -> Handle Error.
	 * @param instruction
	 */
	private void handleTimeoutOnMasterPeer(
			LoadBalancingInstructionMessage instruction) {
		;
		
		int lbProcessId = instruction.getLoadBalancingProcessId();
		LoadBalancingMasterStatus status = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(lbProcessId);
		
		if(status==null) {
			LOG.debug("Timeout or Failure occured. Current status: null");
			return;
		}
		
		LOG.debug("Timeout or Failure occured. Current status: " + status.getPhase());
		
		
		switch(instruction.getMsgType()){
			case LoadBalancingInstructionMessage.INITIATE_LOADBALANCING:
				if(status.getPhase().equals(LB_PHASES.INITIATING)) {
					ResponseHandler.handleError(status,this);
				}
				break;
			case LoadBalancingInstructionMessage.ADD_QUERY:
				if(status.getPhase().equals(LB_PHASES.COPYING)) {
					ResponseHandler.handleError(status,this);
				}
				break;
			case LoadBalancingInstructionMessage.COPY_RECEIVER:
				if(status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS)) {
					ResponseHandler.handleError(status,this);
				}
				break;
			case LoadBalancingInstructionMessage.COPY_SENDER:
				if(status.getPhase().equals(LB_PHASES.RELINKING_SENDERS)) {
					ResponseHandler.handleError(status,this);
				}
				break;
			case LoadBalancingInstructionMessage.DELETE_RECEIVER:
			case LoadBalancingInstructionMessage.DELETE_SENDER:
				if(status.getPhase().equals(LB_PHASES.INITIATING)) {
					ResponseHandler.handleError(status,this);
				}
				break;
		}
		
	}
	
	
}