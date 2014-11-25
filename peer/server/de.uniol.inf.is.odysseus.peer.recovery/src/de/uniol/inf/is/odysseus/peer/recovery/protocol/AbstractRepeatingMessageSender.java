package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;

/**
 * Abstract entity to send messages. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractRepeatingMessageSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(TupleSendSender.class);

	/**
	 * The result code for successes.
	 */
	public static final String OK_RESULT = "OK";

	/**
	 * Data structure to keep all currently running repeating message send
	 * processes in mind.
	 */
	protected final Map<UUID, RepeatingMessageSend> mSenderMap = Maps
			.newConcurrentMap();

	/**
	 * Data structure to keep the destinations of all currently running
	 * repeating message send processes in mind.
	 */
	protected final Map<UUID, PeerID> mDestinationMap = Maps.newConcurrentMap();

	/**
	 * Data structure to keep already received results of all currently running
	 * repeating message send processes in mind.
	 */
	protected final Map<UUID, String> mResultMap = Maps.newConcurrentMap();

	/**
	 * Sends given message to a given peer by using a repeating message send
	 * process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param message
	 *            The message to send. <br />
	 *            Must be not null.
	 * @param uuid
	 *            The unique id of <code>message</code>. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	protected boolean repeatingSend(PeerID destination, IMessage message,
			UUID uuid, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(uuid);
		Preconditions.checkNotNull(communicator);

		RepeatingMessageSend msgSender = new RepeatingMessageSend(communicator,
				message, destination);

		synchronized (mSenderMap) {
			mSenderMap.put(uuid, msgSender);
		}
		synchronized (mDestinationMap) {
			mDestinationMap.put(uuid, destination);
		}

		msgSender.start();
		LOG.debug("Sent {} to peerID {}", message, destination);

		LOG.debug("Waiting for response from peer...");
		while (senderIsActive(uuid)) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {

				LOG.error("...interrupted!", e);
				return false;

			}

		}

		String result = "";
		synchronized (mResultMap) {
			if (!mResultMap.containsKey(uuid)) {
				result = "Peer is not reachable!";
			} else {
				result = mResultMap.get(uuid);
			}
			mResultMap.remove(uuid);

		}
		synchronized (mSenderMap) {
			mSenderMap.remove(uuid);
		}
		synchronized (mDestinationMap) {
			mDestinationMap.remove(uuid);
		}

		System.out.println(message.getClass().getSimpleName());
		if (result.equals(OK_RESULT)) {
			return true;
		}

		// else
		LOG.error("Could not send {}: {}", message, result);
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

		synchronized (mSenderMap) {
			if (!mSenderMap.containsKey(id)) {
				return false;
			}

			// else
			return mSenderMap.get(id).isRunning();
		}
	}
	
}