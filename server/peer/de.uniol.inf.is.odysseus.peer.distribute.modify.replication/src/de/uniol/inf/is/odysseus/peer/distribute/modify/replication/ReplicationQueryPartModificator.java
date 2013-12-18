package de.uniol.inf.is.odysseus.peer.distribute.modify.replication;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
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
 * merge the result sets of the replicates for each relative sink within every single query part.
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
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, 
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
					ReplicationQueryPartModificator.determineDegreeOfReplication(queryParts.size(), modificatorParameters);
			
			// Copy the query parts
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin = 
					ReplicationQueryPartModificator.copyAndCutQueryParts(queryParts, degreeOfReplication);
			
			// Modify each query part
			for(ILogicalQueryPart originPart : queryParts)
				replicatesToOrigin.putAll(ReplicationQueryPartModificator.modify(originPart, replicatesToOrigin));
			
			// Create the return value
			for(ILogicalQueryPart originPart : replicatesToOrigin.keySet())
					modifiedParts.addAll(replicatesToOrigin.get(originPart));
			
		} catch(Exception e) {
			
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}
		
		return modifiedParts;
		
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
				ReplicationQueryPartModificator.collectRelativeSinks(originPart , modifiedReplicatesToOrigin.get(originPart));
		
		// Modify each sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.modifySink(originPart, replicatesToOrigin, originSink, 
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
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processRealSink(originPart, replicatesToOrigin, originSink, 
					replicatedSinks));
			
		}
		
		// Process replicates for each subscription
		for(LogicalSubscription subscription : originSink.getSubscriptions()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processSubscription(originPart, replicatesToOrigin, originSink, 
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
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		modifiedQueryParts.add(new LogicalQueryPart(merger));
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
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, replicatesToOrigin, 
					replicatedSinks, replicatedTargets, optTargetPart, queryPartOfReplicatedTarget, subscription));
			
		} else {
			
			// Find clones of target
			int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
			List<ILogicalOperator> replicatedTargets = Lists.newArrayList();
			for(ILogicalQueryPart replicate : replicatesToOrigin.get(optTargetPart.get()))
				replicatedTargets.add(((ImmutableList<ILogicalOperator>) replicate.getOperators()).get(targetNo));
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, replicatesToOrigin, 
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
			modifiedQueryParts.add(new LogicalQueryPart(merger));
			modifiedReplicatesToOrigin.put(new LogicalQueryPart(merger), modifiedQueryParts);
			
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
			modifiedQueryParts.add(new LogicalQueryPart(operatorsWithMerger));
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
			ReplicationQueryPartModificator.log.debug("Inserted a merger between {} and {}.", strSinks, replicatedTarget.get().getName());
			
		}
		return modifiedReplicatesToOrigin;
		
	}

	/**
	 * Collects all relative (and real) sinks of a query part and it's replicates. <br />
	 * The origin query part will not be modified.
	 * @param originPart The query part whose sinks shall be collected.
	 * @param replicates A collection of all replicates of <code>originPart</code>.
	 * @return A mapping of replicated relative (and real) sinks to the origin ones.
	 * @throws NullPointerException if <code>originPart</code> or <code>replicates</code> is null.
	 * @throws IllegalArgumentException if at least one entry of <code>replicates</code> does not contain all sinks of <code>originPart</code>.
	 */
	private static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSinks(
			ILogicalQueryPart originPart, Collection<ILogicalQueryPart> replicates) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part to collect relative sinks must be not null!");
		else if(replicates == null)
			throw new NullPointerException("List of replicates to collect relative sinks must be not null!");
		
		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = Maps.newHashMap();
		
		// Collect origin sinks
		List<ILogicalOperator> originSinks = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(originPart);
		for(int sinkNo = 0; sinkNo < originSinks.size(); sinkNo++) {
			
			Collection<ILogicalOperator> copiedSinks = Lists.newArrayList();
			
			try {
			
				for(ILogicalQueryPart replicate : replicates)
					copiedSinks.add(((List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(replicate)).get(sinkNo));
				
			} catch(IndexOutOfBoundsException e) {
				
				throw new IllegalArgumentException("At least one replicate does not contain all sinks of the origin!");
				
			}
			
			copiedToOriginSinks.put(originSinks.get(sinkNo), copiedSinks);
			ReplicationQueryPartModificator.log.debug("Found {} as a relative sink of {}.", originSinks.get(sinkNo).getName(), originPart);
			
		}
		
		return copiedToOriginSinks;
		
	}

	/**
	 * Makes as many copies of query parts as given by the degree of replication. All copied query parts will be cut, so there will be no 
	 * subscription from or to outwards a query part. <br />
	 * The origin query parts will not be modified.
	 * @param queryParts A collection of query parts to copy.
	 * @param degreeOfReplication The degree of replication is the number of copies to make.
	 * @throws NullPointerException if <code>queryParts</code> is null.
	 * @throws IllegalArgumentException if <code>degreeOfReplication</code> is less than 1.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyAndCutQueryParts(
			Collection<ILogicalQueryPart> queryParts, int degreeOfReplication) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(queryParts == null)
			throw new NullPointerException("Query parts to be copied must be not null!");
		else if(degreeOfReplication < 1)
			throw new IllegalArgumentException("Degree of replication must be greater than 0!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginPart = Maps.newHashMap();
	
		Collection<Map<ILogicalQueryPart, ILogicalQueryPart>> plainCopies = Lists.newArrayList();
		for(int copyNo = 0; copyNo < degreeOfReplication; copyNo++)
			plainCopies.add(LogicalQueryHelper.copyQueryPartsDeep(queryParts));
		
		for(Map<ILogicalQueryPart, ILogicalQueryPart> plainCopyMap : plainCopies) {
			
			for(ILogicalQueryPart copy : plainCopyMap.keySet() ) {
				
				Collection<ILogicalQueryPart> copyList = null;
				
				if(copiesToOriginPart.containsKey(plainCopyMap.get(copy)))
					copyList = copiesToOriginPart.get(plainCopyMap.get(copy));
				else {
					
					copyList = Lists.newArrayList();
					copiesToOriginPart.put(plainCopyMap.get(copy), copyList);
					
				}
			
				LogicalQueryHelper.cutQueryPart(copy);
				copyList.add(copy);
				ReplicationQueryPartModificator.log.debug("Created query part {} as a copy of query part {}.", copy, plainCopyMap.get(copy));
				
			}
			
		}

		return copiesToOriginPart;
		
	}

	/**
	 * Determines the degree of replications given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION replication <degree>
	 * @param numQueryParts The number of query parts, which shall be replicated.
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter "replication".
	 * @return The degree of parallelism given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null or if no {@link IP2PDictionary} is available.
	 * @throws IllegalArgumentException if <code>numQueryParts</code> is less than one, if <code>modificatorParameters</code> is empty or 
	 * if the parameter for the degree of replication within <code>modificatorParameters</code> is less than {@value #min_degree}.
	 * @throws NumberFormatException if the parameter for the degree of replication within <code>modificatorParameters</code> could not be cast to 
	 * an integer.
	 */
	private static int determineDegreeOfReplication(int numQueryParts, List<String> modificatorParameters) 
			throws NullPointerException, IllegalArgumentException, NumberFormatException {
		
		// Preconditions 1
		if(numQueryParts < 1)
			throw new IllegalArgumentException("At least one query part is needed for replication!");
		else if(modificatorParameters == null)
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
		
		// The bound IP2PDictionary
		Optional<IP2PDictionary> optDict = Activator.getP2PDictionary();
		
		// Preconditions 3
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
		
		ReplicationQueryPartModificator.log.debug("Degree of replication set to {}.", degreeOfReplication);
		return degreeOfReplication;
		
	}

}