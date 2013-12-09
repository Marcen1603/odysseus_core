package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.P2PNetworkManagerService;

public class Communicator implements IAdvertisementListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(Communicator.class);
	private static final long WAIT_TIME = 4000;
	
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final Map<String, Mailbox<AuctionResponseAdvertisement>> mailboxForAuctions = Maps.newHashMap();
	
	private static Communicator instance;

	public final void activate() {
		instance = this;
	}

	public final void deactivate() {
		executorService.shutdown();
		
		instance = null;
	}
	
	public static Communicator getInstance() {
		return instance;
	}

	public Future<List<AuctionResponseAdvertisement>> publishAuction(final AuctionQueryAdvertisement adv) {
		synchronized (mailboxForAuctions) {
			Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(adv.getAuctionId());
			if (mailbox == null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForAuctions.put(adv.getAuctionId(), mailbox);
			}
		}

		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
		for (PeerID id : P2PDictionaryService.get().getRemotePeerIDs()) {
			if (!id.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				JxtaServicesProviderService.get().getDiscoveryService().remotePublish(id.toString(), adv, WAIT_TIME);
			}
		}
		
		return executorService.submit(new Callable<List<AuctionResponseAdvertisement>>() {
			@Override
			public List<AuctionResponseAdvertisement> call() throws Exception {
				Thread.sleep(WAIT_TIME);
				synchronized (mailboxForAuctions) {
					Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.remove(adv.getAuctionId());
					return mailbox.getMails();
				}
			}
		});
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof AuctionResponseAdvertisement) {
			processAuctionResponeAdvertisement((AuctionResponseAdvertisement)advertisement);
			
		} else if (advertisement instanceof AuctionQueryAdvertisement) {
			processActionQueryAdvertisement((AuctionQueryAdvertisement)advertisement);
		}
	}

	private void processActionQueryAdvertisement(AuctionQueryAdvertisement adv) {
		if (!adv.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
			if (!adv.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				LOG.debug("Received query to bid to auction {}", adv.getAuctionId());
				ILogicalQuery query = Helper.getLogicalQuery(adv.getPqlStatement()).get(0);
				double bidValue = BidCalculator.calcBid(query, adv.getTransCfgName());

				if (bidValue > 0) {
					final AuctionResponseAdvertisement bid = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
					bid.setAuctionId(adv.getAuctionId());
					bid.setBid(bidValue);
					bid.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
					bid.setPqlStatement(adv.getPqlStatement());
					bid.setTransCfgName(adv.getTransCfgName());
					bid.setSharedQueryID(adv.getSharedQueryID());
					bid.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
					JxtaServicesProviderService.get().getDiscoveryService().remotePublish(adv.getOwnerPeerId().toString(), bid, WAIT_TIME);
					LOG.debug("Sent bid {} to auction {} of peer {}", new String[] { "" + bidValue, adv.getAuctionId(), adv.getOwnerPeerId().toString() });
				} else {
					LOG.debug("Offering no bid to auction {} of peer {}", adv.getAuctionId(), adv.getOwnerPeerId().toString());
				}
			}
		}
	}

	private void processAuctionResponeAdvertisement(AuctionResponseAdvertisement advertisement) {
		if (!advertisement.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
			synchronized (this.mailboxForAuctions) {
				Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(advertisement.getAuctionId());
				if (mailbox != null) {
					mailbox.getMails().add(advertisement);
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
	}
}
