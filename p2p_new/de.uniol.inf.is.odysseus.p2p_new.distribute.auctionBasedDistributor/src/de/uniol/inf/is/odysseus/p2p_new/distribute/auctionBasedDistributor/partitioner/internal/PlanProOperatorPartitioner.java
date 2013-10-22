package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

import java.util.List;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.SubPlanManipulator;

public class PlanProOperatorPartitioner extends AbstractPartitioner {
	private static final Logger log = LoggerFactory
			.getLogger(PlanProOperatorPartitioner.class);

	public void activate() {
	}

	public void deactivate() {
	}		

	public PlanProOperatorPartitioner() {
		super();
	}
	@Override
	public List<SubPlan> partitionWithDummyOperators(ILogicalOperator query, ID sharedQueryId, QueryBuildConfiguration transCfg) throws CouldNotPartitionException {
		List<SubPlan> parts = partitionLogical(query, sharedQueryId, transCfg); 
		manipulator.insertDummyAOs(parts);
		return parts;
	}

	private List<ILogicalOperator> collectOperators(ILogicalOperator currentOperator) {
		List<ILogicalOperator> subPlans = Lists.newArrayList();
		_collectOperators(currentOperator, subPlans);
		return subPlans;
	}
	
	private void _collectOperators(ILogicalOperator currentOperator,
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

	private List<SubPlan> partitionLogical(ILogicalOperator query,
			ID sharedQueryId, QueryBuildConfiguration transCfg)
			throws CouldNotPartitionException {
		
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
//					log.debug("Root operator and added to sub plan: {}", operator);
					parts.add(new SubPlan("local", operator));
					visitedOperators.add(operator);
				}
			}
			else if(Helper.isSourceOperator(operator)){
//				log.debug("StreamAO operator found");

				if(!visitedOperators.contains(operator)) {
//					parts.add(new SubPlan("local", operator));
					visitedOperators.add(operator);
				}
			}
			else {
				if(!visitedOperators.contains(operator)) {
					parts.add(new SubPlan(null, operator));
					visitedOperators.add(operator);
//					log.debug("New sub plan added: {}", operator);
				}
			}
		}
		return parts;
	}
}
