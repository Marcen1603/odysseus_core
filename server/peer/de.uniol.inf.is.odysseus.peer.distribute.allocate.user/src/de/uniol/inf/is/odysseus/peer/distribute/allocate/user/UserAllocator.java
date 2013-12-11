package de.uniol.inf.is.odysseus.peer.distribute.allocate.user;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.user.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.user.service.P2PNetworkManagerService;

public class UserAllocator implements IQueryPartAllocator {

	private static final Random random = new Random(0);
	
	@Override
	public String getName() {
		return "user";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		
		Map<String, PeerID> peerNameMap = determinePeerNames();
		peerNameMap.put(P2PNetworkManagerService.get().getLocalPeerName(), localPeerID);
		peerNameMap.put("local", localPeerID);
		
		for( ILogicalQueryPart part : queryParts ) {
			String destinationName = determineMostMentionedDestinationName(part);
			
			if( peerNameMap.containsKey(destinationName)) {
				allocationMap.put(part, peerNameMap.get(destinationName));
			} else {
				PeerID peerID = chooseRandomPeerID( peerNameMap.values(), localPeerID);
				peerNameMap.put(destinationName, peerID);
				
				allocationMap.put(part, peerID);
			}
		}
		
		return allocationMap;
	}

	private static Map<String, PeerID> determinePeerNames() {
		ImmutableList<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();

		Map<String, PeerID> peerNameMap = Maps.newHashMap();
		for( PeerID remotePeerID : remotePeerIDs ) {
			Optional<String> optPeerName = P2PDictionaryService.get().getRemotePeerName(remotePeerID);
			if( optPeerName.isPresent() ) {
				peerNameMap.put(optPeerName.get(), remotePeerID);
			}
		}
		
		return peerNameMap;
	}

	private static String determineMostMentionedDestinationName(ILogicalQueryPart part) {
		ImmutableCollection<ILogicalOperator> operators = part.getOperators();
		
		Map<String, Integer> countMap = Maps.newHashMap();
		for( ILogicalOperator operator : operators ) {
			String destinationOfOperator = operator.getDestinationName();
			
			if( !Strings.isNullOrEmpty(destinationOfOperator)) {
				if(countMap.containsKey(destinationOfOperator)) {
					countMap.put( destinationOfOperator, countMap.get(destinationOfOperator) + 1);
				} else {
					countMap.put(destinationOfOperator, 1);
				}
			}
		}
		
		if( countMap.isEmpty() ) {
			return "local";
		}
		
		String mostName = "local";
		int mostCount = Integer.MIN_VALUE;
		
		for( String destinationName : countMap.keySet() ) {
			int count = countMap.get(destinationName);
			if(count > mostCount ) {
				mostName = destinationName;
				mostCount = count;
			}
		}
		
		return mostName;
	}

	private static PeerID chooseRandomPeerID(Collection<PeerID> values, PeerID localPeerID) {
		PeerID current = null;
		
		do {
			int choice = random.nextInt(values.size());
			Iterator<PeerID> peerIDIterator = values.iterator();
			for( int i = 0; i < choice + 1; i++ ) {
				current = peerIDIterator.next();
			}
		} while(current == null || current.equals(localPeerID));
		
		return current;
	}

}
