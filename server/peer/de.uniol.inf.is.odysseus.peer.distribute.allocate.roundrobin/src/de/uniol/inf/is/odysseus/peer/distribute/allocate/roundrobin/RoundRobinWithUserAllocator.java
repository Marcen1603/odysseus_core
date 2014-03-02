package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import net.jxta.peer.PeerID;

// TODO javaDoc
public class RoundRobinWithUserAllocator extends AbstractRoundRobinAllocator {
	
	private static IP2PDictionary p2pDictionary;
	
	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		
		RoundRobinWithUserAllocator.p2pDictionary = serv;
		
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
	
		if(serv.equals(RoundRobinWithUserAllocator.p2pDictionary))
			p2pDictionary = null;
		
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		RoundRobinWithUserAllocator.p2pNetworkManager = serv;
		
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		if(serv.equals(RoundRobinWithUserAllocator.p2pNetworkManager))
			p2pNetworkManager = null;
		
	}
	
	private static Map<String, PeerID> determinePeerNames() {
		
		ImmutableList<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();

		Map<String, PeerID> peerNameMap = Maps.newHashMap();
		
		for(PeerID remotePeerID : remotePeerIDs ) {
			
			Optional<String> optPeerName = p2pDictionary.getRemotePeerName(remotePeerID);
			if(optPeerName.isPresent())
				
				peerNameMap.put(optPeerName.get(), remotePeerID);
			
		}
		
		return peerNameMap;
		
	}
	
	private static Optional<String> determineDestinationNameFromUser(ILogicalQueryPart part) {
		
		Collection<ILogicalOperator> operators = part.getOperators();		
		Map<String, Integer> countMap = Maps.newHashMap();
		
		for(ILogicalOperator operator : operators ) {
			
			String destinationOfOperator = operator.getDestinationName();
			
			if(Strings.isNullOrEmpty(destinationOfOperator))
				continue;
			else if(countMap.containsKey(destinationOfOperator))
				countMap.put(destinationOfOperator, countMap.get(destinationOfOperator) + 1);
			else countMap.put(destinationOfOperator, 1);
		}
		
		if(countMap.isEmpty())
			return Optional.absent();
		
		Optional<String> mostName = Optional.absent();
		int mostCount = Integer.MIN_VALUE;
		
		for(String destinationName : countMap.keySet()) {
			
			int count = countMap.get(destinationName);
			
			if(count > mostCount ) {
				
				mostName = Optional.of(destinationName);
				mostCount = count;
				
			}
			
		}
		
		return mostName;
	}

	@Override
	public String getName() {
		
		return "roundrobinwithuser";
		
	}
	
	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID) {
		
		return Lists.newArrayList(knownRemotePeers);
		
	}


	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers,
			PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		Collection<ILogicalQueryPart> notAllocatedParts = Lists.newArrayList();
		
		Map<String, PeerID> peerNameMap = RoundRobinWithUserAllocator.determinePeerNames();
		peerNameMap.put(p2pNetworkManager.getLocalPeerName(), localPeerID);
		peerNameMap.put("local", localPeerID);
		
		for(ILogicalQueryPart part : queryParts) {
			
			Optional<String> destinationName = RoundRobinWithUserAllocator.determineDestinationNameFromUser(part);
			
			if(destinationName.isPresent() && peerNameMap.containsKey(destinationName.get()))
				allocationMap.put(part, peerNameMap.get(destinationName.get()));
			else notAllocatedParts.add(part);
		}
		
		allocationMap.putAll(super.allocate(notAllocatedParts, query, knownRemotePeers, localPeerID, config, allocatorParameters));
		return allocationMap;
		
	}

}