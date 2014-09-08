package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.OpenMessage;

public class SocketDataTransmissionSender extends EndpointDataTransmissionSender {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionSender.class);

	private final IPeerCommunicator peerCommunicator;

	private final Map<PeerID, ServerSocket> serverSocketMap = Maps.newConcurrentMap();
	private final Map<PeerID, Socket> clientSocketMap = Maps.newConcurrentMap();
	private final Collection<PeerID> toRemoveList = Lists.newArrayList();

	public SocketDataTransmissionSender(IPeerCommunicator communicator, String peerID, String id) {
		super(communicator, peerID, id);

		this.peerCommunicator = communicator;
	}

	@Override
	protected void processOpenMessage(final PeerID senderPeer, OpenMessage message) {
		super.processOpenMessage(senderPeer, message);

		Thread acceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(0);
					serverSocketMap.put(senderPeer, serverSocket);

					sendPortMessage(serverSocket.getLocalPort(), senderPeer);
					Socket clientSocket = serverSocket.accept();
					clientSocketMap.put(senderPeer, clientSocket);

				} catch (IOException e) {
					LOG.error("Could not create Server socket", e);
				}
			}
		});
		acceptThread.setName("ServerSocket accept thread for " + senderPeer.toString());
		acceptThread.setDaemon(true);
		acceptThread.start();
	}

	private void sendPortMessage(int localPort, PeerID senderPeer) {
		PortMessage msg = new PortMessage(localPort, getId());
		try {
			peerCommunicator.send(senderPeer, msg);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send port message", e);
		}
	}

	@Override
	protected void processCloseMessage(PeerID senderPeer, CloseMessage message) {
		for (PeerID pid : serverSocketMap.keySet()) {
			try {
				serverSocketMap.get(pid).close();
			} catch (IOException e) {
			}
		}
		serverSocketMap.clear();

		for (PeerID pid : clientSocketMap.keySet()) {
			try {
				clientSocketMap.get(pid).close();
			} catch (IOException e) {
			}
		}
		clientSocketMap.clear();

		super.processCloseMessage(senderPeer, message);
	}

	@Override
	public final void sendData(byte[] data) throws DataTransmissionException {
		sendImpl(data, (byte) 0);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) throws DataTransmissionException {
		byte[] data = ObjectByteConverter.objectToBytes(punctuation);
		sendImpl(data, (byte) 1);
	}

	private void sendImpl(byte[] data, byte flag) {
		toRemoveList.clear();
		byte[] rawData = new byte[data.length + 5];
		insertInt(rawData, 0, data.length + 1);
		rawData[4] = flag;
		System.arraycopy(data, 0, rawData, 5, data.length);

		for (PeerID pid : clientSocketMap.keySet()) {
			try {
				Socket cs = clientSocketMap.get(pid);
				cs.getOutputStream().write(rawData);
				cs.getOutputStream().flush();

			} catch (IOException e) {
				LOG.error("Could not send data", e);
				toRemoveList.add(pid);
			}
		}

		if (!toRemoveList.isEmpty()) {
			for (PeerID pid : toRemoveList) {
				Socket socket = clientSocketMap.remove(pid);
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
					}
				}

				ServerSocket serverSocket = serverSocketMap.remove(pid);
				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}
}
