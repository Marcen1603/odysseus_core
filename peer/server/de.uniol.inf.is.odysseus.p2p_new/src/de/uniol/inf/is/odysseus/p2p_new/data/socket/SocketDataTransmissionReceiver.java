package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.PeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

	private final MessageByteBuffer mb = new MessageByteBuffer();

	private Socket socket;
	private InetAddress address;
	private Boolean receiving = false;
	private final byte[] buffer = new byte[P2PNewPlugIn.TRANSPORT_BUFFER_SIZE];
//	private int receivedBytes = 0;
//	private int packageCount = 0;
	
	public SocketDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) throws DataTransmissionException {
		super(communicator, peerID, id);

		determinePeerAddress(peerID);
	}
	
	@Override
	public void open() {
		super.open();
		
		getPeerCommunicator().addListener(this, PortMessage.class);
	}

	@Override
	public void close() {
		getPeerCommunicator().removeListener(this, PortMessage.class);
		
		super.close();
	}

	private void determinePeerAddress(String peerID) throws DataTransmissionException {
		Optional<String> optAddress = PeerDictionary.getInstance().getRemotePeerAddress(toPeerID(peerID));
		if (optAddress.isPresent()) {
			try {
				String addressString = optAddress.get();
				if (addressString.startsWith("[")) {
					// IPv6
					address = InetAddress.getByName(addressString.substring(0, addressString.indexOf("]") + 1));
					LOG.debug("Using IPv6 for socket connection: {}", address);
				} else {
					// IPv4
					int portIndex = addressString.indexOf(":");
					if (portIndex >= 0) {
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

			if (portMessage.getId() == getId()) {
				final int port = portMessage.getPort();

				LOG.debug("Received portMessage for port " + portMessage.getPort() + " from {}", PeerDictionary.getInstance().getRemotePeerName(senderPeer));

				// Send PortAckMessage
				sendPortAckMessage(communicator, senderPeer, port, portMessage.getId());

				synchronized (receiving) {
					if (receiving) {
						LOG.debug("{}: Already receiving ... aborting", this);
						return;
					}
					receiving = true;
					LOG.debug("{}: Beginning receiving async", this);

					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								socket = new Socket(address, port);
								LOG.debug("Opened socket on port {} for address {}", port, address);
								InputStream inputStream = socket.getInputStream();
								while (true) {
									int bytesRead = inputStream.read(buffer);
									if (bytesRead == -1) {
										LOG.debug("Reached end of data stream. Socket closed...");
										break;
									} else if (bytesRead > 0) {
//										receivedBytes +=  bytesRead;
										
//										System.err.println("Received package : " + bytesRead + " --> " + receivedBytes);
//										System.err.println("Packages : " + (packageCount++));
										
										byte[] msg = new byte[bytesRead];
										if( buffer == null ) {
											// closed
											break;
										}
										
										System.arraycopy(buffer, 0, msg, 0, bytesRead);

										mb.put(msg);

										List<byte[]> packets = mb.getPackets();
										for (byte[] packet : packets ) {
											processBytes(packet);
										}
									}
									synchronized( receiving ) {
										if( !receiving ) {
											break;
										}
									}
								}
							} catch (SocketException e) {
								tryCloseSocket(socket);
								if (!e.getMessage().equals("socket closed")) {
									LOG.error("Exception while reading socket data", e);
								}

							} catch (IOException e) {
								LOG.error("Exception while reading socket data", e);

								tryCloseSocket(socket);
							} finally {
								socket = null;
								synchronized (receiving) {
									receiving = false;
									LOG.debug("Receiving ended");
								}
							}
						}

					});
					t.setName("Reading data thread");
					t.setDaemon(true);
					t.start();
				}
			}
		} else {
			super.receivedMessage(communicator, senderPeer, message);
		}
	}
	
	@Override
	public void stopReceiving() {
		synchronized(receiving) {
			receiving=false;
		}
	}

	private void sendPortAckMessage(IPeerCommunicator communicator, PeerID receiverPeer, int port, int id) {
		PortAckMessage portAckMessage = new PortAckMessage(port, id);
		try {
			communicator.send(receiverPeer, portAckMessage);
			LOG.debug("Sent portAckMessage for port " + port + " to {}", PeerDictionary.getInstance().getRemotePeerName(receiverPeer));
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send the portAckMessage to peer {}", PeerDictionary.getInstance().getRemotePeerName(receiverPeer));
		}
	}

	@Override
	public void setPeerId(PeerID peerId) throws DataTransmissionException {
		// Only changes peer Id. Still need to determine new Address.
		super.setPeerId(peerId);
		LOG.debug("Set new Peer Id to {}", peerId);
		determinePeerAddress(peerId.toString());
	}

	private void processBytes(byte[] msg) {
		byte[] realMsg = new byte[msg.length - 1];
		System.arraycopy(msg, 1, realMsg, 0, realMsg.length);

//		System.err.println("Received packages " + (packageCount++));

		byte flag = msg[0];
		if (flag == 0) {
			// data
			fireDataEvent(realMsg);
		} else if (flag == 1) {
			IPunctuation punc = (IPunctuation) ObjectByteConverter.bytesToObject(realMsg);
			firePunctuation(punc);
		} else if( flag == 2 ) {
			fireDoneEvent();
		} else {
			LOG.error("Unknown flag {}", flag);
		}
	}

	private static void tryCloseSocket(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e1) {
		}
	}
	

	@Override
	public void sendClose() throws DataTransmissionException {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		super.sendClose();
	}
}
