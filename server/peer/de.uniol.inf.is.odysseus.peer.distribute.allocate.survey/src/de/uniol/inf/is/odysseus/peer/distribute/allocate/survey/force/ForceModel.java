package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

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

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryPartGraphNode;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class ForceModel {

	private static final int MAX_ITERATIONS = 10000;
	private static final Logger LOG = LoggerFactory.getLogger(ForceModel.class);
	private static final Random RAND = new Random();

	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	
	private final ForceNode localForceNode;
	private final Collection<ForceNode> forceNodes;
	private final Map<ILogicalQueryPart, Collection<Bid>> bids;

	// called by OSGi-DS
	public static void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
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
	
	public ForceModel() {
		// used for OSGi-DS
		localForceNode = null;
		forceNodes = Lists.newArrayList();
		bids = Maps.newHashMap();
	}
	
	public ForceModel(Map<ILogicalQueryPart, Collection<Bid>> bids, QueryPartGraph partGraph) {
		Preconditions.checkNotNull(bids, "Map of bid must not be null!");
		Preconditions.checkNotNull(partGraph, "Query Part graph must not be null!");
		
		this.bids = bids;
		
		localForceNode = createLocalForceNode();

		forceNodes = createForceNodes(bids);
		forceNodes.add(localForceNode);

		LOG.debug("Force Nodes");
		printForceNodes(forceNodes);
		
		createForces(forceNodes, localForceNode, partGraph);
		
		LOG.debug("Attached forces");
		printForceNodes(forceNodes);
	}

	private static void createForces(Collection<ForceNode> forceNodes, ForceNode localForceNode, QueryPartGraph partGraph) {
		for( ForceNode forceNode : forceNodes.toArray(new ForceNode[0]) ) {
			if( !forceNode.isFixed() ) {
				ILogicalQueryPart queryPart = forceNode.getQueryPart();
				
				QueryPartGraphNode graphNode = partGraph.getGraphNode(queryPart);
				Collection<QueryPartGraphNode> nextQueryPartNodes = graphNode.getNextNodes();
				if( !nextQueryPartNodes.isEmpty() ) {
					for( QueryPartGraphNode nextQueryPartNode : nextQueryPartNodes ) {
						ILogicalQueryPart nextQueryPart = nextQueryPartNode.getQueryPart();
						ForceNode nextForceNode = determineForceNode(nextQueryPart, forceNodes);
						new Force(forceNode, nextForceNode, 1); // TODO: Insert Data rate
					}
				} else {
					new Force( forceNode, localForceNode, 1 ); // TODO: Insert Data rate
				}
			}
		}
	}

	private static ForceNode createLocalForceNode() {
		Optional<IPingMapNode> optPingMapNode = pingMap.getNode(p2pNetworkManager.getLocalPeerID());
		Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
		
		return new ForceNode(nodePosition);
	}

	private static Vector3D createRandomVector3D() {
		return new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0);
	}

	private Collection<ForceNode> createForceNodes(Map<ILogicalQueryPart, Collection<Bid>> bids) {
		Collection<ForceNode> forceNodes = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : bids.keySet() ) {
			
			Collection<Bid> queryPartBids = bids.get(queryPart);
			Bid bestBid = determineBestBid(queryPartBids);
			Optional<IPingMapNode> optPingMapNode = pingMap.getNode(bestBid.getBidderPeerID());
			LOG.debug("Best bid for query part {} is from {}", queryPart, p2pDictionary.getRemotePeerName(bestBid.getBidderPeerID()));
			
			Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
			forceNodes.add(new ForceNode(nodePosition, queryPart, queryPartBids.size() == 1));
		}
		
		return forceNodes;
	}

	private static Bid determineBestBid(Collection<Bid> bids) {
		Bid bestBid = null;
		for (Bid bid : bids) {
			if (bestBid == null || bid.getValue() > bestBid.getValue()) {
				bestBid = bid;
			}
		}
		return bestBid;
	}
	
	private static void printForceNodes(Collection<ForceNode> nodes) {
		if( LOG.isDebugEnabled() ) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("[\n");
			for( ForceNode node : nodes ) {
				sb.append("\t").append(node).append("\n");
			}
			
			sb.append("]");
			LOG.debug(sb.toString());
		}
	}
	
	private static ForceNode determineForceNode(ILogicalQueryPart queryPart, Collection<ForceNode> forceNodes) {
		for( ForceNode forceNode : forceNodes ) {
			if( !forceNode.isFixed() && queryPart.equals(forceNode.getQueryPart())) {
				return forceNode;
			}
		}
		throw new RuntimeException("Could not get forcenode for querypart " + queryPart);
	}

	public void run() {
		for( int i = 0; i < MAX_ITERATIONS; i++ ) {
			for( ForceNode node : forceNodes ) {
				node.tick();
			}
		}
		
		LOG.debug("Finished model-Running");
		printForceNodes(forceNodes);
	}

	public Map<ILogicalQueryPart, PeerID> getResult() {
		Map<PeerID, Vector3D> positionMap = Maps.newHashMap();
		for( PeerID peerID : pingMap.getRemotePeerIDs() ) {
			positionMap.put(peerID, pingMap.getNode(peerID).get().getPosition());
		}
		positionMap.put(pingMap.getLocalPeerID(), pingMap.getLocalPosition());
		normalize(positionMap);
		
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		for( ILogicalQueryPart queryPart : bids.keySet() ) {
			
			Collection<Bid> bidsForQueryPart = bids.get(queryPart);
			if( bidsForQueryPart.size() == 1 ) {
				result.put(queryPart, bidsForQueryPart.iterator().next().getBidderPeerID());
			} else {
				ForceNode forceNodeOfQueryPart = determineForceNode(queryPart, forceNodes);
				PeerID bestPeer = determineNearestPeerWithBestBid( forceNodeOfQueryPart.getPosition(), positionMap, bidsForQueryPart);
				
				result.put(queryPart, bestPeer);
			}
			
		}
		
		return result;
	}

	private static void normalize(Map<PeerID, Vector3D> positionMap) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		for( Vector3D position : positionMap.values()) {
			if( position.getX() < minX ) {
				minX = position.getX();
			}
			if( position.getY() < minY ) {
				minY = position.getY();
			}
			if( position.getZ() < minZ ) {
				minZ = position.getZ();
			}
			
			if( position.getX() > maxX ) {
				maxX = position.getX();
			}
			if( position.getY() > maxY ) {
				maxY = position.getY();
			}
			if( position.getZ() > maxZ ) {
				maxZ = position.getZ();
			}
		}
		
		double distX = maxX - minX;
		double distY = maxY - minY;
		double distZ = maxZ - minZ;
		
		for( PeerID peerID : positionMap.keySet().toArray(new PeerID[0])) {
			Vector3D position = positionMap.get(peerID);
			Vector3D newPos = new Vector3D( distX > 0 ? (position.getX() - minX) / distX : 1, 
					distY > 0 ? (position.getY() - minY) / distY : 1,
					distZ > 0 ? (position.getZ() - minZ) / distZ : 1);
			
			positionMap.put(peerID, newPos);
		}
	}

	private static PeerID determineNearestPeerWithBestBid(Vector3D position, Map<PeerID, Vector3D> positionMap, Collection<Bid> bidsForQueryPart) {
		Map<PeerID, Bid> bidMap = createBidMap(bidsForQueryPart);
		
		PeerID bestPeer = null;
		double bestPeerDistanceToPerfect = Double.MAX_VALUE;
		for( PeerID peerID : bidMap.keySet() ) {
			double bidValueOfPeer = bidMap.get(peerID).getValue();
			Vector3D positionOfPeer = positionMap.get(peerID);

			double distX = Math.abs(position.getX() - positionOfPeer.getX());
			double distY = Math.abs(position.getY() - positionOfPeer.getY());
			double distZ = Math.abs(position.getZ() - positionOfPeer.getZ());
			double distB = 1 - bidValueOfPeer;
			
			double peerDistanceToPerfect = Math.sqrt( (distX * distX) + (distY * distY ) + (distZ * distZ) + (distB * distB));
			if( bestPeer == null || peerDistanceToPerfect < bestPeerDistanceToPerfect ) {
				bestPeer = peerID;
				bestPeerDistanceToPerfect = peerDistanceToPerfect;
			}
		}
		
		return bestPeer;
	}

	private static Map<PeerID, Bid> createBidMap(Collection<Bid> bids) {
		Map<PeerID, Bid> bidMap = Maps.newHashMap();
		for( Bid bid : bids ) {
			bidMap.put(bid.getBidderPeerID(), bid);
		}
		return bidMap;
	}
}
