package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiverListener;

public class MovingStateReceiver implements ITransmissionReceiverListener {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateReceiver.class);
	
	private final ITransmissionReceiver transmission;
	
	
	
	public MovingStateReceiver(String peerID, String pipeID) throws DataTransmissionException {
		
		transmission = DataTransmissionManager.getInstance().registerTransmissionReceiver(peerID, pipeID);
		transmission.addListener(this);
		transmission.open();
		transmission.sendOpen();
	}

	@Override
	public void onReceiveData(ITransmissionReceiver receiver, byte[] data) {
		String receivedString = new String(data);
		LOG.debug("Received String: ");
		LOG.debug(receivedString);
	}

	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver,
			IPunctuation punc) {
		//ignore as we are not an operator
		
	}

	@Override
	public void onReceiveDone(ITransmissionReceiver receiver) {
		//ignore as we are not an operator
		
	}
}
