package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;

/**
 * Entity to send recovery instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings({"unused"}) // TODO
public class RecoveryInstructionSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryInstructionSender.class);

	/**
	 * The result code for successes.
	 */
	private static final String OK_RESULT = "OK";

	/**
	 * The single instance of this class.
	 */
	private static RecoveryInstructionSender cInstance = new RecoveryInstructionSender();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link RecoveryInstructionSender}.
	 */
	public static RecoveryInstructionSender getInstance() {
		return cInstance;
	}

	/**
	 * Data structure to keep all currently running repeating message send
	 * processes in mind.
	 */
	private final Map<Object, RepeatingMessageSend> cSenderMap = Maps
			.newConcurrentMap();

	/**
	 * Data structure to keep the destinations of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<Object, PeerID> cDestinationMap = Maps.newConcurrentMap();

	/**
	 * Data structure to keep already received results of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<Object, Boolean> cResultMap = Maps.newConcurrentMap();

	/**
	 * Sends a hold on instruction with a given pipe id to a given peer by using
	 * a repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipeId
	 *            The given pipe id. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendHoldOnInstruction(PeerID destination, PipeID pipeId,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipeId);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createHoldOnMessage(pipeId),
				communicator);
	}

	/**
	 * Sends a go on instruction with a given pipe id to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipeId
	 *            The given pipe id. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendGoOnInstruction(PeerID destination, PipeID pipeId,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipeId);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createGoOnMessage(pipeId),
				communicator);
	}

	/**
	 * Sends an add query instruction with a given shared query id, given PQL
	 * code and a given recovery process id to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The given shared query id. <br />
	 *            Must be not null.
	 * @param pql
	 *            The given PQL code. <br />
	 *            Must be not null.
	 * @param recoveryProcessStateId
	 *            The given id of the recovery process. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendAddQueryInstruction(PeerID destination,
			ID sharedQueryId, String pql, UUID recoveryProcessStateId,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(recoveryProcessStateId);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createAddQueryMessage(pql,
						sharedQueryId, recoveryProcessStateId), communicator);
	}

	/**
	 * Sends an update sender instruction with a given shared query and a given
	 * new sender to a given peer by using a repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The given shared query id. <br />
	 *            Must be not null.
	 * @param newSender
	 *            The given new sender. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendUpdateSenderInstruction(PeerID destination,
			ID sharedQueryId, PeerID newSender, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(newSender);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createUpdateSenderMessage(newSender,
						sharedQueryId), communicator);
	}

	/**
	 * Sends an update receiver instruction with a given shared query, a given
	 * pipe id and a given new sender to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The given shared query id. <br />
	 *            Must be not null.
	 * @param pipeId
	 *            The given pipe id. <br />
	 *            Must be not null.
	 * @param newSender
	 *            The given new sender. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendUpdateReceiverInstruction(PeerID destination,
			ID sharedQueryId, PeerID newSender, PipeID pipeId,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(newSender);
		Preconditions.checkNotNull(pipeId);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createUpdateReceiverMessage(
						newSender, pipeId, sharedQueryId), communicator);
	}

	/**
	 * Sends a buddy instruction with a given shared query and given backup
	 * information to a given peer by using a repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The given shared query id. <br />
	 *            Must be not null.
	 * @param info
	 *            The given backup information. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendBuddyInstruction(PeerID destination, ID sharedQueryId,
			List<String> info, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(info);
		Preconditions.checkNotNull(communicator);

		return sendInstruction(destination,
				RecoveryInstructionMessage.createBeBuddyMessage(sharedQueryId,
						info), communicator);
	}

	/**
	 * Sends a given recovery instruction message to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param message
	 *            The given message. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendInstruction(PeerID destination,
			RecoveryInstructionMessage message, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(communicator);

		RepeatingMessageSend msgSender = new RepeatingMessageSend(communicator,
				message, destination);

		// TODO
		return false;

	}

}