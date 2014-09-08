package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

	private final byte[] buffer = new byte[P2PNewPlugIn.TRANSPORT_BUFFER_SIZE];
	
	private final MessageByteBuffer mb = new MessageByteBuffer();
	
	private Socket socket;
	private InetAddress address;

	public SocketDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) throws DataTransmissionException {
		super(communicator, peerID, id);

		communicator.addListener(this, PortMessage.class);
		Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(toPeerID(peerID));
		if (optAddress.isPresent()) {
			try {
				String addressString = optAddress.get();
				if( addressString.startsWith("[")) {
					// IPv6
					address = InetAddress.getByName(addressString.substring(0, addressString.indexOf("]") + 1));
					LOG.debug("Using IPv6 for socket connection: {}", address);
				} else {
					// IPv4
					int portIndex = addressString.indexOf(":");
					if( portIndex >= 0 ) {
						address = InetAddress.getByName(addressString.substring(0, portIndex));
					} else {
						address = InetAddress.getByName(addressString);
					}
					LOG.debug("Using IPv4 for socket connection: {}", address);
				}
			} catch (UnknownHostException e) {
				address = null;
				throw new DataTransmissionException("Address of peerid '" + peerID + "' can not be determined", e);
			}
		} else {
			throw new DataTransmissionException("Direct address peerid '" + peerID + "' can not be determined");
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
									LOG.debug("Reached end of data stream. Socket closed...");
									break;
								} else if( bytesRead > 0 ) {
									byte[] msg = new byte[bytesRead];
									System.arraycopy(buffer, 0, msg, 0, bytesRead);
									
									mb.put(msg);
									
									byte[] packet = mb.getPacket();
									if( packet != null ) {
										processBytes(packet);
									}
								}
							}
						} catch( SocketException e ) {
							tryCloseSocket(socket);
							if( !e.getMessage().equals("socket closed")) {
								LOG.error("Exception while reading socket data", e);
							}
							
						} catch (IOException e) {
							LOG.error("Exception while reading socket data", e);

							tryCloseSocket(socket);
							socket = null;
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
	
	private void processBytes(byte[] msg) {
		byte[] realMsg = new byte[msg.length - 1];
		System.arraycopy(msg, 1, realMsg, 0, realMsg.length);
		
		byte flag = msg[0];
		if( flag == 0 ) {
			// data
			fireDataEvent(realMsg);
		} else if (flag == 1){
			IPunctuation punc = (IPunctuation)ObjectByteConverter.bytesToObject(realMsg);
			firePunctuation(punc);
		} else {
			LOG.error("Unknown flag {}", flag);
		}
	}
	
	private static void tryCloseSocket(Socket socket) {
		try {
			if( socket != null ) {
				socket.close();
			}
		} catch (IOException e1) {
		}
	}

	@Override
	public void sendClose() throws DataTransmissionException {
		if( socket != null ) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		super.sendClose();
	}
}
