package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiverListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol.IStateReceivedListener;

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
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateReceiver.class);

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
	 * Flag that indicates if receiving is finished
	 */
	private boolean receivingFinished = false;

	/***
	 * Clears receiving finished Flag.
	 */
	public void resetFinished() {
		this.receivingFinished = false;
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
	public MovingStateReceiver(String peerID, String pipeID)
			throws DataTransmissionException {

		transmission = DataTransmissionManager.getInstance()
				.registerTransmissionReceiver(peerID, pipeID);
		transmission.addListener(this);
		transmission.open();
		transmission.sendOpen();
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
	public void addListener(IStateReceivedListener listener) {
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
		if (!receivingFinished) {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInput in = null;

			try {
				in = new ObjectInputStream(bis);
				receivedState = (Serializable) in.readObject();
				receivingFinished = true;
				LOG.debug("Received: ");
				LOG.debug(receivedState.toString());
				notifyListeners();
			} catch (IOException e) {
				LOG.error("Error while deserializing bytes.");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				LOG.error("Class not found.");
				e.printStackTrace();
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
	}

	/***
	 * not implemented
	 */
	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver,
			IPunctuation punc) {
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
			throw new LoadBalancingException(
					"Tried injecting State without having state received.");
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
