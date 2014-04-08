package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

	private Socket socket;
	
	public SocketDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) {
		super(communicator, peerID, id);
		
		communicator.addListener(this, PortMessage.class);
	}
	 
	@Override
	public void sendOpen() throws DataTransmissionException {
		super.sendOpen();
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof PortMessage ) {
			PortMessage portMessage = (PortMessage)message;
			int port = portMessage.getPort();
			
			Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(senderPeer);
			if( optAddress.isPresent() ) {
				try {
					socket = new Socket(InetAddress.getByName(optAddress.get()), port);
					
					// TODO: Hier weitermachen
					
				} catch (IOException e) {
					LOG.error("Could not create socket", e);
				}
			} else {
				LOG.error("Could not determine address");
			}
		} else {
			super.receivedMessage(communicator, senderPeer, message);
		}
	}
	
	@Override
	public void sendClose() throws DataTransmissionException {
		super.sendClose();
	}
}
