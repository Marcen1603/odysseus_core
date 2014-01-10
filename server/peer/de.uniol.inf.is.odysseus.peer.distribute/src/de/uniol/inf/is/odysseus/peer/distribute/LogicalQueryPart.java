package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class LogicalQueryPart implements ILogicalQueryPart {

	private final Collection<ILogicalOperator> operators = Lists.newArrayList();
	private final Collection<ILogicalQueryPart> avoidedQueryParts = Lists.newArrayList();
	
	public LogicalQueryPart(Collection<ILogicalOperator> partOperators) {
		Preconditions.checkNotNull(partOperators, "List operators forming a logical query part must not be null!");
		Preconditions.checkArgument(!partOperators.isEmpty(), "List of operators forming a logical query part must not be empty!");
		
		operators.addAll(partOperators);
	}

	public LogicalQueryPart(ILogicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator for forming a logical query part must not be null!");
		
		operators.add(operator);
	}
	
	public LogicalQueryPart( Collection<ILogicalOperator> partOperators, Collection<ILogicalQueryPart> avoidedQueryParts ) {
		Preconditions.checkNotNull(partOperators, "List operators forming a logical query part must not be null!");
		Preconditions.checkArgument(!partOperators.isEmpty(), "List of operators forming a logical query part must not be empty!");
		
		if( avoidedQueryParts != null && !avoidedQueryParts.isEmpty() ) {
			this.avoidedQueryParts.addAll(avoidedQueryParts);
		}
	}
	
	public LogicalQueryPart( Collection<ILogicalOperator> partOperators, ILogicalQueryPart avoidedQueryPart ) {
		Preconditions.checkNotNull(partOperators, "List operators forming a logical query part must not be null!");
		Preconditions.checkArgument(!partOperators.isEmpty(), "List of operators forming a logical query part must not be empty!");
		
		if( avoidedQueryPart != null ) {
			avoidedQueryParts.add(avoidedQueryPart);
		}
	}
	
	public LogicalQueryPart( ILogicalOperator partOperator, Collection<ILogicalQueryPart> avoidedQueryParts ) {
		Preconditions.checkNotNull(partOperator, "Operator for forming a logical query part must not be null!");
		
		if( avoidedQueryParts != null && !avoidedQueryParts.isEmpty() ) {
			this.avoidedQueryParts.addAll(avoidedQueryParts);
		}
	}
	
	public LogicalQueryPart( ILogicalOperator partOperator, ILogicalQueryPart avoidedQueryPart ) {
		Preconditions.checkNotNull(partOperator, "Operator for forming a logical query part must not be null!");
		
		if( avoidedQueryPart != null ) {
			avoidedQueryParts.add(avoidedQueryPart);
		}		
	}
	
	@Override
	public ILogicalQueryPart clone() {
		return new LogicalQueryPart(getOperators(), getAvoidingQueryParts());
	}

	@Override
	public ImmutableCollection<ILogicalOperator> getOperators() {
		return ImmutableList.copyOf(operators);
	}

	@Override
	public ImmutableCollection<ILogicalQueryPart> getAvoidingQueryParts() {
		return ImmutableList.copyOf(avoidedQueryParts);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( ILogicalOperator operator : operators ) {
			sb.append(operator.getName()).append("(").append(operator.hashCode()).append(")  ");
		}
		sb.append("}");
		return sb.toString();
	}
}
	