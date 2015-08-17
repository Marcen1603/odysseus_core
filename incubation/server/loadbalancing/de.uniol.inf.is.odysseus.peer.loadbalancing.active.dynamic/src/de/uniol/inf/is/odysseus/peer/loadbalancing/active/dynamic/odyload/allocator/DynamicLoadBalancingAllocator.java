package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.allocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OsgiServiceProvider;

public class DynamicLoadBalancingAllocator implements ILoadBalancingAllocator {
	

	private static final Logger LOG = LoggerFactory.getLogger(DynamicLoadBalancingAllocator.class);
	
	IQueryPartAllocator surveyAllocator;
	
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
	

	@Override
	public String getName() {
		return OdyLoadConstants.ALLOCATOR_NAME;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			Collection<PeerID> knownRemotePeers, PeerID localPeerID)
			throws QueryPartAllocationException {
		
		Preconditions.checkNotNull(surveyAllocator,"Survey Allocator not bound.");
		
		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		
		if(surveyAllocator==null) {
			throw new QueryPartAllocationException("Survey Allocator not bound.");
		}
		
		List<String> allocatorParameters = createAllocationParameters();
		
		
		QueryBuildConfiguration config = executor.getBuildConfigForQuery(query);
		
		if(config==null) {
			LOG.error("No Query Build Configuration defined.");
			throw new QueryPartAllocationException("No QueryBuildConfiguration defined.");
		}
		
		if(knownRemotePeers.contains(localPeerID)) {
			knownRemotePeers.remove(localPeerID);
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
		
		int latencyWeight = OdyLoadConstants.SURVEY_LATENCY_WEIGHT;
		int bidWeight = OdyLoadConstants.SURVEY_BID_WEIGHT;
		//Own bid should not be used, as we try to move Query Parts away in Load Balancing :)
		String isOwnBidUsed = OdyLoadConstants.SURVEY_USE_OWN_BID;
		String bidProviderName = OdyLoadConstants.BID_PROVIDER_NAME;
		
		parameters.add(""+latencyWeight);
		parameters.add(""+bidWeight);
		parameters.add(isOwnBidUsed);
		parameters.add(bidProviderName);
		
		return parameters;
	}

}
