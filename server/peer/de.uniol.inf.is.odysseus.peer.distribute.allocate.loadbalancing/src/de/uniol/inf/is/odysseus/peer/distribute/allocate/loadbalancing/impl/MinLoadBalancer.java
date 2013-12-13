package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import com.google.common.collect.Maps;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class MinLoadBalancer implements ILoadBalancer {

	private static class Usage {
		public long mem;
		public double cpu;
	}
	
	@Override
	public Map<ILogicalQueryPart, PeerID> balance(Map<PeerID, IResourceUsage> currentResourceUsageMap, Map<ILogicalQueryPart, OperatorCost<?>> partCosts) {
		
		Map<PeerID, Usage> estimatedUsages = transform( currentResourceUsageMap );
		
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		for( ILogicalQueryPart queryPart : partCosts.keySet() ) {
			PeerID minPeerID = determineMinimumPeerID(estimatedUsages);
			
			result.put(queryPart, minPeerID);
			
			estimatedUsages.get(minPeerID).mem += partCosts.get(queryPart).getMemCost();
			estimatedUsages.get(minPeerID).cpu += partCosts.get(queryPart).getCpuCost();
		}
		
		return result;
	}

	private static Map<PeerID, Usage> transform(Map<PeerID, IResourceUsage> currentResourceUsageMap) {
		Map<PeerID, Usage> usageMap = Maps.newHashMap();
		for( PeerID peerID : currentResourceUsageMap.keySet() ) {
			IResourceUsage resUsage = currentResourceUsageMap.get(peerID);
			
			Usage usage = new Usage();
			usage.mem = resUsage.getMemMaxBytes() - resUsage.getMemFreeBytes();
			usage.cpu = resUsage.getCpuMax() - resUsage.getCpuFree();
			usageMap.put(peerID, usage);
		}
		return usageMap;
	}

	private static PeerID determineMinimumPeerID(Map<PeerID, Usage> estimatedUsages) {
		Usage min = new Usage();
		min.mem = Long.MAX_VALUE;
		min.cpu = Double.MAX_VALUE;
		
		PeerID minPeerID = null;
		for( PeerID peerID : estimatedUsages.keySet() ) {
			Usage usage = estimatedUsages.get(peerID);
			if( usage.mem < min.mem && usage.cpu < min.cpu ) {
				min = usage;
				minPeerID = peerID;
			}
		}
		
		return minPeerID;
	}

}
