package de.uniol.inf.is.odysseus.costmodel.logical.util;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

public class CostModelUtil {

	private CostModelUtil() {
		
	}
	
	public static Collection<ILogicalOperator> getAllLogicalOperators(ILogicalOperator logicalOperator) {
		Collection<ILogicalOperator> allOperators = Lists.newArrayList();
		
		collectLogicalOperators(logicalOperator, allOperators);
		
		return allOperators;
	}

	private static void collectLogicalOperators(ILogicalOperator logicalOperator, Collection<ILogicalOperator> allOperators) {
		if( !allOperators.contains(logicalOperator)) {
			allOperators.add(logicalOperator);
			
			for( LogicalSubscription logSub : logicalOperator.getSubscriptions() ) {
				collectLogicalOperators(logSub.getSink(), allOperators);
			}
			
			for( LogicalSubscription logSub : logicalOperator.getSubscribedToSource() ) {
				collectLogicalOperators(logSub.getSource(), allOperators);
			}
		}
	}


	public static Collection<ILogicalOperator> filterForLogicalSources(Collection<ILogicalOperator> operators) {
		return Collections2.filter(operators, new Predicate<ILogicalOperator>() {
			@Override
			public boolean apply(ILogicalOperator input) {
				return input.getSubscribedToSource().isEmpty();
			}
		});
	}
}
