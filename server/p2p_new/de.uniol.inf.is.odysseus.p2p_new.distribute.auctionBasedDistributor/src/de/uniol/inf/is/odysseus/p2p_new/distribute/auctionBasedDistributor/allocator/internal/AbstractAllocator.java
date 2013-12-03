package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.allocator.internal;

import java.util.List;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.allocator.Allocator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator.Communicator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.AuctionSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.SubPlanManipulator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public abstract class AbstractAllocator implements Allocator {
	private static final Logger log = LoggerFactory.getLogger(AbstractAllocator.class);
	protected SubPlanManipulator partManipulator = new SubPlanManipulator();
	protected IPQLGenerator generator;
	protected Communicator communicator;
	protected CostCalculator calculator;
	protected IServerExecutor executor;
	
	public AbstractAllocator() {
	}
	
	public void bindPQLGenerator(IPQLGenerator generator) {
		this.generator = generator;
	}
	
	public void unbindPQLGenerator(IPQLGenerator generator) {
		if( this.generator == generator ) {
			generator = null;
		}
	}
	
	public void bindCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}
	
	public void unbindCommunicator(Communicator communicator) {
		if( this.communicator == communicator ) {
			communicator = null;
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
	
	@Override
	public ILogicalQuery allocate(ILogicalQuery query,
			ID sharedQueryID, List<SubPlan> subPlans,
			QueryBuildConfiguration transCfg) {
		
		// ignoriert lokal auszuführende pläne
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(sharedQueryID, subPlans, transCfg);
		
		subPlans = evaluateBids(auctions, subPlans);
		
		// falls alle Pläne lokale ausgeführt werden soll,
		// füge keine Jxta-Operatoren hinzu
		if(!(subPlans.size() == getLocalPlans(subPlans).size())) {
			partManipulator.insertJxtaOperators(subPlans, sharedQueryID.toString(),
				communicator.getLocalPeerGroupId());						
			
			// ignoriert lokal auszuführende pläne
			assignRemoteSubPlans(subPlans, sharedQueryID, transCfg);		
		}
		
		List<SubPlan> mergedLocalPlans = getLocalPlans(subPlans);				
		
		ILogicalQuery plan = Helper.transformToQueryWithTopAO(mergedLocalPlans, generator, query.toString());
		
		return plan;
	}

	/**
	 * Untersucht die Ergebnisse der durchgeführten Auktionen
	 * und stellt eine logische Beziehung zwischen Teilplänen und Peers her
	 * durch das Setzen des DestinationNamens (subPlan.getDestinationName).
	 * Diese Beziehung wird im nächsten internen Verarbeitungsschritt genutzt,
	 * um die Pläne zu allokieren.
	 * @param auctions Zusammenfassung der durchgeführten Auktionen
	 * @param subPlans Teilpläne, die Gegendstand der Auktionen waren
	 * @return Zu allokierende Teilpläne
	 */
	protected abstract List<SubPlan> evaluateBids(List<AuctionSummary> auctions,
			List<SubPlan> subPlans);
	
	private List<AuctionSummary> publishAuctionsForRemoteSubPlans(ID sharedQueryID, List<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(subPlans, "SubPlans must not be null!");
		
		List<AuctionSummary> auctions = Lists.newArrayList();
		
		int auctionsCount = 0;
		for(SubPlan subPlan : subPlans) {		
			if(subPlan.getDestinationName()!=null &&
					subPlan.getDestinationName().equals("local")) {
					log.info("Sub plan {} will be placed on local host", subPlan);
					continue;
			}
			final AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());
			adv.setPqlStatement(generator.generatePQLStatement(subPlan.getLogicalPlan()));
			adv.setSharedQueryID(sharedQueryID);
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(UUID.randomUUID().toString());
			auctions.add(new AuctionSummary(adv, communicator.publishAuction(adv), subPlan));
//			log.debug("Sub plan {} listed for auction", subPlan);
			auctionsCount++;
		}	
			
		log.info("Auctions for {} remote sub plans listed (sharedQueryId: {})", auctionsCount, sharedQueryID);
		
		return auctions;
	}
	
	private void assignRemoteSubPlans(List<SubPlan> subPlans, ID sharedQueryID, QueryBuildConfiguration transCfg) {
		for(SubPlan plan : subPlans) {
			if(plan.getDestinationName().equals("local"))
				continue;
			
			final QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory
					.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
			adv.setPqlStatement(generator.generatePQLStatement(plan.getLogicalPlan()));
			adv.setSharedQueryID(sharedQueryID);
			adv.setTransCfgName(transCfg.getName());			
		
			communicator.sendSubPlan(plan.getDestinationName(), adv);
		}		
	}		
	
	protected List<SubPlan> getLocalPlans(List<SubPlan> subPlans) {
		List<SubPlan> localParts = Lists.newArrayList();
		for(SubPlan subPlan : subPlans) {
			if(subPlan.getDestinationName()!=null && subPlan.getDestinationName().equals("local")) {
				localParts.add(subPlan);
			}
		}	
		
		return localParts;
	}		
	
	protected double calcOwnBid(String pqlStatement, String transCfgName) {
		ILogicalQuery query = Helper.getLogicalQuery(executor, pqlStatement).get(0);
		CostSummary costs = calculator.calcCostsForPlan(query, transCfgName);		
		
		return calculator.calcBid(query.getLogicalPlan(), costs);
	}	
}
