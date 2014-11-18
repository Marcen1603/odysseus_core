package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;

/****
 * Implements a sender for serialized statefulPO states.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateSender {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateSender.class);

	/***
	 * Transmission Sender
	 */
	private final ITransmissionSender transmission;

	/**
	 * Flag that is set when transmission is successful
	 */
	private boolean successfullyTransmitted = false;

	/***
	 * sets transmission successful Flag.
	 */
	public void setSuccessfullyTransmitted() {
		successfullyTransmitted = true;
	}

	/**
	 * 
	 * @return True if state was already successfully transmitted.
	 */
	public boolean isSuccessfullyTransmitted() {
		return successfullyTransmitted;
	}

	/***
	 * Constructor
	 * 
	 * @param peerID
	 *            PeerID to correspond with.
	 * @param pipeID
	 *            PipeID which should be used for transmission
	 * @throws DataTransmissionException
	 */
	public MovingStateSender(String peerID, String pipeID)
			throws DataTransmissionException {
		this.transmission = DataTransmissionManager.getInstance()
				.registerTransmissionSender(peerID, pipeID);
		this.transmission.open();
	}

	/**
	 * Sends a serializable data
	 * 
	 * @param toSend
	 *            Serializable data to send.
	 * @throws LoadBalancingException
	 */
	public void sendData(Serializable toSend) throws LoadBalancingException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(toSend);
			byte[] rawBytes = bos.toByteArray();
			transmission.sendData(rawBytes);
		} catch (DataTransmissionException e) {
			LOG.error("Could not send Data.");
			e.printStackTrace();
			throw new LoadBalancingException("Could not send Data.");
		} catch (IOException e) {
			LOG.error("Could not serialize Data to Bytes.");
			e.printStackTrace();
			throw new LoadBalancingException("Could not serialize Data.");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

	}

}
