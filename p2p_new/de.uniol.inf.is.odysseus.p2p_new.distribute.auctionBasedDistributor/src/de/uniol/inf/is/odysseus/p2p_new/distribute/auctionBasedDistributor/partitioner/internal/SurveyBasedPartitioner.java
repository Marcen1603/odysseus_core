package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.SubPlanManipulator;

public class SurveyBasedPartitioner extends AbstractPartitioner {
	private static final Logger log = LoggerFactory.getLogger(SurveyBasedPartitioner.class);
	private SubPlanManipulator partManipulator;
	
	public void activate() {
		partManipulator = new SubPlanManipulator();
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
		advertisements.add(calcCostsProOperaotr(queryPlan,  transCfg.getName()));
		
		Map<String, CostSummary> costsProOperator = this.calcAvgCostsProOperator(queryPlan, advertisements);
		
		final List<Vote> votes = summariseVotes(advertisements);
		
		List<SubPlan> subPlans = this.seperateLocalSubPlansLogical(queryPlan);
		partManipulator.insertDummyAOs(subPlans);
		SubPlan planToDistribute = subPlans.get(1); // 0=localPlan
				
		List<SubPlan> result = _partition(planToDistribute, costsProOperator, votes);
		result.add(subPlans.get(0));
		return subPlans;
	}

	public List<SubPlan> _partition(SubPlan planToDistribute,
			final Map<String, CostSummary> planCost, final List<Vote> votes) {
		final ILogicalOperator startOperator = getSomeLeafOperator(planToDistribute);
		final List<SubPlan> subPlans = Lists.newArrayList();
		final List<ILogicalOperator> visitedOperators = Lists.newArrayList();

		
		final double targetAbsoluteCosts[] = new double[1];
		final double currentAbsoluteSubPlanCosts[] =  new double[1];
		final double currentAbsoluteTotalCosts[] =  new double[1];
		final double totalAbsoluteCosts = getTotalAbsoluteCosts(planCost);
		final List<ILogicalOperator> currentSubPlanOperators = Lists.newArrayList();
		
		currentAbsoluteSubPlanCosts[0] = 0;
		targetAbsoluteCosts[0] = totalAbsoluteCosts * votes.iterator().next().getPercentageOfBearableCosts();
		
		Helper.iterateThroughPlan(startOperator, visitedOperators,
				new Function<ILogicalOperator, Void>() {
					@Override
					public Void apply(ILogicalOperator operator) {
						String id = operator.getParameterInfos().get("id".toUpperCase());
						double operatorAbsoluteCosts = planCost.get(id).getCpuCost();
						double newCosts = currentAbsoluteSubPlanCosts[0] + operatorAbsoluteCosts;
						
						currentAbsoluteTotalCosts[0] += operatorAbsoluteCosts;
						if(currentAbsoluteTotalCosts[0]<totalAbsoluteCosts) {
							if(newCosts<=targetAbsoluteCosts[0]) {
								currentSubPlanOperators.add(operator);
								currentAbsoluteSubPlanCosts[0] += operatorAbsoluteCosts;
							}
							else {
								subPlans.add(new SubPlan(null,
										currentSubPlanOperators.toArray(new ILogicalOperator[0])));
								// sollte kein fehler auftreten, da vorher schon überprüft wurde,
								// ob der plan in seiner gänze zerlegt werden kann
								targetAbsoluteCosts[0] = totalAbsoluteCosts * votes.iterator().next().getPercentageOfBearableCosts();
								currentAbsoluteSubPlanCosts[0] = operatorAbsoluteCosts;
								currentSubPlanOperators.clear();
								currentSubPlanOperators.add(operator);
							}
						}
						else {
							currentSubPlanOperators.add(operator);
							subPlans.add(new SubPlan(null,
									currentSubPlanOperators.toArray(new ILogicalOperator[0])));
						}
						return null;
					}				
		});
		// correct 
		if(subPlans.isEmpty()) {
			subPlans.add(new SubPlan(null,
					currentSubPlanOperators.toArray(new ILogicalOperator[0])));
		}
		return subPlans;
	}

	private List<SubPlan> seperateLocalSubPlansLogical(ILogicalOperator plan) {
		List<SubPlan> newSubPlans = Lists.newArrayList();
		
		SubPlan localPlan = null;
		
		SubPlan totalPlan = new SubPlan(null,
				DistributionHelper.collectOperators(plan).toArray(new ILogicalOperator[0]));
		
		List<ILogicalOperator> localOperators = totalPlan.findOperatorsByType(AccessAO.class, StreamAO.class, TopAO.class);
		if(!localOperators.isEmpty()) {
			// applicationtime und topao...
			
			for(ILogicalOperator localOperator : Lists.newArrayList(localOperators)) {
				if (localOperator instanceof TopAO) {
					for(LogicalSubscription sub : localOperator.getSubscribedToSource()) {
						ILogicalOperator src = sub.getTarget();
						if(!localOperators.contains(src)) {
							localOperators.add(src);
						}
					}
					localOperator.unsubscribeFromAllSources();
					localOperators.remove(localOperator);
				}
			}
			
			for(ILogicalOperator localOperator : Lists.newArrayList(localOperators)) {
				for(LogicalSubscription sub : localOperator.getSubscriptions()) {
					if(sub.getTarget() instanceof TimestampAO) {
						localOperators.add(sub.getTarget());
						localOperators.addAll(allSubscriptions(sub.getTarget()));
					}
				}
			}
			
			localPlan = new SubPlan("local", localOperators.toArray(new ILogicalOperator[0]));
			totalPlan.removeOperators(localOperators.toArray(new ILogicalOperator[0]));
			newSubPlans.add(localPlan);
		}
		newSubPlans.add(totalPlan);
		return newSubPlans;
	}

	private List<ILogicalOperator> allSubscriptions(ILogicalOperator target) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		if(target.getSubscriptions().isEmpty()) {
			operators.add(target);
		}
		else {
			for(LogicalSubscription sub : target.getSubscriptions()) {
				operators.addAll(allSubscriptions(sub.getTarget()));				
			}
		}
		return operators;
	}

	private double getTotalAbsoluteCosts(Map<String, CostSummary> planCost) {
		double costs = 0;
		for(CostSummary c : planCost.values()) {
			costs+=c.getCpuCost()*100;
		}
		return costs/100;
	}

	private ILogicalOperator getSomeLeafOperator(SubPlan planToDistribute) {
		try {
			if(!planToDistribute.getSources().isEmpty()) {
				return planToDistribute.getSources().get(0);
			}
			else {
				return planToDistribute.getDummySources().get(0);
			}
		}
		catch(IndexOutOfBoundsException | NoSuchElementException e) {
			throw new RuntimeException("Could not find any source/leaf operator in the query plan");	
		}
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
