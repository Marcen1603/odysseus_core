package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocationPlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;

public class Communicator implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(Communicator.class);
	private static final long WAIT_TIME_MILLIS = 8 * 1000;

	private static Communicator instance;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;

	private final ExecutorService executorService = Executors.newCachedThreadPool();
	
	private final Map<String, Collection<Bid>> mailboxForAuctions = Maps.newHashMap();

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

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

	public Future<Collection<Bid>> publishAuction(final AuctionQueryAdvertisement adv) {
		synchronized (mailboxForAuctions) {
			mailboxForAuctions.put(adv.getAuctionId().toString(), Lists.<Bid>newArrayList());
		}
		
		try {
			jxtaServicesProvider.getDiscoveryService().publish(adv, WAIT_TIME_MILLIS, WAIT_TIME_MILLIS);
			jxtaServicesProvider.getDiscoveryService().remotePublish(adv, WAIT_TIME_MILLIS);
		} catch (IOException e) {
			LOG.error("Could not publish auction", e);
		}

		return executorService.submit(new Callable<Collection<Bid>>() {
			@Override
			public Collection<Bid> call() throws Exception {
				Thread.sleep(WAIT_TIME_MILLIS);

				synchronized (mailboxForAuctions) {
					return mailboxForAuctions.remove(adv.getAuctionId().toString());
				}
			}
		});
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof AuctionResponseAdvertisement) {
			processAuctionResponeAdvertisement((AuctionResponseAdvertisement) advertisement);

		} else if (advertisement instanceof AuctionQueryAdvertisement) {
			processActionQueryAdvertisement((AuctionQueryAdvertisement) advertisement);
		}
	}

	private void processActionQueryAdvertisement(AuctionQueryAdvertisement adv) {
		if (!adv.getOwnerPeerId().equals(p2pNetworkManager.getLocalPeerID())) {
			LOG.debug("Received query to bid to auction {}", adv.getAuctionId());
			ILogicalQuery query = Helper.getLogicalQuery(adv.getPqlStatement()).get(0);

			IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
			Optional<Double> optBidValue = bidProvider.calculateBid(query, adv.getTransCfgName());

			if (optBidValue.isPresent()) {
				double bidValue = optBidValue.get();
				
				AuctionResponseAdvertisement bid = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
				bid.setAuctionId(adv.getAuctionId());
				bid.setBid(new Bid(p2pNetworkManager.getLocalPeerID(), bidValue));
				bid.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
				
				jxtaServicesProvider.getDiscoveryService().remotePublish(adv.getOwnerPeerId().toString(), bid, WAIT_TIME_MILLIS);

				LOG.debug("Sent bid {} to auction {} of peer {}", new String[] { "" + bidValue, adv.getAuctionId().toString(), adv.getOwnerPeerId().toString() });
			} else {
				LOG.debug("Offering no bid to auction {} of peer {}", adv.getAuctionId(), adv.getOwnerPeerId().toString());
			}
		}
	}

	private void processAuctionResponeAdvertisement(AuctionResponseAdvertisement advertisement) {
		if (!advertisement.getBid().getBidderPeerID().equals(p2pNetworkManager.getLocalPeerID())) {
			synchronized (this.mailboxForAuctions) {
				Collection<Bid> mailbox = mailboxForAuctions.get(advertisement.getAuctionId().toString());
				if( mailbox != null ) {
					mailbox.add(advertisement.getBid());
				} 
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
	}
}
