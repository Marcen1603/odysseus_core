package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal;

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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A abstract modifier of {@link ILogicalQueryPart}s, which fragments data streams horizontally from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part.
 * @author Michael Brand
 */
public abstract class AbstractHorizontalFragmentationQueryPartModificator
		extends AbstractFragmentationQueryPartModificator {
	
	// TODO partial aggregations
	
	// TODO WindowAOs?
	
	/**
	 * The logger for this class.
	 */
	private static Logger log = LoggerFactory.getLogger(AbstractHorizontalFragmentationQueryPartModificator.class);
	
	/**
	 * The class of the used operator for reunion.
	 */
	private static final Class<? extends ILogicalOperator> reunionClass = UnionAO.class;

	@Override
	public abstract String getName();
	
	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForReunion(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks, LogicalSubscription subscription, 
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, String sourceName) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new QueryPartModificationException("Origin sink for modification must be not null!");
		else if(copiedSinks == null)
			throw new QueryPartModificationException("List of copied sinks for modification must be not null!");
		else if(subscription == null)
			throw new QueryPartModificationException("Subscription for modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new QueryPartModificationException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		// TODO sourceName not null
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Preconditions 2
		if(originPart.getOperators().contains(subscription.getTarget()))
			return modifiedCopiesToOrigin;
		
		// The query part containing the origin target
		Optional<ILogicalQueryPart> optTargetPart = 
				LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), subscription.getTarget());
		
		try {
		
			// Insert merger
			if(!optTargetPart.isPresent()) {
				
				Optional<List<ILogicalOperator>> copiedTargets = Optional.fromNullable(null);
				Optional<List<ILogicalQueryPart>> queryPartOfCopiedTarget = Optional.fromNullable(null);
				modifiedCopiesToOrigin = AbstractHorizontalFragmentationQueryPartModificator.processTargetOfSubscriptionForReunion(
						originPart, modifiedCopiesToOrigin, originSink, copiedSinks, copiedTargets, optTargetPart, queryPartOfCopiedTarget, subscription, 
						relSinksToFragmentPart, sourceName);
				
			} else {
				
				// Find clones of target
				int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
				List<ILogicalOperator> copiedTargets = Lists.newArrayList();
				for(ILogicalQueryPart copy : copiesToOrigin.get(optTargetPart.get()))
					copiedTargets.add(((ImmutableList<ILogicalOperator>) copy.getOperators()).get(targetNo));
				
				modifiedCopiesToOrigin = AbstractHorizontalFragmentationQueryPartModificator.processTargetOfSubscriptionForReunion(
						originPart, modifiedCopiesToOrigin, originSink, copiedSinks, Optional.of(copiedTargets), optTargetPart, 
						Optional.of((List<ILogicalQueryPart>) copiesToOrigin.get(optTargetPart.get())),	subscription, relSinksToFragmentPart, 
						sourceName);
				
			}
			
		} catch(Exception e) {
			
			AbstractHorizontalFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}
		
		return modifiedCopiesToOrigin;
		
		
	}

	/**
	 * Processes an operator for reunion, which is target of a given subscription. If the target operator was copied, an operator for fragmentation 
	 * was inserted before. Therefore that operator for fragmentation will be removed (optimization). 
	 * Otherwise a single operator for reunion will be inserted for the origin target.
	 * @param originPart The origin query part which contains the origin sink.
	 * @param copiesToOrigin The mapping of copied query parts to the origin ones.
	 * @param originSink The origin relative sink of a query part.
	 * @param copiedSinks The copied relative (and real) sinks of <code>originPart</code>.
	 * @param copiedTargets The copied operators, whose origin one is subscribed to the origin operator of <code>copiedSinks</code>, 
	 * if it's a relative sink, or {@link Optional#absent()}, if the target was not copied.
	 * @param originPartOfTarget The query part containing the origin operator of <code>copiedTarget</code> or {@link Optional#absent()}, 
	 * if <code>copiedTarget</code> is {@link Optional#absent()}.
	 * @param queryPartsOfCopiedTarget The copied query parts containing each entry of <code>copiedTargets </code> or 
	 * {@link Optional#absent()}, if <code>copiedTargets</code> is {@link Optional#absent()}.
	 * @param subscription The subscription to be broken due to the insertion of the operator for reunion.
	 * @param relSinksToFragmentPart A mapping of relevant sinks to new query parts for fragmentation.
	 * @return The updated mapping of copied query parts to the origin ones. If <code>copiedTargets</code> is null, a single operator for reunion 
	 * will be the only operator of a new query part mapped to itself.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code>, <code>copiedSinks</code>, <code>subscription</code>
	 * or <code>relSinksToFragmentPart</code> is null.
	 * TODO sourceName
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key or 
	 * if <code>copiedTargets</code> is present and <code>originPartOfTarget</code> or <code>queryPartsOfCopiedTarget</code> is not present.
	 * @throws InstantiationException if an operator for reunion could not be instantiated.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processTargetOfSubscriptionForReunion(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks,
			Optional<List<ILogicalOperator>> copiedTargets,
			Optional<ILogicalQueryPart> originPartOfTarget,
			Optional<List<ILogicalQueryPart>> queryPartsOfCopiedTarget,
			LogicalSubscription subscription,
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart,
			String sourceName)
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
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
		else if(copiedTargets == null)
			throw new NullPointerException("Optional of the copied targets to process must be not null!");
		else if(originPartOfTarget == null)
			throw new NullPointerException("Optional of the query part containing the origin target to process must be not null!");
		else if(queryPartsOfCopiedTarget == null)
			throw new NullPointerException("Optional of the query parts containing the copied targets to process must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription for modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		else if(copiedTargets.isPresent() && (!originPartOfTarget.isPresent() || !queryPartsOfCopiedTarget.isPresent()))
			throw new IllegalArgumentException("Optional of the query part containing the origin target and optinal of the query parts containing " +
					"the copied targets to process must be present!");
		// TODO sourceName not null
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		if(!copiedTargets.isPresent()) {
			
			// Subscribe a merger for target
			modifiedCopiesToOrigin = AbstractHorizontalFragmentationQueryPartModificator.insertOperatorForReunion(
					originPart, modifiedCopiesToOrigin, copiedSinks, subscription);
			
		} else if(originSink instanceof AccessAO && ((AccessAO) originSink).getAccessAOName().getResourceName().equals(sourceName)) {
			
			ILogicalQueryPart fragmentationPart = null;
			for(ILogicalQueryPart part : relSinksToFragmentPart.keySet()) {
				
				if(relSinksToFragmentPart.get(part).contains(originSink)) {
					
					fragmentationPart = part;
					break;
					
				}
				
			}
			
			ILogicalOperator fragmentAO = fragmentationPart.getOperators().iterator().next();
			ILogicalOperator copiedSink = copiedSinks.iterator().next();
			SDFSchema sourceSchema = originSink.getOutputSchema();
			if(!fragmentAO.getSubscriptions().isEmpty())
				sourceSchema = fragmentAO.getSubscriptions().iterator().next().getSchema();
			fragmentAO.subscribeToSource(copiedSink, 0, 0, sourceSchema);
			
			Collection<ILogicalOperator> newOperators = Lists.newArrayList(copiedSink, fragmentAO);
			Collection<ILogicalQueryPart> newCopies = Lists.newArrayList((ILogicalQueryPart) new LogicalQueryPart(newOperators));
			modifiedCopiesToOrigin.put(originPart, newCopies);
			modifiedCopiesToOrigin.remove(fragmentationPart);
			
		} else {
		
			// Remove inserted operator for fragmentation
			
			modifiedCopiesToOrigin = AbstractHorizontalFragmentationQueryPartModificator.removeOperatorForFragmentation(
					originSink, copiedSinks, relSinksToFragmentPart, modifiedCopiesToOrigin);
			
			
			for(int targetNo = 0; targetNo < copiedTargets.get().size(); targetNo++) {
			
				// Connect copied sink directly to copied target
				
				ILogicalOperator copiedSink = ((List<ILogicalOperator>) copiedSinks).get(targetNo);
				ILogicalOperator copiedTarget = copiedTargets.get().get(targetNo);
				
				copiedSink.subscribeSink(copiedTarget, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
				AbstractHorizontalFragmentationQueryPartModificator.log.debug("Optimization: Connected {} and {}", copiedSink, copiedTarget);
				
			} 
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> removeOperatorForFragmentation(
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSources,
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin) throws NullPointerException {
		
		// TODO Preconditions
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		Collection<ILogicalQueryPart> fragmentPartsToRemove = Lists.newArrayList();
		
		for(ILogicalQueryPart part : relSinksToFragmentPart.keySet()) {
			
			if(relSinksToFragmentPart.get(part).contains(originSink)) {
				
				relSinksToFragmentPart.get(part).remove(originSink);
				
				if(relSinksToFragmentPart.get(part).isEmpty()) {
				
					// Query part does only consist of the single operator for fragmentation
					ILogicalOperator fragmentAO = part.getOperators().iterator().next();
					
					for(LogicalSubscription subToSink : fragmentAO.getSubscriptions())
						fragmentAO.unsubscribeSink(subToSink);
					for(ILogicalOperator copiedSource : copiedSources) {
						
						Collection<LogicalSubscription> subsToRemove = Lists.newArrayList();
						for(LogicalSubscription sub : copiedSource.getSubscriptions()) {
							
							if(sub.getTarget().equals(fragmentAO))
								subsToRemove.add(sub);
							
						}
						for(LogicalSubscription sub : subsToRemove)
							copiedSource.unsubscribeSink(sub);
						
					}
					fragmentAO.unsubscribeFromAllSources();
					Collection<ILogicalQueryPart> newCopies = Lists.newArrayList((ILogicalQueryPart) new LogicalQueryPart(fragmentAO));
					modifiedCopiesToOrigin.put(part, newCopies);
					
					AbstractHorizontalFragmentationQueryPartModificator.log.debug("Removed {}", fragmentAO);
					fragmentPartsToRemove.add(part);
					modifiedCopiesToOrigin.remove(part);
					
				}
				
			}
			
		}
		
		for(ILogicalQueryPart part : fragmentPartsToRemove)
			relSinksToFragmentPart.remove(part);
		
		return modifiedCopiesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertOperatorForReunion(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			Collection<ILogicalOperator> copiedSinks,
			LogicalSubscription subscription)
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null) 
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(copiedSinks == null)
			throw new NullPointerException("List of copied sinks for modification must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription to process must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// The operator for reunion to be inserted
		ILogicalOperator operatorForReunion = null;
		try {
			
			operatorForReunion = AbstractHorizontalFragmentationQueryPartModificator.reunionClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new InstantiationException("Operator for reunion could not be instantiated!");
			
		}
		
		// Subscribe operator for reunion to sinks
		for(int sinkNo = 0; sinkNo < copiedSinks.size(); sinkNo++) {
			
			((List<ILogicalOperator>) copiedSinks).get(sinkNo).subscribeSink(operatorForReunion, sinkNo, subscription.getSourceOutPort(), 
					subscription.getSchema());
			
		}
		
		// Subscribe target to operator for reunion
		operatorForReunion.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), 0, subscription.getSchema());
			
		// Create new query part
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		modifiedQueryParts.add(new LogicalQueryPart(operatorForReunion));
		modifiedCopiesToOrigin.put(new LogicalQueryPart(operatorForReunion), modifiedQueryParts);
		
		if(AbstractHorizontalFragmentationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSinks = "(";
			for(int sinkNo = 0; sinkNo < copiedSinks.size(); sinkNo++) {
				
				strSinks += ((List<ILogicalOperator>) copiedSinks).get(sinkNo).getName();
						
				if(sinkNo == copiedSinks.size() - 1)
					strSinks += ")";
				else strSinks += ", ";
				
			}
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}.", 
					strSinks, subscription.getTarget().getName());
			
		}
		return modifiedCopiesToOrigin;
		
	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSink(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, 
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiedSinks) 
					throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) 
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(copiedSinks == null)
			throw new QueryPartModificationException("List of copied sinks for modification must be not null!");
		else if(originSink == null)
			throw new QueryPartModificationException("Origin sink for modification must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// The operator for reunion to be inserted
		ILogicalOperator operatorForReunion = null;
		try {
			
			operatorForReunion = AbstractHorizontalFragmentationQueryPartModificator.reunionClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new QueryPartModificationException("Merger could not be instantiated!");
			
		}
		
		for(int sinkNo = 0; sinkNo < copiedSinks.size(); sinkNo++) {
		
			ILogicalOperator replicatedSink = ((List<ILogicalOperator>) copiedSinks).get(sinkNo);
			replicatedSink.subscribeSink(operatorForReunion, sinkNo, 0, replicatedSink.getOutputSchema());
			
		}
		
		// Create new query part
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		modifiedQueryParts.add(new LogicalQueryPart(operatorForReunion));
		modifiedCopiesToOrigin.put(new LogicalQueryPart(operatorForReunion), modifiedQueryParts);
		
		if(AbstractHorizontalFragmentationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSinks = "(";
			for(int sinkNo = 0; sinkNo < copiedSinks.size(); sinkNo++) {
				
				strSinks += ((List<ILogicalOperator>) copiedSinks).get(sinkNo).getName();
						
				if(sinkNo == copiedSinks.size() - 1)
					strSinks += ")";
				else strSinks += ", ";
				
			}
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion after {}.", strSinks);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}
	
	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForFragmentation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, 
			ILogicalOperator source, 
			Collection<ILogicalOperator> copiedSources, 
			LogicalSubscription subscription, 
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart) 
					throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(source == null)
			throw new QueryPartModificationException("Origin source for modification must be not null!");
		else if(copiedSources == null)
			throw new QueryPartModificationException("List of copied sources for modification must be not null!");
		else if(subscription == null)
			throw new QueryPartModificationException("Subscription for modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new QueryPartModificationException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Preconditions 2
		if(originPart.getOperators().contains(subscription.getTarget()))
			return modifiedCopiesToOrigin;
		
		// The query part containing the origin target
		Optional<ILogicalQueryPart> optTargetPart = 
				LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), subscription.getTarget());
		
		try {
		
			// Insert operator for fragmentation
			if(!optTargetPart.isPresent()) {
				
				Optional<List<ILogicalOperator>> copiedTargets = Optional.fromNullable(null);
				Optional<List<ILogicalQueryPart>> queryPartOfCopiedTarget = Optional.fromNullable(null);
				modifiedCopiesToOrigin = this.processTargetOfSubscriptionForFragmentation(
						originPart, modifiedCopiesToOrigin, source, copiedSources, copiedTargets, optTargetPart, queryPartOfCopiedTarget, subscription, 
						relSinksToFragmentPart);
				
			} else {
				
				// Find clones of target
				int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
				List<ILogicalOperator> copiedTargets = Lists.newArrayList();
				for(ILogicalQueryPart copy : modifiedCopiesToOrigin.get(optTargetPart.get()))
					copiedTargets.add(((ImmutableList<ILogicalOperator>) copy.getOperators()).get(targetNo));
				
				modifiedCopiesToOrigin = this.processTargetOfSubscriptionForFragmentation(
						originPart, modifiedCopiesToOrigin, source, copiedSources, Optional.of(copiedTargets), optTargetPart, 
						Optional.of((List<ILogicalQueryPart>) copiesToOrigin.get(optTargetPart.get())),	subscription, relSinksToFragmentPart);
				
			}
			
		} catch(Exception e) {
			
			AbstractHorizontalFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	// TODO javaDoc
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processTargetOfSubscriptionForFragmentation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator source,
			Collection<ILogicalOperator> copiedSources,
			Optional<List<ILogicalOperator>> copiedTargets,
			Optional<ILogicalQueryPart> originPartOfTarget,
			Optional<List<ILogicalQueryPart>> queryPartsOfCopiedTarget,
			LogicalSubscription subscription,
			Map<ILogicalQueryPart, Collection<ILogicalOperator>> relSinksToFragmentPart)
					throws NullPointerException, IllegalArgumentException, InstantiationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(source == null)
			throw new NullPointerException("Origin source for modification must be not null!");
		else if(copiedSources == null)
			throw new NullPointerException("List of copied sources for modification must be not null!");
		else if(copiedTargets == null)
			throw new NullPointerException("Optional of the copied targets to process must be not null!");
		else if(originPartOfTarget == null)
			throw new NullPointerException("Optional of the query part containing the origin target to process must be not null!");
		else if(queryPartsOfCopiedTarget == null)
			throw new NullPointerException("Optional of the query parts containing the copied targets to process must be not null!");
		else if(subscription == null)
			throw new NullPointerException("Subscription for modification must be not null!");
		else if(relSinksToFragmentPart == null)
			throw new NullPointerException("Mapping of relative sinks to new inserted part of fragmentation must be not null!");
		else if(copiedTargets.isPresent() && (!originPartOfTarget.isPresent() || !queryPartsOfCopiedTarget.isPresent()))
			throw new IllegalArgumentException("Optional of the query part containing the origin target and optinal of the query parts containing " +
					"the copied targets to process must be present!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
				
		// The operator for fragmentation to be inserted
		ILogicalOperator operatorForFragmentation = null;
		try {
			
			operatorForFragmentation = this.getFragmentationClass().newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new InstantiationException("Operator for fragmentation could not be instantiated!");
			
		}
		
		// Subscribe operator for fragmentation to sources
		for(int sourceNo = 0; sourceNo < copiedSources.size(); sourceNo++) {
			
			operatorForFragmentation.subscribeSink(((List<ILogicalOperator>) copiedSources).get(sourceNo), subscription.getSinkInPort(), sourceNo, 
					subscription.getSchema());
			
		}
		
		// Subscribe targets to operator for fragmentation
		List<ILogicalOperator> targets = Lists.newArrayList();
		if(!copiedTargets.isPresent())
			targets.add(subscription.getTarget());
		else targets = copiedTargets.get();
		for(ILogicalOperator target : targets)
			target.subscribeSink(operatorForFragmentation, 0, subscription.getSourceOutPort(), subscription.getSchema());
		
		// Create new query part
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		ILogicalQueryPart newPart = new LogicalQueryPart(operatorForFragmentation);
		modifiedQueryParts.add(newPart);
		modifiedCopiesToOrigin.put(newPart, modifiedQueryParts);
		
		if(relSinksToFragmentPart.containsKey(newPart))
			relSinksToFragmentPart.get(newPart).add(subscription.getTarget());
		else {
			
			Collection<ILogicalOperator> originTargets = Lists.newArrayList(subscription.getTarget());
			relSinksToFragmentPart.put(newPart, originTargets);
			
		}
		
		if(AbstractHorizontalFragmentationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSources = "(";
			for(int sourceNo = 0; sourceNo < copiedSources.size(); sourceNo++) {
				
				strSources += ((List<ILogicalOperator>) copiedSources).get(sourceNo).getName();
						
				if(sourceNo == copiedSources.size() - 1)
					strSources += ")";
				else strSources += ", ";
				
			}
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for fragmentation between {} and {}.", 
					strSources, targets);
			
		}
		
		return modifiedCopiesToOrigin;
		
	}
	
	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSourceForFragmentation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator relativeSource,
			Collection<ILogicalOperator> copiedSources)
				throws QueryPartModificationException {
		
		// TODO Preconditions
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Cut off real sources, if there were any other operators within the query part
		Collection<LogicalSubscription> subsToCut = Lists.newArrayList();
		SDFSchema sourceSchema = relativeSource.getOutputSchema();
		if(originPart.getOperators().size() > 1) {
			
			Collection<ILogicalQueryPart> newCopies = Lists.newArrayList();
		
			for(int copyNo = 0; copyNo < copiesToOrigin.get(originPart).size(); copyNo++) {
			
				ILogicalOperator sourceToRemove = ((List<ILogicalOperator>) copiedSources).get(copyNo);
				for(LogicalSubscription subscription : sourceToRemove.getSubscriptions()) {
					
					sourceSchema = subscription.getSchema();
					subsToCut.add(subscription);
					sourceToRemove.unsubscribeSink(subscription);
					
				}
				
				ILogicalQueryPart copy = ((List<ILogicalQueryPart>) copiesToOrigin.get(originPart)).get(copyNo);
				Collection<ILogicalOperator> operatorsWithoutSource = Lists.newArrayList(copy.getOperators());
				operatorsWithoutSource.remove(sourceToRemove);
				newCopies.add(new LogicalQueryPart(operatorsWithoutSource));
				
			}
			
			modifiedCopiesToOrigin.put(originPart, newCopies);
			
		}
		
		// Keep only one copy of the source
		ILogicalOperator sourceToKeep = copiedSources.iterator().next();
		
		// The operator for fragmentation to be inserted
		ILogicalOperator operatorForFragmentation = null;
		if(originPart.getOperators().size() > 1) {
		
			try {
				
				operatorForFragmentation = this.getFragmentationClass().newInstance();
				
			} catch(InstantiationException | IllegalAccessException e) {
				
				throw new QueryPartModificationException("Operator for fragmentation could not be instantiated!");
				
			}
			
			// Subscribe operator for fragmentation to source
			operatorForFragmentation.subscribeToSource(sourceToKeep, 0, 0, sourceSchema);
			
			// Subscribe targets to operator for fragmentation
			for(LogicalSubscription subscription : relativeSource.getSubscriptions()) {
				
				int indexOfOriginTarget = 
						((List<ILogicalOperator>) ((Collection<ILogicalOperator>) originPart.getOperators())).indexOf(subscription.getTarget());
				String strTargets = "(";
				
				for(int copyNo = 0; copyNo < modifiedCopiesToOrigin.get(originPart).size(); copyNo++) {
					
					ILogicalOperator copiedTarget = ((List<ILogicalOperator>) ((Collection<ILogicalOperator>) 
							(((List<ILogicalQueryPart>) ((Collection<ILogicalQueryPart>) 
									modifiedCopiesToOrigin.get(originPart))).get(copyNo).getOperators()))).get(indexOfOriginTarget);
					operatorForFragmentation.subscribeSink(copiedTarget, subscription.getSinkInPort(), copyNo, subscription.getSchema());
					
					strTargets += copiedTarget.getName();
					if(copyNo == modifiedCopiesToOrigin.get(originPart).size() - 1)
						strTargets += ")";
					else strTargets += ", ";
					
				}
				
				AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for fragmentation between {} and {}.", 
						sourceToKeep.getName(), strTargets);
				
			}
			
		}
		
		// Create new query part
		ILogicalQueryPart newPart = null;
		if(originPart.getOperators().size() == 1)
			newPart = originPart;
		else newPart = new LogicalQueryPart(relativeSource);
		Collection<ILogicalOperator> operatorsOfCopy = Lists.newArrayList(sourceToKeep);
		if(operatorForFragmentation != null)
			operatorsOfCopy.add(operatorForFragmentation);
		Collection<ILogicalQueryPart> copies = Lists.newArrayList((ILogicalQueryPart) new LogicalQueryPart(operatorsOfCopy));
		modifiedCopiesToOrigin.put(newPart, copies);
		
		return modifiedCopiesToOrigin;
		
	}

	// TODO javaDoc
	protected abstract Class<? extends ILogicalOperator> getFragmentationClass();

}