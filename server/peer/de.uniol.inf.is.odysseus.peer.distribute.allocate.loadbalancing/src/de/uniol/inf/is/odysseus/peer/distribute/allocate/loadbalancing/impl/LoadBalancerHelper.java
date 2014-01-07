package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl.AbstractLoadBalancer.Usage;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

final class LoadBalancerHelper {

	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancerHelper.class);
	private static final double THEORETICAL_CPU_LOAD_PER_STOPPED_QUERY = 0.001;
	
	private LoadBalancerHelper() {
		
	}

	public static Map<PeerID, Usage> transform(Map<PeerID, IResourceUsage> currentResourceUsageMap) {
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
		
		if( LOG.isDebugEnabled() ) {
			printPeerResourceUsageMap(usageMap);
		}
		
		return usageMap;
	}
	
	private static double determineTheoreticalCPULoad(IResourceUsage usage) {
		double theoLoad = ( usage.getCpuMax() - usage.getCpuFree() ) + (THEORETICAL_CPU_LOAD_PER_STOPPED_QUERY * usage.getStoppedQueriesCount());
		return theoLoad > usage.getCpuMax() ? usage.getCpuMax() : theoLoad;
	}
	

	private static void printPeerResourceUsageMap(Map<PeerID, Usage> estimatedUsages) {
		for (PeerID peerID : estimatedUsages.keySet()) {
			Usage estimateUsage = estimatedUsages.get(peerID);
			LOG.debug("ResourceUsage: mem = {}, cpu = {} of peer {}", new Object[] { estimateUsage.mem, estimateUsage.cpu, peerID });
		}
	}
}
