package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

/**
 * A {@link QueryPart} is a list of {@link ILogicalOperator}s, which can be linked to a specified destination 
 * (Peer). Furthermore {@link JxtaSenderAO}s and {@link JxtaReceiverPO}s can be linked with a {@link QueryPart} 
 * for connecting different {@link QueryPart}s on different Peers.
 * @author Timo Michelsen, Michael Brand
 */
public class QueryPart {

	/**
	 * The PQL parameter key to specify the destination (peer), where a {@link QueryPart} shall be executed.
	 */
	private static final String DESTINATION_PQLPARAMETER_KEY = "DESTINATION";

	/**
	 * All {@link ILogicalOperator}s, which have no sources in this {@link QueryPart}, 
	 * mapped with the {@link JxtaReceiverAO}s connected to the {@link ILogicalOperator}. <br />
	 * A list of {@link JxtaReceiverAOs} for an {@link ILogicalOperator} can be empty.
	 */
	private final Map<ILogicalOperator, List<JxtaReceiverAO>> relativeSources;

	/**
	 * All {@link ILogicalOperator}s, which have no sinks in this {@link QueryPart}, 
	 * mapped with the {@link JxtaSenderAO}s connected to the {@link ILogicalOperator}. <br />
	 * A list of {@link JxtaSenderAOs} for an {@link ILogicalOperator} can be empty.
	 */
	private final Map<ILogicalOperator, List<JxtaSenderAO>> relativeSinks;

	/**
	 * The collection of all {@link ILogicalOperator}s within this {@link QueryPart}.
	 */
	private final Collection<ILogicalOperator> operators;

	/**
	 * The destination (peer), where this {@link QueryPart} shall be executed.
	 */
	private final String destinationName;

	/**
	 * Constructs a new {@link QueryPart} with a specified destination.
	 * @param operators The collection of all {@link ILogicalOperator}s within this {@link QueryPart}. <br />
	 * <code>operators</code> must not be null and not be empty.
	 * @param destinationName The destination (peer), where this {@link QueryPart} shall be executed.
	 */
	public QueryPart(Collection<ILogicalOperator> operators, String destinationName) {
		
		// Preconditions
		Preconditions.checkNotNull(operators, "List of operators must not be null!");
		Preconditions.checkArgument(!filter(operators).isEmpty(), "List of operators must not be empty!");

		this.destinationName = destinationName;
		this.operators = filter(operators);
		this.relativeSinks = determineRelativeSinks(this.operators);
		this.relativeSources = determineRelativeSources(this.operators);
		
	}
	
	/**
	 * Constructs a new {@link QueryPart} without a specified destination.
	 * @param operators The collection of all {@link ILogicalOperator}s within this {@link QueryPart}. <br />
	 * <code>operators</code> must not be null and not be empty.
	 */
	public QueryPart( Collection<ILogicalOperator> operators ) {
		
		this(operators, null);
		
	}

	/**
	 * Maps a {@link JxtaReceiverAO} to an {@link ILogicalOperator} within this {@link QueryPart}. <br />
	 * This {@link ILogicalOpeerator} shall be a relative source. <br />
	 * No subscriptions are made.
	 * @see #getRelativeSources()
	 * @param accessAO The {@link JxtaReceiverAO} to be mapped with the specified {@link ILogicalOperator}.
	 * @param forOperator The specified {@link ILogicalOperator}. <br />
	 * <code>forOperator</code> must not be null and a relative source of this {@link QueryPart}.
	 */
	public final void addAccessAO(JxtaReceiverAO accessAO, ILogicalOperator forOperator) {
		
		// Preconditions
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSources.containsKey(forOperator));

