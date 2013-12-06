package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

import java.util.List;

import net.jxta.id.ID;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;

public final class PlanProOperatorPartitioner  {
	
	private PlanProOperatorPartitioner() {
		
	}
	
	public static List<SubPlan> partitionWithDummyOperators(ILogicalOperator query, ID sharedQueryId, QueryBuildConfiguration transCfg) {
		List<SubPlan> parts = partitionLogical(query, sharedQueryId, transCfg); 
		SubPlanManipulator.insertDummyAOs(parts);
		return parts;
	}

	private static List<ILogicalOperator> collectOperators(ILogicalOperator currentOperator) {
		List<ILogicalOperator> subPlans = Lists.newArrayList();
		_collectOperators(currentOperator, subPlans);
		return subPlans;
	}
	
	private static void _collectOperators(ILogicalOperator currentOperator,
			List<ILogicalOperator> subPlans) {
		if (!(subPlans.contains(currentOperator))) {
			subPlans.add(currentOperator);
			
			for (final LogicalSubscription subscription : currentOperator
					.getSubscriptions()) {
				_collectOperators(subscription.getTarget(), subPlans);
			}

			for (final LogicalSubscription subscription : currentOperator
					.getSubscribedToSource()) {
				_collectOperators(subscription.getTarget(), subPlans);
			}	
		}	
	}

	private static List<SubPlan> partitionLogical(ILogicalOperator query,
			ID sharedQueryId, QueryBuildConfiguration transCfg) {
		
		final List<ILogicalOperator> operators = collectOperators(query);
		List<SubPlan> parts = Lists.newArrayList();
		List<ILogicalOperator> visitedOperators = Lists.newArrayList();
		
		for (final ILogicalOperator operator : operators) {
			if (operator instanceof TopAO) {
				for(LogicalSubscription sub : operator.getSubscribedToSource()) {
					ILogicalOperator src = sub.getTarget();
					if(!visitedOperators.contains(src)) {
						parts.add(new SubPlan("local", src));
						visitedOperators.add(src);
					}
				}
				operator.unsubscribeFromAllSources();
			}
			else if(operator.getSubscriptions().isEmpty()) {
				if(!visitedOperators.contains(operator)) {
					parts.add(new SubPlan("local", operator));
					visitedOperators.add(operator);
				}
			}
			else if(Helper.isSourceOperator(operator)){
				if(!visitedOperators.contains(operator)) {
					parts.add(new SubPlan(operator));
					visitedOperators.add(operator);
				}
			}
			else {
				if(!visitedOperators.contains(operator)) {
					parts.add(new SubPlan(operator));
					visitedOperators.add(operator);
				}
			}
		}
		return parts;
	}
}
