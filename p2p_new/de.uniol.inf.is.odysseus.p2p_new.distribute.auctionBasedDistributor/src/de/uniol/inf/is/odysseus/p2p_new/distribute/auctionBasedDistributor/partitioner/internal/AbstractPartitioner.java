package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator.Communicator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.SubPlanManipulator;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public abstract class AbstractPartitioner implements Partitioner {
	private static final Logger log = LoggerFactory.getLogger(AbstractPartitioner.class);
	protected Communicator communicator;
	protected IPQLGenerator generator;
	protected CostCalculator costCalculator;
	protected SubPlanManipulator manipulator;
	protected IServerExecutor executor;
	
	public AbstractPartitioner() {
		manipulator = new SubPlanManipulator();
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
		this.costCalculator = calculator;
	}
	
	public void unbindCostCalculator(CostCalculator calculator) {
		if( this.costCalculator == calculator ) {
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
	
	public List<CostResponseAdvertisement> requestCostsForPlan(ILogicalOperator plan, ID sharedQueryId,
			String transCfgName) {
		final CostQueryAdvertisement adv = (CostQueryAdvertisement) AdvertisementFactory
				.newAdvertisement(CostQueryAdvertisement.getAdvertisementType());
		adv.setPqlStatement(generator.generatePQLStatement(plan));
		adv.setSharedQueryID(sharedQueryId);
		adv.setTransCfgName(transCfgName);
		Future<List<CostResponseAdvertisement>> futureResult = communicator.askPeersForPlanCosts(adv);
		
		try {
			return futureResult.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, CostSummary> calcAvgCostsProOperator(final ILogicalOperator query, final List<CostResponseAdvertisement> advertisements) {
		Map<String, CostSummary> costs = Maps.newHashMap();
		
		for(CostResponseAdvertisement adv : advertisements) {
			for(String operatorId : adv.getCostSummary().keySet()) {
				CostSummary newCost = adv.getCostSummary().get(operatorId);
				CostSummary costTotal = costs.get(operatorId);
				if(costTotal==null) {
					costTotal = new CostSummary(operatorId, 0, 0, query);
				}
				costTotal = new CostSummary(operatorId, costTotal.getCpuCost()+newCost.getCpuCost(),
						costTotal.getMemCost()+newCost.getMemCost(), query);
				costs.put(operatorId, costTotal);
			}
		}
		
		costs = Maps.transformEntries(costs, new EntryTransformer<String, CostSummary, CostSummary>() {
			@Override
			public CostSummary transformEntry(String key, CostSummary value) {
				return new CostSummary(key, value.getCpuCost()/advertisements.size(),
						value.getMemCost()/advertisements.size(), query);
			}
		
		});
		
		costs = addCostsForIgnoredOperators(query, costs);
		
		return costs;
	}
	
	protected CostResponseAdvertisement calcCostsProOperaotr(
			ILogicalOperator plan, String transCfgName) {
		
		Map<String, CostSummary> costsProOperator = 
				this.costCalculator.calcCostsProOperator(
						Helper.wrapInLogicalQuery(plan, null, null), transCfgName, false);
		
		CostSummary sum = CostSummary.calcSum(costsProOperator.values());
		CostSummary relativeCosts = this.costCalculator.calcBearableCostsInPercentage(sum);
		double bid = this.costCalculator.calcBid(plan, sum);
		
		CostResponseAdvertisement costAdv = (CostResponseAdvertisement) AdvertisementFactory.newAdvertisement(CostResponseAdvertisement.getAdvertisementType());
		costAdv.setCostSummary(costsProOperator);
		costAdv.setOwnerPeerId(communicator.getLocalPeerID());
		costAdv.setPqlStatement(null);
		costAdv.setTransCfgName(null);
		costAdv.setSharedQueryID(null);
		costAdv.setID(null);
		costAdv.setPercentageOfBearableCpuCosts(relativeCosts.getCpuCost());
		costAdv.setPercentageOfBearableMemCosts(relativeCosts.getMemCost());
		costAdv.setBid(bid);
		
		return costAdv;
	}

	private Map<String, CostSummary> addCostsForIgnoredOperators(ILogicalOperator query,
			Map<String, CostSummary> costs) {
		costs = Maps.newHashMap(costs);
		for(ILogicalOperator op : DistributionHelper.collectOperators(query)) {
			String opId = op.getParameterInfos().get("id".toUpperCase());
			CostSummary cost = costs.get(opId);
			if(cost==null) {
				costs.put(opId, new CostSummary(opId, 0, 0, query));
			}
		}
		return costs;
	}
}
