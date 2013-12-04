package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.partitioner.internal;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;

public class SurveyBasedPartitioner extends AbstractPartitioner {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SurveyBasedPartitioner.class);
	
	public void activate() {
	}

	public void deactivate() {
	}		

	public SurveyBasedPartitioner() {
		super();
	}
	
	@Override
	public List<SubPlan> partitionWithDummyOperators(ILogicalOperator queryPlan, ID sharedQueryId, QueryBuildConfiguration transCfg)
			throws CouldNotPartitionException {		

		List<CostResponseAdvertisement> advertisements = this.requestCostsForPlan(queryPlan, sharedQueryId, transCfg.getName());		
		Map<String, CostSummary> costsProOperator = this.calcAvgCostsProOperator(queryPlan, advertisements, transCfg.getName());
		
		final List<Vote> votes = summariseVotes(advertisements);
		
		List<SubPlan> subPlans = this.seperateLocalSubPlansLogical(queryPlan);
		manipulator.insertDummyAOs(subPlans);
		SubPlan planToDistribute = subPlans.get(0); // 1+ -> localPlans
				
		List<SubPlan> result = _partition(planToDistribute, costsProOperator, new TargetSize() {
			@Override
			public double getNextSize(double totalAbsoluteCosts) {
				if(votes.iterator().hasNext()) {
					return totalAbsoluteCosts * votes.iterator().next().getPercentageOfBearableCosts();
				}
				
				return 0;
			}
		});
		
		if(subPlans.size()>1)
			result.addAll(subPlans.subList(1, subPlans.size()));
		return result;
	}
	
	private List<Vote> summariseVotes(List<CostResponseAdvertisement> votes) throws CouldNotPartitionException {
		double totalCosts = 0;
		if(votes == null || votes.isEmpty())
			throw new CouldNotPartitionException("Survey was not successfull: Got no votes");
		
		Map<Double, Vote> mappedVotes = Maps.newHashMap();
		for(CostResponseAdvertisement vote: votes) {
			// aufrunden in 10er SChritten
			// z.b.: 83,4 => round(8,34)=8 => 8*10=80
			double bearableCosts = vote.getPercentageOfBearableCpuCosts();
			bearableCosts *= 100; // die werde liegen zwischen [0,1]
			bearableCosts = Math.round(bearableCosts/10)*10;
			bearableCosts /= 100; // wieder auf [0,1] abbilden
			
			Vote total = mappedVotes.get(bearableCosts);
			if(total==null) {
				total = new Vote(0, bearableCosts, 0);
			}
			Vote newVote = new Vote(total.getBid()+vote.getBid(), bearableCosts, total.getCount()+1);
			mappedVotes.put(bearableCosts, newVote);
			totalCosts+=bearableCosts;
		}			
		
		if(totalCosts<1) {
			throw new CouldNotPartitionException("Can't partition the plan completely");
		}
		
		List<Vote> summarisedVotes = Lists.newArrayList(mappedVotes.values());
		Collections.sort(summarisedVotes);
		Collections.reverse(summarisedVotes);
		return summarisedVotes;
	}
}
