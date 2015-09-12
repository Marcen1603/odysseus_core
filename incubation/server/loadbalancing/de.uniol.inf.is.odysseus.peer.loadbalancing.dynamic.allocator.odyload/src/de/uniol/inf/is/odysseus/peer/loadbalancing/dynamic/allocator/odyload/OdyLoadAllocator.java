package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.allocator.odyload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.Bid;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.IFastAuctionCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;

public class OdyLoadAllocator implements ILoadBalancingAllocator {
	
	private final boolean FAST_AUCTION = true;

	private static final Logger LOG = LoggerFactory.getLogger(OdyLoadAllocator.class);
	
	IQueryPartAllocator surveyAllocator;
	IServerExecutor executor;
	IFastAuctionCommunicator fastAuctionCommunicator;
	
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
	
	public void bindFastAuctionCommunicator(IFastAuctionCommunicator serv) {
		fastAuctionCommunicator = serv;
	}
	
	public void unbindFastAuctionCommunicator(IFastAuctionCommunicator serv) {
		if(serv==fastAuctionCommunicator) {
			fastAuctionCommunicator = null;
		}
	}
	
	public void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}

	public void unbindExecutor(IExecutor serv) {
		if(executor==serv){
			executor = null;
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
		
		if(FAST_AUCTION) {
			Preconditions.checkNotNull(fastAuctionCommunicator,"Fast Auction Communicator not bound.");
			Preconditions.checkNotNull(executor, "No executor bound.");
			
			QueryBuildConfiguration config = executor.getBuildConfigForQuery(query);
			
			if(config==null) {
				LOG.error("No Query Build Configuration defined.");
				throw new QueryPartAllocationException("No QueryBuildConfiguration defined.");
			}
			
			if(knownRemotePeers.contains(localPeerID)) {
				knownRemotePeers.remove(localPeerID);
			}
			
			HashMap<Integer,ILogicalQueryPart> auctionIDs = new HashMap<Integer,ILogicalQueryPart>();
			for(ILogicalQueryPart part : queryParts) {
				auctionIDs.put(fastAuctionCommunicator.sendAuctionToMultiplePeers(knownRemotePeers, part, config, OdyLoadConstants.BID_PROVIDER_NAME),part);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HashMap<ILogicalQueryPart,PeerID> resultMap = new HashMap<ILogicalQueryPart,PeerID>();
			
			for(int auctionID : auctionIDs.keySet()) {
				List<Bid> bids = fastAuctionCommunicator.getBids(auctionID);
				
				double maxBid = Double.NEGATIVE_INFINITY;
				PeerID peerWithMaxBid = localPeerID;
				
				for(Bid bid : bids) {
					if(bid.getBid()>maxBid) {
						maxBid = bid.getBid();
						peerWithMaxBid = bid.getPeerID();
					}
				}
				
				resultMap.put(auctionIDs.get(auctionID), peerWithMaxBid);
				
			}
			return resultMap;
		}
		else {
			Preconditions.checkNotNull(surveyAllocator,"Survey Allocator not bound.");
			Preconditions.checkNotNull(executor, "No executor bound.");
			
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
	}

	
	@Override
	/**
	 * In this implementation the previous allocation map is ignored and faulty peers are removed from known Peers. 
	 * Apart from that, the allocation is called.
	 */
	public Map<ILogicalQueryPart, PeerID> reallocate(
			Map<ILogicalQueryPart, PeerID> previousAllocationMap,
			Collection<PeerID> faultPeers, Collection<PeerID> knownRemotePeers,
			PeerID localPeerID) throws QueryPartAllocationException {
		
		//FIXME is not implemented as this method is not used in Odyload.
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
