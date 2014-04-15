package de.uniol.inf.is.odysseus.peer.distribute.partition.operatorsetcloud;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;


public final class StatefulOperatorsCollection {

	private final Collection<Class<? extends ILogicalOperator>> statefulOperators = Lists.newArrayList();
	
	public StatefulOperatorsCollection() {
		statefulOperators.add(UnionAO.class);
		statefulOperators.add(JoinAO.class);
		statefulOperators.add(AggregateAO.class);
		statefulOperators.add(JoinAO.class);
	}

	public boolean isStateful( ILogicalOperator operator ) {
		Preconditions.checkNotNull(operator, "Operator to check for statfulness must not be null!");
		
		return statefulOperators.contains(operator.getClass());
	}
}
