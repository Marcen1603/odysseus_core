package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator;

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
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisement;

public class DefaultCommunicator implements Communicator, IAdvertisementListener{
	private static final Logger log = LoggerFactory.getLogger(DefaultCommunicator.class);
	private static final long WAIT_TIME = 10000;
	private ExecutorService executorService;
	
	private IP2PDictionary dictionary;
	private IJxtaServicesProvider jxtaServiceProvider;
	private CostCalculator calculator;
	private IServerExecutor executor;
	
	private Map<ID, Mailbox<CostResponseAdvertisement>> mailboxForPlanCosts;
	private Map<String, Mailbox<AuctionResponseAdvertisement>> mailboxForAuctions;
	
	public final void activate() {
		executorService = Executors.newCachedThreadPool();
		mailboxForPlanCosts = Maps.newHashMap();
		mailboxForAuctions = Maps.newHashMap();
	}

	public final void deactivate() {
		executorService.shutdown();
		executorService = null;
		mailboxForPlanCosts = null;
	}	
	
	public void bindP2PDictionary(IP2PDictionary dict) {
		dictionary = dict;
	}
	
	public void unbindP2PDictionary(IP2PDictionary dict) {
		if( dict == dictionary ) {
			dictionary = null;
		}
	}	
	
	public void bindCostCalculator(CostCalculator calculator) {
		this.calculator = calculator;
	}
	
	public void unbindCostCalculator(CostCalculator calculator) {
		if( this.calculator == calculator ) {
			calculator = null;
		}
	}			
	
	public void bindExecutor(IExecutor executor) {
		if(!(executor instanceof IServerExecutor))
			throw new RuntimeException("An ServerExecutor is needed");
		this.executor = (IServerExecutor)executor;
	}
	public void unbindExecutor(IExecutor executor) {
		if( this.executor == executor ) {
			executor = null;
		}
	}		
	
	public void bindJxtaServicesProvider(IJxtaServicesProvider jxtaServiceProvider) {
		this.jxtaServiceProvider = jxtaServiceProvider;
	}
	
	public void unbindJxtaServicesProvider(IJxtaServicesProvider jxtaServiceProvider) {
		if( this.jxtaServiceProvider == jxtaServiceProvider ) {
			jxtaServiceProvider = null;
		}
	}			
	
	@Override
	public Future<List<CostResponseAdvertisement>> askPeersForPlanCosts(final CostQueryAdvertisement adv) {
		synchronized(mailboxForPlanCosts) {
			Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.get(adv.getSharedQueryID());
			if(mailbox==null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForPlanCosts.put(adv.getSharedQueryID(), mailbox);
			}			
		}
		
		adv.setID(IDFactory.newPipeID(dictionary.getLocalPeerGroupID()));
		adv.setOwnerPeerId(dictionary.getLocalPeerID());
		for (PeerID id : dictionary.getRemotePeerIDs()) {
			if (!id.equals(dictionary.getLocalPeerID())) {
				jxtaServiceProvider.getDiscoveryService().remotePublish(
						id.toString(), adv, WAIT_TIME);
			}
		}		
		return executorService.submit(
				new Callable<List<CostResponseAdvertisement>>() {
			@Override
			public List<CostResponseAdvertisement> call() throws Exception {
				Thread.sleep(WAIT_TIME);
				synchronized(mailboxForPlanCosts) {
					Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.remove(adv.getSharedQueryID());
					return mailbox.getMails();
				}
			}
		});
	}
	
	
	@Override
	public Future<List<AuctionResponseAdvertisement>> publishAuction(
			final AuctionQueryAdvertisement adv) {
		synchronized(mailboxForAuctions) {
			Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(adv.getAuctionId());
			if(mailbox==null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForAuctions.put(adv.getAuctionId(), mailbox);
			}			
		}
		
		adv.setID(IDFactory.newPipeID(dictionary.getLocalPeerGroupID()));
		adv.setOwnerPeerId(dictionary.getLocalPeerID());
		for (PeerID id : dictionary.getRemotePeerIDs()) {
			if (!id.equals(dictionary.getLocalPeerID())) {
				jxtaServiceProvider.getDiscoveryService().remotePublish(
						id.toString(), adv, WAIT_TIME);
			}
		}		
		return executorService.submit(
				new Callable<List<AuctionResponseAdvertisement>>() {
			@Override
			public List<AuctionResponseAdvertisement> call() throws Exception {
				Thread.sleep(WAIT_TIME);
				synchronized(mailboxForAuctions) {
					Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.remove(adv.getAuctionId());
					return mailbox.getMails();
				}
			}
		});
	}	
	

