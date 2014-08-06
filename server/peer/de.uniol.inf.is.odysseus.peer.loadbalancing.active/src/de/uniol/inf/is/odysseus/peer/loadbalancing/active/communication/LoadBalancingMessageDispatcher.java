package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.RepeatingMessageSend;

public class LoadBalancingMessageDispatcher {
	
	IPeerCommunicator peerCommunicator;
	ISession session;
	int lbProcessId;
	
	
	RepeatingMessageSend currentJob;
	ConcurrentHashMap<String,RepeatingMessageSend> currentJobs;
	
	
	public LoadBalancingMessageDispatcher(IPeerCommunicator peerCommunicator, ISession session, int lbProcessId) {
		this.peerCommunicator = peerCommunicator;
		this.session = session;
		this.lbProcessId = lbProcessId;
	}
	

	public void sendInstallSuccess(PeerID destination,Collection<Integer> queryIDs) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,queryIDs);
			this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destination);
			currentJob.start();
	}
	
	
	
	public void sendDuplicateSuccess(PeerID destination, String pipeID) {
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(pipeID)) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,destination);
			this.currentJobs.put(pipeID, job);
			job.start();
		}
	}
	

	public void sendAckInit(PeerID destinationPeerId) {
		this.currentJob = new RepeatingMessageSend(peerCommunicator,new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.ACK_LOADBALANCING),destinationPeerId);
		currentJob.start();
	}

	
	public void sendDeleteOperator(boolean isSender, String peerId,
			String pipeId,IMessageDeliveryFailedListener listener) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteOperatorMsg(isSender, lbProcessId, pipeId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(pipeId)) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,LoadBalancingHelper.toPeerID(peerId));
			this.currentJobs.put(pipeId, job);
			job.addListener(listener);
			job.start();
		}
	}
	
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql,IMessageDeliveryFailedListener listener) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createAddQueryMsg(lbProcessId, queryPartPql);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}
	

	
	public void sendInstallFailure(PeerID initiatingPeer) {
			LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY);
			this.currentJob = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
			currentJob.start();
	}
	

	public void sendDuplicateFailure(PeerID initiatingPeer) {
		LoadBalancingResponseMessage message = new LoadBalancingResponseMessage(lbProcessId,LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
		currentJob.start();
	}
	
	public void sendCopyOperator(boolean isSender, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId, IMessageDeliveryFailedListener listener) {
		
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(newPipeId)) {
			LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createCopyOperatorMsg(isSender, newPeerId, oldPipeId, newPipeId);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
			this.currentJobs.put(newPipeId, job);
			job.addListener(listener);
			job.start();
		}
		
	}

	public int getNumberOfRunningJobs() {
		return currentJobs.size();
	}
	
	public void sendInitiate(PeerID volunteeringPeer,IMessageDeliveryFailedListener listener) {
			this.currentJob = new RepeatingMessageSend(peerCommunicator,LoadBalancingInstructionMessage.createInitiateMsg(lbProcessId),volunteeringPeer);
			currentJob.addListener(listener);
			currentJob.start();
	}
	
	public void stopRunningJob() {
		if(this.currentJob!=null) {
			currentJob.stopRunning();
			currentJob = null;
			currentJob.clearListeners();
		}
	}
	
	public void stopRunningJob(String job) {
		if(this.currentJobs.contains(job)) {
			currentJobs.get(job).stopRunning();
			currentJobs.get(job).clearListeners();
			currentJobs.remove(job);
		}
	}
	
	public void stopAllMessages() {
		
		if(this.currentJobs!=null) {
			for(RepeatingMessageSend job : currentJobs.values()) {
				job.stopRunning();
				job.clearListeners();
			}
			currentJobs.clear();
			currentJobs=null;
		}
		
		if(this.currentJob!=null) {
			this.currentJob.stopRunning();
			this.currentJob.clearListeners();
			this.currentJob=null;
		}
	}

	public void sendSyncFinished(PeerID initiatingPeer, String pipeId) {
		LoadBalancingResponseMessage message = LoadBalancingResponseMessage.createSyncFinishedMsg(lbProcessId, pipeId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(initiatingPeer.toString())) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
			this.currentJobs.put(initiatingPeer.toString(), job);
			job.start();
		}
	}


	public void sendAbortInstruction(PeerID peerID,IMessageDeliveryFailedListener listener) {
		LoadBalancingAbortMessage message = LoadBalancingAbortMessage.createAbortInstructionMsg(lbProcessId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(peerID.toString())) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,peerID);
			this.currentJobs.put(peerID.toString(), job);
			job.addListener(listener);
			job.start();
		}
		
	}
	
	public void sendAbortResponse(PeerID peerID) {
		LoadBalancingAbortMessage message = LoadBalancingAbortMessage.createAbortResponseMsg(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,peerID);
		currentJob.start();
	}
	


}
