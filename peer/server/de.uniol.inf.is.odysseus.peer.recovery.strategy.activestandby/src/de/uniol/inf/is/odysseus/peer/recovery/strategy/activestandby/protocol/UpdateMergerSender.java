package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.protocol;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;

/**
 * Entity to send merger update instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class UpdateMergerSender implements IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(UpdateMergerSender.class);

	/**
	 * The result code for successes.
	 */
	public static final String OK_RESULT = "OK";

	/**
	 * The single instance of this class.
	 */
	private static UpdateMergerSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link UpdateMergerSender}.
	 */
	public static UpdateMergerSender getInstance() {
		return cInstance;
	}

	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional
			.absent();

	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to bind. <br />
	 *            Must be not null.
	 */
	public static void bindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {

			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a Peer dictionary.", serv.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The peer communicator, if there is one bound.
	 */
	private Optional<IPeerCommunicator> mPeerCommunicator = Optional.absent();

	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to bind. <br />
	 *            Must be not null.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);
		if (!mPeerCommunicator.isPresent()) {
			mPeerCommunicator = Optional.of(serv);
			cInstance = this;
			serv.registerMessageType(UpdateMergerResponseMessage.class);
			serv.addListener(this, UpdateMergerResponseMessage.class);
			LOG.debug("Bound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
		}
	}

	/**
	 * Unbinds a peer communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);

		if (mPeerCommunicator.isPresent() && mPeerCommunicator.get() == serv) {
			mPeerCommunicator = Optional.absent();
			serv.unregisterMessageType(UpdateMergerResponseMessage.class);
			serv.removeListener(this, UpdateMergerResponseMessage.class);
			cInstance = null;
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
		}
	}

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
		String peer = cPeerDictionary.get().getRemotePeerName(destination);
		LOG.debug("Sent {} to peer {}", message, peer);

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

	/**
	 * Handling of a received response message.
	 * 
	 * @param messageID
	 *            The id of the received message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message, if the response is a fail or
	 *            {@link Optional#absent()}, if the response is a fail.
	 */
	private void handleResponseMessage(UUID messageID,
			Optional<String> errorMessage) {
		Preconditions.checkNotNull(messageID);

		synchronized (mSenderMap) {
			RepeatingMessageSend sender = mSenderMap.get(messageID);

			if (sender != null) {
				sender.stopRunning();
				mSenderMap.remove(messageID);
			}
		}

		String result = OK_RESULT;
		if (errorMessage.isPresent()) {
			result = errorMessage.get();
		}
		synchronized (mResultMap) {
			mResultMap.put(messageID, result);
		}

	}

	/**
	 * Sends given update merger instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe to find connected mergers. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendInstruction(PeerID destination, PipeID pipe) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipe);

		UpdateMergerMessage message = new UpdateMergerMessage(pipe);
		return repeatingSend(destination, message, message.getUUID(),
				mPeerCommunicator.get());

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof UpdateMergerResponseMessage) {
			UpdateMergerResponseMessage response = (UpdateMergerResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());
		}
	}

}