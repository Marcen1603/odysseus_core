package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Collection;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingResponseMessage;

public class LoadBalancingMessageDispatcher {
	
	

	public static void sendInstallSuccess(IPeerCommunicator peerCommunicator, PeerID destination, int lbProcessId, Collection<Integer> queryIDs) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,queryIDs);
			try {
				peerCommunicator.send(destination, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	
	public static void sendDuplicateSuccess(IPeerCommunicator peerCommunicator, PeerID destination, int lbProcessId, String pipeID) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,pipeID);
			try {
				peerCommunicator.send(destination, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	


	/**
	 * Sends a copy Message to initiating Peer, this message initiates a copy
	 * process for given LoadBalancing Process. This should be an answer to the
	 * initiating peer, after being accepted as loadbalancing partner. (Ack
	 * Message)
	 * 
	 * @param destinationPeerId
	 *            initiating Peer
	 * @param loadBalancingProcessId
	 *            Peer-unique ID which identifies the referenced LoadBalancing
	 *            Process.
	 * 
	 */
	public static void sendAckInit(IPeerCommunicator peerCommunicator, PeerID destinationPeerId,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId,
						new LoadBalancingResponseMessage(
								loadBalancingProcessId,LoadBalancingResponseMessage.ACK_LOADBALANCING));
			}
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}

	
	public static void sendDeleteOperator(IPeerCommunicator peerCommunicator, boolean isSender, String peerId,
			String pipeId, int lbProcessId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteOperatorMsg(isSender, lbProcessId, pipeId);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(LoadBalancingHelper.toPeerID(peerId), message);
			} catch (PeerCommunicationException e) {
				// TODO
			}
		}
	}
	
	public static void sendAddQuery(IPeerCommunicator peerCommunicator, int loadBalancingProcessID,
			PeerID destinationPeer, String queryPartPql) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createAddQueryMsg(loadBalancingProcessID, queryPartPql);
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
	public static void sendInstallFailure(IPeerCommunicator peerCommunicator, PeerID initiatingPeer, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY);
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	

	/**
	 * Send Failure Message (used when OTHER Peer than the initiating Peer has an error!)
	 * @param initiatingPeer Peer that initiated LoadBalancing (controlling Peer)
	 * @param lbProcessId loadBalancing Process Id
	 */
	public static void sendDuplicateFailure(IPeerCommunicator peerCommunicator, PeerID initiatingPeer, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER);
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
	public static void sendAbort(IPeerCommunicator peerCommunicator, PeerID peerToNotify, int lbProcessId, Integer[] queriesToRemove) {
		if(peerCommunicator!=null) {
			LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId, queriesToRemove);
			try {
				peerCommunicator.send(peerToNotify, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	public static void sendAbort(IPeerCommunicator peerCommunicator, PeerID peerToNotify, int lbProcessId) {
		if(peerCommunicator!=null) {
			LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId);
			try {
				peerCommunicator.send(peerToNotify, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}

	public static void sendCopyOperator(IPeerCommunicator peerCommunicator, boolean isSender,
			int loadBalancingProcessID, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createCopyOperatorMsg(isSender, newPeerId, oldPipeId, newPipeId);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}

	public static void sendInitiate(IPeerCommunicator peerCommunicator, PeerID volunteeringPeer,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator
						.send(volunteeringPeer,
								LoadBalancingInstructionMessage.createInitiateMsg(loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}
	


	/**
	 * Sends finished Message
	 * 
	 * @param initiatingPeer
	 *            Peer which should receive the message. (The one which
	 *            initiated the Load Balancing)
	 * @param lbProcessId
	 *            Load Balancing Process Id.
	 */
	public static void sendDeleteQuery(IPeerCommunicator peerCommunicator, PeerID initiatingPeer,
			int lbProcessId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteQueryMsg(lbProcessId);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}


	public static void sendAbortToDuplicatingPeer(
			IPeerCommunicator peerCommunicator,int lbProcessId, PeerID peerID, String pipeID) {
		LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId,pipeID);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(peerID, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
		
	}


}
