package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackResponseMessage;

/**
 * Used to send Messages in LoadBalancing.
 * @author badagent
 *
 */
public class ParallelTrackMessageDispatcher {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ParallelTrackMessageDispatcher.class);
	
	/**
	 * Peer Communicator
	 */
	IPeerCommunicator peerCommunicator;
	
	/**
	 * Session
	 */
	ISession session;
	
	/**
	 * LoadBalancing Process Id
	 */
	int lbProcessId;
	
	
	/**
	 * used to keep track of a single Repeating Message
	 */
	RepeatingMessageSend currentJob;
	
	/**
	 * Used to keep track of multiple repeating messages.
	 */
	ConcurrentHashMap<String,RepeatingMessageSend> currentJobs;
	
	
	/**
	 * Constructor
	 * @param peerCommunicator
	 * @param session
	 * @param lbProcessId
	 */
	public ParallelTrackMessageDispatcher(IPeerCommunicator peerCommunicator, int lbProcessId) {
		this.peerCommunicator = peerCommunicator;
		this.session = OsgiServiceManager.getActiveSession();
		this.lbProcessId = lbProcessId;
	}
	
	/***
	 * Send by master Peer to slave Peer to stop spamming Messages 
	 * @param destination
	 */
	public void sendMsgReceived(PeerID destination) {
		ParallelTrackInstructionMessage message = ParallelTrackInstructionMessage.createMessageReceivedMsg(lbProcessId);
		try {
			LOG.debug("Send MessageReceived");
			peerCommunicator.send(destination, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Send by slave peer to master peer to indicate a successfull Query Installing
	 * @param destination
	 */
	public void sendInstallSuccess(PeerID destination) {
			LOG.debug("Send Install Success");
			ParallelTrackResponseMessage message = ParallelTrackResponseMessage.createInstallSuccessMessage(lbProcessId);
			this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destination);
			currentJob.start();
	}
	
	
	
	/**
	 * Send by slave peer to master peer to indicate a successfull Duplication of a pipe
	 * @param destination
	 * @param pipeID
	 */
	public void sendDuplicateSuccess(PeerID destination, String pipeID) {
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.containsKey(pipeID)) {
			LOG.debug("Send DuplicateSuccess");
			ParallelTrackResponseMessage message = ParallelTrackResponseMessage.createDuplicateSuccessMessage(lbProcessId,pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,destination);
			this.currentJobs.put(pipeID, job);
			job.start();
		}
	}
	
	/**
	 * Send by Master Peer to slave Peer to stop spamming.
	 * @param destination
	 * @param pipeID
	 */
	public void sendPipeSuccessReceivedMsg(PeerID destination, String pipeID) {
		ParallelTrackInstructionMessage message = ParallelTrackInstructionMessage.createPipeReceivedMsg(lbProcessId, pipeID);
		try {
			LOG.debug("Send SuccessReceived");
			peerCommunicator.send(destination, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}
	
	
	

	/**
	 * Acknowledges Init LoadBalancing
	 * @param destinationPeerId
	 */
	public void sendAckInit(PeerID destinationPeerId) {

		LOG.debug("Send AckInit");
		this.currentJob = new RepeatingMessageSend(peerCommunicator,ParallelTrackResponseMessage.createAckLoadbalancingMessage(lbProcessId),destinationPeerId);
		currentJob.start();
	}

	/**
	 * Sends instruction to delete an Operator (after Sync)
	 * @param isSender
	 * @param peerId
	 * @param pipeId
	 * @param listener
	 */
	public void sendDeleteOperator(boolean isSender, String peerId,
			String pipeId,IMessageDeliveryFailedListener listener) {
		ParallelTrackInstructionMessage message = ParallelTrackInstructionMessage.createDeleteOperatorMsg(isSender, lbProcessId, pipeId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.containsKey(pipeId)) {

			LOG.debug("Send DeleteOperator");
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,LoadBalancingHelper.toPeerID(peerId));
			this.currentJobs.put(pipeId, job);
			job.addListener(listener);
			job.start();
		}
	}
	
	/**
	 * Send instruction to add Query
	 * @param destinationPeer
	 * @param queryPartPql
	 * @param listener
	 */
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql,IMessageDeliveryFailedListener listener) {

		LOG.debug("Send AddQuery");
		ParallelTrackInstructionMessage message = ParallelTrackInstructionMessage.createAddQueryMsg(lbProcessId, queryPartPql);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}
	

	/**
	 * When installing Query goes wrong.
	 * @param initiatingPeer
	 */
	public void sendInstallFailure(PeerID initiatingPeer) {

			LOG.debug("Send InstallFailure");
			ParallelTrackResponseMessage message = ParallelTrackResponseMessage.createInstallFailureMessage(lbProcessId);
			this.currentJob = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
			currentJob.start();
	}
	

	/**
	 * When duplicating Pipe goes wrong
	 * @param initiatingPeer
	 */
	public void sendDuplicateFailure(PeerID initiatingPeer) {

		LOG.debug("Send DuplicateFailure to initiating Peer " + initiatingPeer + " for lbProcessId " + lbProcessId);
		ParallelTrackResponseMessage message = ParallelTrackResponseMessage.createDuplicateFailureMessage(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
		currentJob.start();
	}
	
	/**
	 * Sends Instruction to copy a pipe
	 * @param isSender
	 * @param destinationPeer
	 * @param oldPipeId
	 * @param newPipeId
	 * @param newPeerId
	 * @param listener
	 */
	public void sendCopyOperator(boolean isSender, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId, IMessageDeliveryFailedListener listener) {
		
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.containsKey(newPipeId)) {

			LOG.debug("Send CopyOperator (isSender:" + isSender + ")");
			ParallelTrackInstructionMessage message = ParallelTrackInstructionMessage.createCopyOperatorMsg(lbProcessId,isSender, newPeerId, oldPipeId, newPipeId);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
			this.currentJobs.put(newPipeId, job);
			job.addListener(listener);
			job.start();
		}
		
	}

	/**
	 * Returns currently running Message Jobs.
	 * @return
	 */
	public int getNumberOfRunningJobs() {
		return currentJobs.size();
	}
	
	/**
	 * Sends initiate LoadBalancing to volunteering peer.
	 * @param volunteeringPeer
	 * @param listener
	 */
	public void sendInitiate(PeerID volunteeringPeer,IMessageDeliveryFailedListener listener) {
		
			LOG.debug("Send InitiateLB");
			this.currentJob = new RepeatingMessageSend(peerCommunicator,ParallelTrackInstructionMessage.createInitiateMsg(lbProcessId),volunteeringPeer);
			currentJob.addListener(listener);
			currentJob.start();
	}
	
	/**
	 * Stops a running message job.
	 */
	public void stopRunningJob() {
		if(this.currentJob!=null) {
			currentJob.stopRunning();
			currentJob.clearListeners();
			currentJob = null;
			
		}
	}
	
	/**
	 * Stops a running Message job.
	 * @param job
	 */
	public void stopRunningJob(String job) {
		if(this.currentJobs.containsKey(job)) {
			currentJobs.get(job).stopRunning();
			currentJobs.get(job).clearListeners();
			currentJobs.remove(job);
		}
	}
	
	/**
	 * Stops all Messages.
	 */
	public void stopAllMessages() {
		if(this.currentJobs!=null) {
			LOG.debug("Stopping repeating Message Jobs.");
			for(RepeatingMessageSend job : currentJobs.values()) {
				job.stopRunning();
				job.clearListeners();
			}
			currentJobs.clear();
			currentJobs=null;
		}
		
		if(this.currentJob!=null) {
			LOG.debug("Stopping repeating Message Job.");
			this.currentJob.stopRunning();
			this.currentJob.clearListeners();
			this.currentJob=null;
		}
	}

	/**
	 * Sends Sync finished Message.
	 * @param initiatingPeer
	 * @param pipeId
	 */
	public void sendSyncFinished(PeerID initiatingPeer, String pipeId) {

		LOG.debug("Send SyncFinished for pipeID " + pipeId);
		ParallelTrackResponseMessage message = ParallelTrackResponseMessage.createSyncFinishedMsg(lbProcessId, pipeId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if(!this.currentJobs.containsKey(initiatingPeer.toString())) {
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,initiatingPeer);
			this.currentJobs.put(initiatingPeer.toString(), job);
			job.start();
		}
	}


	/**
	 * Sends Abort.
	 * @param peerID
	 * @param listener
	 */
	public void sendAbortInstruction(PeerID peerID,IMessageDeliveryFailedListener listener) {

		
		ParallelTrackAbortMessage message = ParallelTrackAbortMessage.createAbortInstructionMsg(lbProcessId);
		if(this.currentJobs==null) {
			this.currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
		}
		if((this.currentJobs!=null) && !this.currentJobs.containsKey(peerID.toString())) {
			LOG.debug("Send AbortInstruction");
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,message,peerID);
			this.currentJobs.put(peerID.toString(), job);
			job.addListener(listener);
			job.start();
		}
		
	}
	
	/**
	 * Sends Response to Abort.
	 * @param peerID
	 */
	public void sendAbortResponse(PeerID peerID) {
		ParallelTrackAbortMessage message = ParallelTrackAbortMessage.createAbortResponseMsg(lbProcessId);
		try {
			LOG.debug("Send AbortResponse");
			peerCommunicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	public void sendDeleteFinished(PeerID peerID, String oldPipeId) {
		ParallelTrackResponseMessage response = ParallelTrackResponseMessage.createDeleteFinishedMessage(lbProcessId, oldPipeId);
		try {
			LOG.debug("Send DELETE_FINISHED Response");
			peerCommunicator.send(peerID, response);
		}
		catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}
	
	


}
