package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class Pinger extends RepeatingJobThread implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);
	private static final Random RAND = new Random();
	private static final int MAX_PEERS_TO_PING = 5;

	private static final int PING_INTERVAL = 2000;

	private static final byte PING_FLAG_BYTE = 78;
	private static final byte PONG_FLAG_BYTE = 79;

	private static IP2PDictionary dictionary;
	private static IPeerCommunicator peerCommunicator;
	private static PingMap pingMap;
	private static Pinger instance;

	public Pinger() {
		super(PING_INTERVAL, "Ping Thread");
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		dictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (dictionary == serv) {
			dictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindPingMap(IPingMap serv) {
		pingMap = (PingMap)serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
		
		start();
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		
		stopRunning();
	}

	public static Pinger getInstance() {
		return instance;
	}
	
	@Override
	public void beforeJob() {
		peerCommunicator.addListener(this);
	}

	@Override
	public void doJob() {
		Collection<PeerID> remotePeers = dictionary.getRemotePeerIDs();
		Collection<PeerID> selectedPeers = selectRandomPeers(remotePeers);

		byte[] message = createPingMessage();
		try {
			for (PeerID remotePeer : selectedPeers) {
				if (peerCommunicator.isConnected(remotePeer)) {
					peerCommunicator.send(remotePeer, message);
				}
			}
		} catch (PeerCommunicationException e) {
			//LOG.error("Could not send ping message", e);
		}
	}

	private static Collection<PeerID> selectRandomPeers(Collection<PeerID> remotePeers) {
		if (remotePeers.size() <= MAX_PEERS_TO_PING) {
			return Lists.newArrayList(remotePeers);
		}

		Collection<PeerID> selectedPeers = Lists.newArrayList();
		while (selectedPeers.size() < MAX_PEERS_TO_PING) {
			int index = RAND.nextInt(remotePeers.size());
			Iterator<PeerID> iterator = remotePeers.iterator();
			PeerID lastIteratedPeer = null;
			for (int counter = 0; counter < index; counter++) {
				lastIteratedPeer = iterator.next();
			}

			if (!selectedPeers.contains(lastIteratedPeer)) {
				selectedPeers.add(lastIteratedPeer);
			}
		}
		
		return selectedPeers;
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
		byte[] doubleXArray = ByteBuffer.allocate(8).putDouble(pingMap.getLocalPosition().getX()).array();
		byte[] doubleYArray = ByteBuffer.allocate(8).putDouble(pingMap.getLocalPosition().getY()).array();
		byte[] doubleZArray = ByteBuffer.allocate(8).putDouble(pingMap.getLocalPosition().getZ()).array();

		byte[] message = new byte[33];
		message[0] = PONG_FLAG_BYTE;
		System.arraycopy(longArray, 0, message, 1, longArray.length);
		System.arraycopy(doubleXArray, 0, message, 9, doubleXArray.length);
		System.arraycopy(doubleYArray, 0, message, 17, doubleYArray.length);
		System.arraycopy(doubleZArray, 0, message, 25, doubleZArray.length);

		return message;
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
			double remoteZ = buffer.getDouble();
			pingMap.update(senderPeer, new Vector3D(remoteX, remoteY, remoteZ), latency);
		}
	}
}
