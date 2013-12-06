package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.communicator;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.advertisement.AuctionResponseAdvertisement;


public interface Communicator {
	public Future<List<AuctionResponseAdvertisement>> publishAuction(AuctionQueryAdvertisement adv);
	public Collection<PeerID> getRemotePeerIds();
	public PeerGroupID getLocalPeerGroupId();
	public ID generateSharedQueryId();
	public PeerID getLocalPeerID();	
}
