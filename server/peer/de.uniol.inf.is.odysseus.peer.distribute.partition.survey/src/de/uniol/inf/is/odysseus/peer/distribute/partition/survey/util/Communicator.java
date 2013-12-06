package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.Mailbox;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PNetworkManagerService;

public final class Communicator implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(Communicator.class);
	private static final long WAIT_TIME = 4000;
	
	private static Communicator instance;

	private ExecutorService executorService = Executors.newCachedThreadPool();
	private Map<ID, Mailbox<CostResponseAdvertisement>> mailboxForPlanCosts = Maps.newHashMap();

	public void activate() {
		instance = this;
	}
	
	public void deactivate() {
		instance = null;
	}
	
	public static boolean isActivated() {
		return getInstance() != null;
	}

	public static Communicator getInstance() {
		return instance;
	}
	
	public Future<List<CostResponseAdvertisement>> askPeersForPlanCosts(final CostQueryAdvertisement adv) {
		synchronized (mailboxForPlanCosts) {
			Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.get(adv.getSharedQueryID());
			if (mailbox == null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForPlanCosts.put(adv.getSharedQueryID(), mailbox);
			}
		}

		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
		for (PeerID id : P2PDictionaryService.get().getRemotePeerIDs()) {
			if (!id.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				JxtaServicesProviderService.get().getDiscoveryService().remotePublish(id.toString(), adv, WAIT_TIME);
			}
		}
		return executorService.submit(new Callable<List<CostResponseAdvertisement>>() {
			@Override
			public List<CostResponseAdvertisement> call() throws Exception {
				Thread.sleep(WAIT_TIME);
				synchronized (mailboxForPlanCosts) {
					Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.remove(adv.getSharedQueryID());
					return mailbox.getMails();
				}
			}
		});
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof CostResponseAdvertisement) {
			CostResponseAdvertisement adv = (CostResponseAdvertisement) advertisement;
			if (!adv.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				LOG.debug("Received response to query to calculate costs for plan {}", adv.getSharedQueryID().toString());
				synchronized (mailboxForPlanCosts) {
					Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.get(adv.getSharedQueryID());
					if (mailbox != null) {
						mailbox.getMails().add(adv);
					}
				}
			}
		} else if (advertisement instanceof CostQueryAdvertisement) {
			CostQueryAdvertisement adv = ((CostQueryAdvertisement) advertisement);
			if (!adv.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				if (!adv.getOwnerPeerId().equals(P2PNetworkManagerService.get().getLocalPeerID())) {
					try {
						LOG.debug("Received query to calculate costs for plan {}", adv.getSharedQueryID().toString());
						ILogicalQuery plan = Helper.getLogicalQuery(adv.getPqlStatement()).get(0);

						Map<String, CostSummary> costsProOperator = CostCalculator.calcCostsProOperator(plan, adv.getTransCfgName(), false);

						CostSummary sum = CostSummary.calcSum(costsProOperator.values());
						CostSummary relativeCosts = CostCalculator.calcBearableCostsInPercentage(sum);
						double bid = CostCalculator.calcBid(plan.getLogicalPlan(), sum, false);

						CostResponseAdvertisement costAdv = (CostResponseAdvertisement) AdvertisementFactory.newAdvertisement(CostResponseAdvertisement.getAdvertisementType());
						costAdv.setCostSummary(costsProOperator);
						costAdv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
						costAdv.setPqlStatement(adv.getPqlStatement());
						costAdv.setTransCfgName(adv.getTransCfgName());
						costAdv.setSharedQueryID(adv.getSharedQueryID());
						costAdv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
						costAdv.setPercentageOfBearableCpuCosts(relativeCosts.getCpuCost());
						costAdv.setPercentageOfBearableMemCosts(relativeCosts.getMemCost());
						costAdv.setBid(bid);
						JxtaServicesProviderService.get().getDiscoveryService().remotePublish(adv.getOwnerPeerId().toString(), costAdv, WAIT_TIME);
						LOG.info("Sent costs ({}) for plan {} to peer {}", new String[] { sum.toString(), adv.getSharedQueryID().toString(), adv.getOwnerPeerId().toString() });
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {

	}

}
