package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.communicator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;

public class DefaultCommunicator implements Communicator, IAdvertisementListener {
	private static final Logger log = LoggerFactory.getLogger(DefaultCommunicator.class);
	private static final long WAIT_TIME = 4000;
	private ExecutorService executorService;

	private IP2PDictionary dictionary;
	private IP2PNetworkManager networkManager;
	private IJxtaServicesProvider jxtaServiceProvider;
	private CostCalculator calculator;
	private IServerExecutor executor;

	private Map<String, Mailbox<AuctionResponseAdvertisement>> mailboxForAuctions;

	public final void activate() {
		executorService = Executors.newCachedThreadPool();
		mailboxForAuctions = Maps.newHashMap();
	}

	public final void deactivate() {
		executorService.shutdown();
		executorService = null;
	}

	public void bindP2PDictionary(IP2PDictionary dict) {
		dictionary = dict;
	}

	public void unbindP2PDictionary(IP2PDictionary dict) {
		if (dict == dictionary) {
			dictionary = null;
		}
	}

	public void bindP2PNetworkManager(IP2PNetworkManager manager) {
		networkManager = manager;
	}

	public void unbindP2PNetworkManager(IP2PNetworkManager manager) {
		if (manager == networkManager) {
			networkManager = null;
		}
	}

	public void bindCostCalculator(CostCalculator calculator) {
		this.calculator = calculator;
	}

	public void unbindCostCalculator(CostCalculator calculator) {
		if (this.calculator == calculator) {
			calculator = null;
		}
	}

	public void bindExecutor(IExecutor executor) {
		if (!(executor instanceof IServerExecutor))
			throw new RuntimeException("An ServerExecutor is needed");
		this.executor = (IServerExecutor) executor;
	}

	public void unbindExecutor(IExecutor executor) {
		if (this.executor == executor) {
			executor = null;
		}
	}

	public void bindJxtaServicesProvider(IJxtaServicesProvider jxtaServiceProvider) {
		this.jxtaServiceProvider = jxtaServiceProvider;
	}

	public void unbindJxtaServicesProvider(IJxtaServicesProvider jxtaServiceProvider) {
		if (this.jxtaServiceProvider == jxtaServiceProvider) {
			jxtaServiceProvider = null;
		}
	}

	@Override
	public Future<List<AuctionResponseAdvertisement>> publishAuction(final AuctionQueryAdvertisement adv) {
		synchronized (mailboxForAuctions) {
			Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(adv.getAuctionId());
			if (mailbox == null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForAuctions.put(adv.getAuctionId(), mailbox);
			}
		}

		adv.setID(IDFactory.newPipeID(networkManager.getLocalPeerGroupID()));
		adv.setOwnerPeerId(networkManager.getLocalPeerID());
		for (PeerID id : dictionary.getRemotePeerIDs()) {
			if (!id.equals(networkManager.getLocalPeerID())) {
				jxtaServiceProvider.getDiscoveryService().remotePublish(id.toString(), adv, WAIT_TIME);
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
			AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) advertisement;
			if (!adv.getOwnerPeerId().equals(networkManager.getLocalPeerID())) {
				synchronized (this.mailboxForAuctions) {
					Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(adv.getAuctionId());
					if (mailbox != null) // es werden noch antworten mehr
											// entgegegen genommen
						mailbox.getMails().add(adv);
				}
			}
		} else if (advertisement instanceof AuctionQueryAdvertisement) {
			AuctionQueryAdvertisement adv = ((AuctionQueryAdvertisement) advertisement);
			if (!adv.getOwnerPeerId().equals(networkManager.getLocalPeerID())) {
				if (!adv.getOwnerPeerId().equals(networkManager.getLocalPeerID())) {
					log.debug("Received query to bid to auction {}", adv.getAuctionId());
					ILogicalQuery query = Helper.getLogicalQuery(executor, adv.getPqlStatement()).get(0);
					CostSummary costs = calculator.calcCostsForPlan(query, adv.getTransCfgName());

					double bidValue = calculator.calcBid(query.getLogicalPlan(), costs) + 0.2;

					if (bidValue > 0) {
						final AuctionResponseAdvertisement bid = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
						bid.setAuctionId(adv.getAuctionId());
						bid.setBid(bidValue);
						bid.setOwnerPeerId(networkManager.getLocalPeerID());
						bid.setPqlStatement(adv.getPqlStatement());
						bid.setTransCfgName(adv.getTransCfgName());
						bid.setSharedQueryID(adv.getSharedQueryID());
						bid.setID(IDFactory.newPipeID(networkManager.getLocalPeerGroupID()));
						this.jxtaServiceProvider.getDiscoveryService().remotePublish(adv.getOwnerPeerId().toString(), bid, WAIT_TIME);
						log.info("Sent bid {} to auction {} of peer {}", new String[] { "" + bidValue, adv.getAuctionId(), adv.getOwnerPeerId().toString() });
					} else {
						log.info("Offering no bid to auction {} of peer {}", adv.getAuctionId(), adv.getOwnerPeerId().toString());
					}
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {

	}

	@Override
	public Collection<PeerID> getRemotePeerIds() {
		return dictionary.getRemotePeerIDs();
	}

	@Override
	public PeerGroupID getLocalPeerGroupId() {
		return networkManager.getLocalPeerGroupID();
	}

	@Override
	public PeerID getLocalPeerID() {
		return networkManager.getLocalPeerID();
	}

	@Override
	public ID generateSharedQueryId() {
		final String timestamp = String.valueOf(System.currentTimeMillis());
		return IDFactory.newContentID(networkManager.getLocalPeerGroupID(), false, timestamp.getBytes());
	}
}
