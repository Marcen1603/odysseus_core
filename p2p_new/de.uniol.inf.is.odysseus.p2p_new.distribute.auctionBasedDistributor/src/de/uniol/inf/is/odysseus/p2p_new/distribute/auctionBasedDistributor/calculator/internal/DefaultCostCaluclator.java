package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.internal;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator.Communicator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal.PlanProOperatorPartitioner;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.SubPlanManipulator;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class DefaultCostCaluclator implements CostCalculator {
	private static final Logger log = LoggerFactory.getLogger(DefaultCostCaluclator.class);
	private OperatorCostModel costModel;
	private IServerExecutor executor;
	private PlanProOperatorPartitioner operatorPartitioner;
	private IPQLGenerator generator;
	private Communicator communicator;
	
	private SubPlanManipulator manipulator;
	private BidCalculator bidCalculator;
	private OperatorCostsCalculator operatorCostsCalculator;
	private RelativeCostsCalculator relativeCostsCalculator;
	
	public DefaultCostCaluclator() {
	}
	
	public void activate() {
		manipulator = new SubPlanManipulator();
		bidCalculator = new BidCalculator(executor, costModel);
		operatorCostsCalculator = new OperatorCostsCalculator(
				executor, generator,
				operatorPartitioner, costModel, manipulator);
		relativeCostsCalculator = new RelativeCostsCalculator(costModel, executor, generator);
	}
	
	public void deactivate() {
		
	}
	
	public void bindCostModel(OperatorCostModel costModel) {
		this.costModel = costModel;
		log.debug("CostModel binded {}", costModel);
	}
	
	public void unbindCostModel(OperatorCostModel dataDictionaryProvider) {
		if( this.costModel == costModel ) {
			costModel = null;
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
	
	public void bindPlanProOperatorPartitioner(Partitioner operatorPartitioner) {
		this.operatorPartitioner = (PlanProOperatorPartitioner) operatorPartitioner;
	}
	
	public void unbindPlanProOperatorPartitioner(Partitioner operatorPartitioner) {
		if( this.operatorPartitioner == operatorPartitioner ) {
			operatorPartitioner = null;
		}
	}			
	
	public double calcBid(String pqlStatement, String transCfgName) {
		return bidCalculator.calcBid(pqlStatement, transCfgName);
	}

	@Override
	public CostSummary calcCostsForPlan(ILogicalQuery query, String transCfgName) {					
		IPhysicalQuery p = Helper.getPhysicalQuery(executor, query, transCfgName);
		
		ICost<IPhysicalOperator> cost = costModel.estimateCost(p.getPhysicalChilds(), false);
		OperatorCost<IPhysicalOperator>  c = ((OperatorCost<IPhysicalOperator> )cost);
		
		double cpuCost = c.getCpuCost();
		double memCost = c.getMemCost();
		return new CostSummary(null, cpuCost, memCost, query.getLogicalPlan());
	}

	@Override
	public CostSummary calcBearableCostsInPercentage(
			CostSummary absoluteCosts) {
		return relativeCostsCalculator.calcBearableCostsInPercentage(absoluteCosts.getOperatorId(),
				absoluteCosts.getCpuCost(), absoluteCosts.getMemCost(), absoluteCosts.getPlan());
	}

	@Override
	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts) {
		return bidCalculator.calcBid(plan, absolutePlanCosts.getCpuCost(), absolutePlanCosts.getMemCost());
	}

	@Override
	public Map<String, CostSummary> calcCostsProOperator(ILogicalQuery plan,
			String transCfgName, boolean discardPlan) {
		
		if(!discardPlan) {
			plan = Helper.copyQuery(generator, executor, plan);
		}		
		
		return operatorCostsCalculator.calcCostsProOperator(plan.getLogicalPlan(), transCfgName);
	}		
}
