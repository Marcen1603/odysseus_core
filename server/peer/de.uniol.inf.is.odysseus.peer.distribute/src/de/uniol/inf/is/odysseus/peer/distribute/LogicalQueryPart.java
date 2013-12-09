package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class LogicalQueryPart implements ILogicalQueryPart {

	private final Collection<ILogicalOperator> operators = Lists.newArrayList();
	
	public LogicalQueryPart(Collection<ILogicalOperator> partOperators) {
		Preconditions.checkNotNull(partOperators, "List operators forming a logical query part must not be null!");
		Preconditions.checkArgument(!partOperators.isEmpty(), "List of operators forming a logical query part must not be empty!");
		
		operators.addAll(partOperators);
	}

	public LogicalQueryPart(ILogicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator for forming a logical query part must not be null!");
		
		operators.add(operator);
	}
	
	public LogicalQueryPart(LogicalQueryPart part) {
		
		Preconditions.checkNotNull(part, "Logical query part to make a copy of must not be null!");
		
		// The list of origins
		final List<ILogicalOperator> origins = Lists.newArrayList();
		
		// The list of clones
		final List<ILogicalOperator> clones = Lists.newArrayList();
		
		// 1. Clone operators without subscriptions
		for(ILogicalOperator origin : part.getOperators()) {
		
			origins.add(origin);
			clones.add(origin.clone());
			
		}
		
		// 2. Insert subscriptions
		for(int operatorNo = 0; operatorNo < origins.size(); operatorNo++) {
			
			final ILogicalOperator origin = origins.get(operatorNo);
			final ILogicalOperator clone = origins.get(operatorNo);
			
			for(LogicalSubscription subToSource : origin.getSubscribedToSource()) {
				
				final int sinkInPort = subToSource.getSinkInPort();
				final int sourceOutPort = subToSource.getSourceOutPort();
				final SDFSchema schema = subToSource.getSchema();
				
				ILogicalOperator source = null;
				final Optional<ILogicalOperator> optSource = 
						LogicalQueryPart.findCloneOf(subToSource.getTarget(), origins, clones);
				if(optSource.isPresent())
					source = optSource.get();
				else source = subToSource.getTarget();
				
				clone.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
				
			}
			
			for(LogicalSubscription subToSink : origin.getSubscriptions()) {
				
				/*
				 * Precondition: Take only those subscriptions, which targets are not within the 
				 * logical query part. All others will be processed by the loop through the 
				 * subscriptions to sources.
				 */			
				if(origins.contains(subToSink.getTarget()))
					continue;
				
				final int sinkInPort = subToSink.getSinkInPort();
				final int sourceOutPort = subToSink.getSourceOutPort();
				final SDFSchema schema = subToSink.getSchema();		
				final ILogicalOperator sink = subToSink.getTarget();
				
				clone.subscribeSink(sink, sinkInPort, sourceOutPort, schema);
				
			}
			
		}
		
		this.operators.addAll(clones);
		
	}
	
	@Override
	public ILogicalQueryPart clone() {
		
		return new LogicalQueryPart(this);
		
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
	
	/**
	 * Searches a clone of a logical operator.
	 * @param operator The logical operator, whose clone is in demand.
	 * @param origins The list of origin logical operators. <br />
	 * Must have the same order as <code>clones</code>.
	 * @param clones The list of cloned logical operators. <br />
	 * Must have the same order as <code>origins</code>.
	 * @return The clone of <code>operator</code> if present; 
	 * {@link Optional#absent()}, if <code>operator</code> is not within <code>origins</code> and 
	 * therefore it's clone is not within <code>clones</code>.
	 */
	private static Optional<ILogicalOperator> findCloneOf(ILogicalOperator operator,
			List<ILogicalOperator> origins, List<ILogicalOperator> clones) {
		
		// Preconditions
		if(operator == null || origins == null || clones == null)
			return Optional.absent();
		
		for(int operatorNo = 0; operatorNo < origins.size(); operatorNo++) {
			
			if(operator.equals(origins.get(operatorNo)))
				return Optional.fromNullable(clones.get(operatorNo));
			
		}
		
		return Optional.absent();
		
	}
}
	