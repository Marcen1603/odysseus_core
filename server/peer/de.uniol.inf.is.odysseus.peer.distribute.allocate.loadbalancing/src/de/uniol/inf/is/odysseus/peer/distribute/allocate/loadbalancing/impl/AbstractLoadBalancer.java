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
	private static final double THEORETICAL_CPU_LOAD_PER_STOPPED_QUERY = 0.001;

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

		Map<PeerID, Usage> estimatedUsages = transform(currentResourceUsageMap);
		if (LOG.isDebugEnabled()) {
			printPeerResourceUsageMap(estimatedUsages);
		}

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

	private static void printPeerResourceUsageMap(Map<PeerID, Usage> estimatedUsages) {
		for (PeerID peerID : estimatedUsages.keySet()) {
			Usage estimateUsage = estimatedUsages.get(peerID);
			Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(peerID);
			LOG.debug("ResourceUsage of {}: mem = {}, cpu = {}", new Object[] { peerName.isPresent() ? peerName.get() : "<unknown>", estimateUsage.mem, estimateUsage.cpu });
		}
	}

	private static Map<PeerID, Usage> transform(Map<PeerID, IResourceUsage> currentResourceUsageMap) {
		Map<PeerID, Usage> usageMap = Maps.newHashMap();
		for (PeerID peerID : currentResourceUsageMap.keySet()) {
			IResourceUsage resUsage = currentResourceUsageMap.get(peerID);

			Usage usage = new Usage();
			usage.mem = resUsage.getMemMaxBytes() - resUsage.getMemFreeBytes();
			usage.cpu = determineTheoreticalCPULoad(resUsage);
			usage.maxCpu = resUsage.getCpuMax();
			usage.maxMem = resUsage.getMemMaxBytes();
			usage.stoppedQueriesCount = resUsage.getRunningQueriesCount();
			usage.runningQueriesCount = resUsage.getStoppedQueriesCount();
			
			usageMap.put(peerID, usage);
		}
		
		return usageMap;
	}
	
	private static double determineTheoreticalCPULoad(IResourceUsage usage) {
		double theoLoad = ( usage.getCpuMax() - usage.getCpuFree() ) + (THEORETICAL_CPU_LOAD_PER_STOPPED_QUERY * usage.getStoppedQueriesCount());
		return theoLoad > usage.getCpuMax() ? usage.getCpuMax() : theoLoad;
	}
	
	protected abstract PeerID selectPeerID(Map<PeerID, Usage> currentUsages, long newMemCost, double newCpuCost);
}
