package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public abstract class AbstractLoadBalancer implements ILoadBalancer {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalancer.class);

	protected static class Usage {
		public long mem;
		public long maxMem;
		
		public double cpu;
		public double maxCpu;
		
		public int runningQueriesCount;
		public int stoppedQueriesCount;
	}

	@Override
	public Map<ILogicalQueryPart, List<PeerID>> balance(Map<PeerID, IResourceUsage> currentResourceUsageMap, Map<ILogicalQueryPart, IPhysicalCost> partCosts) {

		Map<PeerID, Usage> estimatedUsages = LoadBalancerHelper.transform(currentResourceUsageMap);

		Map<ILogicalQueryPart, List<PeerID>> result = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : partCosts.keySet()) {
			LOG.debug("Allocating query part {}", queryPart);
			
			long newMemCost = (long)partCosts.get(queryPart).getMemorySum();
			double newCpuCost = partCosts.get(queryPart).getCpuSum();
			Collection<PeerID> avoidingPeers = determineAvoidedPeers(queryPart, result);
			
			List<PeerID> sortedPeerIDs = sortPeerIDs(estimatedUsages, avoidingPeers, newMemCost, newCpuCost);
			
			if( sortedPeerIDs == null || sortedPeerIDs.isEmpty() ) {
				// protected method --> check return value!
				throw new RuntimeException("Sorted peerIDs for load balancing returned null or is empty!");
			}
			
			result.put(queryPart, sortedPeerIDs);
		}

		return result;
	}

	private static Collection<PeerID> determineAvoidedPeers(ILogicalQueryPart queryPart, Map<ILogicalQueryPart, List<PeerID>> balancedQueryParts) {
		Collection<PeerID> avoidedPeers = Lists.newArrayList();
		
		for( ILogicalQueryPart avoidedPart : queryPart.getAvoidingQueryParts() ) {
			PeerID avoidedPeerID = balancedQueryParts.get(avoidedPart).get(0);
			if( avoidedPeerID != null && !avoidedPeers.contains(avoidedPeerID)) {
				avoidedPeers.add(avoidedPeerID);
			}
		}
		
		return avoidedPeers;
	}
	
	protected abstract List<PeerID> sortPeerIDs(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost);
}
