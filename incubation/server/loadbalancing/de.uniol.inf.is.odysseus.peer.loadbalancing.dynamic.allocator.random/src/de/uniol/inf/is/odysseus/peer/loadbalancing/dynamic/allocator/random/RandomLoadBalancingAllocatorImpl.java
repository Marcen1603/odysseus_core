package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.allocator.random;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;

public class RandomLoadBalancingAllocatorImpl implements ILoadBalancingAllocator {

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			Collection<PeerID> knownRemotePeers, PeerID localPeerID)
			throws QueryPartAllocationException {
		
		Map<ILogicalQueryPart,PeerID> resultMap = new HashMap<ILogicalQueryPart,PeerID>();
		
		for(ILogicalQueryPart queryPart : queryParts) {
			if(knownRemotePeers.size()<1) {
				resultMap.put(queryPart, localPeerID);
				continue;
			}
			resultMap.put(queryPart, chooseRandom(knownRemotePeers));
			
		}
		return resultMap;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(
			Map<ILogicalQueryPart, PeerID> previousAllocationMap,
			Collection<PeerID> faultPeers, Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	private PeerID chooseRandom(Collection<PeerID> peerIDs) {
		Random rnd = new Random(System.currentTimeMillis());
		int indexOfResult = rnd.nextInt(peerIDs.size());
		return (PeerID)peerIDs.toArray()[indexOfResult];
	}

}
