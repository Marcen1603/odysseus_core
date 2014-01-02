package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.service.PeerCommunicatorService;

public class Pinger extends RepeatingJobThread implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);

	private static final int PING_INTERVAL = 5000;

	private static final byte PING_FLAG_BYTE = 78;
	private static final byte PONG_FLAG_BYTE = 79;

	private final Map<PeerID, Long> pings = Maps.newHashMap();

	public Pinger() {
		super(PING_INTERVAL, "Ping Thread");
	}

	@Override
	public void beforeJob() {
		waitForPeerCommunicator();
		
		PeerCommunicatorService.get().addListener(this);
	}

	private static void waitForPeerCommunicator() {
		while( !PeerCommunicatorService.isBound() ) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void doJob() {
		Collection<PeerID> remotePeers = PeerResourceUsageManager.getInstance().getRemotePeers();

		byte[] message = createPingMessage();
		IPeerCommunicator communicator = PeerCommunicatorService.get();
		try {
			for (PeerID remotePeer : remotePeers) {
				if( communicator.isConnected(remotePeer)) {
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

	@Override
	public void afterJob() {
		PeerCommunicatorService.get().removeListener(this);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, byte[] message) {
		if( message[0] == PING_FLAG_BYTE ) {
			message[0] = PONG_FLAG_BYTE;
			try {
				if( communicator.isConnected(senderPeer)) {
					communicator.send(senderPeer, message);
				}
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send pong-message", e);
			}
		} else if (message[0] == PONG_FLAG_BYTE ) {
			ByteBuffer buffer = ByteBuffer.wrap(message);
			buffer.get();
			
			long ping = System.currentTimeMillis() - buffer.getLong();
			synchronized( pings ) {
				LOG.debug("Ping is {} of {}", ping, senderPeer);
				pings.put(senderPeer, ping);
			}
		}
	}
	
	public Optional<Long> getPing( PeerID peerID ) {
		synchronized(pings) {
			return Optional.fromNullable(pings.get(peerID));
		}
	}
	
	public ImmutableMap<PeerID, Long> getPingMap() {
		synchronized( pings ) {
			return ImmutableMap.copyOf(pings);
		}
	}
}
