package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.Helper;

public class SurveyBasedPartitioner implements IQueryPartitioner {

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws QueryPartitionException {
		Helper.addOperatorIds(operators);
		ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), true);

		// copy --> original
		Map<ILogicalOperator, ILogicalOperator> operatorCopyMap = createOperatorCopyMap(operators);
		createSubscriptionsInCopies(operatorCopyMap);
		try {
			List<SubPlan> subPlans = SurveyBasedPartitionerImpl.partition(operatorCopyMap.keySet().iterator().next(), sharedQueryID, config);
			List<ILogicalQueryPart> logicalQueries = transformToLogicalPlan(subPlans, operatorCopyMap);

			return logicalQueries;
		} catch (CouldNotPartitionException e) {
			throw new QueryPartitionException("Could not use partitioner", e);
		}
	}

	private static Map<ILogicalOperator, ILogicalOperator> createOperatorCopyMap(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, ILogicalOperator> copyMap = Maps.newHashMap();
		for (ILogicalOperator operator : operators) {
			copyMap.put(operator.clone(), operator);
		}
		return copyMap;
	}

	private static void createSubscriptionsInCopies(Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for( ILogicalOperator copyOperator : operatorCopyMap.keySet()) {
			ILogicalOperator originalOperator = operatorCopyMap.get(copyOperator);
			
			for( LogicalSubscription sub : originalOperator.getSubscriptions() ) {
				ILogicalOperator originalTarget = sub.getTarget();
				ILogicalOperator copyTarget = getCopyOfMap(originalTarget, operatorCopyMap);
				
				copyOperator.subscribeSink(copyTarget, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			}
		}
	}

	private static ILogicalOperator getCopyOfMap(ILogicalOperator originalOperator, Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for( ILogicalOperator copy : operatorCopyMap.keySet() ) {
			if( operatorCopyMap.get(copy).equals(originalOperator)) {
				return copy;
			}
		}
		
		throw new RuntimeException("Could not find the copy of " + originalOperator);
	}

	private static List<ILogicalQueryPart> transformToLogicalPlan(List<SubPlan> subPlans, Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		List<ILogicalQueryPart> logicalQueries = Lists.newArrayList();
		for (SubPlan subPlan : subPlans) {
			List<ILogicalOperator> operatorsForLogicalQueryPart = Lists.newArrayList();

			for (ILogicalOperator operator : subPlan.getOperators()) {
				operatorsForLogicalQueryPart.add(operatorCopyMap.get(operator));
			}

			logicalQueries.add(new LogicalQueryPart(operatorsForLogicalQueryPart));
		}
		return logicalQueries;
	}

}
