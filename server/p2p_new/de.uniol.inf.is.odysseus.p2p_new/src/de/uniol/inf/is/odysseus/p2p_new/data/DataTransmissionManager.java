package de.uniol.inf.is.odysseus.p2p_new.data;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.CloseAckMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.DataMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.DoneMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.OpenAckMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.OpenMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.PunctuationMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.socket.PortMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.socket.SocketDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.socket.SocketDataTransmissionSender;

public class DataTransmissionManager {
	
	private static DataTransmissionManager instance;
	private static IPeerCommunicator peerCommunicator;
	
	// called by OSGi-DS
	public void bindPeerCommunicator( IPeerCommunicator serv ) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(DataMessage.class);
		peerCommunicator.registerMessageType(OpenMessage.class);
		peerCommunicator.registerMessageType(OpenAckMessage.class);
		peerCommunicator.registerMessageType(CloseMessage.class);
		peerCommunicator.registerMessageType(CloseAckMessage.class);
		peerCommunicator.registerMessageType(DoneMessage.class);
		peerCommunicator.registerMessageType(PortMessage.class);
		peerCommunicator.registerMessageType(PunctuationMessage.class);
	}
	
	// called by OSGi-DS
	public void unbindPeerCommunicator( IPeerCommunicator serv ) {
		if( peerCommunicator == serv ) {
			peerCommunicator.unregisterMessageType(DataMessage.class);
			peerCommunicator.unregisterMessageType(OpenMessage.class);
			peerCommunicator.unregisterMessageType(OpenAckMessage.class);
			peerCommunicator.unregisterMessageType(CloseMessage.class);
			peerCommunicator.unregisterMessageType(CloseAckMessage.class);
			peerCommunicator.unregisterMessageType(DoneMessage.class);
			peerCommunicator.unregisterMessageType(PortMessage.class);
			peerCommunicator.unregisterMessageType(PunctuationMessage.class);
			
			peerCommunicator = null;
		}
	}
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	public static DataTransmissionManager getInstance() {
		return instance;
	}
	
	public static boolean isActivated() {
		return instance != null;
	}
	
	public static void waitFor() {
		while( !isActivated() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
	
	// called by JxtaSenderPO
	public ITransmissionSender registerTransmissionSender( String destination, String id ) throws DataTransmissionException {
		try {
			return new SocketDataTransmissionSender(peerCommunicator, destination, id);
		} catch( Throwable t) {
			throw new DataTransmissionException(t);
		}
	}
	
	// called be JxtaReceiverPO
	public ITransmissionReceiver registerTransmissionReceiver( String source, String id ) throws DataTransmissionException {
		return new SocketDataTransmissionReceiver(peerCommunicator, source, id);
	}
}
