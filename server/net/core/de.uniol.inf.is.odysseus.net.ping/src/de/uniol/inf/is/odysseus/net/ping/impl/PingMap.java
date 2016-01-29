package de.uniol.inf.is.odysseus.net.ping.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.ping.IPingMap;
import de.uniol.inf.is.odysseus.net.ping.IPingMapListener;
import de.uniol.inf.is.odysseus.net.ping.IPingMapNode;

public class PingMap implements IPingMap  {

	private static final Logger LOG = LoggerFactory.getLogger(PingMap.class);
	
	private static final Random RAND = new Random();
	
	private static PingMap instance;
	
	private final Map<IOdysseusNode, PingMapNode> pingNodeMap = Maps.newConcurrentMap();
	private final Map<IOdysseusNode, NumberAverager> ownPings = Maps.newConcurrentMap();
	private final Collection<IPingMapListener> listeners = Lists.newArrayList();
	
	private Vector3D localPosition = new Vector3D(0,0,0);
	private double timestep = 1.0;
	
	public void activate() {
		LOG.debug("PingMap activated");
		
		instance = this;
	}
	
	public void deactivate() {
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
	public Optional<IPingMapNode> getPingNode(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node for pingMap must not be null!");
		
		if( node.isLocal() ) {
			PingMapNode localPingMapNode = new PingMapNode(node);
			localPingMapNode.setPosition(getLocalPosition());
			return Optional.<IPingMapNode>of(localPingMapNode);
		}
		return Optional.fromNullable((IPingMapNode)pingNodeMap.get(node));
	}
	
	@Override
	public Vector3D getLocalPosition() {
		return localPosition;
	}

	public void update( IOdysseusNode node, Vector3D position, long latency ) {
		Preconditions.checkNotNull(node, "node to set latency must not be null!");
		Preconditions.checkNotNull(position, "New position for update must not be null!");
		Preconditions.checkArgument(latency >= 0, "Latency to set in ping map must be positive!");
		
		addLatencyData(node, latency);
		latency = (long)ownPings.get(node).getAverage();
		
		PingMapNode pingNode = getPingMapNode(node);
		pingNode.setPosition(position);
		
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Updateing position to {}, latency={} of node {}", new Object[]{toString(position), latency, node});
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

	private void addLatencyData(IOdysseusNode node, long latency) {
		NumberAverager numberAverager = ownPings.get(node);
		if( numberAverager == null ) {
			numberAverager = new NumberAverager(1);
			ownPings.put(node, numberAverager);
		}
		numberAverager.addValue(latency);
	}

	private PingMapNode getPingMapNode(IOdysseusNode node) {
		PingMapNode pingNode = pingNodeMap.get(node);
		if( pingNode == null ) {
			pingNode = new PingMapNode(node);
			pingNodeMap.put(node, pingNode);
		}
		return pingNode;
	}
	
	@Override
	public void setPosition( IOdysseusNode node, Vector3D position ) {
		Preconditions.checkNotNull(node, "node must not be null!");
		Preconditions.checkNotNull(position, "Position must not be null!");
		Preconditions.checkArgument(!node.isLocal(), "Cannot set position of local node directly!");
		
		PingMapNode pingNode = getPingMapNode(node);
		pingNode.setPosition(position);
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
	public Optional<Double> getPing(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node to get ping must not be null!");
		
		if( node.isLocal()) {
			return Optional.of(0.0);
		}
		
		NumberAverager avg = ownPings.get(node);
		return avg != null ? Optional.of(avg.getAverage()) : Optional.<Double>absent();
	}

	@Override
	public Optional<Double> getRemotePing(IOdysseusNode startNode, IOdysseusNode endNode) {
		Preconditions.checkNotNull(startNode, "Starting node must not be null!");
		Preconditions.checkNotNull(endNode, "Ending node must not be null!");
		
		if( startNode.equals(endNode)) {
			return Optional.of(0.0);
		}
		
		PingMapNode startPingNode = pingNodeMap.get(startNode);
		PingMapNode endPingNode = pingNodeMap.get(endNode);
		
		if( startPingNode == null || endPingNode == null ) {
			return Optional.absent();
		}
		
		Vector3D distance = startPingNode.getPosition().subtract(endPingNode.getPosition());
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
	
	public void removePingNode( IOdysseusNode node ) {
		pingNodeMap.remove(node);
		ownPings.remove(node);
		
		firePingMapChangeEvent();
	}

	public void clearPingNodes() {
		pingNodeMap.clear();
		ownPings.clear();
		
		timestep = 1.0;
		
		firePingMapChangeEvent();
	}
	
	@Override
	public Collection<IPingMapNode> getPingNodes() {
		return Lists.newArrayList(pingNodeMap.values());
	}
	
	@Override
	public Collection<IOdysseusNode> getOdysseusNodes() {
		return Lists.newArrayList(pingNodeMap.keySet());
	}
}
