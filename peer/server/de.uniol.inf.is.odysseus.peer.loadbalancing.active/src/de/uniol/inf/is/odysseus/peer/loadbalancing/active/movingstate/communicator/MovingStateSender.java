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

public class MovingStateSender {
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateSender.class);
	
	private final ITransmissionSender transmission;
	
	public  MovingStateSender(String peerID, String pipeID) throws DataTransmissionException {
		this.transmission = DataTransmissionManager.getInstance()
				.registerTransmissionSender(peerID, pipeID);
		this.transmission.open();
	}

	public void sendData(Serializable toSend) {
		
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
		} catch (IOException e) {
			LOG.error("Could not serialize Data to Bytes.");
			e.printStackTrace();
		} 
		finally {
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