		relativeSources.get(forOperator).add(accessAO);
		
	}

	/**
	 * Maps a {@link JxtaSenderAO} to an {@link ILogicalOperator} within this {@link QueryPart}. <br />
	 * This {@link ILogicalOpeerator} shall be a relative sink. <br />
	 * No subscriptions are made.
	 * @see #getRelativeSinks()
	 * @param senderAO The {@link JxtaSenderAO} to be mapped with the specified {@link ILogicalOperator}.
	 * @param forOperator The specified {@link ILogicalOperator}. <br />
	 * <code>forOperator</code> must not be null and a relative sink of this {@link QueryPart}.
	 */
	public final void addSenderAO(JxtaSenderAO senderAO, ILogicalOperator forOperator) {
		
		// Preconditions
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSinks.containsKey(forOperator));

		relativeSinks.get(forOperator).add(senderAO);
		
	}

	/**
	 * Determines if a given {@link ILogicalOperator} is a relative sink of this {@link QueryPart}.
	 * @see #getRelativeSources()
	 * @param op The given {@link ILogicalOperator}.
	 * @return {@link #getRelativeSources()}<code>.contains(op)</code>.
	 */
	public final boolean containsRelativeSource(ILogicalOperator op) {
		
		return relativeSources.containsKey(op);
		
	}

	/**
	 * Returns the destination (peer), where this {@link QueryPart} shall be executed. <br />
	 * The destination can be <code>null</code>.
	 */
	public final Optional<String> getDestinationName() {
		
		return Optional.fromNullable(destinationName);
		
	}

	/**
	 * Returns an immutable copy of the collection of all {@link ILogicalOperator}s within this {@link QueryPart}.
	 */
	public final ImmutableCollection<ILogicalOperator> getOperators() {
		
		return ImmutableList.copyOf(operators);
		
	}

	/**
	 * Returns an immutable copy of the collection of all {@link ILogicalOperator}s, 
	 * which have no sources in this {@link QueryPart}.
	 */
	public final ImmutableCollection<ILogicalOperator> getRelativeSinks() {
		
		return ImmutableList.copyOf(relativeSinks.keySet());
		
	}

	/**
	 * Returns an immutable copy of the collection of all {@link ILogicalOperator}s, 
	 * which have no sinks in this {@link QueryPart}.
	 */
	public final ImmutableCollection<ILogicalOperator> getRelativeSources() {
		
		return ImmutableList.copyOf(relativeSources.keySet());
		
	}

	/**
	 * Removes the destination (peer) for each {@link ILogicalOperator} within this {@link QueryPart}, 
	 * where the {@link ILogicalOperator} shall be executed.
	 */
	public final void removeDestinationName() {
		
		for(final ILogicalOperator operator : operators) {
			
			operator.setDestinationName(null);
			operator.addParameterInfo(DESTINATION_PQLPARAMETER_KEY, null);
			
		}
		
	}
	
	/**
	 * Returns an immutable copy of the collection of all {@link ILogicalOperator}s, 
	 * which have no sources in this {@link QueryPart} and are not mapped to any {@link JxtaSenderAO}. <br />
	 * The returned collection also contains all {@link JxtaSenderAO}s linked with this {@link QueryPart}.
	 */
	public Collection<ILogicalOperator> getRealSinks() {
		
		List<ILogicalOperator> sinks = Lists.newArrayList();
		
		for(ILogicalOperator relativeSink : relativeSinks.keySet()) {
			
			List<JxtaSenderAO> senderAOs = relativeSinks.get(relativeSink);
			
			if(senderAOs.isEmpty() )
				sinks.add(relativeSink);
			else sinks.addAll(senderAOs);
			
		}
		
		return sinks;
		
	}

	/**
	 * Determines if the target {@link ILogicalOperator} of any {@link LogicalSubscription} is not in a given 
	 * collection of {@link IlogicalOperator}s.
	 * @see LogicalSubscription#getTarget()
	 * @param operators The given collection of {@link IlogicalOperator}s.
	 * @param subscriptions A collection of {@link LogicalSubscription}s to be checked.
	 * @return true, if any target {@link ILogicalOperator} of <code>subscriptions</code> is not in 
	 * <code>operators</code>.
	 */
	private static boolean oneTargetNotInList(Collection<ILogicalOperator> operators, 
			Collection<LogicalSubscription> subscriptions) {
		
		for(final LogicalSubscription subscription : subscriptions) {
			
			if(!operators.contains(subscription.getTarget()))
				return true;
			
		}
		
		return false;
		
	}

	/**
	 * Determines all {@link ILogicalOperator}s, which have no sinks in this {@link QueryPart}.
	 * @param operators A collection of {@link ILogicalOperator}s to be checked.
	 * @return A mapping of all relative sinks and an empty list of {@link JxtaSenderAO}s.
	 */
	private static Map<ILogicalOperator, List<JxtaSenderAO>> determineRelativeSinks(
			Collection<ILogicalOperator> operators) {
		
		final Map<ILogicalOperator, List<JxtaSenderAO>> sinksMap = Maps.newHashMap();
		
		for(final ILogicalOperator operator : operators) {
			
			if(operator.getSubscriptions().size() == 0 || 
					oneTargetNotInList(operators, operator.getSubscriptions()))
				sinksMap.put(operator, new ArrayList<JxtaSenderAO>());
			
		}
		
		return sinksMap;
		
	}

	/**
	 * Determines all {@link ILogicalOperator}s, which have no sources in this {@link QueryPart}.
	 * @param operators A collection of {@link ILogicalOperator}s to be checked.
	 * @return A mapping of all relative sources and an empty list of {@link JxtaReceiverAO}s.
	 */
	private static Map<ILogicalOperator, List<JxtaReceiverAO>> determineRelativeSources(
			Collection<ILogicalOperator> operators) {
		
		final Map<ILogicalOperator, List<JxtaReceiverAO>> sourcesMap = Maps.newHashMap();
		
		for(final ILogicalOperator operator : operators) {
			
			if(operator.getSubscribedToSource().size() == 0 || 
					oneTargetNotInList(operators, operator.getSubscribedToSource()))
				sourcesMap.put(operator, new ArrayList<JxtaReceiverAO>());
			
		}
		
		return sourcesMap;
		
	}

	/**
	 * Filters off all {@link ILogicalOperator}s, which are no instance of {@link TopAO}.
	 * @param operators A collection of {@link ILogicalOperator}s to be filtered.
	 * @return The collection <code>operators</code> without any instances of {@link TopAO}.
	 */
	private static Collection<ILogicalOperator> filter(Collection<ILogicalOperator> operators) {
		
		final ImmutableList.Builder<ILogicalOperator> filteredOperators = new ImmutableList.Builder<>();
		
		for(final ILogicalOperator operator : operators) {
			
			if(!(operator instanceof TopAO))
				filteredOperators.add(operator);
			
		}
		
		return filteredOperators.build();
		
	}
	
	@Override
	public String toString() {
		
		String strPart = new String();
		Iterator<ILogicalOperator> iter = this.operators.iterator();
		
		while(iter.hasNext())
			strPart += iter.next().getName() + " ";
		
		if(this.destinationName != null)
			strPart += "; Destination= " + this.destinationName;
		
		return strPart;
		
	}

}