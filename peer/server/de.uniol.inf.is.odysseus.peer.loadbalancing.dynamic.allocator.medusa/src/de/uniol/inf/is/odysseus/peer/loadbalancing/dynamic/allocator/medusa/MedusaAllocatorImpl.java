package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.allocator.medusa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;

public class MedusaAllocatorImpl implements ILoadBalancingAllocator {

	private static final String ALLOCATOR_NAME = "Medusa";
	

	/**
	 * The logger instance for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MedusaAllocatorImpl.class);

	
	@Override
	public String getName() {
		return ALLOCATOR_NAME;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			Collection<PeerID> knownRemotePeers, PeerID localPeerID)
			throws QueryPartAllocationException {
		
		Map<ILogicalQueryPart,PeerID> resultMap = new HashMap<ILogicalQueryPart,PeerID>();
		
		for(ILogicalQueryPart part : queryParts) {
			resultMap.put(part, ContractRegistry.getCheapestOffer());
		}
		return resultMap;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(
			Map<ILogicalQueryPart, PeerID> previousAllocationMap,
			Collection<PeerID> faultPeers, Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException {
		//TODO Not implemented.
		return null;
	}

}
