package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
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
		
		peerCommunicator.registerMessageType(PingMessage.class);
		peerCommunicator.registerMessageType(PongMessage.class);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			
			peerCommunicator.unregisterMessageType(PingMessage.class);
			peerCommunicator.unregisterMessageType(PongMessage.class);
			
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
		
		peerCommunicator.addListener(this, PingMessage.class);
		peerCommunicator.addListener(this, PongMessage.class);
		
		start();
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		
		if( peerCommunicator != null ) {
			peerCommunicator.removeListener(this, PingMessage.class);
			peerCommunicator.removeListener(this, PongMessage.class);
		}
		
		stopRunning();
	}

	public static Pinger getInstance() {
		return instance;
	}
	
	@Override
	public void doJob() {
		Collection<PeerID> remotePeers = dictionary.getRemotePeerIDs();
		Collection<PeerID> selectedPeers = selectRandomPeers(remotePeers);

		try {
			IMessage pingMessage = new PingMessage();
			for (PeerID remotePeer : selectedPeers) {
				if (peerCommunicator.isConnected(remotePeer)) {
					peerCommunicator.send(remotePeer, pingMessage);
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

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PingMessage && PingMap.isActivated()) {
			IMessage pongMessage = new PongMessage( (PingMessage)message, pingMap.getLocalPosition());
			try {
				if (communicator.isConnected(senderPeer)) {
					communicator.send(senderPeer, pongMessage);
				}
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send pong-message", e);
			}
		} else if (message instanceof PongMessage) {
			PongMessage pongMessage = (PongMessage)message;
			long latency = System.currentTimeMillis() - pongMessage.getTimestamp();
			pingMap.update(senderPeer, pongMessage.getPosition(), latency);
		}
	}
}
