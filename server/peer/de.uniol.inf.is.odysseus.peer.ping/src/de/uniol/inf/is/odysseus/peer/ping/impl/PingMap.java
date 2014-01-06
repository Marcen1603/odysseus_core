package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.peer.ping.service.P2PDictionaryService;

public class PingMap extends P2PDictionaryAdapter implements IPingMap  {

	private static final Logger LOG = LoggerFactory.getLogger(PingMap.class);
	
	private static final Random RAND = new Random();
	
	private static PingMap instance;
	
	private final Map<PeerID, PingMapNode> nodes = Maps.newHashMap();
	private final Pinger pinger = new Pinger();
	
	private double localX = 0.0;
	private double localY = 0.0;
	private double timestep = 1.0;
	
	private IP2PDictionary dictionary;
	
	public void activate() {
		dictionary = P2PDictionaryService.waitFor();
		dictionary.addListener(this);
		
		pinger.start();
		
		LOG.debug("PingMap activated");
		
		instance = this;
	}
	
	public void deactivate() {
		dictionary.removeListener(this);
		dictionary = null;
		
		pinger.stopRunning();
		
		LOG.debug("PingMap deactivated");
		
		instance = null;
	}
	
	public static PingMap getInstance() {
		return instance;
	}
	
	public static boolean isActivated() {
		return getInstance() != null;
	}
	
	@Override
	public Optional<IPingMapNode> getNode(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID for pingMap must not be null!");
		
		return Optional.fromNullable((IPingMapNode)nodes.get(peerID));
	}
	
	public double getLocalX() {
		return localX;
	}
	
	public double getLocalY() {
		return localY;
	}

	@Override
	public ImmutableCollection<PeerID> getPeerIDs() {
		return ImmutableSet.copyOf(nodes.keySet());
	}
	
	public void update( PeerID peerID, double x, double y, long latency ) {
		Preconditions.checkNotNull(peerID, "PeerID to set latency must not be null!");
		Preconditions.checkArgument(latency >= 0, "Latency to set in ping map must be positive!");
		
		PingMapNode node = nodes.get(peerID);
		if( node == null ) {
			node = new PingMapNode(peerID);
			nodes.put(peerID, node);
		}
		node.setX(x);
		node.setY(y);
		
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Updateing position to x={}, y={}, latency={} of peer {}", new Object[]{x, y, latency, peerID});
		}
		
		double dirX = x - localX;
		double dirY = y - localY;
		if( Math.abs(dirX) < 0.00000001 && Math.abs(dirY) < 0.00000001) {
			dirX = RAND.nextDouble() * 1000.0;
			dirY = RAND.nextDouble() * 1000.0;
		}
		
		double dirLength = Math.sqrt( (dirX * dirX) + (dirY * dirY));
		
		dirX /= dirLength;
		dirY /= dirLength;
		
		double distToPoint = dirLength - latency;
		
		double displacementX = dirX * distToPoint;
		double displacementY = dirY * distToPoint;
		
		displacementX *= timestep;
		displacementY *= timestep;
		
		timestep = Math.max( 0.05, timestep - 0.025);
		
		localX = localX + displacementX;
		localY = localY + displacementY;
		
		LOG.debug("Local position is now x={}, y={}", localX, localY);
	}

	@Override
	public Optional<Double> getPing(PeerID peerID) {
		PingMapNode node = nodes.get(peerID);
		if( node == null ) {
			return Optional.absent();
		}
		
		double distX = node.getX() - localX;
		double distY = node.getY() - localY;
		double latency = Math.sqrt((distX * distX) + (distY * distY));
		
		LOG.debug("Latency is {} of peer {}", latency, peerID);
		
		return Optional.of(latency);
	}
	
	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		if( !nodes.containsKey(id)) {
			nodes.put(id, new PingMapNode(id));		
		}
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		nodes.remove(id);
	}

	@Override
	public Optional<Double> getRemotePing(PeerID start, PeerID end) {
		PingMapNode startNode = nodes.get(start);
		PingMapNode endNode = nodes.get(end);
		
		if( startNode == null || endNode == null ) {
			return Optional.absent();
		}
		
		double distX = startNode.getX() - endNode.getX();
		double distY = startNode.getY() - endNode.getY();
		
		double latency = Math.sqrt((distX * distX) + (distY * distY));
	
		return Optional.of(latency);
	}

}
