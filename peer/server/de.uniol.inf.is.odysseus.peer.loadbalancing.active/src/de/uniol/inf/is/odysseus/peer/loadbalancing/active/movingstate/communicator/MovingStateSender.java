package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;

public class MovingStateSender {
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateSender.class);
	
	private final ITransmissionSender transmission;
	
	public  MovingStateSender(String peerID, String pipeID) throws DataTransmissionException {
		this.transmission = DataTransmissionManager.getInstance()
				.registerTransmissionSender(peerID, pipeID);
		this.transmission.open();
	}

	public void sendData(String stringToSend) {
		byte[] rawBytes = stringToSend.getBytes();
		try {
			transmission.sendData(rawBytes);
		} catch (DataTransmissionException e) {
			LOG.error("Could not send data", e);
			// TODO: proper error handling
		}
	}
	
	
	
}
