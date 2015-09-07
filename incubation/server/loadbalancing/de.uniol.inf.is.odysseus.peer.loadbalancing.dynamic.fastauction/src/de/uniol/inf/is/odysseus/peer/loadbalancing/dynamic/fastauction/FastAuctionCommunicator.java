package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.messages.FastAuctionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.messages.FastBidMessage;

public class FastAuctionCommunicator implements IPeerCommunicatorListener, IFastAuctionCommunicator {
	
	private volatile int auctionIDCounter = 0;
	

	private static final Logger LOG = LoggerFactory.getLogger(FastAuctionCommunicator.class);

	ConcurrentHashMap<Integer,List<Bid>> collectedBids = new ConcurrentHashMap<>();
	
	public IPeerCommunicator communicator;
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
		
		communicator.registerMessageType(FastAuctionMessage.class);
		communicator.registerMessageType(FastBidMessage.class);
		
		communicator.addListener(this, FastAuctionMessage.class);
		communicator.addListener(this,FastBidMessage.class);
		
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
		

		communicator.removeListener(this, FastAuctionMessage.class);
		communicator.removeListener(this,FastBidMessage.class);
		
		communicator.unregisterMessageType(FastAuctionMessage.class);
		communicator.unregisterMessageType(FastBidMessage.class);
		
		
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		if(message instanceof FastBidMessage) {
			FastBidMessage bidMsg = (FastBidMessage)message;
			if(collectedBids.containsKey(bidMsg.getAuctionID())) {
				List<Bid> bidsForAuction = collectedBids.get(bidMsg.getAuctionID());
				synchronized(bidsForAuction) {
					bidsForAuction.add(new Bid(bidMsg.getAuctionID(),senderPeer,bidMsg.getBid()));
				}
			}
		}
		

		if(message instanceof FastAuctionMessage) {
			FastAuctionMessage auctionMessage = (FastAuctionMessage)message;
			int auctionID = auctionMessage.getAuctionId();
			try {
					ILogicalQuery query = Helper.getLogicalQuery(auctionMessage.getPqlStatement()).get(0);
		
					String bidProviderName = auctionMessage.getBidProviderName();
					
					if(!BidProviderCollector.isBidProviderBound(bidProviderName)) {
						LOG.error("Bid Provider {} not found.",bidProviderName);
						return;
					}
					
					IBidProvider bidProvider = BidProviderCollector.getBidProvider(bidProviderName);
					
					Optional<Double> optBidValue = bidProvider.calculateBid(query,auctionMessage.getBidProviderName());
					
					if(optBidValue.isPresent()) {
						LOG.info("Bidding with bid of {}",optBidValue.get());
						sendBid(senderPeer,auctionID,optBidValue.get());
					}
					else {
						LOG.info("Not biddind.");
					}
					
				}
				catch (Exception e) {
					LOG.error("Exception during Bid Calculation: {}",e.getMessage());
				}
				
			
		}
		
	}
	
	public List<Bid> getBids(int auctionID) {
		return new ArrayList<Bid>(collectedBids.get(auctionID));
	}
	
	
	public void clearBids(Integer auctionID) {
		if(collectedBids.containsKey(auctionID)) {
			collectedBids.remove(auctionID);
		}
	}
	

	public int sendAuctionToMultiplePeers(Collection<PeerID> peers, ILogicalQueryPart queryPart,QueryBuildConfiguration config, String bidProviderName) {
		
		int auctionID = auctionIDCounter++;
		
		String pqlStatement = LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart);
		
		String transCfgName = config.getName();
		
		
		sendAuctionToMultiplePeers(peers, auctionID, transCfgName, pqlStatement,bidProviderName);
		return auctionID;
	}
	
	
	public void sendAuctionToMultiplePeers(Collection<PeerID> peers, int auctionID, String transCfgName, String pqlStatement,String bidProviderName) {
		
		collectedBids.put(auctionID, new ArrayList<Bid>());
		
		for(PeerID peer : peers) {
			sendAuction(peer, auctionID, transCfgName, pqlStatement,bidProviderName);
		}
	}
	
	private void sendAuction(PeerID destination, int auctionID, String transCfgName, String pqlStatement,String bidProviderName) {
		FastAuctionMessage msg = new FastAuctionMessage(auctionID, pqlStatement, transCfgName, bidProviderName);
		try {
			communicator.send(destination, msg);
		} catch (PeerCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendBid(PeerID destination, int auctionID, double bid) {
		try {
			communicator.send(destination, new FastBidMessage(auctionID,bid));
		} catch (PeerCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