	@Override
	public void advertisementAdded(IAdvertisementManager sender,
			Advertisement advertisement) {		
		if(advertisement instanceof CostResponseAdvertisement) {
			CostResponseAdvertisement adv = (CostResponseAdvertisement) advertisement;
			if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
				log.debug("Received response to query to calculate costs for plan {}", adv.getSharedQueryID().toString());
				synchronized(mailboxForPlanCosts) {
					Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.get(adv.getSharedQueryID());
					if(mailbox!=null) // es werden noch antworten mehr entgegegen genommen
						mailbox.getMails().add(adv);
				}
			}
		}
		else if (advertisement instanceof CostQueryAdvertisement) {
			CostQueryAdvertisement adv = ((CostQueryAdvertisement) advertisement);
			if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
				if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
					try {
						log.debug("Received query to calculate costs for plan {}", adv.getSharedQueryID().toString());
						ILogicalQuery plan = Helper.getLogicalQuery(executor, adv.getPqlStatement()).get(0);
						
						Map<String, CostSummary> costsProOperator = calculator.calcCostsProOperator(
								plan, adv.getTransCfgName(), false);
						
						CostSummary sum = CostSummary.calcSum(costsProOperator.values());
						
						CostSummary relativeCosts = this.calculator.calcBearableCostsInPercentage(sum);
						
						double bid = calculator.calcBid(plan.getLogicalPlan(),sum);
						
						CostResponseAdvertisement costAdv = (CostResponseAdvertisement) AdvertisementFactory.newAdvertisement(CostResponseAdvertisement.getAdvertisementType());
						costAdv.setCostSummary(costsProOperator);
						costAdv.setOwnerPeerId(dictionary.getLocalPeerID());
						costAdv.setPqlStatement(adv.getPqlStatement());
						costAdv.setTransCfgName(adv.getTransCfgName());
						costAdv.setSharedQueryID(adv.getSharedQueryID());
						costAdv.setID(IDFactory.newPipeID(dictionary.getLocalPeerGroupID()));
						costAdv.setPercentageOfBearableCpuCosts(relativeCosts.getCpuCost());
						costAdv.setPercentageOfBearableMemCosts(relativeCosts.getMemCost());
						costAdv.setBid(bid);
						this.jxtaServiceProvider.getDiscoveryService().remotePublish(
								adv.getOwnerPeerId().toString(), costAdv, WAIT_TIME);
						log.info("Sent costs ({}) for plan {} to peer {}",
								new String[] {sum.toString(), adv.getSharedQueryID().toString(), adv.getOwnerPeerId().toString()});
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
			}
		}
		else if(advertisement instanceof AuctionResponseAdvertisement) {
			AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) advertisement;
			if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
				synchronized(this.mailboxForAuctions) {
					Mailbox<AuctionResponseAdvertisement> mailbox = mailboxForAuctions.get(adv.getAuctionId());
					if(mailbox!=null) // es werden noch antworten mehr entgegegen genommen
						mailbox.getMails().add(adv);
				}
			}
		}	
		else if (advertisement instanceof AuctionQueryAdvertisement) {
			AuctionQueryAdvertisement adv = ((AuctionQueryAdvertisement) advertisement);
			if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
				if(!adv.getOwnerPeerId().equals(dictionary.getLocalPeerID())) {
					log.debug("Received query to bid to auction {}", adv.getAuctionId());
					ILogicalQuery query = Helper.getLogicalQuery(executor, adv.getPqlStatement()).get(0);
					CostSummary costs = calculator.calcCostsForPlan(query, adv.getTransCfgName());
					
					double bidValue = calculator.calcBid(query.getLogicalPlan(), costs)+0.2;	
					
					if(bidValue > 0) {
						final AuctionResponseAdvertisement bid = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
						bid.setAuctionId(adv.getAuctionId());
						bid.setBid(bidValue);
						bid.setOwnerPeerId(dictionary.getLocalPeerID());
						bid.setPqlStatement(adv.getPqlStatement());
						bid.setTransCfgName(adv.getTransCfgName());
						bid.setSharedQueryID(adv.getSharedQueryID());
						bid.setID(IDFactory.newPipeID(dictionary.getLocalPeerGroupID()));
						this.jxtaServiceProvider.getDiscoveryService().remotePublish(
								adv.getOwnerPeerId().toString(), bid, WAIT_TIME);
						log.info("Sent bid {} to auction {} of peer {}",
								new String[]{""+bidValue, adv.getAuctionId(), adv.getOwnerPeerId().toString()});
					}
					else {
						log.info("Offering no bid to auction {} of peer {}", adv.getAuctionId(),adv.getOwnerPeerId().toString());
					}
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender,
			Advertisement adv) {
		
	}

	@Override
	public Collection<PeerID> getRemotePeerIds() {
		return dictionary.getRemotePeerIDs();
	}

	@Override
	public PeerGroupID getLocalPeerGroupId() {
		return dictionary.getLocalPeerGroupID();
	}
	
	@Override
	public PeerID getLocalPeerID() {
		return dictionary.getLocalPeerID();
	}	

	@Override
	public void sendSubPlan(String destination,
			QueryPartAdvertisement adv) {
		adv.setID(IDFactory.newPipeID(dictionary.getLocalPeerGroupID()));
		adv.setPeerID(destination);
		jxtaServiceProvider.getDiscoveryService().remotePublish(destination, adv, WAIT_TIME);	
	}
	
	@Override
	public ID generateSharedQueryId() {
		final String timestamp = String.valueOf(System.currentTimeMillis());
		return IDFactory.newContentID(dictionary.getLocalPeerGroupID(), false, timestamp.getBytes());
	}
}
