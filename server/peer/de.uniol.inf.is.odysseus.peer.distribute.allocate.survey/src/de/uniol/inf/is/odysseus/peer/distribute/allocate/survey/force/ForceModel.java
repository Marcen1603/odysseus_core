package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphConnection;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphNode;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class ForceModel {

	private static class ValuePeerPair implements Comparable<ValuePeerPair> {
		public final double value;
		public final PeerID peerID;
		
		public ValuePeerPair(double value, PeerID peerID) {
			Preconditions.checkNotNull(peerID, "PeerID must not be null!");

			this.value = value;
			this.peerID = peerID;
		}
		
		@Override
		public int compareTo(ValuePeerPair o) {
			return Double.compare(value, o.value);
		}
	}
	
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
	
	public ForceModel(Map<ILogicalQueryPart, Collection<Bid>> bids, QueryPartGraph partGraph, Map<ILogicalOperator, Double> operatorDataRateMap) {
		Preconditions.checkNotNull(bids, "Map of bid must not be null!");
		Preconditions.checkNotNull(partGraph, "Query Part graph must not be null!");
		
		this.bids = bids;
		
		localForceNode = createLocalForceNode();

		forceNodes = createForceNodes(bids, partGraph);
		forceNodes.add(localForceNode);

		LOG.debug("Force Nodes");
		printForceNodes(forceNodes);
		
		createForces(forceNodes, localForceNode, partGraph, operatorDataRateMap);
		
		LOG.debug("Attached forces");
		printForceNodes(forceNodes);
	}

	private static ForceNode createLocalForceNode() {
		Optional<IPingMapNode> optPingMapNode = pingMap.getNode(p2pNetworkManager.getLocalPeerID());
		Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
		
		return new ForceNode(nodePosition);
	}

	private static Vector3D createRandomVector3D() {
		return new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0);
	}

	private Collection<ForceNode> createForceNodes(Map<ILogicalQueryPart, Collection<Bid>> bids, QueryPartGraph partGraph) {
		Collection<ForceNode> forceNodes = Lists.newArrayList();
		
		if( LOG.isDebugEnabled() ) {
			for( PeerID peerID : pingMap.getRemotePeerIDs() ) {
				Vector3D position = pingMap.getNode(peerID).get().getPosition();
				LOG.debug("Current position of {}: {}", p2pDictionary.getRemotePeerName(peerID).get(), toString(position));
			}
			LOG.debug("Current position of {}: {}", p2pNetworkManager.getLocalPeerName(), toString(pingMap.getLocalPosition()));
		}
		
		for( ILogicalQueryPart queryPart : bids.keySet() ) {
			
			Collection<Bid> queryPartBids = bids.get(queryPart);
			Bid bestBid = determineBestBid(queryPartBids);
			Optional<IPingMapNode> optPingMapNode = pingMap.getNode(bestBid.getBidderPeerID());
			LOG.debug("Best bid for query part {} is from {}", queryPart, p2pDictionary.getRemotePeerName(bestBid.getBidderPeerID()).get());
			
			Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
			forceNodes.add(new ForceNode(nodePosition, queryPart, queryPartBids.size() == 1));	
		}		
		
		return forceNodes;
	}
	
	private static void createForces(Collection<ForceNode> forceNodes, ForceNode localForceNode, QueryPartGraph partGraph, Map<ILogicalOperator, Double> operatorDataRateMap) {
		for( ForceNode forceNode : forceNodes.toArray(new ForceNode[0]) ) {
			if( !forceNode.isFixed() ) {
				ILogicalQueryPart queryPart = forceNode.getQueryPart();
				
				QueryPartGraphNode graphNode = partGraph.getGraphNode(queryPart);
				Collection<QueryPartGraphConnection> nextQueryPartConnections = graphNode.getConnectionsAsStart();
				
				if( !nextQueryPartConnections.isEmpty() ) {
					for( QueryPartGraphConnection nextQueryPartConnection : nextQueryPartConnections ) {
						ILogicalOperator relativeSource = nextQueryPartConnection.getStartOperator();
						Double dataRate = operatorDataRateMap.get(relativeSource);
						
						ILogicalQueryPart nextQueryPart = nextQueryPartConnection.getEndNode().getQueryPart();
						
						ForceNode nextForceNode = determineForceNode(nextQueryPart, forceNodes);
						new Force(forceNode, nextForceNode, dataRate != null ? dataRate : 1); 
					}
					
				} else {
					Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
					ILogicalOperator firstRelativeSink = relativeSinks.iterator().next();
					Double dataRate = operatorDataRateMap.get(firstRelativeSink);
					
					new Force( forceNode, localForceNode, dataRate != null ? dataRate : 1);
				}
				
				if( graphNode.getConnectionsAsEnd().isEmpty() ) {
					Collection<ILogicalOperator> sources = LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(queryPart);
					for( ILogicalOperator source : sources ) {
						
						if( source instanceof JxtaReceiverAO ) {
							JxtaReceiverAO ao = (JxtaReceiverAO)source;
							Optional<SourceAdvertisement> optAdv = ao.getImportedSourceAdvertisement();
							// an imported source with clear providing peer?
							if( optAdv.isPresent() ) {
								PeerID providingPeerID = optAdv.get().getPeerID();
								Optional<IPingMapNode> optNode = pingMap.getNode(providingPeerID);
								if( optNode.isPresent() ) {
									ForceNode peerForceNode = new ForceNode(optNode.get().getPosition());
									forceNodes.add(peerForceNode);
									
									new Force( peerForceNode, forceNode, 10);
								} 
							}
						}
						
					}
				}
			}
		}
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
			if( queryPart.equals(forceNode.getQueryPart())) {
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
		LOG.debug("Evaluating force-results");
		
		Map<PeerID, Vector3D> positionMap = Maps.newHashMap();
		for( PeerID peerID : pingMap.getRemotePeerIDs() ) {
			Vector3D position = pingMap.getNode(peerID).get().getPosition();
			positionMap.put(peerID, position);
			LOG.debug("Current position of {}: {}", p2pDictionary.getRemotePeerName(peerID).get(), toString(position));
		}
		LOG.debug("Current position of {}: {}", p2pNetworkManager.getLocalPeerName(), toString(pingMap.getLocalPosition()));

		positionMap.put(pingMap.getLocalPeerID(), pingMap.getLocalPosition());
		
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		PositionNormalizer normalizer = new PositionNormalizer(positionMap.values());
		for( ILogicalQueryPart queryPart : bids.keySet() ) {
			LOG.debug("Determine best peer for queryPart {}", queryPart);
			
			Collection<Bid> bidsForQueryPart = bids.get(queryPart);
			if( bidsForQueryPart.size() == 1 ) {
				Bid oneAndOnlyBid = bidsForQueryPart.iterator().next();
				result.put(queryPart, oneAndOnlyBid.getBidderPeerID());
				
				LOG.debug("Got only one bid (value={}) from peer {}", oneAndOnlyBid.getValue(), p2pDictionary.getRemotePeerName(oneAndOnlyBid.getBidderPeerID()).get());
			} else {
				LOG.debug("Got {} bids for this query part", bidsForQueryPart.size());
				
				ForceNode forceNodeOfQueryPart = determineForceNode(queryPart, forceNodes);
				Collection<PeerID> avoidingPeers = determineAvoidingPeers(queryPart.getAvoidingQueryParts(), result);
				PeerID bestPeer = determineNearestPeerWithBestBid( forceNodeOfQueryPart.getPosition(), positionMap, normalizer, bidsForQueryPart, avoidingPeers);
				
				result.put(queryPart, bestPeer);
			}
		}
		
		return result;
	}

	private static Collection<PeerID> determineAvoidingPeers(ImmutableCollection<ILogicalQueryPart> avoidingQueryParts, Map<ILogicalQueryPart, PeerID> currentAllocationMap) {
		Collection<PeerID> avoidedPeers = Lists.newLinkedList();
		for( ILogicalQueryPart queryPart : avoidingQueryParts ) {
			PeerID avoidedPeer = currentAllocationMap.get(queryPart);
			if( avoidedPeer != null && !avoidedPeers.contains(avoidedPeer) ) {
				if(LOG.isDebugEnabled() ) {
					LOG.debug("One avoiding part is {} from {}", avoidingQueryParts, p2pDictionary.getRemotePeerName(avoidedPeer).get());
				}
				
				avoidedPeers.add(avoidedPeer);
			}
		}
		return avoidedPeers;
	}

	private static PeerID determineNearestPeerWithBestBid(Vector3D position, Map<PeerID, Vector3D> positionMap, PositionNormalizer normalizer, Collection<Bid> bidsForQueryPart, Collection<PeerID> avoidedPeers) {
		Map<PeerID, Bid> bidMap = createBidMap(bidsForQueryPart);

		List<ValuePeerPair> peerValues = Lists.newLinkedList();
		
		Vector3D normPosition = normalizer.normalize(position);
		
		if( LOG.isDebugEnabled() && !avoidedPeers.isEmpty()) {
			printAvoidingPeerList(avoidedPeers);
		}
		
		for( PeerID peerID : bidMap.keySet() ) {
			double bidValueOfPeer = bidMap.get(peerID).getValue();
			Vector3D normPositionOfPeer = normalizer.normalize(positionMap.get(peerID));

			double distX = Math.abs(normPosition.getX() - normPositionOfPeer.getX());
			double distY = Math.abs(normPosition.getY() - normPositionOfPeer.getY());
			double distZ = Math.abs(normPosition.getZ() - normPositionOfPeer.getZ());
			double distB = 1 - bidValueOfPeer;
			
			double peerDistanceToPerfect = Math.sqrt( (distX * distX) + (distY * distY ) + (distZ * distZ) + (distB * distB));
			peerValues.add( new ValuePeerPair(peerDistanceToPerfect, peerID));
			
			if( LOG.isDebugEnabled() ) {
				double latency = Math.sqrt( (distX * distX) + (distY * distY ) + (distZ * distZ));
				LOG.debug("Peer {} has bidValue of {} and due to latency {} a peerValue of {}", 
						new Object[] {p2pDictionary.getRemotePeerName(peerID).get(), bidValueOfPeer, latency, peerDistanceToPerfect});
			}
		}
		
		Collections.sort(peerValues);
		
		int index = 0;
		PeerID currentChoice = peerValues.get(0).peerID;
		while( avoidedPeers.contains(currentChoice) ) {
			index++;
			
			if( index == peerValues.size() ) {
				// checked all...
				currentChoice = peerValues.get(0).peerID;
				break;
			}
			
			currentChoice = peerValues.get(index).peerID;
		}
		
		LOG.debug("Best (non-avoided) peer is {}", p2pDictionary.getRemotePeerName(currentChoice).get());
		return currentChoice;
	}

	private static void printAvoidingPeerList(Collection<PeerID> avoidedPeers) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Avoiding peers: ");
		for( PeerID peerID : avoidedPeers ) {
			sb.append(p2pDictionary.getRemotePeerName(peerID).get()).append("  ");
		}
		
		LOG.debug(sb.toString());
	}
	
	private static String toString( Vector3D v ) {
		return v.getX() + "/" + v.getY() + "/" + v.getZ();
	}

	private static Map<PeerID, Bid> createBidMap(Collection<Bid> bids) {
		Map<PeerID, Bid> bidMap = Maps.newHashMap();
		for( Bid bid : bids ) {
			bidMap.put(bid.getBidderPeerID(), bid);
		}
		return bidMap;
	}
}
