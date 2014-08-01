package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingResponseMessage;

public class LoadBalancingMessageDispatcher {
	
	IPeerCommunicator peerCommunicator;
	ISession session;
	int lbProcessId;
	
	
	ConcurrentHashMap<String,RepeatingMessageSend> currentJobs;
	
	LoadBalancingMessageDispatcher(IPeerCommunicator peerCommunicator, ISession session, int lbProcessId) {
		this.peerCommunicator = peerCommunicator;
		this.session = session;
		this.lbProcessId = lbProcessId;
	}
	

	public void sendInstallSuccess(PeerID destination,Collection<Integer> queryIDs) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,queryIDs);
			try {
				peerCommunicator.send(destination, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	
	public void sendDuplicateSuccess(PeerID destination, String pipeID) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,pipeID);
			try {
				peerCommunicator.send(destination, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	

	public void sendAckInit(PeerID destinationPeerId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId,
						new LoadBalancingResponseMessage(
								lbProcessId,LoadBalancingResponseMessage.ACK_LOADBALANCING));
			}
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}

	
	public void sendDeleteOperator(boolean isSender, String peerId,
			String pipeId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteOperatorMsg(isSender, lbProcessId, pipeId);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(LoadBalancingHelper.toPeerID(peerId), message);
			} catch (PeerCommunicationException e) {
				// TODO
			}
		}
	}
	
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createAddQueryMsg(lbProcessId, queryPartPql);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}
	

	
	public void sendInstallFailure(PeerID initiatingPeer) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY);
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	

	public void sendDuplicateFailure(PeerID initiatingPeer) {
		if(peerCommunicator!=null) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER);
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	public void sendAbort(PeerID peerToNotify, Integer[] queriesToRemove) {
		if(peerCommunicator!=null) {
			LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId, queriesToRemove);
			try {
				peerCommunicator.send(peerToNotify, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}
	
	public void sendAbort(PeerID peerToNotify) {
		if(peerCommunicator!=null) {
			LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId);
			try {
				peerCommunicator.send(peerToNotify, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}

	public void sendCopyOperator(boolean isSender, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createCopyOperatorMsg(isSender, newPeerId, oldPipeId, newPipeId);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}

	public void sendInitiate(PeerID volunteeringPeer) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator
						.send(volunteeringPeer,
								LoadBalancingInstructionMessage.createInitiateMsg(lbProcessId));
			}
		} catch (PeerCommunicationException e) {
			//TODO
		}
	}

	public void sendDeleteQuery(PeerID initiatingPeer) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteQueryMsg(lbProcessId);
		if (peerCommunicator != null) {
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				//TODO
			}
		}
	}


	public void sendAbortToDuplicatingPeer(PeerID peerID, String pipeID) {
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
