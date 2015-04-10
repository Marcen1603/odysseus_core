package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.TimeoutTask;

/**
 * Entity to handle received tuple send instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class TupleSendReceiver extends AbstractRepeatingMessageReceiver {
	
	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(TupleSendReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static TupleSendReceiver cInstance;

	/**
	 * This map is used to save, for which pipes and failed Peers we already got holdOns, cause we will only accept one
	 * holdOn per pipe for a failed peer
	 */
	private static HashMap<PipeID, List<PeerID>> holdOnMap = new HashMap<PipeID, List<PeerID>>();

	
	/**
	 * Delay in milliseconds until hold on timeout
	 */
	private static final long HOLD_ON_TIMEOUT_IN_MS= 60*1000;

	/**
	 * This map contains TimeoutTasks for each pipeId that got a holdOn
	 */
	private Map<String, TimeoutTask> holdOnTimeoutTasks = Collections.synchronizedMap(new HashMap<String, TimeoutTask>());

	
	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link TupleSendReceiver}.
	 */
	public static TupleSendReceiver getInstance() {
		return cInstance;
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryTupleSendMessage.class);
		serv.addListener(this, RecoveryTupleSendMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryTupleSendMessage.class);
		serv.removeListener(this, RecoveryTupleSendMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryTupleSendMessage) {
			RecoveryTupleSendMessage tsMessage = (RecoveryTupleSendMessage) message;
			if (!mReceivedUUIDs.contains(tsMessage.getUUID())) {
				mReceivedUUIDs.add(tsMessage.getUUID());
			} else {
				return;
			}

			RecoveryTupleSendResponseMessage response = null;
			try {
				if (tsMessage.isHoldOnInstruction()) {
					LOG.debug("Got HoldOn-message. Will hold on now ... (for pipe {})", tsMessage.getPipeId()
							.toString());
					this.holdOn(tsMessage.getPipeId(), tsMessage.getFailedPeerId());
				} else {
					LOG.debug("Got GoOn-message. Will go on now ... (for pipe {})", tsMessage.getPipeId().toString());
					this.goOn(tsMessage.getPipeId());
				}
				response = new RecoveryTupleSendResponseMessage(tsMessage.getUUID());
			} catch (Exception e) {
				response = new RecoveryTupleSendResponseMessage(tsMessage.getUUID(), e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send tuple send instruction response message!", e);
			}
		}
	}

	private void holdOn(PipeID pipeId, PeerID failedPeer) throws Exception {
		// Here we want to store the tuples
		if (holdOnMap.containsKey(pipeId)) {
			List<PeerID> failedPeersForPipeId = holdOnMap.get(pipeId);
			if (failedPeersForPipeId.contains(failedPeer)) {
				// We already got a holdOn for this pipe for this failed peer -> Don't hold on
				LOG.debug("Won't hold on, cause I already got a holdOn for this pipe for this failed peer.");
				return;
			} 
			// Save, that we got a holdOn for this failed peer
			failedPeersForPipeId.add(failedPeer);
		} else {
			// Save, that we got a holdOn for this failed peer
			List<PeerID> failedPeersForPipeId = new ArrayList<PeerID>();
			failedPeersForPipeId.add(failedPeer);
			holdOnMap.put(pipeId, failedPeersForPipeId);
		}
		
		// Start buffering ("hold on")
		RecoveryHelper.startBuffering(pipeId.toString());
		
		// Start timeout for buffering
		startHoldOnTimeout(pipeId.toString());
	}

	private void goOn(PipeID pipeId) throws Exception {
		// Here we want to empty the buffer and go on sending the tuples to the
		// next peer
		RecoveryHelper.resumeFromBuffering(pipeId.toString());
		
		// Stop timeout for buffering
		stopHoldOnTimeout(pipeId.toString());
	}
	

	private void startHoldOnTimeout(final String pipeID) {
		TimeoutTask timeOutTask = new TimeoutTask() {
			
			@Override
			public void runAfterTimeout() {
				try {
					holdOnTimeoutTasks.remove(pipeID);
					LOG.debug("Start resuming from buffer after timeout, pipeId = {}",pipeID);
					RecoveryHelper.resumeFromBuffering(pipeID);
				} catch (Exception e) {
					LOG.error("Exception while resuming from buffer after timeout, pipeId = {} ", pipeID, e);
				}
			}
		};
		holdOnTimeoutTasks.put(pipeID, timeOutTask);
		timeOutTask.start(HOLD_ON_TIMEOUT_IN_MS);
	}
	

	private void stopHoldOnTimeout(final String pipeID) {
		TimeoutTask timeOutTask = holdOnTimeoutTasks.remove(pipeID);
		if (timeOutTask != null) {
			timeOutTask.setActive(false);
		}
	}

}