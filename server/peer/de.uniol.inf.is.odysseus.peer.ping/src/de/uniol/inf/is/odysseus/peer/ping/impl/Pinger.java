package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.nio.ByteBuffer;
import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.ping.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.ping.service.PeerCommunicatorService;

public class Pinger extends RepeatingJobThread implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);

	private static final int PING_INTERVAL = 5000;

	private static final byte PING_FLAG_BYTE = 78;
	private static final byte PONG_FLAG_BYTE = 79;

	public Pinger() {
		super(PING_INTERVAL, "Ping Thread");
	}

	@Override
	public void beforeJob() {
		PeerCommunicatorService.waitFor();
		P2PDictionaryService.waitFor();

		PeerCommunicatorService.get().addListener(this);
	}

	@Override
	public void doJob() {
		Collection<PeerID> remotePeers = P2PDictionaryService.get().getRemotePeerIDs();

		byte[] message = createPingMessage();
		IPeerCommunicator communicator = PeerCommunicatorService.get();
		try {
			for (PeerID remotePeer : remotePeers) {
				if (communicator.isConnected(remotePeer)) {
					communicator.send(remotePeer, message);
				}
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send ping message", e);
		}
	}

	private static byte[] createPingMessage() {
		byte[] longArray = ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array();

		byte[] message = new byte[9];
		message[0] = PING_FLAG_BYTE;
		System.arraycopy(longArray, 0, message, 1, longArray.length);
		return message;
	}
	
	private static byte[] createPongMessage(byte[] pingMessage) {
		ByteBuffer buffer = ByteBuffer.wrap(pingMessage);
		buffer.get(); // PING_FLAG_BYTE
		
		byte[] longArray = ByteBuffer.allocate(8).putLong(buffer.getLong()).array();
		byte[] doubleXArray = ByteBuffer.allocate(8).putDouble(PingMap.getInstance().getLocalX()).array();
		byte[] doubleYArray = ByteBuffer.allocate(8).putDouble(PingMap.getInstance().getLocalY()).array();

		byte[] message = new byte[25];
		message[0] = PONG_FLAG_BYTE;
		System.arraycopy(longArray, 0, message, 1, longArray.length);
		System.arraycopy(doubleXArray, 0, message, 9, doubleXArray.length);
		System.arraycopy(doubleYArray, 0, message, 17, doubleYArray.length);

		return message;
	}

	@Override
	public void afterJob() {
		PeerCommunicatorService.get().removeListener(this);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, byte[] message) {
		if (message[0] == PING_FLAG_BYTE) {
			byte[] pongMessage = createPongMessage(message);
			try {
				if (communicator.isConnected(senderPeer)) {
					communicator.send(senderPeer, pongMessage);
				}
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send pong-message", e);
			}
		} else if (message[0] == PONG_FLAG_BYTE) {
			ByteBuffer buffer = ByteBuffer.wrap(message);
			buffer.get();

			long latency = System.currentTimeMillis() - buffer.getLong();
			double remoteX = buffer.getDouble();
			double remoteY = buffer.getDouble();
			
			PingMap.getInstance().update(senderPeer, remoteX, remoteY, latency);
		}
	}
}
