package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * Provides Methods to implement a fast Contract-Net-Auction-Protocol by using direct Messages between Peers instead of Advertisements
 * @author badagent
 *
 */
public interface IFastAuctionCommunicator {
	/**
	 * Returns list of received Bids for Auction ID
	 * Between sending the Auction and calling this method, the processing Class should wait for peers to send bids.
	 * @param auctionID
	 * @return List of Bids.
	 */
	public List<Bid> getBids(int auctionID);
	
	/**
	 * Clears Bids for AuctionID
	 * @param auctionID Auction ID 
	 */
	public void clearBids(Integer auctionID);
	/**
	 * Distributes Auction to List of Peers.
	 * @param peers Peers that should receive Auctio (e.g. all known peers)
	 * @param queryPart Query part that should be distributed.
	 * @param config QueryBuildConfig for QueryPart.
	 * @param bidProviderName Bid Provider name to use for Bid Calculation.
	 * @return Auction ID
	 */
	public int sendAuctionToMultiplePeers(Collection<PeerID> peers, ILogicalQueryPart queryPart,QueryBuildConfiguration config, String bidProviderName);
}
