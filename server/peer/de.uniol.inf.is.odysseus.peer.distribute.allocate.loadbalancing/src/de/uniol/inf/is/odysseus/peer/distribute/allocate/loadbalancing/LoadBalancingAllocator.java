package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl.LoadBalancerFactory;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl.LoadBalancerFactoryException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.service.PeerResourceUsageManagerService;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class LoadBalancingAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancingAllocator.class);
	
	@Override
	public String getName() {
		return "loadbalacing";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		Preconditions.checkArgument(allocatorParameters.size() == 1, "Load balancing allocator needs exactly one parameter (e. g. 'avg')");
		String loadBalancerType = allocatorParameters.get(0);
		
		ILoadBalancer balancer = tryCreateLoadBalancer(loadBalancerType);
		LOG.debug("Load balancer allocator uses load balancer of type '{}'", loadBalancerType);
		
		Map<PeerID, IResourceUsage> currentResourceUsageMap = determineCurrentResourceUsagesOfPeers();
		return balancer.balance(currentResourceUsageMap, queryParts);
	}

	private static Map<PeerID, IResourceUsage> determineCurrentResourceUsagesOfPeers() {
		IPeerResourceUsageManager usageManager = PeerResourceUsageManagerService.get();
		Collection<PeerID> remotePeers = usageManager.getRemotePeers();
		
		Map<PeerID, IResourceUsage> usages = Maps.newHashMap();
		for( PeerID remotePeer : remotePeers ) {
			usages.put(remotePeer, usageManager.getRemoteResourceUsage(remotePeer).get());
		}
		
		Optional<IResourceUsage> localUsage = usageManager.getLocalResourceUsage();
		if( localUsage.isPresent() ) {
			usages.put(localUsage.get().getPeerID(), localUsage.get());
		}
		
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
