package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
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
public abstract class AbstractFragmentationQueryPartModificator implements
		IQueryPartModificator {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentationQueryPartModificator.class);
	
	/**
	 * The minimum degree of fragmentation.
	 */
	private static final int min_degree = 2;

	@Override
	public Collection<ILogicalQueryPart> modify(
			Collection<ILogicalQueryPart> queryParts,
			QueryBuildConfiguration config, List<String> modificatorParameters)
			throws QueryPartModificationException {
	
		// Preconditions
		if(queryParts == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Query parts to be modified must be not null!");
			AbstractFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(queryParts.isEmpty()) {
			
			AbstractFragmentationQueryPartModificator.log.warn("No query parts given to fragment!");
			return queryParts;
			
		}
		
		// The return value
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		
		try {
		
			// Determine degree of fragmentation and source to be fragmented
			final String sourceName = AbstractFragmentationQueryPartModificator.determineSourceName(modificatorParameters);
			final int degreeOfFragmentation = AbstractFragmentationQueryPartModificator.determineDegreeOfFragmentation(modificatorParameters);
			
			// Determine all relevant (e1) and irrelevant (e2) query parts
			// Note: Some of the origin query parts may be split.
			IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> relevantAndIrrelevantParts = 
					AbstractFragmentationQueryPartModificator.determineRelevantAndIrrelevantParts(queryParts, sourceName);
			Collection<ILogicalQueryPart> relevantParts = relevantAndIrrelevantParts.getE1();
			
			// Preconditions
			if(relevantParts.isEmpty()) {
				
				AbstractFragmentationQueryPartModificator.log.warn("No query parts given to fragment depending on the source to be fragmented!");
				return queryParts;
				
			}
			
			modifiedParts.addAll(relevantParts);
			Collection<ILogicalQueryPart> irrelevantParts = relevantAndIrrelevantParts.getE2();
			modifiedParts.addAll(irrelevantParts);
			
			// Copy the query parts
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin = 
					LogicalQueryHelper.copyAndCutQueryParts(modifiedParts, degreeOfFragmentation);
			
			// Keep only one copy of irrelevant parts
			for(ILogicalQueryPart originPart : irrelevantParts) {
					
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
			
			// Check, if the degree of fragmentation is suitable for the number of available peers
			AbstractFragmentationQueryPartModificator.validateDegreeOfFragmentation(relevantParts.size(), degreeOfFragmentation);
			AbstractFragmentationQueryPartModificator.log.debug("Degree of fragmentation set to {}.", degreeOfFragmentation);
			
			// Modify each relevant query part for insertion of operators for fragmentation
			for(ILogicalQueryPart originPart : relevantParts) {
				
				copiesToOrigin = this.modifyPartForFragmentation(originPart, copiesToOrigin, relevantParts, irrelevantParts, sourceName);
				
			}
			
			// Modify each relevant query part for insertion of operators for reunion
			for(ILogicalQueryPart originPart : relevantParts)
				copiesToOrigin = this.modifyPartForMerging(originPart, copiesToOrigin, relevantParts, irrelevantParts);
			
			// Connect irrelevant query parts to other irrelevant parts and parts not given within queryParts
			for(ILogicalQueryPart originPart : irrelevantParts) {
				
				copiesToOrigin = AbstractFragmentationQueryPartModificator.attachIrrelevantAndOtherPartsToIrrelevantParts(
						originPart, copiesToOrigin, relevantParts, irrelevantParts);
				
			}
			
			// Create the return value
			modifiedParts.clear();
			for(ILogicalQueryPart originPart : copiesToOrigin.keySet())
					modifiedParts.addAll(copiesToOrigin.get(originPart));
			
		} catch(Exception e) {
			
			AbstractFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}
		
		return modifiedParts;
	
	}
	
	/**
	 * Attaches an irrelevant part to the copies of a relative sink.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originSource The origin, relative source.
	 * @param copiesOfOriginSource The copies of <code>originSource</code>, if present.
	 * @param subscription The origin subscription of <code>originSource</code>.
	 * @param irrelevantParts A collection of all irrelevant query parts.
	 * @param toSource True, is <code>subscription</code> is a subscription to source; false, else.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException if <code>copiesToOrigin</code>, <code>originSource</code>, <code>copiesOfOriginSource</code>, 
	 * <code>subscription</code> or <code>irrevelantParts</code> is null.
	 * @throws IllegalArgumentException if <code>irrelevantParts</code> does not contain a part containing the target of<code>subscription</code>, 
	 * if there are more than one copy of the irrelevant part to attach or 
	 * if the copy of the irrelevant part does not contain enough operators.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachIrrelevantPart(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource,
			LogicalSubscription subscription, 
			Collection<ILogicalQueryPart> irrelevantParts, 
			boolean toSource)
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(originSource == null)
			throw new NullPointerException("The origin relative source must be not null!");
		else if(copiesOfOriginSource == null)
			throw new NullPointerException("Collection of copies of the origin source must be not null!");
		else if(subscription == null)
			throw new NullPointerException("The subscription for attachment must be not null!");
		else if(irrelevantParts == null)
			throw new NullPointerException("The collection of irrelevant query parts must be not null!");
		else if(!LogicalQueryHelper.determineQueryPart(irrelevantParts, subscription.getTarget()).isPresent())
			throw new IllegalArgumentException("The target of the subscription must be part of an irrelevant query part!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		ILogicalOperator target = subscription.getTarget();
		
		for(ILogicalQueryPart originPart : irrelevantParts) {
			
			if(originPart.getOperators().contains(target)) {
				
				Collection<ILogicalQueryPart> copies = modifiedCopiesToOrigin.get(originPart);
				if(copies == null || copies.size() != 1)
					throw new IllegalArgumentException("There must be exactly one copy of " + originPart.toString() + "!");
				
				ILogicalQueryPart copy = copies.iterator().next();
				
				int operatorIndex = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) originPart.getOperators()).indexOf(target);
				if(copy.getOperators().size() <= operatorIndex)
					throw new IllegalArgumentException("The copy of " + originPart.toString() + " does not have a correct amount of operators!");
				
				ILogicalOperator copiedTarget = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) copy.getOperators()).get(operatorIndex);
				
				for(ILogicalOperator copiedSource : copiesOfOriginSource) {
					
					if(toSource)
						copiedSource.subscribeToSource(copiedTarget, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
					else copiedSource.subscribeSink(copiedTarget, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
					AbstractFragmentationQueryPartModificator.log.debug("Connected {} to {}", copiedSource, copiedTarget);
					
					
				}
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a relevant query part by inserting an operator for fragmentation for every real source. <br />
	 * For a relative source connected to another relevant part each copy will be subscribed to the copies of 
	 * the subscription target due to optimization. <br />
	 * For a relative source connected to an irrelevant part each copy will be subscribed to the single copy of 
	 * the subscription target. <br />
	 * For a relative source connected to a part which is not given, an operator for fragmentation will be inserted.
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param relevantParts A collection of all relevant query parts.
	 * @param irrelevantParts A collection of all irrelevant query parts.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>relevantParts</code>, <code>sourceName</code> or 
	 * <code>irrevelantParts</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>relevantParts</code> does not contain <code>originPart</code>.
	 * @throws QueryPartModificationException if any real source of <code>originPart</code> is not a source to be fragmented or 
	 * if any error occurs in {@link #modifyRealSource(ILogicalQueryPart, Map, ILogicalOperator, Collection)}.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyPartForFragmentation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			Collection<ILogicalQueryPart> relevantParts,
			Collection<ILogicalQueryPart> irrelevantParts,
			String sourceName)
			throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relevantParts == null)
			throw new NullPointerException("Collection of relevant parts for fragmentation must be not null!");
		else if(!relevantParts.contains(originPart))
			throw new IllegalArgumentException("Collection of relevant parts for fragmentation must contain the origin query part for modification!");
		else if(irrelevantParts == null)
			throw new NullPointerException("Collection of irrelevant parts for fragmentation must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sources
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = 
				LogicalQueryHelper.collectRelativeSources(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Process each relative source, which is relevant for fragmentation
		for(ILogicalOperator originSource : copiedToOriginSources.keySet()) {
			
			if(originSource.getSubscribedToSource().isEmpty()) {
				
				// Preconditions
				if(!(originSource instanceof AccessAO) || !((AccessAO) originSource).getAccessAOName().getResourceName().equals(sourceName)) {
					
					throw new QueryPartModificationException("The real source " + originSource.toString() + " of part " + 
							originPart.toString() + " is not a source to be fragmented! " +
									"Therefore this real source must not be part of a relevant part!");
					
				}
				
				modifiedCopiesToOrigin = this.modifyRealSource(originPart, modifiedCopiesToOrigin, relevantParts, originSource, 
						copiedToOriginSources.get(originSource));
				
			} else {
				
				for(LogicalSubscription subToSource : originSource.getSubscribedToSource()) {
					
					ILogicalOperator target = subToSource.getTarget();
					
					// Preconditions
					if(originPart.getOperators().contains(target))
						continue;
					
					// Determine query part of the target, if present
					Optional<ILogicalQueryPart> optTargetPart = LogicalQueryHelper.determineQueryPart(relevantParts, subToSource.getTarget());
					
					if(optTargetPart.isPresent() && subToSource.getTarget() instanceof AccessAO && 
							((AccessAO) subToSource.getTarget()).getAccessAOName().getResourceName().equals(sourceName)) {
						
						continue;
						// Done by the modification of the source
						
					} else if(optTargetPart.isPresent()) {
						
						modifiedCopiesToOrigin = this.subscribeDirect(
								modifiedCopiesToOrigin, originSource, copiedToOriginSources.get(originSource), subToSource);
						
					} else if(LogicalQueryHelper.determineQueryPart(irrelevantParts, target).isPresent()) {
						
						modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.attachIrrelevantPart(
								modifiedCopiesToOrigin, originSource, copiedToOriginSources.get(originSource), subToSource, irrelevantParts, true);
						
					} else {
						
						modifiedCopiesToOrigin = this.modifySubscriptionForFragmentation(
								modifiedCopiesToOrigin, originSource, copiedToOriginSources.get(originSource), subToSource);
						
					}
					
				}
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}
	
	/**
	 * Subscribes the copies of a relative source directly to the copies of a target given by a subscription.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originSource The origin, relative source.
	 * @param copiesOfOriginSource The copies of <code>originSource</code>.
	 * @param subscription The origin subscription of <code>originSource</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> subscribeDirect(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource,
			LogicalSubscription subscription) 
			throws QueryPartModificationException;

	/**
	 * Modifies a subscription for insertion of an operator for fragmentation.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originSource The origin, relative source.
	 * @param copiesOfOriginSource The copies of <code>originSource</code>.
	 * @param subscription The origin subscription of <code>originSource</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForFragmentation(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource,
			LogicalSubscription subscription)
			throws QueryPartModificationException;

	/**
	 * Modifies a real source for insertion of an operator for fragmentation.
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param relevantParts A collection of all relevant query parts.
	 * @param originSource The origin, relative source.
	 * @param copiesOfOriginSource The copies of <code>originSource</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSource(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			Collection<ILogicalQueryPart> relevantParts,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource)
			throws QueryPartModificationException;

	/**
	 * Modifies a relevant query part by inserting an operator for reunion for every real sink. <br />
	 * For a relative sink connected to another relevant part each copy has been subscribed by the copies of 
	 * the subscription target before due to optimization. <br />
	 * For a relative sinks connected to a part which is not given, an operator for reunion will be inserted.
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param relevantParts A collection of all relevant query parts.
	 * @param irrelevantParts A collection of all irrelevant query parts.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>relevantParts</code> or 
	 * <code>irrevelantParts</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>relevantParts</code> does not contain <code>originPart</code>.
	 * @throws QueryPartModificationException if any real sink of <code>originPart</code> is connected to an irrelevant part or 
	 * if any error occurs in {@link #modifyRealSink(ILogicalQueryPart, Map, ILogicalOperator, Collection)}.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyPartForMerging(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, 
			Collection<ILogicalQueryPart> relevantParts,
			Collection<ILogicalQueryPart> irrelevantParts)
			throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relevantParts == null)
			throw new NullPointerException("Collection of relevant parts for fragmentation must be not null!");
		else if(!relevantParts.contains(originPart))
			throw new IllegalArgumentException("Collection of relevant parts for fragmentation must contain the origin query part for modification!");
		else if(irrelevantParts == null)
			throw new NullPointerException("Collection of irrelevant parts for fragmentation must be not null!");
	
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Process each relative sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			if(originSink.getSubscriptions().isEmpty()) {
				
				modifiedCopiesToOrigin = this.modifyRealSink(modifiedCopiesToOrigin, originSink, 
						copiedToOriginSinks.get(originSink));
				
			} else {
				
				for(LogicalSubscription subToSink : originSink.getSubscriptions()) {
					
					ILogicalOperator target = subToSink.getTarget();
					
					// Preconditions
					if(originPart.getOperators().contains(target))
						continue;
					else if(LogicalQueryHelper.determineQueryPart(relevantParts, target).isPresent()) {
						
						continue;
						// Done by modifyPartForFragmentation
						
					} else if(LogicalQueryHelper.determineQueryPart(irrelevantParts, target).isPresent()) {
						
						throw new QueryPartModificationException("The relevant, relative sink " + originSink + 
								" must not be connected to an irrelevant part!");
						
					} else {
						
						modifiedCopiesToOrigin = this.modifySubscriptionForReunion(
								modifiedCopiesToOrigin, originSink, copiedToOriginSinks.get(originSink), subToSink);
						
					}
					
				}
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Modifies a subscription for insertion of an operator for reunion.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originSink The origin, relative sink.
	 * @param copiesOfOriginSink The copies of <code>originSink</code>.
	 * @param subscription The origin subscription of <code>originSink</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForReunion(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription)
			throws QueryPartModificationException;
	
	/**
	 * Modifies a real sink for insertion of an operator for reunion.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originSink The origin, relative sink.
	 * @param copiesOfOriginSink The copies of <code>originSink</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	protected abstract Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSink(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink, 
			Collection<ILogicalOperator> copiesOfOriginSink)
			throws QueryPartModificationException;
	
	/**
	 * Attaches an irrelevant part to a part not given for modification.
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param relevantParts A collection of all relevant query parts.
	 * @param irrelevantParts A collection of all irrelevant query parts.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException if <code>copiesToOrigin</code>, <code>relevantParts</code> or <code>irrevelantParts</code> is null.
	 * @throws IllegalArgumentException if <code>irrelevantParts</code> does not contain a part containing <code>originPart</code>, 
	 * if there are more than one copy of <code>originPart</code> or 
	 * if the copy of <code>originPart</code> does not contain enough operators.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachIrrelevantAndOtherPartsToIrrelevantParts(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			Collection<ILogicalQueryPart> relevantParts,
			Collection<ILogicalQueryPart> irrelevantParts)
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relevantParts == null)
			throw new NullPointerException("Collection of relevant parts for fragmentation must be not null!");
		else if(irrelevantParts == null)
			throw new NullPointerException("Collection of irrelevant parts for fragmentation must be not null!");
		else if(!irrelevantParts.contains(originPart))
			throw new IllegalArgumentException("Collection of irrelevant parts for fragmentation must contain the origin query part for modification!");
	
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));
		
		// Process each relative sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			if(originSink.getSubscriptions().isEmpty())
				continue;
			else {
				
				for(LogicalSubscription subToSink : originSink.getSubscriptions()) {
					
					ILogicalOperator target = subToSink.getTarget();
					
					if(originPart.getOperators().contains(target))
						continue;
					else if(LogicalQueryHelper.determineQueryPart(relevantParts, target).isPresent()) {
						
						continue;
						// Done by modifyPartForFragmentation
						
					} else if(LogicalQueryHelper.determineQueryPart(irrelevantParts, target).isPresent()) {
						
						AbstractFragmentationQueryPartModificator.attachIrrelevantPart(
								modifiedCopiesToOrigin, originSink, copiedToOriginSinks.get(originSink), subToSink, irrelevantParts, false);
						
					} else {
						
						Collection<ILogicalQueryPart> copies = modifiedCopiesToOrigin.get(originPart);
						if(copies == null || copies.size() != 1)
							throw new IllegalArgumentException("There must be exactly one copy of " + originPart.toString() + "!");
						
						ILogicalQueryPart copy = copies.iterator().next();
						
						int operatorIndex = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) originPart.getOperators()).indexOf(originSink);
						if(copy.getOperators().size() <= operatorIndex)
							throw new IllegalArgumentException("The copy of " + originPart.toString() + " does not have a correct amount of operators!");
						
						ILogicalOperator copiedSink = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) copy.getOperators()).get(operatorIndex);
						copiedSink.subscribeSink(target, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
						target.unsubscribeFromSource(originSink, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
						
					}
					
				}
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	/**
	 * Compares the degree of fragmentation given by the user with the number of available peers. <br />
	 * If there are not enough peers available a warning will be logged. 
	 * @param numQueryParts The number of query parts, which shall be fragmented.
	 * @param degreeOfFragmentation The degree of fragmentation given by the user.
	 * @throws NullPointerException if no {@link IP2PDictionary} could be found.
	 * @throws IllegalArgumentException if <code>numQueryParts</code> is less than one or 
	 * if <code>degreeOfFragmentation</code> is less {@value #min_degree}.
	 */
	private static void validateDegreeOfFragmentation(
			int numQueryParts,
			int degreeOfFragmentation) 
			throws IllegalArgumentException, NullPointerException {
		
		// Preconditions 1
		if(numQueryParts < 1)
			throw new IllegalArgumentException("At least one query part is needed for fragmentation!");
		else if(degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree)
			throw new IllegalArgumentException("Degree of fragmentation must be at least " + 
					AbstractFragmentationQueryPartModificator.min_degree + "!");
		
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

	/**
	 * Determines all relevant and irrelevant parts for fragmentation. <br />
	 * Origin parts within <code>queryParts</code> may be split.
	 * @param queryParts A collection of all query parts to modify.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A pair of relevant (e1) and irrelevant (e2) parts.
	 * @throws NullPointerException if <code>queryParts</code> or <code>sourceName</code> is null.
	 */
	private static IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determineRelevantAndIrrelevantParts(
			Collection<ILogicalQueryPart> queryParts, 
			String sourceName) 
			throws NullPointerException {
		
		// Preconditions
		if(queryParts == null)
			throw new NullPointerException("Query parts for modification must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Collection<ILogicalQueryPart> relevantParts = Lists.newArrayList();
		Collection<ILogicalQueryPart> irrelevantParts = Lists.newArrayList();
		
		for(ILogicalQueryPart part : queryParts) {
			
			Collection<ILogicalOperator> relevantOperators = 
					AbstractFragmentationQueryPartModificator.determineRelevantOperators(part, sourceName);
			if(relevantOperators.size() == part.getOperators().size()) {
				
				relevantParts.add(part);
				AbstractFragmentationQueryPartModificator.log.debug("Found {} as a relevant part", part);
				
			} else if(!relevantOperators.isEmpty()) {
				
				ILogicalQueryPart relevantPart = new LogicalQueryPart(relevantOperators);
				relevantParts.add(relevantPart);
				
				Collection<ILogicalOperator> irrelevantOperators = Lists.newArrayList(part.getOperators());
				irrelevantOperators.removeAll(relevantOperators);
				ILogicalQueryPart irrelevantPart = new LogicalQueryPart(irrelevantOperators);
				irrelevantParts.add(irrelevantPart);
				
				AbstractFragmentationQueryPartModificator.log.debug("Split {} into {} as a relevant and {} as an irrelevant part", 
						new Object[] {part, relevantPart, irrelevantPart});
				
				
			} else {
				
				irrelevantParts.add(part);
				AbstractFragmentationQueryPartModificator.log.debug("Found {} as an irrelevant part", part);				
				
			}
			
			
		}
		
		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(relevantParts, irrelevantParts);
		
	}

	/**
	 * Determine all relevant operators for fragmentation of a given query part.
	 * @param queryPart The query part to modify.
	 * @param sourceName The name of the source to be fragmented.
	 * @return A collection of all operators relevant for fragmentation. 
	 * Those operators have a data stream as an input, which depend on the source to be fragmented.
	 * @throws NullPointerException if <code>queryPart</code> or <code>sourceName</code> is null.
	 */
	private static Collection<ILogicalOperator> determineRelevantOperators(
			ILogicalQueryPart queryPart, 
			String sourceName)
			throws NullPointerException {
		
		// Preconditions
		if(queryPart == null)
			throw new NullPointerException("Query part for modification must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		// The return value
		Collection<ILogicalOperator> relevantOperators = Lists.newArrayList();
		
		for(ILogicalOperator operator : queryPart.getOperators()) {
			
			if(AbstractFragmentationQueryPartModificator.isOperatorRelevant(operator, sourceName))
				relevantOperators.add(operator);
			
		}
		
		return relevantOperators;
		
	}

	/**
	 * Checks, if an operator is relevant for fragmentation.
	 * @param operator The operator to check.
	 * @param sourceName The name of the source to be fragmented.
	 * @return true, if the operator is an access operator for the given source or if 
	 * any incoming data stream depends on the given source.
	 * @throws NullPointerException if <code>operator</code> or <code>sourceName</code> is null.
	 */
	private static boolean isOperatorRelevant(
			ILogicalOperator operator,
			String sourceName) 
			throws NullPointerException{
		
		// Preconditions
		if(operator == null)
			throw new NullPointerException("Operator to process must be not null!");
		else if(sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		
		if(operator.getSubscribedToSource().isEmpty() && operator instanceof AccessAO && 
				((AccessAO) operator).getAccessAOName().getResourceName().equals(sourceName))
			return true;
		
		for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
			
			if(AbstractFragmentationQueryPartModificator.isOperatorRelevant(subToSource.getTarget(), sourceName))
				return true;
			
		}
		
		return false;
		
	}

	/**
	 * Determines the degree of fragmentation given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION <code>fragmentation-strategy-name</code> <code>source-name</code> <code>degree</code>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter <code>fragmentation-strategy-name<code/>.
	 * @return The degree of fragmentation given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 */
	private static int determineDegreeOfFragmentation(
			List<String> modificatorParameters) 
			throws NullPointerException {
		
		// Preconditions 1
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		
		// The return value
		int degreeOfFragmentation = -1;
		
		// Preconditions 2
		if(modificatorParameters.size() < 2) {
			
			AbstractFragmentationQueryPartModificator.log.warn(
					"Parameters for query part fragmentation strategy does not contain the degree of fragmentation! " +
					"The degree of fragmentation is set to {}.", AbstractFragmentationQueryPartModificator.min_degree);
			degreeOfFragmentation = 2;
			
		} else try {
		
			degreeOfFragmentation = Integer.parseInt(modificatorParameters.get(1));
		
			// Preconditions 3
			if(degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree) {
				
				AbstractFragmentationQueryPartModificator.log.warn(
						"Second parameter for query part fragmentation strategy, the degree of fragmentation, " +
						"should be at least {}. The degree of fragmentation is set to {}.", 
						AbstractFragmentationQueryPartModificator.min_degree, AbstractFragmentationQueryPartModificator.min_degree);
				degreeOfFragmentation = 2;			
				
			}
		
		} catch(NumberFormatException e) {
			
			AbstractFragmentationQueryPartModificator.log.error("Second parameter for query part fragmentation strategy must be an integer!", e);
			AbstractFragmentationQueryPartModificator.log.warn("The degree of fragmentation is set to {}.", 
					AbstractFragmentationQueryPartModificator.min_degree);
			return AbstractFragmentationQueryPartModificator.min_degree;
			
		}		
		
		return degreeOfFragmentation;
		
	}

	/**
	 * Determines the name of the source to be fragmented given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION <code>fragmentation-strategy-name</code> <code>source-name</code> <code>degree</code>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter <code>fragmentation-strategy-name</code>.
	 * @return The name of the source given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 * @throws IllegalArgumentException if <code>modificatorParameters</code> is empty.
	 */
	private static String determineSourceName(
			List<String> modificatorParameters) 
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part modificator must not be null!");
		else if(modificatorParameters.isEmpty())
			throw new IllegalArgumentException("Parameters for query part fragmentation stratgey does neither contain " +
					"the name of the source to be fragmented nor the degree of fragmentation!");
		
		return modificatorParameters.get(0);
		
	}
	
}