package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.OpenMessage;

public class SocketDataTransmissionSender extends EndpointDataTransmissionSender {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionSender.class);

	private static final Random RAND = new Random();
	private ServerSocket serverSocket;
	
	private final IPeerCommunicator peerCommunicator;
	
	public SocketDataTransmissionSender(IPeerCommunicator communicator, String peerID, String id) {
		super(communicator, peerID, id);
		
		this.peerCommunicator = communicator;
	}

	@Override
	protected void processOpenMessage(PeerID senderPeer, OpenMessage message) {
		super.processOpenMessage(senderPeer, message);
		
		if( serverSocket == null ) {
			try {
				serverSocket = new ServerSocket(chooseRandomPort());
				sendPortMessage( serverSocket.getLocalPort(), senderPeer );
				
				Socket clientSocket = serverSocket.accept();
				
				// TODO: Hier weitermachen
				
			} catch (IOException e) {
				LOG.error("Could not create server socket", e);
			}
		}
	}
	
	private void sendPortMessage(int localPort, PeerID senderPeer) {
		PortMessage msg = new PortMessage(localPort);
		try {
			peerCommunicator.send(senderPeer, msg);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send port message", e);
		}
	}

	private static int chooseRandomPort() {
		return RAND.nextInt(40000) + 10000;
	}

	@Override
	protected void processCloseMessage(PeerID senderPeer, CloseMessage message) {
		super.processCloseMessage(senderPeer, message);
	}
	
	@Override
	public void sendData(byte[] data) throws DataTransmissionException {
		super.sendData(data);
	}
	
}
