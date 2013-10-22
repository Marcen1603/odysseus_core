package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public class FixedSizePartitioner extends AbstractPartitioner {
	
	public void activate() {
	}

	public void deactivate() {
	}		

	public FixedSizePartitioner() {
		super();
	}
	
	@Override
	public List<SubPlan> partitionWithDummyOperators(
			ILogicalOperator queryPlan, ID sharedQueryId,
			QueryBuildConfiguration transCfg) throws CouldNotPartitionException {
		
		List<CostResponseAdvertisement> advertisements = this.requestCostsForPlan(queryPlan, sharedQueryId, transCfg.getName());
		Map<String, CostSummary> costsProOperator = this.calcAvgCostsProOperator(queryPlan, advertisements, transCfg.getName());
		
		List<SubPlan> subPlans = this.seperateLocalSubPlansLogical(queryPlan);
		manipulator.insertDummyAOs(subPlans);
		SubPlan planToDistribute = subPlans.get(1); // 0=localPlan
		
		List<SubPlan> result = _partition(planToDistribute, costsProOperator, new TargetSize() {

			@Override
			public double getNextSize(double totalAbsoluteCosts) {
				return 0.3; // 0,3 cpu costs pro partition
			}
			
		});
		result.add(subPlans.get(0));
		return subPlans;
	}
}
