package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.service.P2PDictionaryService;
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
	public Map<ILogicalQueryPart, PeerID> balance(Map<PeerID, IResourceUsage> currentResourceUsageMap, Map<ILogicalQueryPart, OperatorCost<?>> partCosts) {

		Map<PeerID, Usage> estimatedUsages = LoadBalancerHelper.transform(currentResourceUsageMap);

		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : partCosts.keySet()) {
			LOG.debug("Allocating query part {}", queryPart);
			
			long newMemCost = (long)partCosts.get(queryPart).getMemCost();
			double newCpuCost = partCosts.get(queryPart).getCpuCost();

			PeerID minPeerID = selectPeerID(estimatedUsages, newMemCost, newCpuCost);
			
			if( minPeerID == null ) {
				// protected method --> check return value!
				throw new RuntimeException("Selecting peerID for load balancing returned null!");
			}
			
			Usage minUsage = estimatedUsages.get(minPeerID);
			
			Optional<String> optPeerName = P2PDictionaryService.get().getRemotePeerName(minPeerID);
			String peerName = optPeerName.isPresent() ? optPeerName.get() : "<unknown>";
			if (LOG.isDebugEnabled()) {
				LOG.debug("Allocated to {} with mem={} and cpu={}", new Object[] { peerName, minUsage.mem, minUsage.cpu });
			}

			result.put(queryPart, minPeerID);

			minUsage.mem += newMemCost;
			minUsage.cpu += newCpuCost;
			minUsage.runningQueriesCount++;
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Set usage of {} to mem={} and cpu={}", new Object[] { peerName, minUsage.mem, minUsage.cpu });
			}
		}

		return result;
	}
	
	protected abstract PeerID selectPeerID(Map<PeerID, Usage> currentUsages, long newMemCost, double newCpuCost);
}
