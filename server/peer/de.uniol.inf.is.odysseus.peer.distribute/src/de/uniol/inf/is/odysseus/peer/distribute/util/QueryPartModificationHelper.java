package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;

/**
 * A helper class for {@link IQueryPartModificator} implementations.
 * @author Michael Brand
 */
public class QueryPartModificationHelper {

	/**
	 * Performs a deep cloning of a query part. <br />
	 * Each operator will be cloned and for the subscriptions there are two scenarios: <br />
	 * 1. If both operators connected with the subscription are within the query part, the clones 
	 * of both operators will be connected as the origin ones. <br />
	 * 2. If one operator connected with the subscription is not within the query part, it will 
	 * get an additional subscription to the clone of the other operator.
	 * @param originPart The origin part to be cloned.
	 * @return A clone of <code>originPart</code>.
	 */
	public static ILogicalQueryPart cloneLogicalQueryPart(final ILogicalQueryPart originPart) {
		
		Preconditions.checkNotNull(originPart, "Logical query part to clone must not be null!");
		
		// The list of origins
		final List<ILogicalOperator> origins = Lists.newArrayList();
		
		// The list of clones
		final List<ILogicalOperator> clones = Lists.newArrayList();
		
		// 1. Clone operators without subscriptions
		for(ILogicalOperator origin : originPart.getOperators()) {
		
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
						QueryPartModificationHelper.findCloneOf(subToSource.getTarget(), origins, clones);
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
		
		return new LogicalQueryPart(clones);
		
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
	private static Optional<ILogicalOperator> findCloneOf(final ILogicalOperator operator,
			final List<ILogicalOperator> origins, final List<ILogicalOperator> clones) {
		
		// Preconditions
		if(operator == null || origins == null || clones == null)
			return Optional.absent();
		
		for(int operatorNo = 0; operatorNo < origins.size(); operatorNo++) {
			
			if(operator.equals(origins.get(operatorNo)))
				return Optional.fromNullable(clones.get(operatorNo));
			
		}
		
		return Optional.absent();
		
	}
	
	/**
	 * Collects all operators within a query part, which are subscribed to sources outside the query part.
	 * @param part The query part to process.
	 * @return A collection of all relative sources within <code>part</code>.
	 */
	public static Collection<ILogicalOperator> getRelativeSourcesOfLogicalQueryPart(
			final ILogicalQueryPart part) {
		
		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");
		
		// The return value
		final Collection<ILogicalOperator> relativeSources = Lists.newArrayList();
		
		for(ILogicalOperator operator : part.getOperators()) {
			
			for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
				
				if(!part.getOperators().contains(subToSource.getTarget())) {
					
					relativeSources.add(operator);
					break;
					
				}
				
			}
			
		}
		
		return relativeSources;
		
	}
	
	/**
	 * Collects all operators within a query part, which are subscribed to sinks outside the query part.
	 * @param part The query part to process.
	 * @return A collection of all relative sinks within <code>part</code>.
	 */
	public static Collection<ILogicalOperator> getRelativeSinksOfLogicalQueryPart(
			final ILogicalQueryPart part) {
		
		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");
		
		// The return value
		final Collection<ILogicalOperator> relativeSinks = Lists.newArrayList();
		
		for(ILogicalOperator operator : part.getOperators()) {
			
			for(LogicalSubscription subToSink : operator.getSubscriptions()) {
				
				if(!part.getOperators().contains(subToSink.getTarget())) {
					
					relativeSinks.add(operator);
					break;
					
				}
				
			}
			
		}
		
		return relativeSinks;
		
	}
	
}