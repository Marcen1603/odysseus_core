package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;

public class QueryPart {

	private final Collection<ILogicalOperator> relativeSources;
	private final Collection<ILogicalOperator> relativeSinks;
	private final Collection<ILogicalOperator> operators;
	
	private final String destinationName;
	private final String name;
	
	public QueryPart(Collection<ILogicalOperator> operators, String destinationName, String partName) {
		Preconditions.checkNotNull(operators, "List of operators must not be null!");
		Preconditions.checkArgument(!operators.isEmpty(), "List of operators must not be empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationName), "Destination name must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(partName), "Query Part name must not be null or empty!");
		
		this.destinationName = destinationName;
		this.operators = filter(operators);
		this.name = partName;
		this.relativeSinks = determineRelativeSinks(this.operators);
		this.relativeSources = determineRelativeSources(this.operators);
	}

	public final ImmutableCollection<ILogicalOperator> getOperators() {
		return ImmutableList.copyOf(operators);
	}
	
	public final boolean containsRelativeSource( ILogicalOperator op ){
		return relativeSources.contains(op);
	}
	
	public final ImmutableCollection<ILogicalOperator> getRelativeSources() {
		return ImmutableList.copyOf(relativeSources);
	}
	
	public final ImmutableCollection<ILogicalOperator> getRelativeSinks() {
		return ImmutableList.copyOf(relativeSinks);
	}
	
	public final String getDestinationName() {
		return destinationName;
	}
	
	public final String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	private static Collection<ILogicalOperator> determineRelativeSources(Collection<ILogicalOperator> operators) {
		ImmutableList.Builder<ILogicalOperator> sources = new ImmutableList.Builder<>();
		for( ILogicalOperator operator : operators ) {
			if( operator.getSubscribedToSource().size() == 0 || allTargetsNotInList(operators, operator.getSubscribedToSource())) {
				sources.add(operator);
			} 
		}
		return sources.build();
	}

	private static Collection<ILogicalOperator> determineRelativeSinks(Collection<ILogicalOperator> operators) {
		ImmutableList.Builder<ILogicalOperator> sinks = new ImmutableList.Builder<>();
		for( ILogicalOperator operator : operators ) {
			if( operator.getSubscriptions().size() == 0 || allTargetsNotInList(operators, operator.getSubscriptions())) {
				sinks.add(operator);
			} 
		}
		return sinks.build();
	}

	private static boolean allTargetsNotInList(Collection<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {
		for( LogicalSubscription subscription : subscriptions ) {
			if( operators.contains(subscription.getTarget())) {
				return false;
			}
		}
		return true;
	}

	private static Collection<ILogicalOperator> filter(Collection<ILogicalOperator> operators) {
		ImmutableList.Builder<ILogicalOperator> filteredOperators = new ImmutableList.Builder<>();
		for( ILogicalOperator operator : operators ) {
			if( !(operator instanceof TopAO )) {
				filteredOperators.add(operator);
			}
		}
		return filteredOperators.build();
	}
}
