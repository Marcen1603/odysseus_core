package de.uniol.inf.is.odysseus.peer.distribute.modify.replication;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A modifier of {@link ILogicalQueryPart}s, which replicates query parts and inserts operators to 
 * merge the result sets of the replicates for each relative sink within every single query part. <br />
 * Usage in Odysseus Script: <br />
 * #PEER_MODIFICATION replication &lt;number of replicates&gt;
 * @author Michael Brand
 */
public class ReplicationQueryPartModificator implements IQueryPartModificator {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(ReplicationQueryPartModificator.class);
	
	/**
	 * The name of this modificator.
	 */
	private static final String name = "replication";
	
	/**
	 * The minimum degree of replication.
	 */
	private static final int min_degree = 2;
	
	/**
	 * The class of the used merger.
	 */
	private static final Class<? extends ILogicalOperator> mergerClass = ReplicationMergeAO.class;

	@Override
	public String getName() {
		
		return ReplicationQueryPartModificator.name;
		
	}

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, 
			QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {
		
		// Preconditions
		if(queryParts == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Query parts to be modified must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		if(queryParts.isEmpty()) {
			
			ReplicationQueryPartModificator.log.warn("No query parts given to replicate");
			return modifiedParts;
			
		}
		
		try {
		
			// Determine degree of replication
			final int degreeOfReplication = 
					ReplicationQueryPartModificator.determineDegreeOfReplication(modificatorParameters);
			ReplicationQueryPartModificator.checkDegreeOfReplication(queryParts.size(), degreeOfReplication);
			ReplicationQueryPartModificator.log.debug("Degree of replication set to {}.", degreeOfReplication);
			
			IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> replicatableAndIrreplicatableParts = 
					ReplicationQueryPartModificator.determineRealSinks(queryParts);
			modifiedParts.addAll(replicatableAndIrreplicatableParts.getE1());
			modifiedParts.addAll(replicatableAndIrreplicatableParts.getE2());
			
			// Copy the query parts
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin = 
					LogicalQueryHelper.copyAndCutQueryParts(modifiedParts, degreeOfReplication);
			
			// Keep only one copy of real sinks
			for(ILogicalQueryPart realSink : replicatableAndIrreplicatableParts.getE2()) {
					
				ILogicalQueryPart copyToKeep = replicatesToOrigin.get(realSink).iterator().next();
				replicatesToOrigin.get(realSink).clear();
				replicatesToOrigin.get(realSink).add(copyToKeep);
				
			}
			
			if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
				
				for(ILogicalQueryPart origin : replicatesToOrigin.keySet()) {
					
					for(ILogicalQueryPart copy : replicatesToOrigin.get(origin))
						ReplicationQueryPartModificator.log.debug("Created query part {} as a copy of query part {}.", copy, origin);
					
				}
				
			}
			
			// Modify each query part
			for(ILogicalQueryPart originPart : replicatableAndIrreplicatableParts.getE1())
				replicatesToOrigin.putAll(ReplicationQueryPartModificator.modify(originPart, replicatesToOrigin));
			
			// Create the return value
			modifiedParts.clear();
			for(ILogicalQueryPart originPart : replicatesToOrigin.keySet()) {
				
				for(ILogicalQueryPart modifiedPart : replicatesToOrigin.get(originPart)) {
					
					Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList(replicatesToOrigin.get(originPart));
					avoidedParts.remove(modifiedPart);
					
					modifiedPart.addAvoidingQueryParts(avoidedParts);

					modifiedParts.add(modifiedPart);
				}
				
		}
			
		} catch(Exception e) {
			
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}	
		
		return ReplicationQueryPartModificator.setQueryPartsToAvoid(modifiedParts);
		
	}

	// TODO javaDoc
	private static Collection<ILogicalQueryPart> setQueryPartsToAvoid(Collection<ILogicalQueryPart> parts) throws QueryPartModificationException {
		
		Preconditions.checkNotNull(parts, "Collection of query parts must be not null!");
		
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList(parts);
		
		for(ILogicalQueryPart part : modifiedParts) {
			
			for(ILogicalOperator operator : part.getOperators()) {
				
				if(!operator.getClass().equals(ReplicationQueryPartModificator.mergerClass))
					continue;
				
				final ILogicalOperator merger = operator;
				
				for(LogicalSubscription subToSource : merger.getSubscribedToSource()) {
					
					final ILogicalOperator source = subToSource.getTarget();					
					final Optional<ILogicalQueryPart> optPartOfSource = LogicalQueryHelper.determineQueryPart(modifiedParts, source);
					
					if(!optPartOfSource.isPresent() || optPartOfSource.get().equals(part)) {
						
						final String errorMessage = "Query part of " + source + " is either not present or the same part as of the following merger!";
						ReplicationQueryPartModificator.log.error(errorMessage);
						throw new QueryPartModificationException(errorMessage);
						
					}
					
					final ILogicalQueryPart partOfSource = optPartOfSource.get();
					if(!part.getAvoidingQueryParts().contains(partOfSource))
						part.addAvoidingQueryPart(partOfSource);
					if(!partOfSource.getAvoidingQueryParts().contains(part))
						partOfSource.addAvoidingQueryPart(part);
					
				}
				
			}
			
		}
		
		return modifiedParts;
		
	}

