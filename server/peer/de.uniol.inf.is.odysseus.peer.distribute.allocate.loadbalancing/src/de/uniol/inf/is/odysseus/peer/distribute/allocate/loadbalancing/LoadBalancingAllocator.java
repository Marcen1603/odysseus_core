package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl.LoadBalancerFactory;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl.LoadBalancerFactoryException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class LoadBalancingAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancingAllocator.class);
	
	private static IPeerResourceUsageManager peerResourceUsageManager;

	// called by OSGi-DS
	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		peerResourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if (peerResourceUsageManager == serv) {
			peerResourceUsageManager = null;
		}
	}
	
	@Override
	public String getName() {
		return "loadbalancing";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		Preconditions.checkArgument(allocatorParameters.size() == 1, "Load balancing allocator needs exactly one parameter (e. g. 'avg')");
		String loadBalancerType = allocatorParameters.get(0);
		
		ILoadBalancer balancer = tryCreateLoadBalancer(loadBalancerType);
		LOG.debug("Load balancer allocator uses load balancer of type '{}'", loadBalancerType);
		
		Map<PeerID, IResourceUsage> currentResourceUsageMap = determineCurrentResourceUsagesOfPeers();
		
		// copy --> original
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);
		Map<ILogicalQueryPart, OperatorCost<?>> partCosts = CostEstimationHelper.determineQueryPartCosts(queryPartsCopyMap.keySet(), config);
		
		Map<ILogicalQueryPart, PeerID> balancedParts = balancer.balance(currentResourceUsageMap, partCosts);
		
		Map<ILogicalQueryPart, PeerID> allocationMap = reassignToOriginalQueryParts( balancedParts, queryPartsCopyMap );
		return allocationMap;
	}

	private static Map<ILogicalQueryPart, PeerID> reassignToOriginalQueryParts(Map<ILogicalQueryPart, PeerID> queryPartsCopies, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPartCopy : queryPartsCopies.keySet() ) {
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(queryPartCopy);
			result.put(originalQueryPart, queryPartsCopies.get(queryPartCopy));
		}
		
		return result;
	}

	private static Map<PeerID, IResourceUsage> determineCurrentResourceUsagesOfPeers() {
		Collection<PeerID> remotePeers = peerResourceUsageManager.getRemotePeerIDs();
		
		Map<PeerID, IResourceUsage> usages = Maps.newHashMap();
		for( PeerID remotePeer : remotePeers ) {
			try {
				usages.put(remotePeer, peerResourceUsageManager.getRemoteResourceUsage(remotePeer).get().get());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Could not determine resource usage", e);
			}
		}
		
		IResourceUsage localUsage = peerResourceUsageManager.getLocalResourceUsage();
		usages.put(peerResourceUsageManager.getLocalPeerID(), localUsage);
		
		return usages;
	}

	private static ILoadBalancer tryCreateLoadBalancer(String type) throws QueryPartAllocationException {
		try {
			return LoadBalancerFactory.create(type);
		} catch (LoadBalancerFactoryException e) {
			throw new QueryPartAllocationException("Could not create load balancer", e);
		}
	}
}
