package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

/**
 * Used to repeatedly send Messages to other peers. Copied from P2P Package and
 * slightly modified.
 * 
 * @author Timo Michelsen, Carsten Cordes
 *
 */
public class RepeatingMessageSend extends RepeatingJobThread {

	/**
	 * Holds listeners that can be informed when message delivery fails.
	 */
	ArrayList<IMessageDeliveryFailedListener> listeners;

	/**
	 * Only used for Debug purpose. No Message is sent. Timeout is called
	 * instantly.
	 */
	public boolean TEST_TIMEOUT = false;

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RepeatingMessageSend.class);

	/**
	 * Time until warning is logged.
	 */
	private static final int WARNING_TIME_INTERVAL_MILLIS = 30 * 1000;

	/**
	 * Time until Message is aborted.
	 */
	private static final int ABORT_MILLIS = 60 * 1000;

	/**
	 * Interval between repeating send attempts.
	 */
	private static final int REPEAT_SEND_INTERVAL_MILLIS = 1000;

	/***
	 * PeerCommunicator
	 */
	private final IPeerCommunicator communicator;

	/**
	 * Message to send.
	 */
	private final IMessage message;

	/**
	 * Destination Peer
	 */
	private final PeerID peerID;

	/**
	 * Used to track time.
	 */
	private int sendingTime;

	/**
	 * Has user been warned already?
	 */
	private boolean warned;

	/**
	 * Constructor
	 * 
	 * @param communicator
	 *            Communicator
	 * @param message
	 *            Message to send.
	 * @param destination
	 *            Destination Peer
	 */
	public RepeatingMessageSend(IPeerCommunicator communicator,
			IMessage message, PeerID destination) {
		super(REPEAT_SEND_INTERVAL_MILLIS, "Repeating message thread: "
				+ message.getClass().getName());

		this.communicator = communicator;
		this.message = message;
		this.peerID = destination;
		this.listeners = new ArrayList<IMessageDeliveryFailedListener>();
	}

	/**
	 * Adds Message-delivery failed listener.
	 * 
	 * @param listener
	 */
	public void addListener(IMessageDeliveryFailedListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Clears all listeners.
	 */
	public void clearListeners() {
		this.listeners.clear();
	}

	/**
	 * Notifies all Listeners
	 */
	public void notifyListeners() {
		LOG.error("Notifying listeners");
		for (IMessageDeliveryFailedListener listener : listeners) {
			listener.update(message, peerID);
		}
	}

	/***
	 * Sends a message repeatedly.
	 */
	@Override
	public void doJob() {
		try {
			if (TEST_TIMEOUT) {
				LOG.error("TEST_TIMEOUT activated.");
				notifyListeners();
				stopRunning();
				return;
			}
			LOG.debug("Retrying sending message of type '{}'", message
					.getClass().getName());

			communicator.send(peerID, message);

			sendingTime += getIntervalMillis();
			if (!warned && sendingTime >= WARNING_TIME_INTERVAL_MILLIS) {
				LOG.error(
						"RepeatingMessageThread runs more than "
								+ WARNING_TIME_INTERVAL_MILLIS
								+ " ms now! Message: {}", message.getClass()
								.getName());
				warned = true;
			} else if (sendingTime > ABORT_MILLIS) {
				LOG.error("RepeatingMessageThread runs now more than "
						+ ABORT_MILLIS + " ms now. Abort sending message {}",
						message.getClass().getName());
				notifyListeners();
				stopRunning();

			}

		} catch (PeerCommunicationException e) {
			LOG.error("Could not repeatedly send message of type {}", message
					.getClass().getName(), e);
			notifyListeners();
			stopRunning();

		}
	}

}