	/**
	 * Determines all query parts to be replicated. <br />
	 * Origin parts within <code>queryParts</code> may be split.
	 * @param queryParts A collection of query parts to modify.
	 * @return A pair of query parts to be replicated (e1) and parts containing only real sinks (e2).
	 * @throws NullPointerException if <code>queryParts</code> is null.
	 */
	private static IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determineRealSinks(
			Collection<ILogicalQueryPart> queryParts) {
		
		// Preconditions
		if(queryParts == null)
			throw new NullPointerException("Query parts for modification must be not null!");
		
		// The return value
		Collection<ILogicalQueryPart> partsToBeFragmented = Lists.newArrayList();
		Collection<ILogicalQueryPart> otherParts = Lists.newArrayList();
		
		for(ILogicalQueryPart part : queryParts) {
			
			Collection<ILogicalOperator> relevantOperators = ReplicationQueryPartModificator.determineRelevantOperators(part);
			if(relevantOperators.size() == part.getOperators().size()) {
				
				partsToBeFragmented.add(part);
				ReplicationQueryPartModificator.log.debug("Found {} as a part to be replicated", part);
				
			} else if(!relevantOperators.isEmpty()) {
				ReplicationQueryPartModificator.log.debug("Split {} to form as a non-replicateable part", new Object[] {part});
				
				Collection<ILogicalOperator> allOperatorsOfPart = part.getOperators();
				partsToBeFragmented.add(part);
				part.removeAllOperators();
				part.addOperators(relevantOperators);
				
				Collection<ILogicalOperator> realSinks = Lists.newArrayList(allOperatorsOfPart);
				realSinks.removeAll(relevantOperators);
			
				ILogicalQueryPart otherPart = new LogicalQueryPart(realSinks);
				otherParts.add(otherPart);
			
				ReplicationQueryPartModificator.log.debug("Non-replicatable part is {}", otherPart);
				
			} else {
				
				otherParts.add(part);
				ReplicationQueryPartModificator.log.debug("Found {} as a real sink", part);				
				
			}
			
			
		}
		
		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(partsToBeFragmented, otherParts);
		
	}

	/**
	 * Determine all relevant operators for replication of a given query part.
	 * @param queryPart The query part to modify.
	 * @return A collection of all operators relevant for replication. <br />
	 * Those operators are no real sinks.
	 * @throws NullPointerException if <code>queryPart</code> is null.
	 */
	private static Collection<ILogicalOperator> determineRelevantOperators(
			ILogicalQueryPart queryPart)
			throws NullPointerException {
		
		// Preconditions
		if(queryPart == null)
			throw new NullPointerException("Query part for modification must be not null!");
		
		// The return value
		Collection<ILogicalOperator> relevantOperators = Lists.newArrayList();
		
		for(ILogicalOperator operator : queryPart.getOperators()) {
			
			if(operator.isSinkOperator() && !operator.isSourceOperator())
				continue;
			
			relevantOperators.add(operator);
			
		}
		
		return relevantOperators;
		
	}

