package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionReceiver;

public class SocketDataTransmissionReceiver extends EndpointDataTransmissionReceiver {

//	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionReceiver.class);

//	private Socket socket;
//	private InetAddress address;

	public SocketDataTransmissionReceiver(IPeerCommunicator communicator, String peerID, String id) {
		super(communicator, peerID, id);

		communicator.addListener(this, PortMessage.class);
//		Optional<String> optAddress = P2PDictionary.getInstance().getRemotePeerAddress(toPeerID(peerID));
//		if (optAddress.isPresent()) {
//			try {
//				address = InetAddress.getByName(optAddress.get());
//			} catch (UnknownHostException e) {
//				address = null;
//				throw new DataTransmissionException("Address can not be determined", e);
//			}
//		} else {
//			throw new DataTransmissionException("Direct address can not be determined");
//		}
	}

	@Override
	public void sendOpen() throws DataTransmissionException {
		super.sendOpen();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PortMessage) {
//			PortMessage portMessage = (PortMessage) message;
//			final int port = portMessage.getPort();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
//					try {
//						socket = new Socket(address, port);
						
//						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						// TODO: Daten lesen
						
//					} catch (IOException e) {
//						socket = null;
//						LOG.error("Could not create socket", e);
//					}
				}
			});
			t.setName("Reading data thread");
			t.setDaemon(true);
			t.start();
			
		} else {
			super.receivedMessage(communicator, senderPeer, message);
		}
	}

	@Override
	public void sendClose() throws DataTransmissionException {
		super.sendClose();
	}
}
