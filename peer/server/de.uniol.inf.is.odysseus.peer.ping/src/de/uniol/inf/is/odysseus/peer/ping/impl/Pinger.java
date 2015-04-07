package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class Pinger extends RepeatingJobThread implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);
	private static final Random RAND = new Random();
	private static final int MIN_PEERS_TO_PING = 5;
	private static final double PEERS_TO_PING_PERCENTAGE = 0.70; 

	private static final int PING_INTERVAL = 10000;
	private static final int MAX_PONG_WAIT_MILLIS = 30000;

	private static IPeerDictionary dictionary;
	private static IPeerCommunicator peerCommunicator;
	private static PingMap pingMap;
	private static Pinger instance;

	private final Map<PeerID, Long> waitingPongMap = Maps.newHashMap();

	public Pinger() {
		super(PING_INTERVAL, "Ping Thread");
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		dictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
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
		pingMap = (PingMap) serv;
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

		if (peerCommunicator != null) {
			peerCommunicator.removeListener(this, PingMessage.class);
			peerCommunicator.removeListener(this, PongMessage.class);
		}

		synchronized (waitingPongMap) {
			waitingPongMap.clear();
		}

		stopRunning();
	}

	public static Pinger getInstance() {
		return instance;
	}

	@Override
	public void doJob() {
		Collection<PeerID> remotePeers = dictionary.getRemotePeerIDs();

		cleanWaitingPongMap();

		Collection<PeerID> selectedPeers = selectRandomPeers(remotePeers);

		try {
			if (!selectedPeers.isEmpty()) {
				IMessage pingMessage = new PingMessage();
				for (PeerID remotePeer : selectedPeers) {
					LOG.debug("Send ping-message to {}", dictionary.getRemotePeerName(remotePeer));

					peerCommunicator.send(remotePeer, pingMessage);
					synchronized (waitingPongMap) {
						waitingPongMap.put(remotePeer, System.currentTimeMillis());
					}
				}
			} else {
				if( LOG.isDebugEnabled() ) {
					synchronized (waitingPongMap) {
						LOG.debug("No peers for pinging currently available.");
						if( !waitingPongMap.isEmpty() ) {
							LOG.debug("Waiting for {} pong messages...", waitingPongMap.size());
						}
					}
				}
			}
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send ping message", e);
		}
	}

	private void cleanWaitingPongMap() {
		long timestamp = System.currentTimeMillis();
		synchronized (waitingPongMap) {
			for (PeerID pid : waitingPongMap.keySet().toArray(new PeerID[0])) {
				if (timestamp - waitingPongMap.get(pid) > MAX_PONG_WAIT_MILLIS) {
					waitingPongMap.remove(pid);
				}
			}
		}
	}

	private Collection<PeerID> selectRandomPeers(Collection<PeerID> remotePeers) {
		List<PeerID> availablePeers = Lists.newArrayList(remotePeers);
		synchronized (waitingPongMap) {
			availablePeers.removeAll(waitingPongMap.keySet());
		}
		
		int peersToPingCount = Math.max(MIN_PEERS_TO_PING, (int)(availablePeers.size() * PEERS_TO_PING_PERCENTAGE)); 
				
		if (availablePeers.size() <= peersToPingCount) {
			return Lists.newArrayList(availablePeers);
		}

		Collection<PeerID> selectedPeers = Lists.newArrayList();
		while (!availablePeers.isEmpty() && selectedPeers.size() < peersToPingCount) {
			int index = RAND.nextInt(availablePeers.size());

			selectedPeers.add(availablePeers.remove(index));
		}

		return selectedPeers;
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof PingMessage && PingMap.isActivated()) {
			LOG.debug("Got ping message from {}", dictionary.getRemotePeerName(senderPeer));
			PingMessage pingMessage = (PingMessage) message;

			IMessage pongMessage = new PongMessage(pingMessage, pingMap.getLocalPosition());
			try {
				communicator.send(senderPeer, pongMessage);
			} catch (PeerCommunicationException e) {
				LOG.debug("Could not send pong-message", e);
			}
		} else if (message instanceof PongMessage) {
			LOG.debug("Got pong message from {}", dictionary.getRemotePeerName(senderPeer));
			PongMessage pongMessage = (PongMessage) message;
			synchronized (waitingPongMap) {
				waitingPongMap.remove(senderPeer);
			}

			long latency = System.currentTimeMillis() - pongMessage.getTimestamp();
			LOG.debug("Latency to {} is now {} ms", dictionary.getRemotePeerName(senderPeer), latency);
			pingMap.update(senderPeer, pongMessage.getPosition(), latency);
		}
	}
}
