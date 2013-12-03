package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisement;


public interface Communicator {
	public Future<List<CostResponseAdvertisement>> askPeersForPlanCosts(CostQueryAdvertisement adv);
	public Future<List<AuctionResponseAdvertisement>> publishAuction(AuctionQueryAdvertisement adv);
	public Collection<PeerID> getRemotePeerIds();
	public PeerGroupID getLocalPeerGroupId();
	public void sendSubPlan(String destination, QueryPartAdvertisement adv);
	public ID generateSharedQueryId();
	public PeerID getLocalPeerID();	
}
