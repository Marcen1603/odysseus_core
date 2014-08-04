package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingResponseMessage;

public class LoadBalancingMessageDispatcher {
	
	IPeerCommunicator peerCommunicator;
	ISession session;
	int lbProcessId;
	
	
	RepeatingMessageSend currentJob;
	ConcurrentHashMap<String,RepeatingMessageSend> currentJobs;
	
	LoadBalancingMessageDispatcher(IPeerCommunicator peerCommunicator, ISession session, int lbProcessId) {
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
			String pipeId) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createDeleteOperatorMsg(isSender, lbProcessId, pipeId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(pipeId)) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,LoadBalancingHelper.toPeerID(peerId));
			this.currentJobs.put(pipeId, job);
			job.start();
		}
	}
	
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql) {
		LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createAddQueryMsg(lbProcessId, queryPartPql);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
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
			String oldPipeId, String newPipeId, String newPeerId) {
		
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(newPipeId)) {
			LoadBalancingInstructionMessage message = LoadBalancingInstructionMessage.createCopyOperatorMsg(isSender, newPeerId, oldPipeId, newPipeId);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
			this.currentJobs.put(newPipeId, job);
			job.start();
		}
		
	}

	public int getNumberOfRunningJobs() {
		return currentJobs.size();
	}
	
	public void sendInitiate(PeerID volunteeringPeer) {
			this.currentJob = new RepeatingMessageSend(peerCommunicator,LoadBalancingInstructionMessage.createInitiateMsg(lbProcessId),volunteeringPeer);
			currentJob.start();
	}
	
	public void stopRunningJob() {
		if(this.currentJob!=null) {
			currentJob.stopRunning();
			currentJob = null;
		}
	}
	
	public void stopRunningJob(String job) {
		if(this.currentJobs.contains(job)) {
			currentJobs.get(job).stopRunning();
			currentJobs.remove(job);
		}
	}
	
	public void stopAllMessages() {
		
		if(this.currentJobs!=null) {
			for(RepeatingMessageSend job : currentJobs.values()) {
				job.stopRunning();
			}
			currentJobs.clear();
			currentJobs=null;
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


	public void sendAbort(PeerID peerID) {
		LoadBalancingAbortMessage message = new LoadBalancingAbortMessage(lbProcessId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.contains(peerID.toString())) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,peerID);
			this.currentJobs.put(peerID.toString(), job);
			job.start();
		}
		
	}
	


}
