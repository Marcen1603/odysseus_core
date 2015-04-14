package de.uniol.inf.is.odysseus.peer.transmission;

import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.CloseAckMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.DataMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.DoneMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.OpenAckMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.OpenMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.PunctuationMessage;
import de.uniol.inf.is.odysseus.peer.transmission.socket.PortAckMessage;
import de.uniol.inf.is.odysseus.peer.transmission.socket.PortMessage;
import de.uniol.inf.is.odysseus.peer.transmission.socket.SocketDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.peer.transmission.socket.SocketDataTransmissionSender;

public class DataTransmissionManager {
	
	private static IPeerCommunicator peerCommunicator;
	
	// called by OSGi-DS
	public static void bindPeerCommunicator( IPeerCommunicator serv ) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(DataMessage.class);
		peerCommunicator.registerMessageType(OpenMessage.class);
		peerCommunicator.registerMessageType(OpenAckMessage.class);
		peerCommunicator.registerMessageType(CloseMessage.class);
		peerCommunicator.registerMessageType(CloseAckMessage.class);
		peerCommunicator.registerMessageType(DoneMessage.class);
		peerCommunicator.registerMessageType(PortMessage.class);
		peerCommunicator.registerMessageType(PunctuationMessage.class);
		peerCommunicator.registerMessageType(PortAckMessage.class);
	}
	
	// called by OSGi-DS
	public static void unbindPeerCommunicator( IPeerCommunicator serv ) {
		if( peerCommunicator == serv ) {
			peerCommunicator.unregisterMessageType(DataMessage.class);
			peerCommunicator.unregisterMessageType(OpenMessage.class);
			peerCommunicator.unregisterMessageType(OpenAckMessage.class);
			peerCommunicator.unregisterMessageType(CloseMessage.class);
			peerCommunicator.unregisterMessageType(CloseAckMessage.class);
			peerCommunicator.unregisterMessageType(DoneMessage.class);
			peerCommunicator.unregisterMessageType(PortMessage.class);
			peerCommunicator.unregisterMessageType(PunctuationMessage.class);
			peerCommunicator.unregisterMessageType(PortAckMessage.class);
			
			peerCommunicator = null;
		}
	}
	
	// called by JxtaSenderPO
	public static ITransmissionSender registerTransmissionSender( String destination, String id ) throws DataTransmissionException {
		try {
			return new SocketDataTransmissionSender(peerCommunicator, destination, id);
		} catch( Throwable t) {
			throw new DataTransmissionException(t);
		}
	}
	
	// called be JxtaReceiverPO
	public static ITransmissionReceiver registerTransmissionReceiver( String source, String id ) throws DataTransmissionException {
		return new SocketDataTransmissionReceiver(peerCommunicator, source, id);
	}
}
