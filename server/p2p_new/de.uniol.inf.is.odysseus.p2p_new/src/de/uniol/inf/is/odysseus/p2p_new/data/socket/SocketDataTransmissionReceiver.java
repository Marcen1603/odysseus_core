package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

	private final byte[] buffer = new byte[P2PNewPlugIn.TRANSPORT_BUFFER_SIZE];
	
	private Socket socket;
	private InetAddress address;

	public SocketDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) throws DataTransmissionException {
		super(communicator, peerID, id);

		communicator.addListener(this, PortMessage.class);
		Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(toPeerID(peerID));
		if (optAddress.isPresent()) {
			try {
				address = InetAddress.getByName(optAddress.get().substring(0, optAddress.get().indexOf(":")));
			} catch (UnknownHostException e) {
				address = null;
				throw new DataTransmissionException("Address can not be determined", e);
			}
		} else {
			throw new DataTransmissionException("Direct address can not be determined");
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PortMessage) {
			PortMessage portMessage = (PortMessage) message;
			
			if( portMessage.getId() == getId() ) {
				final int port = portMessage.getPort();
	
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							socket = new Socket(address, port);
							InputStream inputStream = socket.getInputStream();
							while( true ) {
								int bytesRead = inputStream.read(buffer);
								if( bytesRead == -1 ) {
									break;
								} else if( bytesRead > 0 ) {
									byte[] msg = new byte[bytesRead - 1];
									System.arraycopy(buffer, 1, msg, 0, msg.length);
									
									byte flag = buffer[0];
									if( flag == 0 ) {
										// data
										fireDataEvent(msg);
									} else {
										IPunctuation punc = (IPunctuation)ObjectByteConverter.bytesToObject(msg);
										firePunctuation(punc);
									}
								}
							}
							
						} catch (IOException e) {
							socket = null;
							LOG.error("Exception while reading socket data", e);
						}
					}
				});
				t.setName("Reading data thread");
				t.setDaemon(true);
				t.start();
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
