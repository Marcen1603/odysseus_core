package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol.IStateReceivedListener;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionManager;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionReceiverListener;

/***
 * Receiver for serialized states of StatefulPOs
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateReceiver implements ITransmissionReceiverListener {

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MovingStateReceiver.class);

	/**
	 * Transmission Receiver
	 */
	private final ITransmissionReceiver transmission;

	/***
	 * Listeners to notify
	 */
	private ArrayList<IStateReceivedListener> listener = new ArrayList<IStateReceivedListener>();

	/***
	 * Pipe ID to use for transmission
	 */
	private String pipe;

	/**
	 * Received State
	 */
	private Serializable receivedState;

	/**
	 * Flag that indicates if we already received the announcement
	 */
	private boolean announcementReceived = false;

	/**
	 * Flag that indicates if receiving is finished
	 */
	private boolean stateReceived = false;

	private StateAnnouncement announcement;

	private int messagePartCounter;

	private byte[] stateBytes;

	private Object semaphore = new Object();

	/***
	 * Clears receiving finished Flag.
	 */
	public void resetFinished() {
		this.announcementReceived = false;
		this.stateReceived = false;
	}

	/***
	 * Constructor
	 * 
	 * @param peerID
	 *            PeerID of corresponding peer
	 * @param pipeID
	 *            Pipe ID to use for transmission
	 * @throws DataTransmissionException
	 */
	public MovingStateReceiver(String peerID, String pipeID) throws DataTransmissionException {

		transmission = DataTransmissionManager.registerTransmissionReceiver(peerID, pipeID);
		transmission.addListener(this);
		transmission.open();
		transmission.sendOpen();

		LOG.debug("New MovingStateReceiver with pipe ID {} created.", pipeID);
		this.pipe = pipeID;
	}

	/**
	 * Notifies all listeners that state is received
	 */
	public void notifyListeners() {
		for (IStateReceivedListener l : listener) {
			if (l != null) {
				l.stateReceived(pipe);
			}
		}
	}

	/**
	 * Add listener
	 * 
	 * @param listener
	 *            IStateReceivedListener
	 */
	public synchronized void addListener(IStateReceivedListener listener) {
		if (!this.listener.contains(listener))
			this.listener.add(listener);
	}

	/**
	 * True if status is received
	 * 
	 * @return True if state has been received, false if not.
	 */
	public boolean hasStatus() {
		return this.receivedState != null;
	}

	@Override
	/***
	 * EventHandler that fires if Transmission receives data.
	 */
	public void onReceiveData(ITransmissionReceiver receiver, byte[] data) {

		LOG.debug("Received something??");

		if (!announcementReceived) {
			stateReceived = false;

			Serializable temp = convertBytesToSerializable(data);
			if (temp instanceof StateAnnouncement) {
				announcement = (StateAnnouncement) temp;
				announcementReceived = true;

				// set message counter to zero and initialize array
				messagePartCounter = 0;
				stateBytes = new byte[announcement.getArrayLenght()];
				LOG.error(String.format("State Transmission: StateAnnouncement received. %d messages need to be processed.", announcement.getNumberOfMessages()));
			}
			return;
		}

		if (announcementReceived && !stateReceived) {
			if (data.length <= StateAnnouncement.MAX_MESSAGE_SIZE) {
				synchronized (semaphore) {
					int index = 0;
					try {
						for (int i = 0; i < data.length; i++) {
							index = messagePartCounter * StateAnnouncement.MAX_MESSAGE_SIZE + i;
							stateBytes[index] = data[i];
						}
						messagePartCounter++;
						LOG.debug(String.format("State Transmission: Message %d of %d received.", messagePartCounter, announcement.getNumberOfMessages()));
					} catch (IndexOutOfBoundsException e) {
						LOG.error(String.format("State Transmission: Maximum index of array is %d but requested index is %d. Transmission aborted.", stateBytes.length - 1, index));
						resetOnError();
					}
				}
			} else {
				LOG.error(String.format("State Transmission: Message %d is too long. Maximum size is: %d byte. Aktual size is %d byte. Transmission aborted.", messagePartCounter, StateAnnouncement.MAX_MESSAGE_SIZE, data.length));
				resetOnError();
			}
			LOG.debug("{} of {} received.", messagePartCounter, announcement.getNumberOfMessages());
			if (messagePartCounter == announcement.getNumberOfMessages()) {
				// if all message parts are received, we can convert it to
				// serializable
				LOG.debug("Converting to serializable");
				Checksum checksum = new CRC32();
				checksum.update(stateBytes, 0, stateBytes.length);
				long checksumValue = checksum.getValue();

				if (checksumValue == announcement.getChecksum()) {
					LOG.debug("State Transmission: CRC Checksum Validation successful");
					receivedState = convertBytesToSerializable(stateBytes);

					if (receivedState instanceof IOperatorState) {
						LOG.error("State Transmission: Received object is an instance of IOperatorState. Transmission completed");
						stateReceived = true;
						notifyListeners();
					} else {
						LOG.error("State Transmission: Received object is no instance of IOperatorState. Transmission aborted");
						resetOnError();
					}

					announcementReceived = false;
				} else {
					LOG.error("State Transmission: CRC Checksum Validation failed. Transmission aborted.");
					resetOnError();
				}

			}
		}
	}

	/**
	 * Resets the internal state of transmission. Needed if receiver receives
	 * new state
	 */
	private void resetOnError() {
		announcementReceived = false;
		stateReceived = false;
		receivedState = null;
		messagePartCounter = 0;
	}

	/**
	 * converts the given byte array into serializable object
	 * 
	 * @param data
	 *            byte array of an serializable object
	 * @return serializable object
	 */
	private Serializable convertBytesToSerializable(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;

		try {
			in = new ObjectInputStream(bis);
			return (Serializable) in.readObject();
		} catch (IOException e) {
			LOG.error("Error while deserializing bytes.");
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			LOG.error("Class not found.");
			e.printStackTrace();
			return null;
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	/***
	 * not implemented
	 */
	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver, IPunctuation punc) {
		// ignore as we are not an operator

	}

	/***
	 * Injects a previously received state in an operator
	 * 
	 * @param operator
	 *            Operator where state should be injected
	 * @throws LoadBalancingException
	 */
	@SuppressWarnings("rawtypes")
	public void injectState(IStatefulPO operator) throws LoadBalancingException {
		if (receivedState == null) {
			throw new LoadBalancingException("Tried injecting State without having state received.");
		}
		if (operator instanceof IPipe) {
			IPipe physicalOp = (IPipe) operator;
			MovingStateHelper.startBuffering(physicalOp);
			operator.setState(receivedState);
			MovingStateHelper.stopBuffering(physicalOp);
		} else if (operator instanceof ISink) {
			ISink physicalOp = (ISink) operator;
			MovingStateHelper.startBuffering(physicalOp);
			operator.setState(receivedState);
			MovingStateHelper.stopBuffering(physicalOp);
		}
	}

	/***
	 * Called when receiving is finished (not used here)
	 */
	@Override
	public void onReceiveDone(ITransmissionReceiver receiver) {

	}
}
