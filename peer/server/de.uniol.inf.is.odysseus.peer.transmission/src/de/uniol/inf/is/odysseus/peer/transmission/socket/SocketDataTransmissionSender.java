package de.uniol.inf.is.odysseus.peer.transmission.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.CloseMessage;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.EndpointDataTransmissionSender;
import de.uniol.inf.is.odysseus.peer.transmission.endpoint.OpenMessage;
import de.uniol.inf.is.odysseus.peer.util.ObjectByteConverter;

public class SocketDataTransmissionSender extends EndpointDataTransmissionSender {

	private static final Logger LOG = LoggerFactory.getLogger(SocketDataTransmissionSender.class);
	
	private static final byte DATA_FLAG = 0;
	private static final byte PUNC_FLAG = 1;
	private static final byte DONE_FLAG = 2;

	private final IPeerCommunicator peerCommunicator;

	private final Map<PeerID, ServerSocket> serverSocketMap = Maps.newConcurrentMap();
	private final Map<PeerID, SocketDataChannel> clientSocketMap = Maps.newConcurrentMap();
	private final List<PeerID> openCallers = Lists.newArrayList();
	private final Map<PeerID, List<byte[]>> waitingBuffer = Maps.newConcurrentMap();
	private final List<byte[]> globalBuffer = Lists.newArrayList();

	private final Collection<PeerID> toRemoveList = Lists.newArrayList();
	private Object semaphore = new Object();

	private RepeatingMessageSend portMessageRepeater;
	
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

					LOG.debug("Opened (unaccepted) server socket for {} on port {}.", senderPeer, serverSocket.getLocalPort());
					sendPortMessage(serverSocket.getLocalPort(), senderPeer);

					Socket clientSocket = serverSocket.accept();
					LOG.debug("Server socket on port {} was accepted from peer {}", serverSocket.getLocalPort(), senderPeer);
					
					SocketDataChannel channel = new SocketDataChannel(clientSocket);
					
					clientSocketMap.put(senderPeer, channel);
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
		LOG.debug("Startet to send port-messages for port " + msg.getPort() + " to {}", senderPeer);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PortAckMessage) {
			PortAckMessage portAckMessage = (PortAckMessage) message;
			synchronized (semaphore) {
				if (portMessageRepeater != null) {
					if (portAckMessage.getId() == getId() && portMessageRepeater != null) {
						LOG.debug("Received portAckMessage for port " + portAckMessage.getPort() + "  from {}", senderPeer);
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
			clientSocketMap.get(pid).close();
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
			List<PeerID> openCallerCopy = new ArrayList<PeerID>(openCallers);
			toRemoveList.clear();
			for (PeerID pid : openCallerCopy) {
	
				if (clientSocketMap.containsKey(pid)) {
	
					try {
						SocketDataChannel cs = clientSocketMap.get(pid);
						
						if (!globalBuffer.isEmpty()) {
							for (byte[] bufferedData : globalBuffer) {
								cs.write(bufferedData);
							}
							globalBuffer.clear();
						}
	
						if (waitingBuffer.containsKey(pid)) {
							for (byte[] bufferedData : waitingBuffer.remove(pid)) {
								cs.write(bufferedData);
							}
						}
						cs.write(rawData);
						
					} catch (IOException e) {
						LOG.error("Could not send data", e.getMessage());
						toRemoveList.add(pid);
					}
				} else {
					if( waitingBuffer.containsKey(pid)) {
						waitingBuffer.get(pid).add(rawData);
					} else {
						LOG.warn("Wanted to send a message to non-opened peer");
					}
				}
			}
	
			if (!toRemoveList.isEmpty()) {
				for (PeerID pid : toRemoveList) {
					SocketDataChannel socket = clientSocketMap.remove(pid);
					if (socket != null) {
						socket.close();
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
	
	@Override
	public void flushBuffers() {
			for (PeerID pid : openCallers) {
	
				if (clientSocketMap.containsKey(pid)) {
	
					try {
						SocketDataChannel cs = clientSocketMap.get(pid);
						
						if (!globalBuffer.isEmpty()) {
							for (byte[] bufferedData : globalBuffer) {
								cs.write(bufferedData);
							}
							globalBuffer.clear();
						}
	
						if (waitingBuffer.containsKey(pid)) {
							for (byte[] bufferedData : waitingBuffer.remove(pid)) {
								cs.write(bufferedData);
							}
						}
						
					} catch (IOException e) {
						LOG.error("Could not send data", e);
						toRemoveList.add(pid);
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
