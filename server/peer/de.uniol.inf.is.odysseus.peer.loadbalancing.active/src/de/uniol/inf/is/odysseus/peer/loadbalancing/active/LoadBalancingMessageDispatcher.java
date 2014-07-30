package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingCopyConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingFailureMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInitiateMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstallingSuccessfulMessage;

public class LoadBalancingMessageDispatcher {
	
	

	public static void sendSuccessMessage(IPeerCommunicator peerCommunicator, PeerID destination, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingInstallingSuccessfulMessage message = new LoadBalancingInstallingSuccessfulMessage(lbProcessId);
			try {
				peerCommunicator.send(destination, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	/**
	 * JxtaOperators)
	 * Sends a delete ConnectionMessage to another Peer. (Removes duplicate
	 * 
	 * @param isSender
	 *            is JxtaOperator Sender?
	 * @param peerId
	 *            Peer which holds Operator
	 * @param pipeId
	 *            old Pipe ID (to delete)
	 * @param lbProcessId
	 *            Load Balancing Process Id
	 */
	public static void sendDeleteJxtaOperatorMessage(IPeerCommunicator peerCommunicator, boolean isSender, String peerId,
			String pipeId, int lbProcessId) {
		LoadBalancingDeleteConnectionMessage message = new LoadBalancingDeleteConnectionMessage(
				lbProcessId, pipeId, isSender);

		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(LoadBalancingHelper.toPeerID(peerId), message);
			} catch (PeerCommunicationException e) {
				// TODO
			}
		}
	}
	
	

	/**
	 * Send Message to other peer with PQL to install.
	 * 
	 * @param loadBalancingProcessID
	 *            current LoadBalancing Process ID.
	 * @param destinationPeer
	 *            Receiving peer.
	 * @param queryPartPql
	 *            QueryPart as PQL.
	 */
	public static void sendAddQueryPartMessage(IPeerCommunicator peerCommunicator, int loadBalancingProcessID,
			PeerID destinationPeer, String queryPartPql) {
		LoadBalancingAddQueryMessage message = new LoadBalancingAddQueryMessage(
				loadBalancingProcessID, queryPartPql);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}
	

	
	/**
	 * Send Failure Message (used when OTHER Peer than the initiating Peer has an error!)
	 * @param initiatingPeer Peer that initiated LoadBalancing (controlling Peer)
	 * @param lbProcessId loadBalancing Process Id
	 */
	public static void sendFailureMessage(IPeerCommunicator peerCommunicator, PeerID initiatingPeer, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingFailureMessage message = new LoadBalancingFailureMessage(lbProcessId);
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	/**
	 * Asks Peer to abort loadBalancing (because an Error happened somewhere)
	 * @param peerToNotify Peer which should receive the message
	 * @param lbProcessId loadBalancing process Id.
	 */
	public static void sendAbortMessage(IPeerCommunicator peerCommunicator, PeerID peerToNotify, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId);
			try {
				peerCommunicator.send(peerToNotify, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}

	/**
	 * Sends a message to a Peer to copy a particular connection with a new Pipe
	 * and Peer ID.
	 * 
	 * @param isSender
	 *            determines, if a sender or a receiver should be duplicated
	 * @param loadBalancingProcessID
	 *            Load Balancing process id.
	 * @param destinationPeer
	 *            Message Destination.
	 * @param oldPipeId
	 *            Pipe Id of pipe which should be replicated
	 * @param newPipeId
	 *            Pipe Id which should substitute old Pipe Id
	 * @param newPeerId
	 *            Peer Id which should substitute old Peer Id
	 */
	public static void sendCopyConnectionMessage(IPeerCommunicator peerCommunicator, boolean isSender,
			int loadBalancingProcessID, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId) {
		LoadBalancingCopyConnectionMessage message = new LoadBalancingCopyConnectionMessage(
				loadBalancingProcessID, isSender, oldPipeId, newPipeId,
				newPeerId);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}
	


	/**
	 * Send Message to initiate LoadBalancing with another Peer
	 * 
	 * @param volunteeringPeer
	 *            Peer that wants to take Load.
	 * @param loadBalancingProcessId
	 *            Peer unique Id referencing the Query Part which should be
	 *            given to volunteering peer.
	 */
	public static void sendInitiateLoadBalancingMessage(IPeerCommunicator peerCommunicator, PeerID volunteeringPeer,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator
						.send(volunteeringPeer,
								new LoadBalancingInitiateMessage(
										loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}
	


	/**
	 * Sends finished Mesage
	 * 
	 * @param initiatingPeer
	 *            Peer which should receive the message. (The one which
	 *            initiated the Load Balancing)
	 * @param lbProcessId
	 *            Load Balancing Process Id.
	 */
	public static void sendLoadBalancingFinishedMessage(IPeerCommunicator peerCommunicator, PeerID initiatingPeer,
			int lbProcessId) {
		LoadBalancingDeleteQueryMessage message = new LoadBalancingDeleteQueryMessage(
				lbProcessId);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}


}
