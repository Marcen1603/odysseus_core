package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

public interface IFastAuctionCommunicator {
	public List<Bid> getBids(int auctionID);
	public void clearBids(Integer auctionID);
	public int sendAuctionToMultiplePeers(Collection<PeerID> peers, ILogicalQueryPart queryPart,QueryBuildConfiguration config, String bidProviderName);
}
