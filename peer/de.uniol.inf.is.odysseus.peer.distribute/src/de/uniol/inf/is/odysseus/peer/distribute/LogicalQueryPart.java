package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class LogicalQueryPart implements ILogicalQueryPart {

	private final Collection<ILogicalOperator> operators = Lists.newArrayList();
	
	public LogicalQueryPart(Collection<ILogicalOperator> partOperators) {
		Preconditions.checkNotNull(partOperators, "List operators forming a logical query part must not be null!");
		Preconditions.checkArgument(!partOperators.isEmpty(), "List of operators forming a logical query part must not be empty!");
		
		operators.addAll(partOperators);
	}

	@Override
	public ImmutableCollection<ILogicalOperator> getOperators() {
		return ImmutableList.copyOf(operators);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( ILogicalOperator operator : operators ) {
			sb.append(operator.getName()).append("  ");
		}
		sb.append("}");
		return sb.toString();
	}
}
	