	/**
	 * Modifies a single query part. For every real sink of the part a single merger will be inserted. 
	 * For every relative (not real) sink of the part each subscription leading outwards the query part containing the sink will be processed.
	 * @see #processSubscription(ILogicalQueryPart, Map, ILogicalOperator, Collection, LogicalSubscription)
	 * @param originPart The origin query part to be modified.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @return The updated mapping of replicated query parts to the origin ones, where the replicated parts contain all inserted mergers.
	 * @throws NullPointerException if <code>originPart</code> or <code>replicatesToOrigin</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modify(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin) 
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");

		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				LogicalQueryHelper.collectRelativeSinks(originPart, modifiedReplicatesToOrigin.get(originPart));
		
		if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
			
			for(ILogicalOperator originSink : copiedToOriginSinks.keySet())
					ReplicationQueryPartModificator.log.debug("Found {} as a relative sink of {}.", originSink.getName(), originPart);
			
		}
		
		// Modify each sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.modifySink(originPart, modifiedReplicatesToOrigin, originSink, 
					copiedToOriginSinks.get(originSink)));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Modifies a relative (or real) sink. For real sinks a single merger will be inserted. 
	 * Otherwise, each subscription leading outwards the query part containing the sink will be processed.
	 * @see #processSubscription(ILogicalQueryPart, Map, ILogicalOperator, Collection, LogicalSubscription)
	 * @param originPart The origin query part which contains the origin sink.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param replicatedSinks The replicates of <code>originSink</code>.
	 * @return The updated mapping of replicated query parts to the origin ones, where the replicated parts contain all inserted mergers.
	 * @throws NullPointerException if <code>originPart</code>, <code>replicatesToOrigin</code>, <code>originSink</code> or 
	 * <code>replicatedSinks</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySink(
			ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin,
			ILogicalOperator originSink, Collection<ILogicalOperator> replicatedSinks) 
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(replicatedSinks == null)
			throw new NullPointerException("List of replicated sinks for modification must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Process real sinks
		if(originSink.getSubscriptions().isEmpty()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processRealSink(originPart, modifiedReplicatesToOrigin, originSink, 
					replicatedSinks));
			
		}
		
		// Process replicates for each subscription
		for(LogicalSubscription subscription : originSink.getSubscriptions()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processSubscription(originPart, modifiedReplicatesToOrigin, originSink, 
					replicatedSinks, subscription));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Processes a real sink, where a single merger will be inserted.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @param originSink The origin sink of a query part.
	 * @param replicatedSinks The replicates of <code>originSink</code>.
	 * @return The updated mapping of replicated query parts to the origin ones. A single merger will be the only operator of a new query part 
	 * mapped to itself.
	 * @throws NullPointerException if <code>originPart</code>, <code>replicatesToOrigin</code>, <code>originSink</code> or 
	 * <code>replicatedSinks</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processRealSink(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> replicatedSinks) throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");
		else if(replicatedSinks == null)
			throw new NullPointerException("List of replicated sinks for modification must be not null!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// The merger to be inserted
		ILogicalOperator merger = null;
		try {
			
			merger = ReplicationQueryPartModificator.mergerClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new InstantiationException("Merger could not be instantiated!");
			
		}
		
		for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
		
			ILogicalOperator replicatedSink = ((List<ILogicalOperator>) replicatedSinks).get(sinkNo);
			replicatedSink.subscribeSink(merger, sinkNo, 0, replicatedSink.getOutputSchema());
			
		}
		
		// Create new query part
		ILogicalQueryPart mergerPart = new LogicalQueryPart(merger);
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		modifiedQueryParts.add(mergerPart);
		modifiedReplicatesToOrigin.put(new LogicalQueryPart(merger), modifiedQueryParts);
		
		if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSinks = "(";
			for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
				
				strSinks += ((List<ILogicalOperator>) replicatedSinks).get(sinkNo).getName();
						
				if(sinkNo == replicatedSinks.size() - 1)
					strSinks += ")";
				else strSinks += ", ";
				
			}
			ReplicationQueryPartModificator.log.debug("Inserted a merger after {}.", strSinks);
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Processes a subscription, which source is a relative sink of a query part. If the target operator of the subscription was replicated, 
	 * a merger will be inserted for each replicated target. Otherwise a single merger will be inserted for the origin target.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param replicatedSinks The replicates of <code>originSink</code>.
	 * @param subscription The subscription to be broken due to the insertion of the merger.
	 * @return The updated mapping of replicated query parts to the origin ones. If the target operator of the subscription was not replicated, 
	 * a single merger will be the only operator of a new query part mapped to itself. Otherwise merger will be part of each 
	 * query part which contains a replicate of the target of the subscription.
	 * @throws NullPointerException if <code>originPart</code>, <code>replicatesToOrigin</code>, <code>originSink</code>, 
	 * <code>replicatedSinks</code> or <code>subscription</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processSubscription(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, ILogicalOperator originSink, 
			Collection<ILogicalOperator> replicatedSinks, LogicalSubscription subscription) 
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions 1
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");
		else if(replicatedSinks == null)
			throw new NullPointerException("List of replicated sinks for modification must be not null!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Preconditions 2
		if(subscription == null || originPart.getOperators().contains(subscription.getTarget()))
			return modifiedReplicatesToOrigin;
		
		// The query part containing the origin target
		Optional<ILogicalQueryPart> optTargetPart = 
				LogicalQueryHelper.determineQueryPart(modifiedReplicatesToOrigin.keySet(), subscription.getTarget());
		
		// Insert merger
		if(!optTargetPart.isPresent()) {
			
			Optional<List<ILogicalOperator>> replicatedTargets = Optional.fromNullable(null);
			Optional<List<ILogicalQueryPart>> queryPartOfReplicatedTarget = Optional.fromNullable(null);
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, modifiedReplicatesToOrigin, 
					replicatedSinks, replicatedTargets, optTargetPart, queryPartOfReplicatedTarget, subscription));
			
		} else {
			
			// Find clones of target
			int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
			List<ILogicalOperator> replicatedTargets = Lists.newArrayList();
			for(ILogicalQueryPart replicate : replicatesToOrigin.get(optTargetPart.get()))
				replicatedTargets.add(((ImmutableList<ILogicalOperator>) replicate.getOperators()).get(targetNo));
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, modifiedReplicatesToOrigin, 
					replicatedSinks, Optional.of(replicatedTargets), optTargetPart, 
					Optional.of((List<ILogicalQueryPart>) replicatesToOrigin.get(optTargetPart.get())),	subscription));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Processes an operator, which is target of a given subscription. If the target operator was replicated, a merger will be inserted 
	 * for each replicated target. Otherwise a single merger will be inserted for the origin target.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @param replicatedSinks The replicated relative (and real) sinks of <code>originPart</code>.
	 * @param replicatedTargets The replicated operators, whose origin one is subscribed to the origin operator of <code>replicatedSinks</code>, 
	 * if it's a relative sink, or {@link Optional#absent()}, if the target was not replicated.
	 * @param originPartOfTarget The query part containing the origin operator of <code>replicatedTarget</code> or {@link Optional#absent()}, 
	 * if <code>replicatedTarget</code> is {@link Optional#absent()}.
	 * @param queryPartsOfReplicatedTarget The replicated query parts containing each entry of <code>replicatedTargets </code> or 
	 * {@link Optional#absent()}, if <code>replicatedTargets</code> is {@link Optional#absent()}.
	 * @param subscription The subscription to be broken due to the insertion of the merger.
	 * @return The updated mapping of replicated query parts to the origin ones. If <code>replicatedTargets</code> is null, a single merger will be the 
	 * only operator of a new query part mapped to itself. Otherwise merger will be part of each <code>queryPartsOfReplicatedTarget</code>.
	 * @throws NullPointerException if <code>originPart</code>, <code>replicatesToOrigin</code>, <code>replicatedSinks</code> or 
	 * <code>subscription</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>replicatedTargets</code> is present and <code>originPartOfTarget</code> or <code>queryPartsOfReplicatedTarget</code> is not present.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processTargetOfSubscription(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, Collection<ILogicalOperator> replicatedSinks, 
			Optional<List<ILogicalOperator>> replicatedTargets, Optional<ILogicalQueryPart> originPartOfTarget, 
			Optional<List<ILogicalQueryPart>> queryPartsOfReplicatedTarget, LogicalSubscription subscription) 
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");
		else if(replicatedSinks == null)
			throw new NullPointerException("List of replicated sinks for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		else if(replicatedTargets == null)
			throw new NullPointerException("Optional of the replicated targets to process must be not null!");
		else if(originPartOfTarget == null)
			throw new NullPointerException("Optional of the query part containing the origin target to process must be not null!");
		else if(queryPartsOfReplicatedTarget == null)
			throw new NullPointerException("Optional of the query parts containing the replicated targets to process must be not null!");
		else if(replicatedTargets.isPresent() && (!originPartOfTarget.isPresent() || !queryPartsOfReplicatedTarget.isPresent()))
			throw new IllegalArgumentException("Optional of the query part containing the origin target and optinal of the query parts containing " +
					"the replicated targets to process must be present!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Subscribe merger for every target
		if(!replicatedTargets.isPresent()) {
			
			Optional<ILogicalOperator> replicatedTarget = Optional.fromNullable(null);
			Optional<ILogicalQueryPart> queryPartOfReplicatedTarget = Optional.fromNullable(null);
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.insertMerger(originPart, modifiedReplicatesToOrigin, 
					replicatedSinks, replicatedTarget , originPartOfTarget, queryPartOfReplicatedTarget, subscription));
			
		} else {
		
			for(int targetNo = 0; targetNo < replicatedTargets.get().size(); targetNo++) {
			
				Optional<ILogicalOperator> replicatedTarget = Optional.of(replicatedTargets.get().get(targetNo));
				Optional<ILogicalQueryPart> queryPartOfReplicatedTarget = Optional.of(queryPartsOfReplicatedTarget.get().get(targetNo));
				modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.insertMerger(originPart, modifiedReplicatesToOrigin, 
						replicatedSinks, replicatedTarget, originPartOfTarget, queryPartOfReplicatedTarget, subscription));
				
			} 
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Inserts a merger between a collection of replicated sinks and a target (replicated or origin). 
	 * @param originPart The origin query part which contains the origin sink.
	 * @param replicatesToOrigin The mapping of replicated query parts to the origin ones.
	 * @param replicatedSinks The replicated relative (and real) sinks of <code>originPart</code>.
	 * @param replicatedTarget The replicated operator, whose origin one is subscribed to the origin operator of <code>replicatedSinks</code>, 
	 * if it's a relative sink, or {@link Optional#absent()}, if the target was not replicated.
	 * @param originPartOfTarget The query part containing the origin operator of <code>replicatedTarget</code> or {@link Optional#absent()}, 
	 * if <code>replicatedTarget</code> is {@link Optional#absent()}.
	 * @param queryPartOfReplicatedTarget The replicated query part containing <code>replicatedTarget</code> or {@link Optional#absent()}, 
	 * if <code>replicatedTarget</code> is {@link Optional#absent()}.
	 * @param subscription The subscription to be broken due to the insertion of the merger.
	 * @return The updated mapping of replicated query parts to the origin ones. If <code>replicatedTarget</code> is null, the merger will be the 
	 * only operator of a new query part mapped to itself. Otherwise the merger will be part of <code>queryPartOfReplicatedTarget</code>.
	 * @throws NullPointerException if <code>originPart</code>, <code>replicatesToOrigin</code>, <code>replicatedSinks</code> or 
	 * <code>subscription</code> is null.
	 * @throws IllegalArgumentException if <code>replicatesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>replicatedTarget</code> is present and <code>originPartOfTarget</code> or <code>queryPartOfReplicatedTarget</code> is not present.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertMerger(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, Collection<ILogicalOperator> replicatedSinks,
			Optional<ILogicalOperator> replicatedTarget, Optional<ILogicalQueryPart> originPartOfTarget, 
			Optional<ILogicalQueryPart> queryPartOfReplicatedTarget, LogicalSubscription subscription) 
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(replicatesToOrigin == null)
			throw new NullPointerException("Mapping of replicates to origin query parts must be not null!");
		else if(!replicatesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of replicates to origin query parts must contain the origin query part for modification!");
		else if(replicatedSinks == null)
			throw new NullPointerException("List of replicated sinks for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		else if(replicatedTarget == null)
			throw new NullPointerException("Optional of the replicated target to process must be not null!");
		else if(originPartOfTarget == null)
			throw new NullPointerException("Optional of the query part containing the origin target to process must be not null!");
		else if(queryPartOfReplicatedTarget == null)
			throw new NullPointerException("Optional of the query part containing the replicated target to process must be not null!");
		else if(replicatedTarget.isPresent() && (!originPartOfTarget.isPresent() || !queryPartOfReplicatedTarget.isPresent()))
			throw new IllegalArgumentException("Optional of the query part containing the origin target and optinal of the query part containing " +
					"the replicated target to process must be present!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// The merger to be inserted
		ILogicalOperator merger = null;
		try {
			
			merger = ReplicationQueryPartModificator.mergerClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new InstantiationException("Merger could not be instantiated!");
			
		}
		
		// Subscribe merger to sinks
		for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
			
			((List<ILogicalOperator>) replicatedSinks).get(sinkNo).subscribeSink(merger, sinkNo, subscription.getSourceOutPort(), 
					subscription.getSchema());
			
		}
		
		// Subscribe target to merger
		if(!replicatedTarget.isPresent()) {
			
			merger.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), 0, subscription.getSchema());
			
			// Create new query part
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			ILogicalQueryPart mergerPart = new LogicalQueryPart(merger);
			modifiedQueryParts.add(mergerPart);
			modifiedReplicatesToOrigin.put(mergerPart, modifiedQueryParts);
			
		}
		else {
			
			merger.subscribeSink(replicatedTarget.get(), subscription.getSinkInPort(), 0, subscription.getSchema());
			
			// Create modified query part
			Collection<ILogicalOperator> operatorsWithMerger = Lists.newArrayList(queryPartOfReplicatedTarget.get().getOperators());
			operatorsWithMerger.add(merger);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			for(ILogicalQueryPart part : replicatesToOrigin.get(originPartOfTarget.get())) {
				
				if(!part.equals(queryPartOfReplicatedTarget.get()))
					modifiedQueryParts.add(part);
				
			}
				
			ILogicalQueryPart mergerPart = new LogicalQueryPart(operatorsWithMerger);
			modifiedQueryParts.add(mergerPart);
			modifiedReplicatesToOrigin.put(originPartOfTarget.get(), modifiedQueryParts);
			
		}
		
		if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSinks = "(";
			for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
				
				strSinks += ((List<ILogicalOperator>) replicatedSinks).get(sinkNo).getName();
						
				if(sinkNo == replicatedSinks.size() - 1)
					strSinks += ")";
				else strSinks += ", ";
				
			}
			String target = "";
			if(replicatedTarget.isPresent())
				target = replicatedTarget.get().getName();
			ReplicationQueryPartModificator.log.debug("Inserted a merger between {} and {}.", strSinks, target);
			
		}
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Determines the degree of replication given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION replication <degree>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter "replication".
	 * @return The degree of replication given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 */
	private static int determineDegreeOfReplication(List<String> modificatorParameters) throws NullPointerException {
		
		// Preconditions 1
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part replicator must not be null!");
		
		// The return value
		int degreeOfReplication = -1;
		
		// Preconditions 2
		if(modificatorParameters.isEmpty()) {
			
			ReplicationQueryPartModificator.log.warn("Parameters for query part replicator does not contain the degree of replication! " +
					"The degree of replication is set to {}.", ReplicationQueryPartModificator.min_degree);
			degreeOfReplication = 2;
			
		} else try {
		
			degreeOfReplication = Integer.parseInt(modificatorParameters.get(0));
		
			// Preconditions 3
			if(degreeOfReplication < ReplicationQueryPartModificator.min_degree) {
				
				ReplicationQueryPartModificator.log.warn("First parameter for query part replicator, the degree of replication, " +
						"should be at least {}. The degree of replication is set to {}.", ReplicationQueryPartModificator.min_degree, 
						ReplicationQueryPartModificator.min_degree);
				degreeOfReplication = 2;			
				
			}
		
		} catch(NumberFormatException e) {
			
			ReplicationQueryPartModificator.log.error("First parameter for query part replicator must be an integer!", e);
			ReplicationQueryPartModificator.log.warn("The degree of replication is set to {}.", ReplicationQueryPartModificator.min_degree);
			return ReplicationQueryPartModificator.min_degree;
			
		}		
		
		return degreeOfReplication;
		
	}
	
