package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendResponseMessage;

/**
 * Entity to send tuple send instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class TupleSendSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(TupleSendSender.class);

	/**
	 * The result code for successes.
	 */
	private static final String OK_RESULT = "OK";

	/**
	 * The single instance of this class.
	 */
	private static TupleSendSender cInstance = new TupleSendSender();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link TupleSendSender}.
	 */
	public static TupleSendSender getInstance() {
		return cInstance;
	}

	/**
	 * Data structure to keep all currently running repeating message send
	 * processes in mind.
	 */
	private final Map<UUID, RepeatingMessageSend> cSenderMap = Maps
			.newConcurrentMap();

	/**
	 * Data structure to keep the destinations of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<UUID, PeerID> cDestinationMap = Maps.newConcurrentMap();

	/**
	 * Data structure to keep already received results of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<UUID, String> cResultMap = Maps.newConcurrentMap();

	/**
	 * Sends given hold on instruction to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendHoldOnInstruction(PeerID destination, PipeID pipe,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, true, communicator);
	}

	/**
	 * Sends given go on instruction to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendGoOnInstruction(PeerID destination, PipeID pipe,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, false, communicator);
	}

	/**
	 * Sends given go on or hold on instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param holdOn
	 *            True for a hold on message; false for a go on message.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendInstruction(PeerID destination, PipeID pipe,
			boolean holdOn, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipe);
		Preconditions.checkNotNull(communicator);

		RecoveryTupleSendMessage message = new RecoveryTupleSendMessage(pipe,
				holdOn);
		RepeatingMessageSend msgSender = new RepeatingMessageSend(communicator,
				message, destination);

		synchronized (cSenderMap) {
			cSenderMap.put(message.getUUID(), msgSender);
		}
		synchronized (cDestinationMap) {
			cDestinationMap.put(message.getUUID(), destination);
		}

		msgSender.start();
		String instr = "go on";
		if (holdOn) {
			instr = "hold on";
		}
		LOG.debug("Sent {} for pipe {} to peerID {}", new Object[] { instr,
				pipe, destination });

		LOG.debug("Waiting for response from peer...");
		while (senderIsActive(message.getUUID())) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {

				LOG.error("...interrupted!", e);
				return false;

			}

		}

		String result = "";
		synchronized (cResultMap) {
			if (!cResultMap.containsKey(message.getUUID())) {
				result = "Could not send backup information: Peer is not reachable!";
			} else {
				result = cResultMap.get(message.getUUID());
			}
			cResultMap.remove(message.getUUID());

		}
		synchronized (cSenderMap) {
			cSenderMap.remove(message.getUUID());
		}
		synchronized (cDestinationMap) {
			cDestinationMap.remove(message.getUUID());
		}

		if (result.equals(OK_RESULT)) {
			return true;
		}

		// else
		LOG.error("Could not send {} instruction: {}", instr, result);
		return false;

	}

	/**
	 * Checks, if a given repeating message send process is still active.
	 * 
	 * @param id
	 *            The UUID of the backup information identifying the repeating
	 *            message send process. <br />
	 *            Must be not null.
	 * @return True, if the given repeating message send process is still
	 *         active; false, else.
	 */
	private boolean senderIsActive(UUID id) {
		Preconditions.checkNotNull(id);

		synchronized (cSenderMap) {
			if (!cSenderMap.containsKey(id)) {
				return false;
			}

			// else
			return cSenderMap.get(id).isRunning();
		}
	}

	/**
	 * Handling of a received response message.
	 * 
	 * @param message
	 *            The received message. <br />
	 *            Must be not null.
	 */
	public void receivedResponseMessage(RecoveryTupleSendResponseMessage message) {
		Preconditions.checkNotNull(message);

		synchronized (cSenderMap) {
			RepeatingMessageSend sender = cSenderMap.get(message.getUUID());

			if (sender != null) {
				sender.stopRunning();
				cSenderMap.remove(message.getUUID());
			}
		}

		String result = OK_RESULT;
		if (!message.isPositive()) {
			result = message.getErrorMessage().get();
		}
		synchronized (cResultMap) {
			cResultMap.put(message.getUUID(), result);
		}

	}
}