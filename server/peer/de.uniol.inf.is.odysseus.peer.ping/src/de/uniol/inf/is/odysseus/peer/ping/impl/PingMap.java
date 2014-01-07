package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class PingMap extends P2PDictionaryAdapter implements IPingMap  {

	private static final Logger LOG = LoggerFactory.getLogger(PingMap.class);
	
	private static final Random RAND = new Random();
	
	private static PingMap instance;
	private static IP2PDictionary dictionary;
	private static IP2PNetworkManager networkManager;
	
	private final Map<PeerID, PingMapNode> nodes = Maps.newHashMap();
	
	private Vector3D localPosition = new Vector3D();
	private double timestep = 1.0;
	
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
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (networkManager == serv) {
			networkManager = null;
		}
	}
	
	public void activate() {
		dictionary.addListener(this);
		
		LOG.debug("PingMap activated");
		
		instance = this;
	}
	
	public void deactivate() {
		dictionary.removeListener(this);
		
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
	
	@Override
	public Vector3D getLocalPosition() {
		return localPosition;
	}

	@Override
	public ImmutableCollection<PeerID> getPeerIDs() {
		return ImmutableSet.copyOf(nodes.keySet());
	}
	
	public void update( PeerID peerID, Vector3D position, long latency ) {
		Preconditions.checkNotNull(peerID, "PeerID to set latency must not be null!");
		Preconditions.checkNotNull(position, "New position for update must not be null!");
		Preconditions.checkArgument(latency >= 0, "Latency to set in ping map must be positive!");
		
		PingMapNode node = nodes.get(peerID);
		if( node == null ) {
			node = new PingMapNode(peerID);
			nodes.put(peerID, node);
		}
		node.setPosition(position);
		
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Updateing position to {}, latency={} of peer {}", new Object[]{position, latency, peerID});
		}
		
		Vector3D direction = position.subtract(localPosition);
		
		if( Math.abs(direction.getX()) < 0.00000001 && Math.abs(direction.getY()) < 0.00000001 && Math.abs(direction.getZ()) < 0.00000001) {
//			direction = new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0);
			direction = new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, 0.0);
		}
		
		double dirLength = getLength(direction);
		direction = direction.normalize();
		
		double distToPoint = dirLength - latency;
		
		Vector3D displacement = direction.scalarMultiply(distToPoint);
		displacement = displacement.scalarMultiply(timestep);
		
		timestep = Math.max( 0.05, timestep - 0.025);
		
		localPosition = localPosition.add(displacement);
		
		LOG.debug("Local position is now {}", localPosition);
	}

	private static double getLength( Vector3D v ) {
		return Math.sqrt( (v.getX() * v.getX() ) + (v.getY() * v.getY()) + (v.getZ() * v.getZ()));
	}
	
	@Override
	public Optional<Double> getPing(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get ping must not be null!");
		
		if( isLocalPeerID(peerID)) {
			return Optional.of(0.0);
		}
		
		PingMapNode node = nodes.get(peerID);
		if( node == null ) {
			return Optional.absent();
		}
		
		Vector3D direction = node.getPosition().subtract(localPosition);
		double latency = getLength(direction);
		
		LOG.debug("Latency is {} of peer {}", latency, peerID);
		
		return Optional.of(latency);
	}

	private boolean isLocalPeerID(PeerID peerID) {
		return networkManager.getLocalPeerID().equals(peerID);
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
		Preconditions.checkNotNull(start, "Starting peerID must not be null!");
		Preconditions.checkNotNull(end, "Ending peerID must not be null!");
		
		if( start.equals(end)) {
			return Optional.of(0.0);
		}
		
		PingMapNode startNode = nodes.get(start);
		PingMapNode endNode = nodes.get(end);
		
		if( startNode == null || endNode == null ) {
			return Optional.absent();
		}
		
		Vector3D distance = startNode.getPosition().subtract(endNode.getPosition());
		double latency = getLength(distance);
	
		return Optional.of(latency);
	}

}
