package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A abstract modifier of {@link ILogicalQueryPart}s, which fragments data streams from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part.
 * @author Michael Brand
 */
public abstract class AbstractFragmentationQueryPartModificator implements IQueryPartModificator {
	
	// TODO still unions after sinks, which are not relevant for fragmentation, when query cloud is used. m.b.

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentationQueryPartModificator.class);
	
	/**
	 * The minimum degree of fragmentation.
	 */
	private static final int min_degree = 2;
	
	@Override
	public abstract String getName();

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, 
			QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {
		
		// Preconditions
		if(queryParts == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Query parts to be modified must be not null!");
			AbstractFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		if(queryParts.isEmpty()) {
			
			AbstractFragmentationQueryPartModificator.log.warn("No query parts given to fragment");
			return modifiedParts;
			
		}
		
		try {
		
			// Determine degree of fragmentation and source to be fragmented
			final String sourceName = AbstractFragmentationQueryPartModificator.determineSourceName(modificatorParameters);
			final int degreeOfFragmentation = AbstractFragmentationQueryPartModificator.determineDegreeOfFragmentation(modificatorParameters);
			
			// Copy the query parts
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin = 
					LogicalQueryHelper.copyAndCutQueryParts(queryParts, degreeOfFragmentation);
			
			// Determine all relevant query parts, relative sources and subscriptions
			Map<ILogicalQueryPart, Map<ILogicalOperator, Collection<LogicalSubscription>>> relevantQueryParts = 
					AbstractFragmentationQueryPartModificator.determineRelevantQueryParts(copiesToOrigin.keySet(), sourceName);
			AbstractFragmentationQueryPartModificator.logRelevantQueryParts(relevantQueryParts);
			
			// Determine all irrelevant query parts
			Collection<ILogicalQueryPart> irrelevantQueryParts = Lists.newArrayList(copiesToOrigin.keySet());
			irrelevantQueryParts.removeAll(relevantQueryParts.keySet());
			
			// Keep only one copy of irrelevant parts
			for(ILogicalQueryPart originPart : irrelevantQueryParts) {
					
				ILogicalQueryPart copyToKeep = copiesToOrigin.get(originPart).iterator().next();
				copiesToOrigin.get(originPart).clear();
				copiesToOrigin.get(originPart).add(copyToKeep);
				
			}
			
			if(AbstractFragmentationQueryPartModificator.log.isDebugEnabled()) {
				
				// Print working copies				
				for(ILogicalQueryPart origin : copiesToOrigin.keySet()) {
					
					for(ILogicalQueryPart copy : copiesToOrigin.get(origin))
						AbstractFragmentationQueryPartModificator.log.debug("Created query part {} as a copy of query part {}.", copy, origin);
					
				}
				
			}			
			AbstractFragmentationQueryPartModificator.checkDegreeOfFragmentation(relevantQueryParts.keySet().size(), degreeOfFragmentation);
			AbstractFragmentationQueryPartModificator.log.debug("Degree of fragmentation set to {}.", degreeOfFragmentation);
			
			// A mapping of relative sinks, which were connected to the targets of the operator for fragmentation before
			// to the inserted operators for fragmentation (as query parts).
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart = Maps.newHashMap();
			
			// Modify each query part for insertion of operators for fragmentation
			for(ILogicalQueryPart originPart : relevantQueryParts.keySet()) {
				
				copiesToOrigin = this.modifyForFragmentation(originPart, copiesToOrigin, 
						relevantQueryParts.get(originPart), relSinksToFragmentPart, sourceName);
				
			}
			
			// Modify each query part for insertion of operators for reunion
			for(ILogicalQueryPart originPart : relevantQueryParts.keySet())
				copiesToOrigin = this.modifyForMerging(originPart, copiesToOrigin, relSinksToFragmentPart, sourceName);
			
			// Attach all query parts which are not relevant for the fragmentation
			for(ILogicalQueryPart originPart : irrelevantQueryParts)
				copiesToOrigin = AbstractFragmentationQueryPartModificator.attachIrrelevantQueryPart(originPart, copiesToOrigin);
			
			// Create the return value
			for(ILogicalQueryPart originPart : copiesToOrigin.keySet())
					modifiedParts.addAll(copiesToOrigin.get(originPart));
			
		} catch(Exception e) {
			
			AbstractFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}
		
		return modifiedParts;
		
	}

	/**
	 * Modifies a single for fragmentation irrelevant query part. For every relative sink subscribed to a fragmented query part, subscriptions 
	 * to each fragment will be made.
	 * @param originPart The origin query part to be modified.
	 * @param copiesToOrigin The mapping of copies query parts to the origin ones.
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws NullPointerException if <code>originPart</code> or <code>copiesToOrigin</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachIrrelevantQueryPart(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Process each relative sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.attachSinkOfIrrelevantPart(originPart, modifiedCopiesToOrigin, originSink, 
					copiedToOriginSinks.get(originSink));
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a single relative sink of a query part which is irrelevant for fragmentation. 
	 * For a relative sink subscribed to a fragmented query part, subscriptions to each fragment will be made.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param replicatesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param copiedSinks The copies of <code>originSink</code>.
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>originSink</code> or 
	 * <code>copiedSinks</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>copiedSinks</code> contains more than one sink.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachSinkOfIrrelevantPart(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink, 
			Collection<ILogicalOperator> copiedSinks) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(copiedSinks == null)
			throw new NullPointerException("List of copied sinks for modification must be not null!");
		else if(copiedSinks.size() != 1)
			throw new IllegalArgumentException("There must be exactly one copy if the origin sink!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		for(LogicalSubscription subscription : originSink.getSubscriptions()) {
			
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.attachSubscriptionOfIrrelevantPart(
					originPart, modifiedCopiesToOrigin, originSink, copiedSinks.iterator().next(), subscription);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Creates subscriptions from a relative sink of a query part which is irrelevant for fragmentation to each fragment.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param copiedSink The copy of <code>originSink</code>.
	 * @param subscription The origin subscription changed due to fragmentation.
	 * @return The updated mapping of copies query parts to the origin ones.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>originSink</code>, 
	 * <code>copiedSink</code> or <code>subscription</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws InstantiationException if a merger could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachSubscriptionOfIrrelevantPart(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			ILogicalOperator copiedSink, LogicalSubscription subscription) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions 1
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(copiedSink == null)
			throw new NullPointerException("Copied sink for modification must be not null!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Preconditions 2
		if(originPart.getOperators().contains(subscription.getTarget()))
			return modifiedCopiesToOrigin;
		
		// The query part containing the origin target
		Optional<ILogicalQueryPart> optTargetPart = 
				LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), subscription.getTarget());
		
		if(!optTargetPart.isPresent()) {
			
			Optional<List<ILogicalOperator>> copiedTargets = Optional.fromNullable(null);
			Optional<List<ILogicalQueryPart>> queryPartsOfCopiedTarget = Optional.fromNullable(null);
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.attachSubscriptionToTarget(originPart, modifiedCopiesToOrigin, 
					originSink, copiedSink, copiedTargets, optTargetPart, queryPartsOfCopiedTarget, subscription);
			
		} else {
			
			// Find clones of target
			int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
			List<ILogicalOperator> copiedTargets = Lists.newArrayList();
			for(ILogicalQueryPart copy : copiesToOrigin.get(optTargetPart.get()))
				copiedTargets.add(((ImmutableList<ILogicalOperator>) copy.getOperators()).get(targetNo));
			
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.attachSubscriptionToTarget(originPart, modifiedCopiesToOrigin, 
					originSink, copiedSink, Optional.of(copiedTargets), optTargetPart, 
					Optional.of((List<ILogicalQueryPart>) copiesToOrigin.get(optTargetPart.get())),	subscription);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Creates a single subscription from a relative sink of a query part which is irrelevant for fragmentation to a fragment.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of <code>originPart</code>.
	 * @param copiedSink The copied relative sink of <code>originPart</code>.
	 * @param copiedTargets The copied operators, whose origin one is subscribed to <code>originSink</code>, or {@link Optional#absent()}, 
	 * if the target was not copied.
	 * @param originPartOfTarget The query part containing the origin operator of <code>copiedTargets</code> or {@link Optional#absent()}, 
	 * if <code>copiedTargets</code> is {@link Optional#absent()}.
	 * @param queryPartsOfCopiedTarget The copied query parts containing each entry of <code>copiedTargets </code> or 
	 * {@link Optional#absent()}, if <code>copiedTargets</code> is {@link Optional#absent()}.
	 * @param subscription The subscription to be modified due to the fragmentation.
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>copiedTargets</code> is present and <code>originPartOfTarget</code> or <code>queryPartsOfCopiedTarget</code> is not present.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachSubscriptionToTarget(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink, ILogicalOperator copiedSink, 
			Optional<List<ILogicalOperator>> copiedTargets, Optional<ILogicalQueryPart> originPartOfTarget, 
			Optional<List<ILogicalQueryPart>> queryPartsOfCopiedTarget, LogicalSubscription subscription) {
		
		// Preconditions
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(copiedSink == null)
			throw new NullPointerException("Copied sink for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		else if(copiedTargets == null)
			throw new NullPointerException("Optional of the replicated targets to process must be not null!");
		else if(originPartOfTarget == null)
			throw new NullPointerException("Optional of the query part containing the origin target to process must be not null!");
		else if(queryPartsOfCopiedTarget == null)
			throw new NullPointerException("Optional of the query parts containing the copied targets to process must be not null!");
		else if(copiedTargets.isPresent() && (!originPartOfTarget.isPresent() || !queryPartsOfCopiedTarget.isPresent()))
			throw new IllegalArgumentException("Optional of the query part containing the origin target and optional of the query parts containing " +
					"the copied targets to process must be present!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		if(!copiedTargets.isPresent()) {
			
			ILogicalOperator target = subscription.getTarget();
			copiedSink.subscribeSink(target, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
		
		} else {
		
			for(int targetNo = 0; targetNo < copiedTargets.get().size(); targetNo++) {
			
				ILogicalOperator target = copiedTargets.get().get(targetNo);
				copiedSink.subscribeSink(target, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
				
			} 
			
		}
		
		// Modify part
		Collection<ILogicalOperator> operators = Lists.newArrayList(originPart.getOperators());
		operators.remove(originSink);
		operators.add(copiedSink);
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		modifiedParts.add(new LogicalQueryPart(operators));
		copiesToOrigin.put(originPart, modifiedParts);
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a single query part for reunion. For every real sink of the part a single operator for reunion will be inserted. 
	 * For every relative (not real) sink of the part each subscription leading outwards the query part containing the sink will be processed.
	 * @param originPart The origin query part to be modified.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @param sourceName TODO
	 * @return The updated mapping of copied query parts to the origin ones, where the copied parts contain all inserted operators for reunion.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiedToOrigin</code>, or <code>relSinksToFragmentPart</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws QueryPartModificationException if an operator for reunion could not be inserted.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyForMerging(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, 
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, String sourceName) 
					throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		// TODO sourceName not null
	
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Modify each relative sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			modifiedCopiesToOrigin = this.modifySink(originPart, modifiedCopiesToOrigin, originSink, copiedToOriginSinks.get(originSink), 
					relSinksToFragmentPart, sourceName);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a relative (or real) sink. For real sinks a single operator for reunion will be inserted. 
	 * Otherwise, each subscription leading outwards the query part containing the sink will be processed.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param copiedSinks The copies of <code>originSink</code>.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @param sourceName TODO
	 * @return The updated mapping of copied query parts to the origin ones, where the copied parts contain all inserted operators for reunion.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>originSink</code>, 
	 * <code>copiedSinks</code> or<code>relSinksToFragmentPart</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws QueryPartModificationException if an operator for reunion could not be inserted.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySink(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks, Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, String sourceName)
				throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new NullPointerException("Origin sink for modification must be not null!");
		else if(copiedSinks == null)
			throw new NullPointerException("List of copied sinks for modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		// TODO sourceName not null
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		if(originSink.getSubscriptions().isEmpty())
			modifiedCopiesToOrigin = this.modifyRealSink(originPart, modifiedCopiesToOrigin, originSink, copiedSinks);
		else {
		
			for(LogicalSubscription subscription : originSink.getSubscriptions()) {
			
				modifiedCopiesToOrigin = this.modifySubscriptionForReunion(originPart, modifiedCopiesToOrigin, originSink, 
						copiedSinks, subscription, relSinksToFragmentPart, sourceName);
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Processes a subscription, which source is a relative sink of a query part. If the target operator of the subscription was copied (fragmented), 
	 * the operator for reunion (to be inserted) and the operator for fragmentation (as the next operator) will be left out due to optimization. 
	 * This optimization can be made, because a data reunion followed by another fragmentation of the same source makes no sense.
	 * Otherwise a single operator for reunion will be inserted for the single target.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param copiedSinks The copies of <code>originSink</code>.
	 * @param subscription The subscription to be broken due to the insertion of the operator for reunion.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @param sourceName TODO
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws QueryPartModificationException if an operator for reunion could not be inserted.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForReunion(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks, LogicalSubscription subscription, 
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, String sourceName) throws QueryPartModificationException;

	/**
	 * Processes a real sink, where a single operator for reunion will be inserted.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin sink of a query part.
	 * @param copiedSinks The copies of <code>originSink</code>.
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws QueryPartModificationException if an operator for reunion could not be inserted.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSink(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks) throws QueryPartModificationException;

	/**
	 * Modifies a single query part. For every relevant real source of the part a single operator for fragmentation will be inserted. 
	 * For every relevant relative (not real) source of the part each subscription from outwards the query part containing the source will be processed.
	 * @param originPart The origin query part to be modified.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param relevantSubscriptionsToSource The mapping of relevant subscriptions for fragmentation to the relative source of <code>originPart</code>.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @param sourceName TODO
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code> or <code>relSinksToFragmentPart</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws QueryPartModificationException if an operator for fragmentation could not be inserted.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyForFragmentation(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, 
			Map<ILogicalOperator, Collection<LogicalSubscription>> relevantSubscriptionsToSource,
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart,
			String sourceName) 
				throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relevantSubscriptionsToSource == null)
			throw new NullPointerException("Mapping of relevant subscriptions for fragmentation to the relative source of the query part for " +
					"modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		// TODO sourceName not null
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sources
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = 
				LogicalQueryHelper.collectRelativeSources(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Process each relative source, which is relevant for fragmentation
		for(ILogicalOperator relevantSource : relevantSubscriptionsToSource.keySet()) {
			
			modifiedCopiesToOrigin = this.modifyRelativeSourceForFragmentation(originPart, modifiedCopiesToOrigin, relevantSource, 
					copiedToOriginSources.get(relevantSource), relevantSubscriptionsToSource.get(relevantSource), relSinksToFragmentPart, sourceName);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a relative (or real) source. For real sources a single operator for reunion will be inserted. 
	 * Otherwise, each subscription coming from outwards the query part containing the source will be processed.
	 * @param originPart The origin query part which contains the origin source.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param relativeSource The origin relative source of a query part.
	 * @param copiedSources The copies of <code>relativeSource</code>.
	 * @param relevantSubscriptions All relevant subscriptions for fragmentation of <code>relativeSource</code>.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @param sourceName TODO
	 * @return The updated mapping of copied query parts to the origin ones, where the copied parts contain all inserted operators for fragmentation.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>relativeSource</code>, 
	 * <code>copiedSources</code>, <code>relevantSubscriptions</code> or <code>relSinksToFragmentPart</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws QueryPartModificationException if an operator for fragmentation could not be inserted.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRelativeSourceForFragmentation(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator relativeSource, 
			Collection<ILogicalOperator> copiedSources, Collection<LogicalSubscription> relevantSubscriptions,
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, String sourceName) 
				throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relativeSource == null)
			throw new NullPointerException("Origin source for modification must be not null!");
		else if(copiedSources == null)
			throw new NullPointerException("List of copied sources for modification must be not null!");
		else if(relevantSubscriptions == null)
			throw new NullPointerException("List of relevant subscriptions for fragmentation must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		// TODO sourceName not null
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		if(relevantSubscriptions.isEmpty()) {
			
			modifiedCopiesToOrigin = this.modifyRealSourceForFragmentation(originPart, modifiedCopiesToOrigin, relativeSource, 
					copiedSources);
			
		} else {
			
			for(LogicalSubscription subscription : relevantSubscriptions) {
				
				modifiedCopiesToOrigin = this.modifySubscriptionForFragmentation(originPart, modifiedCopiesToOrigin, relativeSource, 
							copiedSources, subscription, relSinksToFragmentPart);
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	// TODO javaDoc
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSourceForFragmentation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator relativeSource,
			Collection<ILogicalOperator> copiedSources)
				throws QueryPartModificationException;

	/**
	 * Processes a subscription, which sink is a relative source of a query part. 
	 * A single operator for fragmentation will be inserted for the single target.
	 * @param originPart The origin query part which contains the origin source.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param source The origin relative source of a query part.
	 * @param copiedSources The copies of <code>source</code>.
	 * @param subscription The subscription to be broken due to the insertion of the operator for fragmentation.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @return The updated mapping of copied query parts to the origin ones.
	 * @throws QueryPartModificationException if an operator for fragmentation could not be inserted.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForFragmentation(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator source, Collection<ILogicalOperator> copiedSources, 
			LogicalSubscription subscription, Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart) 
					throws QueryPartModificationException;

	/**
	 * Logs all elements relevant for fragmentation. These elements are relevant query parts, for each relevant query part relevant relative sources 
	 * and for each relevant relative source relevant subscriptions, if the relative source is not a real one.
	 */
	private static void logRelevantQueryParts(Map<ILogicalQueryPart, Map<ILogicalOperator, Collection<LogicalSubscription>>> relevantQueryParts) {

		// Preconditions
		if(relevantQueryParts == null)
			throw new NullPointerException("Mapping of relevant subscriptions for fragmentation to the relative source to the query part for " +
					"modification must be not null!");
		
		for(ILogicalQueryPart part : relevantQueryParts.keySet()) {
			
			AbstractFragmentationQueryPartModificator.log.debug("Found {} as a query part relevant for fragmentation.", part.toString());
			
			for(ILogicalOperator source : relevantQueryParts.get(part).keySet()) {
				
				if(relevantQueryParts.get(part).get(source).isEmpty()) {
					
					AbstractFragmentationQueryPartModificator.log.debug("Found {} in {} as a real source for fragmentation.", 
							source.getName(), part.toString());
					
				} else {
					
					AbstractFragmentationQueryPartModificator.log.debug("Found {} in {} as a relative source for fragmentation.", 
							source.getName(), part.toString());
					
					for(LogicalSubscription sub : relevantQueryParts.get(part).get(source)) {
						
						AbstractFragmentationQueryPartModificator.log.debug("Found the subscription {} from {} in {} as a relative source " +
								"for fragmentation.", new Object[] {sub.toString(), source.getName(), part.toString()});
						
					}
					
				}
				
			}
			
		}
		
	}

	/**
	 * Determines all query parts, which are relevant for the fragmentation. Such query parts contain relative sources passed by a data stream 
	 * coming up from the source to be fragmented or they contain the source to be fragmented itself.
	 * @param queryParts A collection of query parts to analyze.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A mapping of relative sources and subscriptions, which are both relevant for the fragmentation, to their query parts. There may 
	 * be no subscriptions, if the relative source is the source to be fragmented.
	 * @throws NullPointerException if <code>queryParts</code> or <code>sourceName</code> is null.
	 */
	private static Map<ILogicalQueryPart, Map<ILogicalOperator, Collection<LogicalSubscription>>> determineRelevantQueryParts(
			Collection<ILogicalQueryPart> queryParts, String sourceName) throws NullPointerException {
		
		// Preconditions
		if(queryParts == null)
			throw new NullPointerException("Query parts for modification must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Map<ILogicalOperator, Collection<LogicalSubscription>>> relevantQueryParts = Maps.newHashMap();
		
		for(ILogicalQueryPart part : queryParts) {
		
			Optional<Map<ILogicalOperator, Collection<LogicalSubscription>>> optRelevantSources = 
					AbstractFragmentationQueryPartModificator.determineRelevantSourcesOfQueryParts(part, sourceName);
			if(optRelevantSources.isPresent())
				relevantQueryParts.put(part, optRelevantSources.get());
			
		}
		
		return relevantQueryParts;
		
	}

	/**
	 * Determines all relative sources and subscriptions, which are relevant for the fragmentation.
	 * @param queryPart The query part to analyze.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A mapping of subscriptions, which are relevant for the fragmentation, to their relative sources.  There may 
	 * be no subscriptions, if the relative source is the source to be fragmented.
	 * @throws NullPointerException if <code>queryPart</code> or <code>sourceName</code> is null.
	 */
	private static Optional<Map<ILogicalOperator, Collection<LogicalSubscription>>> determineRelevantSourcesOfQueryParts(
			ILogicalQueryPart queryPart, String sourceName) throws NullPointerException {
		
		// Preconditions
		if(queryPart == null)
			throw new NullPointerException("Query part for modification must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Map<ILogicalOperator, Collection<LogicalSubscription>> relevantSources = Maps.newHashMap();
		
		// The relative sources of the query part
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(queryPart);
		
		for(ILogicalOperator source : relativeSources) {
			
			Optional<Collection<LogicalSubscription>> optRelevantSubscriptions = 
					AbstractFragmentationQueryPartModificator.determineRelevantSubscriptions(source, sourceName);
			
			if(optRelevantSubscriptions.isPresent())
				relevantSources.put(source, optRelevantSubscriptions.get());
			
		}
		
		if(relevantSources.isEmpty())
			return Optional.absent();
		else return Optional.of(relevantSources);
		
	}

	/**
	 * Determines all subscriptions to sources, which are relevant for the fragmentation.
	 * @param operator The operator to analyze.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A collection of subscriptions to sources, which are relevant for the fragmentation.
	 * @throws NullPointerException if <code>operator</code> or <code>sourceName</code> is null.
	 */
	private static Optional<Collection<LogicalSubscription>> determineRelevantSubscriptions(
			ILogicalOperator operator, String sourceName) 
					throws NullPointerException {
		
		// Preconditions
		if(operator == null)
			throw new NullPointerException("Operator to analyze must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Collection<LogicalSubscription> relevantSubscriptions = Lists.newArrayList();
		
		if(operator instanceof AccessAO && ((AccessAO) operator).getAccessAOName().getResourceName().equals(sourceName))
			return Optional.of(relevantSubscriptions);
		
		for(LogicalSubscription sub : operator.getSubscribedToSource()) {
			
			ILogicalOperator target = sub.getTarget();
			if((target instanceof AccessAO && ((AccessAO) target).getAccessAOName().getResourceName().equals(sourceName)) ||
					AbstractFragmentationQueryPartModificator.determineRelevantSubscriptions(target, sourceName).isPresent())
				relevantSubscriptions.add(sub);
			
		}
		
		if(relevantSubscriptions.isEmpty())
			return Optional.absent();
		else return Optional.of(relevantSubscriptions);
		
	}
	
	/**
	 * Determines the name of the source to be fragmented given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION <fragmentation strategy name> <source name> <degree>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter <fragmentation strategy name>.
	 * @return The name of the source given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 * @throws IllegalArgumentException if <code>modificatorParameters</code> is empty.
	 */
	private static String determineSourceName(List<String> modificatorParameters) 
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part replicator must not be null!");
		else if(modificatorParameters.isEmpty())
			throw new IllegalArgumentException("Parameters for query part fragmentation stratgey does neither contain " +
					"the name of the source to be fragmented nor the degree of fragmentation!");
		
		return modificatorParameters.get(0);
		
	}
	
	/**
	 * Determines the degree of fragmentation given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION <fragmentation strategy name> <source name> <degree> 
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter <fragmentation strategy name>.
	 * @return The degree of fragmentation given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 */
	private static int determineDegreeOfFragmentation(List<String> modificatorParameters) throws NullPointerException {
		
		// Preconditions 1
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		
		// The return value
		int degreeOfFragmentation = -1;
		
		// Preconditions 2
		if(modificatorParameters.size() < 2) {
			
			AbstractFragmentationQueryPartModificator.log.warn("Parameters for query part fragmentation strategy does not contain the degree of fragmentation! " +
					"The degree of fragmentation is set to {}.", AbstractFragmentationQueryPartModificator.min_degree);
			degreeOfFragmentation = 2;
			
		} else try {
		
			degreeOfFragmentation = Integer.parseInt(modificatorParameters.get(1));
		
			// Preconditions 3
			if(degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree) {
				
				AbstractFragmentationQueryPartModificator.log.warn("Second parameter for query part fragmentation strategy, the degree of fragmentation, " +
						"should be at least {}. The degree of fragmentation is set to {}.", AbstractFragmentationQueryPartModificator.min_degree, 
						AbstractFragmentationQueryPartModificator.min_degree);
				degreeOfFragmentation = 2;			
				
			}
		
		} catch(NumberFormatException e) {
			
			AbstractFragmentationQueryPartModificator.log.error("Second parameter for query part fragmentation strategy must be an integer!", e);
			AbstractFragmentationQueryPartModificator.log.warn("The degree of fragmentation is set to {}.", AbstractFragmentationQueryPartModificator.min_degree);
			return AbstractFragmentationQueryPartModificator.min_degree;
			
		}		
		
		return degreeOfFragmentation;
		
	}
	
	/**
	 * Compares the degree of fragmentation given by the user with the number of available peers. 
	 * @param numQueryParts The number of query parts, which shall be fragmented.
	 * @param degreeOfFragmentation The degree of fragmentation given by the user.
	 * @throws NullPointerException if no {@link IP2PDictionary} could be found.
	 * @throws IllegalArgumentException if <code>numQueryParts</code> is less than one or 
	 * if <code>degreeOfFragmentation</code> is less {@value #min_degree}.
	 */
	private static void checkDegreeOfFragmentation(int numQueryParts, int degreeOfFragmentation) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions 1
		if(numQueryParts < 1)
			throw new IllegalArgumentException("At least one query part is needed for fragmentation!");
		else if(degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree)
			throw new IllegalArgumentException("Degree of fragmentation must be at least " + AbstractFragmentationQueryPartModificator.min_degree + "!");
		
		// The bound IP2PDictionary
		Optional<IP2PDictionary> optDict = Activator.getP2PDictionary();
		
		// Preconditions 2
		if(!optDict.isPresent())
			throw new NullPointerException("No P2PDictionary available!");
		
		// Check number of available peers (inclusive the local one)
		int numRemotePeers = optDict.get().getRemotePeerIDs().size();
		if(numRemotePeers + 1 < degreeOfFragmentation * numQueryParts) {
			
			AbstractFragmentationQueryPartModificator.log.warn("Fragmentation leads to at least {} query parts, " +
					"but there are only {} peers available. Consider to provide more peers. " +
					"For the given configuration some query parts will be executed on the same peer.", 
					degreeOfFragmentation * numQueryParts, numRemotePeers + 1);
			
		}
		
	}

}