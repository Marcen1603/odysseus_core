package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocationPlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class Communicator implements IAdvertisementDiscovererListener {

	private static final Logger LOG = LoggerFactory.getLogger(Communicator.class);
	private static final long WAIT_TIME_MILLIS = 21 * 1000;

	private static Communicator instance;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IPingMap pingMap;

	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final Collection<ID> processedQueryIDs = Lists.newLinkedList();

	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addAdvertisementListener(this);
	}

	// called by OSGi-DS
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager.removeAdvertisementListener(this);
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

	// called by OSGi-DS
	public static void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
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
		try {
			jxtaServicesProvider.publish(adv, WAIT_TIME_MILLIS, WAIT_TIME_MILLIS);
			jxtaServicesProvider.remotePublish(adv, WAIT_TIME_MILLIS);
		} catch (IOException e) {
			LOG.error("Could not publish auction", e);
		}

		return executorService.submit(new Callable<Collection<Bid>>() {
			@Override
			public Collection<Bid> call() throws Exception {
				Thread.sleep(WAIT_TIME_MILLIS);
				
				Collection<AuctionResponseAdvertisement> allResponses = jxtaServicesProvider.getLocalAdvertisements(AuctionResponseAdvertisement.class);
				Collection<AuctionResponseAdvertisement> relevantResponses = filterForAuctionID(allResponses, adv.getAuctionId());
				updatePingMap(relevantResponses);
				
				return getBids(relevantResponses);
			}

		});
	}
	
	private static Collection<AuctionResponseAdvertisement> filterForAuctionID(Collection<AuctionResponseAdvertisement> allResponses, ID auctionId) {
		Collection<AuctionResponseAdvertisement> result = Lists.newLinkedList();
		for( AuctionResponseAdvertisement response : allResponses ) {
			if( response.getAuctionId().equals(auctionId)) {
				result.add(response);
			}
		}
		return result;
	}
	
	private static void updatePingMap(Collection<AuctionResponseAdvertisement> relevantResponses) {
		for( AuctionResponseAdvertisement relevantResponse : relevantResponses ) {
			pingMap.setPosition(relevantResponse.getBid().getBidderPeerID(), relevantResponse.getPingMapPosition());
		}
	}

	private Collection<Bid> getBids(Collection<AuctionResponseAdvertisement> relevantResponses) {
		Collection<Bid> bids = Lists.newLinkedList();
		for( AuctionResponseAdvertisement relevantResponse : relevantResponses ) {
			bids.add(relevantResponse.getBid());
		}
		return bids;
	}
	
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if( advertisement instanceof AuctionQueryAdvertisement ) {
			AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement)advertisement;
			if( !processedQueryIDs.contains(adv.getAuctionId())) {
				try {
					processActionQueryAdvertisement(adv);
				} finally {
					processedQueryIDs.add(adv.getAuctionId());
				}
			}
		}
	}
	
	@Override
	public void updateAdvertisements() {
		// do nothing
	}

	private void processActionQueryAdvertisement(AuctionQueryAdvertisement adv) {
		if (!adv.getOwnerPeerId().equals(p2pNetworkManager.getLocalPeerID())) {

			LOG.debug("Received query to bid to auction {}", adv.getAuctionId());
			LOG.debug("PQL-Statement is \n{}", adv.getPqlStatement());

			ILogicalQuery query = Helper.getLogicalQuery(adv.getPqlStatement()).get(0);

			IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
			Optional<Double> optBidValue = bidProvider.calculateBid(query, adv.getTransCfgName());

			if (optBidValue.isPresent()) {
				double bidValue = optBidValue.get();

				AuctionResponseAdvertisement auctionBidAdvertisement = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
				auctionBidAdvertisement.setAuctionId(adv.getAuctionId());
				auctionBidAdvertisement.setBid(new Bid(p2pNetworkManager.getLocalPeerID(), bidValue));
				auctionBidAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
				auctionBidAdvertisement.setPingMapPosition(pingMap.getLocalPosition());

				jxtaServicesProvider.remotePublishToPeer(auctionBidAdvertisement, adv.getOwnerPeerId(), WAIT_TIME_MILLIS);

				LOG.debug("Sent bid {} to auction {} of peer {}", new String[] { "" + bidValue, adv.getAuctionId().toString(), adv.getOwnerPeerId().toString() });
			} else {
				LOG.debug("Offering no bid to auction {} of peer {}", adv.getAuctionId(), adv.getOwnerPeerId().toString());
			}
		}
	}
}
