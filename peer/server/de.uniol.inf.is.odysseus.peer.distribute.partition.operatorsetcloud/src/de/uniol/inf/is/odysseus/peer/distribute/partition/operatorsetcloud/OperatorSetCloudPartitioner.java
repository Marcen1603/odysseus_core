package de.uniol.inf.is.odysseus.peer.distribute.partition.operatorsetcloud;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class OperatorSetCloudPartitioner implements IQueryPartitioner {

	private static final StatefulOperatorsCollection STATEFUL_OPERATORS = new StatefulOperatorsCollection();
	
	@Override
	public String getName() {
		return "operatorsetcloud";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> ops, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {

		Collection<ILogicalOperator> operatorsToVisit = Lists.newArrayList(ops);
		Collection<ILogicalQueryPart> queryParts = Lists.newArrayList();
		
		while (!operatorsToVisit.isEmpty()) {

			ILogicalOperator operatorToStart = operatorsToVisit.iterator().next();

			Collection<ILogicalOperator> collectedOperators = Lists.newArrayList();
			collectUntilStatefulOperators(operatorToStart, collectedOperators);
			
			operatorsToVisit.removeAll(collectedOperators);
			
			queryParts.add( new LogicalQueryPart(collectedOperators));
		}

		return queryParts;
	}

	private static void collectUntilStatefulOperators(ILogicalOperator operator, Collection<ILogicalOperator> visitedOperators) {
		if( visitedOperators.contains(operator)) {
			return;
		}
		
		visitedOperators.add(operator);

		for (LogicalSubscription sub : operator.getSubscriptions()) {
			ILogicalOperator targetOperator = sub.getTarget();
			if( !isStateful(targetOperator)) {
				collectUntilStatefulOperators(sub.getTarget(), visitedOperators);
			}
		}

		if( !isStateful(operator)) {
			for (LogicalSubscription sub : operator.getSubscribedToSource()) {
				ILogicalOperator targetOperator = sub.getTarget();
				collectUntilStatefulOperators(targetOperator, visitedOperators);
			}
		}
	}

	private static boolean isStateful(ILogicalOperator operator) {
		return STATEFUL_OPERATORS.isStateful(operator);
	}

}
