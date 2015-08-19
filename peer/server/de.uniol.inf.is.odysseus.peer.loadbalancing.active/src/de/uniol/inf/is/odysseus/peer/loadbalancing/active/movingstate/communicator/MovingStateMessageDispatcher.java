package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateResponseMessage;

/**
 * Used to send Messages in MovingState Strategy in LoadBalancing.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateMessageDispatcher {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateMessageDispatcher.class);

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
	ConcurrentHashMap<String, RepeatingMessageSend> currentJobs;

	/**
	 * Constructor
	 * 
	 * @param peerCommunicator
	 * @param session
	 * @param lbProcessId
	 */
	public MovingStateMessageDispatcher(IPeerCommunicator peerCommunicator,
			int lbProcessId) {
		this.peerCommunicator = peerCommunicator;
		this.session = OsgiServiceManager.getActiveSession();
		this.lbProcessId = lbProcessId;
	}

	/***
	 * Send by master Peer to slave Peer to stop spamming Messages
	 * 
	 * @param destination
	 */
	public void sendMsgReceived(PeerID destination) {
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createMessageReceivedMsg(lbProcessId);
		try {
			LOG.debug("Send MessageReceived");
			peerCommunicator.send(destination, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Send by slave peer to master peer to indicate a successfull Query
	 * Installing
	 * 
	 * @param destination
	 */
	public void sendInstallSuccess(PeerID destination) {
		LOG.debug("Send Install Success");
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createInstallSuccessMessage(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				destination);
		currentJob.start();
	}

	/**
	 * Send by slave peer to master peer to indicate a successfull Duplication
	 * of a (sender)pipe
	 * 
	 * @param destination
	 * @param pipeID
	 */
	public void sendDuplicateSenderSuccess(PeerID destination, String pipeID) {
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if (!this.currentJobs.containsKey(pipeID)) {
			LOG.debug("Send DuplicateSENDERSuccess");
			MovingStateResponseMessage message = MovingStateResponseMessage
					.createDuplicateSenderSuccessMessage(lbProcessId, pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, destination);
			this.currentJobs.put(pipeID, job);
			job.start();
		}
	}
	



	/**
	 * Send by slave peer to master peer to indicate a successfull Duplication
	 * of a (receiver)pipe
	 * 
	 * @param destination
	 * @param pipeID
	 */
	public void sendDuplicateReceiverSuccess(PeerID destination, String pipeID) {
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if (!this.currentJobs.containsKey(pipeID)) {
			LOG.debug("Send DuplicateRECEIVERSuccess");
			MovingStateResponseMessage message = MovingStateResponseMessage
					.createDuplicateReceiverSuccessMessage(lbProcessId, pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, destination);
			this.currentJobs.put(pipeID, job);
			job.start();
		}
	}

	/***
	 * Sent a single state copy has been finished (Slave-to-Master)
	 * 
	 * @param destination
	 *            Destination Peer (Master)
	 * @param pipeID
	 *            pipe which was used for transferring the state.
	 */
	public void sendCopyStateFinished(PeerID destination, String pipeID) {
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createStateCopyFinishedMessage(lbProcessId, pipeID);
			LOG.debug("Sending CopyState finished for pipe {}.",pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator, message, destination);
			if(currentJobs==null) {
				currentJobs = new ConcurrentHashMap<String,RepeatingMessageSend>();
			}
			this.currentJobs.putIfAbsent(pipeID, job);
			job.run();
	}

	/**
	 * Send by Master Peer to slave Peer to stop spamming ;)
	 * 
	 * @param destination
	 *            Slave Peer
	 * @param pipeID
	 *            PipeID which the peer claims to have successfully copied on
	 *            and on and on ;)
	 */
	public void sendPipeSuccessReceivedMsg(PeerID destination, String pipeID) {
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createPipeReceivedMsg(lbProcessId, pipeID);
		try {
			LOG.debug("Send SuccessReceived");
			peerCommunicator.send(destination, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Send by Slave Peer to master Peer to stop tell master peer that changing
	 * a Pipe went well.
	 * 
	 * @param destination
	 *            Master Peer
	 * @param pipeID
	 *            Successfully Replicated PipeID
	 */
	public void sendPipeSuccessMsg(PeerID destination, String pipeID) {
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createDuplicateSenderSuccessMessage(lbProcessId, pipeID);
		try {
			LOG.debug("Send DuplicateSuccess");
			peerCommunicator.send(destination, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Acknowledges Init LoadBalancing
	 * 
	 * @param destinationPeerId
	 */
	public void sendAckInit(PeerID destinationPeerId) {

		LOG.debug("Send AckInit");
		this.currentJob = new RepeatingMessageSend(peerCommunicator,
				MovingStateResponseMessage
						.createAckLoadbalancingMessage(lbProcessId),
				destinationPeerId);
		currentJob.start();
	}

	/**
	 * Send instruction to add Query
	 * 
	 * @param destinationPeer
	 *            Slave Peer (volunteer)
	 * @param queryPartPql
	 *            PQL-Query to install
	 * @param listener
	 *            IMessageDeliveryFailedListener that listens for failed
	 *            messages.
	 */
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql,
			IMessageDeliveryFailedListener listener,String sharedQueryID, String masterPeerID, String queryName, String transCfgName, Collection<String> metaDataTypes) {

		LOG.debug("Send AddQuery");
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createAddQueryMsg(lbProcessId, queryPartPql, sharedQueryID, masterPeerID, queryName,transCfgName,metaDataTypes);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				destinationPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}
	
	public void sendAddQueryForMasterQuery(PeerID destinationPeer, String queryPartPql, IMessageDeliveryFailedListener listener, List<String> peerIDs, String sharedQueryID, String queryName, String transCfgName, Collection<String> metaDataTypes) {
		LOG.debug("Send AddQuery for Master Peer:");
		LOG.debug("Shared Query ID:" + sharedQueryID);
		LOG.debug("Number of other Peers:" + peerIDs.size());
		
		MovingStateInstructionMessage message = MovingStateInstructionMessage.createAddQueryMsgForMasterQuery(lbProcessId, queryPartPql,peerIDs, sharedQueryID, queryName,transCfgName,metaDataTypes);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}

	/**
	 * When installing Query goes wrong.
	 * 
	 * @param initiatingPeer
	 *            Master Peer
	 */
	public void sendInstallFailure(PeerID initiatingPeer) {

		LOG.debug("Send InstallFailure");
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createInstallFailureMessage(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				initiatingPeer);
		currentJob.start();
	}

	/**
	 * When duplicating Pipe goes wrong
	 * 
	 * @param initiatingPeer
	 *            Master Peer
	 */
	public void sendDuplicateFailure(PeerID initiatingPeer) {

		LOG.debug("Send DuplicateFailure to initiating Peer " + initiatingPeer
				+ " for lbProcessId " + lbProcessId);
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createDuplicateFailureMessage(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				initiatingPeer);
		currentJob.start();
	}

	/**
	 * Returns Number of currently running Message Jobs.
	 * 
	 * @return number of Currently running message jobs
	 */
	public int getNumberOfRunningJobs() {
		return currentJobs.size();
	}

	/**
	 * Sends initiate LoadBalancing to volunteering peer.
	 * 
	 * @param volunteeringPeer
	 *            slave Peer
	 * @param listener
	 *            IMessageDeliveryFailed Listener
	 */
	public void sendInitiate(PeerID volunteeringPeer,
			IMessageDeliveryFailedListener listener) {

		LOG.debug("Send InitiateLB");
		this.currentJob = new RepeatingMessageSend(peerCommunicator,
				MovingStateInstructionMessage.createInitiateMsg(lbProcessId),
				volunteeringPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}

	/***
	 * Sent to prepare a peer for state copy.
	 * 
	 * @param volunteeringPeer
	 *            Peer that should receive state copies
	 * @param listener
	 *            IMessageDevlieryFailed listener
	 * @param pipeID
	 *            PipeID to use for state copy
	 * @param operatorType
	 *            Type of Operator that should receive state copy
	 * @param operatorIndex
	 *            index of operator in List
	 */
	public void sendInititateStateCopy(PeerID volunteeringPeer,
			IMessageDeliveryFailedListener listener, String pipeID,
			String operatorType, int operatorIndex) {
		LOG.debug("Send INITIATE_STATE_COPY to Peer "
				+ volunteeringPeer.toString());
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		RepeatingMessageSend job = new RepeatingMessageSend(peerCommunicator,
				MovingStateInstructionMessage.createInitiateStateCopyMsg(
						lbProcessId, pipeID, operatorType, operatorIndex),
				volunteeringPeer);
		job.addListener(listener);
		job.start();
		this.currentJobs.put(pipeID, job);
	}

	/**
	 * Acknowledges StateCopy Message and tells Master that everything is
	 * prepared.
	 * 
	 * @param masterPeer
	 *            Master Peer
	 * @param lbProcessId
	 *            Process Id for LoadBalancing Process
	 * @param pipeID
	 *            PipeID of sender/receiver pair that was initialized.
	 */
	public void sendInititiateStateCopyAck(PeerID masterPeer, int lbProcessId,
			String pipeID) {
		LOG.debug("Send INITIATE_STATE_COPY_ACK");
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createAckInitStateCopyMessage(lbProcessId, pipeID);
		try {
			peerCommunicator.send(masterPeer, message);
		} catch (Exception e) {
			// Do nothing as MasterPeer will re-send Instruction.
		}
	}

	/**
	 * Stops a running message job.
	 */
	public void stopRunningJob() {
		if (this.currentJob != null) {
			currentJob.stopRunning();
			currentJob.clearListeners();
			currentJob = null;

		}
	}

	/**
	 * Stops a particular running Message job.
	 * 
	 * @param job
	 */
	public synchronized void stopRunningJob(String job) {
		if (this.currentJobs != null) {
			if (this.currentJobs.containsKey(job)) {
				currentJobs.get(job).stopRunning();
				currentJobs.get(job).clearListeners();
				currentJobs.remove(job);
			}
		}
	}

	/**
	 * Stops all Messages.
	 */
	public synchronized void stopAllMessages() {
		if (this.currentJobs != null) {
			LOG.debug("Stopping repeating Message Jobs.");
			for (RepeatingMessageSend job : currentJobs.values()) {
				job.stopRunning();
				job.clearListeners();
			}
			currentJobs.clear();
			currentJobs = null;
		}

		if (this.currentJob != null) {
			LOG.debug("Stopping repeating Message Job.");
			this.currentJob.stopRunning();
			this.currentJob.clearListeners();
			this.currentJob = null;
		}
	}

	/***
	 * Sends Message to replace a receiver
	 * 
	 * @param peer
	 *            Downstream Peer
	 * @param newPeerId
	 *            New Peer ID the receiver should be set to
	 * @param oldPipeId
	 *            PipeID to identify receiver
	 * @param listener
	 *            IMessageDeliveryFailed listener
	 */
	public void sendReplaceReceiverMessage(PeerID peer, String newPeerId,
			String oldPipeId, String newPipeId, IMessageDeliveryFailedListener listener) {

		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createReplaceReceiverMsg(lbProcessId, newPeerId, oldPipeId,newPipeId);
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if (this.currentJobs != null
				&& !this.currentJobs.containsKey(oldPipeId)) {
			LOG.debug("Sending REPLACE_RECEIVER Message");
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, peer);
			job.addListener(listener);
			this.currentJobs.put(oldPipeId, job);
			job.start();
		}
	}

	/**
	 * 
	 * @param peer
	 * @param newPeerId
	 * @param oldPipeId
	 * @param listener
	 */
	public void sendInstallBufferAndReplaceSenderMessage(PeerID peer,
			String newPeerId, String oldPipeId, String newPipeId,
			IMessageDeliveryFailedListener listener) {
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createInstallBufferAndReplaceSenderMsg(lbProcessId, newPeerId,
						oldPipeId, newPipeId);
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if (!this.currentJobs.containsKey(oldPipeId)) {
			LOG.debug("Sending INSTALL_BUFFER_AND_REPLACE_SENDER Message");
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, peer);
			job.addListener(listener);
			this.currentJobs.put(oldPipeId, job);
			job.start();
		}
	}

	/**
	 * Sends Abort.
	 * 
	 * @param peerID
	 * @param listener
	 */
	public void sendAbortInstruction(PeerID peerID,
			IMessageDeliveryFailedListener listener) {

		MovingStateAbortMessage message = MovingStateAbortMessage
				.createAbortInstructionMsg(lbProcessId);
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if ((this.currentJobs != null)
				&& !this.currentJobs.containsKey(peerID.toString())) {
			LOG.debug("Send AbortInstruction");
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, peerID);
			this.currentJobs.put(peerID.toString(), job);
			job.addListener(listener);
			job.start();
		}

	}

	/**
	 * Sends Response to Abort.
	 * 
	 * @param peerID
	 *            Peer ID that should abort.
	 */
	public void sendAbortResponse(PeerID peerID) {
		MovingStateAbortMessage message = MovingStateAbortMessage
				.createAbortResponseMsg(lbProcessId);
		try {
			LOG.debug("Send AbortResponse");
			peerCommunicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}

	/***
	 * Sends message that indicates copying states is finished.
	 * 
	 * @param destinationPeer
	 *            Slave Peer
	 */
	public void sendFinishedCopyingStates(PeerID destinationPeer) {
		LOG.debug("Sending COPYING_FINISHED to peer with ID "
				+ destinationPeer.toString());
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createFinishedCopyingStatesMsg(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				destinationPeer);
		currentJob.start();
	}

	/***
	 * Ackknowledges that copying state is finished.
	 * 
	 * @param masterPeer
	 *            Master Peer
	 */
	public void sendAckCopyingFinished(PeerID masterPeer) {
		LOG.debug("Sending ACK_COPYING_FINISHED");
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createAckCopyingFinishedMsg(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				masterPeer);
		currentJob.start();
	}

	/**
	 * Sends message that the stream can now be resumed
	 * 
	 * @param peer
	 *            upstream Peer
	 * @param listener
	 *            IMessgeDeliveryFailed listener
	 */
	public synchronized void sendStopBuffering(PeerID peer,
			IMessageDeliveryFailedListener listener) {
		MovingStateInstructionMessage message = MovingStateInstructionMessage
				.createStopBufferingMsg(lbProcessId);
		if (this.currentJobs == null) {
			this.currentJobs = new ConcurrentHashMap<String, RepeatingMessageSend>();
		}
		if (!this.currentJobs.containsKey(peer.toString())) {
			LOG.debug("Sending STOP_BUFFERING Message");
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, peer);
			job.addListener(listener);
			this.currentJobs.put(peer.toString(), job);
			job.start();
		}

	}

	/***
	 * Ackknowledges that Stream should be resumed.
	 * 
	 * @param masterPeer
	 *            Master Peer
	 */
	public void sendStopBufferingFinished(PeerID masterPeer) {
		MovingStateResponseMessage message = MovingStateResponseMessage
				.createStopBufferingFinishedMsg(lbProcessId);
		this.currentJob = new RepeatingMessageSend(peerCommunicator, message,
				masterPeer);
		currentJob.start();
	}

	
	/***
	 * Sends FAIL Message when INITIATE_STATE_COPY fails.
	 * @param senderPeer Master Peer
	 * @param pipeId Pipe ID which failed.
	 */
	public void sendInitiateStateCopyFail(PeerID senderPeer, String pipeID) {
		MovingStateResponseMessage message = MovingStateResponseMessage.createInitStateCopyFail(lbProcessId,pipeID);
		try {
			peerCommunicator.send(senderPeer, message);
		}
		catch(Exception e) {
			//Master Peer will try again.
			LOG.error("Could not send Message.");
		}
		
	}

}
