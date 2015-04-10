package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapListener;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class PingMap extends P2PDictionaryAdapter implements IPingMap  {

	private static final Logger LOG = LoggerFactory.getLogger(PingMap.class);
	
	private static final Random RAND = new Random();
	
	private static PingMap instance;
	private static IP2PDictionary p2pDictionary;
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager networkManager;
	
	private final Map<PeerID, PingMapNode> nodes = Maps.newHashMap();
	private final Map<PeerID, NumberAverager> ownPings = Maps.newConcurrentMap();
	private final Collection<IPingMapListener> listeners = Lists.newArrayList();
	
	private Vector3D localPosition = new Vector3D(0,0,0);
	private double timestep = 1.0;
	
	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
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
		p2pDictionary.addListener(this);
		
		LOG.debug("PingMap activated");
		
		instance = this;
	}
	
	public void deactivate() {
		p2pDictionary.removeListener(this);
		
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
		
		if( networkManager.getLocalPeerID().equals(peerID)) {
			PingMapNode localPingMapNode = new PingMapNode(peerID);
			localPingMapNode.setPosition(getLocalPosition());
			return Optional.<IPingMapNode>of(localPingMapNode);
		}
		return Optional.fromNullable((IPingMapNode)nodes.get(peerID));
	}
	
	@Override
	public Vector3D getLocalPosition() {
		return localPosition;
	}

	public void update( PeerID peerID, Vector3D position, long latency ) {
		Preconditions.checkNotNull(peerID, "PeerID to set latency must not be null!");
		Preconditions.checkNotNull(position, "New position for update must not be null!");
		Preconditions.checkArgument(latency >= 0, "Latency to set in ping map must be positive!");
		
		addLatencyData(peerID, latency);
		latency = (long)ownPings.get(peerID).getAverage();
		
		PingMapNode node = getPingMapNode(peerID);
		node.setPosition(position);
		
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Updateing position to {}, latency={} of peer {}", new Object[]{toString(position), latency, peerDictionary.getRemotePeerName(peerID)});
		}
		
		Vector3D direction = position.subtract(localPosition);
		
		if( Math.abs(direction.getX()) < 0.00000001 && Math.abs(direction.getY()) < 0.00000001 && Math.abs(direction.getZ()) < 0.00000001) {
			direction = new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0);
		}
		
		double dirLength = getLength(direction);
		direction = direction.normalize();
		
		double distToPoint = dirLength - latency;
		
		Vector3D displacement = direction.scalarMultiply(distToPoint);
		displacement = displacement.scalarMultiply(timestep);
		
		timestep = Math.max( 0.2, timestep - 0.05);
		
		localPosition = localPosition.add(displacement);
		
		
		firePingMapChangeEvent();
		
		LOG.debug("Local position is now {}", toString(localPosition));
	}

	private void addLatencyData(PeerID peerID, long latency) {
		NumberAverager numberAverager = ownPings.get(peerID);
		if( numberAverager == null ) {
			numberAverager = new NumberAverager(1);
			ownPings.put(peerID, numberAverager);
		}
		numberAverager.addValue(latency);
	}

	private PingMapNode getPingMapNode(PeerID peerID) {
		PingMapNode node = nodes.get(peerID);
		if( node == null ) {
			node = new PingMapNode(peerID);
			nodes.put(peerID, node);
		}
		return node;
	}
	
	@Override
	public void setPosition( PeerID peerID, Vector3D position ) {
		Preconditions.checkNotNull(peerID, "peerID must not be null!");
		Preconditions.checkNotNull(position, "Position must not be null!");
		Preconditions.checkArgument(!peerID.equals(networkManager.getLocalPeerID()), "Cannot set position of local peer directly!");
		
		PingMapNode node = getPingMapNode(peerID);
		node.setPosition(position);
	}

	private void firePingMapChangeEvent() {
		synchronized( listeners ) {
			for( IPingMapListener listener : listeners ) {
				try {
					listener.pingMapChanged();
				} catch( Throwable t ) {
					LOG.error("Exception in ping map listener", t);
				}
			}
		}
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
		
		NumberAverager avg = ownPings.get(peerID);
		return avg != null ? Optional.of(avg.getAverage()) : Optional.<Double>absent();
	}

	private boolean isLocalPeerID(PeerID peerID) {
		return networkManager.getLocalPeerID().equals(peerID);
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

	@Override
	public void addListener(IPingMapListener listener) {
		Preconditions.checkNotNull(listener, "Pingmap listener to add must not be null!");
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IPingMapListener listener) {
		synchronized(listeners ) {
			listeners.remove(listener);
		}
	}
	
	private static String toString(Vector3D v) {
		return v.getX() + "/" + v.getY() + "/" + v.getZ();
	}

}
