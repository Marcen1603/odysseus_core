package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.OpenMessage;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.PeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class SocketDataTransmissionSender extends EndpointDataTransmissionSender {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionSender.class);
	
	private static final byte DATA_FLAG = 0;
	private static final byte PUNC_FLAG = 1;
	private static final byte DONE_FLAG = 2;

	private final IPeerCommunicator peerCommunicator;

	private final Map<PeerID, ServerSocket> serverSocketMap = Maps.newConcurrentMap();
	private final Map<PeerID, Socket> clientSocketMap = Maps.newConcurrentMap();
	private final List<PeerID> openCallers = Lists.newArrayList();
	private final Map<PeerID, List<byte[]>> waitingBuffer = Maps.newConcurrentMap();
	private final List<byte[]> globalBuffer = Lists.newArrayList();

	private final Collection<PeerID> toRemoveList = Lists.newArrayList();
	private Object semaphore = new Object();

	private RepeatingMessageSend portMessageRepeater;
	
//	private int sendBytes = 0;

	public SocketDataTransmissionSender(IPeerCommunicator communicator, String peerID, String id) {
		super(communicator, peerID, id);

		this.peerCommunicator = communicator;
	}

	@Override
	protected void processOpenMessage(final PeerID senderPeer, OpenMessage message) {
		super.processOpenMessage(senderPeer, message);

		openCallers.add(senderPeer);
		waitingBuffer.put(senderPeer, Lists.<byte[]> newArrayList());

		Thread acceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(0);
					serverSocketMap.put(senderPeer, serverSocket);

					LOG.debug("Opened (unaccepted) server socket for {} on port {}.", PeerDictionary.getInstance().getRemotePeerName(senderPeer), serverSocket.getLocalPort());
					sendPortMessage(serverSocket.getLocalPort(), senderPeer);

					Socket clientSocket = serverSocket.accept();
					LOG.debug("Server socket on port {} was accepted from peer {}", serverSocket.getLocalPort(), PeerDictionary.getInstance().getRemotePeerName(senderPeer));

					clientSocketMap.put(senderPeer, clientSocket);
					for (PeerID peerID : clientSocketMap.keySet()) {
						LOG.debug("clientSocketMap contains " + peerID.toString());
					}

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

		// Repeat this message until we get an PortAckMessage
		portMessageRepeater = new RepeatingMessageSend(peerCommunicator, msg, senderPeer);
		portMessageRepeater.start();
		LOG.debug("Startet to send port-messages for port " + msg.getPort() + " to {}", PeerDictionary.getInstance().getRemotePeerName(senderPeer));
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PortAckMessage) {
			PortAckMessage portAckMessage = (PortAckMessage) message;
			synchronized (semaphore) {
				if (portMessageRepeater != null) {
					if (portAckMessage.getId() == getId() && portMessageRepeater != null) {
						LOG.debug("Received portAckMessage for port " + portAckMessage.getPort() + "  from {}", PeerDictionary.getInstance().getRemotePeerName(senderPeer));
						portMessageRepeater.stopRunning();
						portMessageRepeater = null;
					}
				}
			}

		} else {
			super.receivedMessage(communicator, senderPeer, message);
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
		openCallers.remove(senderPeer);
		waitingBuffer.remove(senderPeer);

		super.processCloseMessage(senderPeer, message);
	}

	@Override
	public final void sendData(byte[] data) throws DataTransmissionException {
		sendImpl(data, DATA_FLAG);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) throws DataTransmissionException {
		byte[] data = ObjectByteConverter.objectToBytes(punctuation);
		sendImpl(data, PUNC_FLAG);
	}
	
	@Override
	public void sendDone() throws DataTransmissionException {
		sendImpl( new byte[0], DONE_FLAG );
	}

	private void sendImpl(byte[] data, byte flag) {
		byte[] rawData = new byte[data.length + 5];
		insertInt(rawData, 0, data.length + 1);
		rawData[4] = flag;
		System.arraycopy(data, 0, rawData, 5, data.length);

		sendDirect(rawData);
	}

	private void sendDirect(byte[] rawData) {

		if (openCallers.isEmpty()) {
			globalBuffer.add(rawData);
			return;
		}

		toRemoveList.clear();
		for (PeerID pid : openCallers) {

			if (clientSocketMap.containsKey(pid)) {

				try {
					Socket cs = clientSocketMap.get(pid);
					
					if (!globalBuffer.isEmpty()) {
						for (byte[] bufferedData : globalBuffer) {
							cs.getOutputStream().write(bufferedData);
							cs.getOutputStream().flush();
							
//							sendBytes += bufferedData.length;
//							System.err.println("Send bytes " + bufferedData.length + " --> " + sendBytes);
						}
						globalBuffer.clear();
					}

					if (waitingBuffer.containsKey(pid)) {
						for (byte[] bufferedData : waitingBuffer.remove(pid)) {
							cs.getOutputStream().write(bufferedData);
							cs.getOutputStream().flush();
//							sendBytes += bufferedData.length;
//							System.err.println("Send bytes " + bufferedData.length + " --> " + sendBytes);
						}
					}


					cs.getOutputStream().write(rawData);
					cs.getOutputStream().flush();
					
//					sendBytes += rawData.length;
//					System.err.println("Send bytes " + rawData.length + " --> " + sendBytes);

				} catch (IOException e) {
					LOG.error("Could not send data", e);
					toRemoveList.add(pid);
				}
			} else {
				waitingBuffer.get(pid).add(rawData);
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

				openCallers.remove(pid);
				waitingBuffer.remove(pid);
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
