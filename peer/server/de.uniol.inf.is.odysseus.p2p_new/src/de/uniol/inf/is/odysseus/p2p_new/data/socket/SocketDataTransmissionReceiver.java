package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.PeerDictionary;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

	private InetAddress address;
	
	private ReceivingDataThread receivingThread;
	private Boolean receiving = false;
		
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
		
		stopReceiving();
		
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
					LOG.debug("{}: Beginning receiving async", this);
					receiving = true;
					

					receivingThread = new ReceivingDataThread(address, port, new IReceivingDataThreadListener() {
						
						@Override
						public void onReceivingPunctuation(IPunctuation punc) {
							firePunctuation(punc);
						}
						
						@Override
						public void onReceivingDoneEvent() {
							fireDoneEvent();
						}
						
						@Override
						public void onReceivingData(byte[] msg) {
							fireDataEvent(msg);
						}
						
						@Override
						public void onFinish() {
							synchronized( receiving ) {
								receiving = false;
							}
						}
					});
					receivingThread.start();
				}
			}
		} else {
			super.receivedMessage(communicator, senderPeer, message);
		}
	}
	
	@Override
	public void stopReceiving() {
		synchronized(receiving ) {
			receiving = false;
		}
		
		if( receivingThread != null ) {
			receivingThread.stopRunning();
			receivingThread = null;
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

	@Override
	public void sendClose() throws DataTransmissionException {
		stopReceiving();
		super.sendClose();
	}
}
