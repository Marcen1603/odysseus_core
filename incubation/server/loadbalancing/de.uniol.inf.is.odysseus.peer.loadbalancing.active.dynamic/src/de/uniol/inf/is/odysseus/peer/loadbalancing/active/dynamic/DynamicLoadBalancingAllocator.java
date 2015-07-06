package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;

public class DynamicLoadBalancingAllocator implements ILoadBalancingAllocator {
	

	private static final Logger LOG = LoggerFactory.getLogger(DynamicLoadBalancingAllocator.class);
	
	IQueryPartAllocator surveyAllocator;
	IServerExecutor executor;
	
	public void bindSurveyAllocator(IQueryPartAllocator serv) {
		if(serv instanceof SurveyBasedAllocator) {
			this.surveyAllocator = serv;
			LOG.debug("Survey Based Allocator bound.");
		}
	}
	
	public void unbindSurveyAllocator(IQueryPartAllocator serv) {
		if(this.surveyAllocator == serv) {
			this.surveyAllocator = null;
			LOG.debug("Survey Based Allocator unbound.");
		}
	}
	
	public void bindExecutor(IExecutor serv) {
		this.executor = (IServerExecutor)serv;
	}
	
	
	public void unbindExecutor(IExecutor serv) {
		if(this.executor == serv) {
			this.executor = null;
		}
	}

	@Override
	public String getName() {
		return DynamicLoadBalancingConstants.ALLOCATOR_NAME;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			Collection<PeerID> knownRemotePeers, PeerID localPeerID)
			throws QueryPartAllocationException {
		
		
		if(surveyAllocator==null) {
			throw new QueryPartAllocationException("Survey Allocator not bound.");
		}
		
		List<String> allocatorParameters = createAllocationParameters();
		
		
		QueryBuildConfiguration config = executor.getBuildConfigForQuery(query);
		
		if(config==null) {
			LOG.error("No Query Build Configuration defined.");
			throw new QueryPartAllocationException("No QueryBuildConfiguration defined.");
		}
		
		Map<ILogicalQueryPart,PeerID> allocationResult = surveyAllocator.allocate(queryParts, query, knownRemotePeers, localPeerID, config, allocatorParameters);
		
		return allocationResult;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(
			Map<ILogicalQueryPart, PeerID> previousAllocationMap,
			Collection<PeerID> faultPeers, Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException {
		
		//TODO implement
		
		return null;
	}
	
	private List<String> createAllocationParameters() {
		ArrayList<String> parameters = new ArrayList<String>();
		
		int latencyWeight = DynamicLoadBalancingConstants.SURVEY_LATENCY_WEIGHT;
		int bidWeight = DynamicLoadBalancingConstants.SURVEY_BID_WEIGHT;
		//Own bid should not be used, as we try to move Query Parts away in Load Balancing :)
		String isOwnBidUsed = DynamicLoadBalancingConstants.SURVEY_USE_OWN_BID;
		String bidProviderName = DynamicLoadBalancingConstants.BID_PROVIDER_NAME;
		
		parameters.add(""+latencyWeight);
		parameters.add(""+bidWeight);
		parameters.add(isOwnBidUsed);
		parameters.add(bidProviderName);
		
		return parameters;
	}

}
