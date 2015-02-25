package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryResponseMessage;

/**
 * Used to send Messages in MovingState Strategy in LoadBalancing.
 * 
 * @author Carsten Cordes
 *
 */
public class InactiveQueryMessageDispatcher {

	private static final Logger LOG = LoggerFactory
			.getLogger(InactiveQueryMessageDispatcher.class);

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
	public InactiveQueryMessageDispatcher(IPeerCommunicator peerCommunicator,
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
		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage
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
		InactiveQueryResponseMessage message = InactiveQueryResponseMessage
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
			InactiveQueryResponseMessage message = InactiveQueryResponseMessage
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
			InactiveQueryResponseMessage message = InactiveQueryResponseMessage
					.createDuplicateReceiverSuccessMessage(lbProcessId, pipeID);
			RepeatingMessageSend job = new RepeatingMessageSend(
					peerCommunicator, message, destination);
			this.currentJobs.put(pipeID, job);
			job.start();
		}
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
		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage
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
		InactiveQueryResponseMessage message = InactiveQueryResponseMessage
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
				InactiveQueryResponseMessage
						.createAckLoadbalancingMessage(lbProcessId),
				destinationPeerId);
		currentJob.start();
	}


	/**
	 * Send instruction to add Query
	 * @param destinationPeer
	 * @param queryPartPql
	 * @param listener
	 */
	public void sendAddQuery(PeerID destinationPeer, String queryPartPql,IMessageDeliveryFailedListener listener, String sharedQueryID, String masterPeer) {
		LOG.debug("Send AddQuery");
		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage.createAddQueryMsg(lbProcessId, queryPartPql,sharedQueryID,masterPeer);
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
		InactiveQueryResponseMessage message = InactiveQueryResponseMessage
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
		InactiveQueryResponseMessage message = InactiveQueryResponseMessage
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
				InactiveQueryInstructionMessage.createInitiateMsg(lbProcessId),
				volunteeringPeer);
		currentJob.addListener(listener);
		currentJob.start();
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
	public void stopRunningJob(String job) {
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
	public void stopAllMessages() {
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
			String oldPipeId, IMessageDeliveryFailedListener listener) {

		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage
				.createReplaceReceiverMsg(lbProcessId, newPeerId, oldPipeId);
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
			String newPeerId, String oldPipeId,
			IMessageDeliveryFailedListener listener) {
		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage
				.createInstallBufferAndReplaceSenderMsg(lbProcessId, newPeerId,
						oldPipeId);
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

		InactiveQueryAbortMessage message = InactiveQueryAbortMessage
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
		InactiveQueryAbortMessage message = InactiveQueryAbortMessage
				.createAbortResponseMsg(lbProcessId);
		try {
			LOG.debug("Send AbortResponse");
			peerCommunicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message:");
			LOG.error(e.getMessage());
		}
	}


	public void sendAddQueryForMasterQuery(PeerID destinationPeer, String queryPartPql, IMessageDeliveryFailedListener listener, List<String> peerIDs, String sharedQueryID) {
		LOG.debug("Send AddQuery for Master Peer:");
		LOG.debug("Shared Query ID:" + sharedQueryID);
		LOG.debug("Number of other Peers:" + peerIDs.size());
		
		InactiveQueryInstructionMessage message = InactiveQueryInstructionMessage.createAddQueryMsgForMasterQuery(lbProcessId, queryPartPql,peerIDs, sharedQueryID);
		this.currentJob = new RepeatingMessageSend(peerCommunicator,message,destinationPeer);
		currentJob.addListener(listener);
		currentJob.start();
	}
	

}
