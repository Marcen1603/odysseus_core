package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class LogicalQueryPart implements ILogicalQueryPart {

	private final Collection<ILogicalOperator> operators = Lists.newArrayList();
	private final Collection<ILogicalQueryPart> avoidedQueryParts = Lists.newArrayList();
	
	public LogicalQueryPart() {
	}
	
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
		this(partOperators);
		
		if( avoidedQueryParts != null && !avoidedQueryParts.isEmpty() ) {
			this.avoidedQueryParts.addAll(avoidedQueryParts);
		}
		
	}
	
	public LogicalQueryPart( Collection<ILogicalOperator> partOperators, ILogicalQueryPart avoidedQueryPart ) {
		this(partOperators);
		
		if( avoidedQueryPart != null ) {
			avoidedQueryParts.add(avoidedQueryPart);
		}
	}
	
	public LogicalQueryPart( ILogicalOperator partOperator, Collection<ILogicalQueryPart> avoidedQueryParts ) {
		this(partOperator);
		
		if( avoidedQueryParts != null && !avoidedQueryParts.isEmpty() ) {
			this.avoidedQueryParts.addAll(avoidedQueryParts);
		}
	}
	
	public LogicalQueryPart( ILogicalOperator partOperator, ILogicalQueryPart avoidedQueryPart ) {
		this(partOperator);
		
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
			sb.append(operator.getName() + " ");//.append("(").append(operator.hashCode()).append(")  ");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void addOperator(ILogicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator to add to queryPart must not be null!");
		Preconditions.checkArgument(!operators.contains(operator), "Operator %s is already added to the queryPart", operator);
		
		operators.add(operator);
	}

	@Override
	public void addOperators(Collection<ILogicalOperator> operators) {
		Preconditions.checkNotNull(operators, "Collection of operators to add to queryPart must not be null!");
		
		for( ILogicalOperator operator : operators ) {
			addOperator(operator);
		}
	}

	@Override
	public void removeOperator(ILogicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator to remove from queryPart must not be null!");
		Preconditions.checkArgument(operators.contains(operator), "Operator %s to remove is not in this queryPart", operator);
		
		operators.remove(operator);
	}

	@Override
	public void removeOperators(Collection<ILogicalOperator> operators) {
		Preconditions.checkNotNull(operators, "Collection of operators to remove to queryPart must not be null!");
		
		for( ILogicalOperator operator : operators ) {
			removeOperator(operator);
		}
	}

	@Override
	public boolean contains(ILogicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator to check in queryPart must not be null!");
		
		return operators.contains(operator);
	}

	@Override
	public void removeAllOperators() {
		operators.clear();
	}

	@Override
	public void addAvoidingQueryPart(ILogicalQueryPart queryPartToAvoid) {
		Preconditions.checkNotNull(queryPartToAvoid, "QueryPartToAvoid must not be null!");
		Preconditions.checkArgument(!avoidedQueryParts.contains(queryPartToAvoid), "The queryPart %s is already avoided in %s", queryPartToAvoid, this);
		
		avoidedQueryParts.add(queryPartToAvoid);
	}

	@Override
	public void addAvoidingQueryParts(Collection<ILogicalQueryPart> queryPartsToAvoid) {
		Preconditions.checkNotNull(queryPartsToAvoid, "Collection of queryParts to avoid must not be null!");
		
		for( ILogicalQueryPart part : queryPartsToAvoid ) {
			addAvoidingQueryPart(part);
		}
	}

	@Override
	public void removeAvoidingQueryPart(ILogicalQueryPart queryPartToAvoid) {
		Preconditions.checkNotNull(queryPartToAvoid, "QueryPartToAvoid must not be null!");
		Preconditions.checkArgument(avoidedQueryParts.contains(queryPartToAvoid), "The queryPart %s to remove is not in %s", queryPartToAvoid, this);
		
		avoidedQueryParts.remove(queryPartToAvoid);
	}

	@Override
	public void removeAvoidingQueryParts(Collection<ILogicalQueryPart> queryPartsToAvoid) {
		Preconditions.checkNotNull(queryPartsToAvoid, "Collection of queryParts to avoid must not be null!");
		
		for( ILogicalQueryPart part : queryPartsToAvoid ) {
			removeAvoidingQueryPart(part);
		}
	}

	@Override
	public boolean contains(ILogicalQueryPart queryPart) {
		Preconditions.checkNotNull(queryPart, "QueryPart to check for avoidance must not be null!");
		
		return avoidedQueryParts.contains(queryPart);
	}

	@Override
	public void removeAllAvoidingQueryParts() {
		avoidedQueryParts.clear();
	}
}
	