	/**
	 * Compares the degree of replications given by the user with the number of available peers. 
	 * @param numQueryParts The number of query parts, which shall be replicated.
	 * @param degreeOfReplication The degree of replication given by the user.
	 * @throws NullPointerException if no {@link IP2PDictionary} could be found.
	 * @throws IllegalArgumentException if <code>numQueryParts</code> is less than one or 
	 * if <code>degreeOfReplication</code> is less {@value #min_degree}.
	 */
	private static void checkDegreeOfReplication(int numQueryParts, int degreeOfReplication) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions 1
		if(numQueryParts < 1)
			throw new IllegalArgumentException("At least one query part is needed for replication!");
		else if(degreeOfReplication < ReplicationQueryPartModificator.min_degree)
			throw new IllegalArgumentException("Degree of replication must be at least " + ReplicationQueryPartModificator.min_degree + "!");
		
		// The bound IP2PDictionary
		Optional<IP2PDictionary> optDict = Activator.getP2PDictionary();
		
		// Preconditions 2
		if(!optDict.isPresent())
			throw new NullPointerException("No P2PDictionary available!");
		
		// Check number of available peers (inclusive the local one)
		int numRemotePeers = optDict.get().getRemotePeerIDs().size();
		if(numRemotePeers + 1 < degreeOfReplication * numQueryParts) {
			
			ReplicationQueryPartModificator.log.warn("Replication leads to at least {} query parts, " +
					"but there are only {} peers available. Consider to provide more peers. " +
					"For the given configuration some query parts will be executed on the same peer.", 
					degreeOfReplication * numQueryParts, numRemotePeers + 1);
			
		}
		
	}

}