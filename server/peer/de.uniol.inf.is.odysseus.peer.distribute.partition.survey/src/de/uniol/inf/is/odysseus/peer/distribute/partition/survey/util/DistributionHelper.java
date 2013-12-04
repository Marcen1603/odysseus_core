package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

public class DistributionHelper {

	public static Collection<ILogicalOperator> collectOperators(ILogicalOperator currentOperator) {
		List<ILogicalOperator> result = Lists.newArrayList();
		collectOperatorsImpl(currentOperator, result);
		return result;
	}
	
	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}
}
