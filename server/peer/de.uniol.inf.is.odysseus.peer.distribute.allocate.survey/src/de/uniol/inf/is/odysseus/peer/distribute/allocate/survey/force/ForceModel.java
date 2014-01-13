package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryPartGraphNode;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public class ForceModel {

	private static final Logger LOG = LoggerFactory.getLogger(ForceModel.class);
	private static final Random RAND = new Random();

	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	
	private ForceNode localForceNode;
	private Collection<ForceNode> forceNodes;
	private ForceNode localNormalizedForceNode;
	private Collection<ForceNode> normalizedForceNodes;

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
	
	public ForceModel() {
		// used for OSGi-DS
	}
	
	public ForceModel(Map<ILogicalQueryPart, Bid> bestBids, QueryPartGraph partGraph) {
		localForceNode = createLocalForceNode();

		forceNodes = createForceNodes(bestBids);
		forceNodes.add(localForceNode);

		// normal --> normalized
		Map<ForceNode, ForceNode> normalizedForceNodeMap = normalizeForceNodes(forceNodes);
		normalizedForceNodes = Lists.newArrayList(normalizedForceNodeMap.values());
		localNormalizedForceNode = normalizedForceNodeMap.get(localForceNode);

		LOG.debug("After normalizing");
		printForceNodes(normalizedForceNodes);
		
		createForces(normalizedForceNodes, localNormalizedForceNode, partGraph);
		LOG.debug("Attached forces");
		printForceNodes(normalizedForceNodes);
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
				if(graphNode.getPreviousNodes().isEmpty() ) {
					ForceNode newFixedForceNode = new ForceNode(forceNode.getPosition());
					forceNodes.add(newFixedForceNode);
					new Force(forceNode, newFixedForceNode, 1);
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

	private static Collection<ForceNode> createForceNodes(Map<ILogicalQueryPart, Bid> bestBids) {
		Collection<ForceNode> forceNodes = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : bestBids.keySet() ) {
			Optional<IPingMapNode> optPingMapNode = pingMap.getNode(bestBids.get(queryPart).getBidderPeerID());
			
			Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
			nodePosition = new Vector3D( nodePosition.getX(), nodePosition.getY(), bestBids.get(queryPart).getValue());
			
			forceNodes.add(new ForceNode(nodePosition, queryPart));
		}
		
		return forceNodes;
	}
	
	private static Map<ForceNode, ForceNode> normalizeForceNodes(Collection<ForceNode> forceNodes) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		for( ForceNode forceNode : forceNodes ) {
			Vector3D position = forceNode.getPosition();
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
		
		Map<ForceNode,ForceNode> normalizedNodes = Maps.newHashMap();
		
		for( ForceNode forceNode : forceNodes ) {
			Vector3D newPos = new Vector3D( distX > 0 ? (forceNode.getPosition().getX() - minX) / distX : 1, 
					distY > 0 ? (forceNode.getPosition().getY() - minY) / distY : 1,
					distZ > 0 ? (forceNode.getPosition().getZ() - minZ) / distZ : 1);
			
			ForceNode normalizedNode = null;
			if( forceNode.isFixed() ) {
				normalizedNode = new ForceNode(newPos);
			} else {
				normalizedNode = new ForceNode(newPos, forceNode.getQueryPart());
			}
			
			normalizedNodes.put(forceNode, normalizedNode);
		}
		
		return normalizedNodes;
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
	
	private static ForceNode determineForceNode(ILogicalQueryPart nextQueryPart, Collection<ForceNode> normalizedForceNodes) {
		for( ForceNode forceNode : normalizedForceNodes ) {
			if( !forceNode.isFixed() && nextQueryPart.equals(forceNode.getQueryPart())) {
				return forceNode;
			}
		}
		throw new RuntimeException("Could not get forcenode for querypart " + nextQueryPart);
	}

	public void run() {
		
	}

	public Map<ILogicalQueryPart, PeerID> getResult() {
		return null;
	}
}
