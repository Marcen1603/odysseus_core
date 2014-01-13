package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal;

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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
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
public abstract class AbstractHorizontalFragmentationQueryPartModificator extends
		AbstractFragmentationQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static Logger log = LoggerFactory.getLogger(AbstractHorizontalFragmentationQueryPartModificator.class);
	
	/**
	 * The class of the used operator for reunion.
	 */
	private static final Class<? extends ILogicalOperator> reunionClass = UnionAO.class;

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForFragmentation(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource,
			LogicalSubscription subscription,
			Collection<IPair<ILogicalOperator, ILogicalOperator>> historyOfOperatorsForFragmentation)
			throws QueryPartModificationException {
		
		// Preconditions
		if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(originSource == null)
			throw new QueryPartModificationException("The origin source must be not null!");
		else if(copiesOfOriginSource == null)
			throw new QueryPartModificationException("The copied sources must be not null!");
		else if(subscription == null)
			throw new QueryPartModificationException("The subscription to modify must be not null!");
		else if(historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Create operator for fragmentation
		ILogicalOperator operatorForFragmentation = null;
		try {
		
			operatorForFragmentation = this.getFragmentationClass().newInstance();
			
		} catch(Exception e) {
			
			AbstractHorizontalFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e.getMessage(), e);
			
		}
	
		// Subscribe the operator for fragmentation to the relative sources
		for(int copyNo = 0; copyNo < copiesOfOriginSource.size(); copyNo++) {
			
			ILogicalOperator copiedSource = ((List<ILogicalOperator>) copiesOfOriginSource).get(copyNo);
			operatorForFragmentation.subscribeSink(copiedSource, subscription.getSinkInPort(), copyNo, subscription.getSchema());
			
		}
		
		AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for fragmentation before ", copiesOfOriginSource);
		
		// Create the query part for the operator for fragmentation
		ILogicalQueryPart fragmentationPart = new LogicalQueryPart(operatorForFragmentation);
		Collection<ILogicalQueryPart> copiesOfFragmentationPart = Lists.newArrayList(fragmentationPart);
		modifiedCopiesToOrigin.put(fragmentationPart, copiesOfFragmentationPart);
		historyOfOperatorsForFragmentation.add(new Pair<ILogicalOperator, ILogicalOperator>(operatorForFragmentation, originSource));
		
		return modifiedCopiesToOrigin;
		
	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSource(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSource,
			Collection<ILogicalOperator> copiesOfOriginSource,
			Collection<IPair<ILogicalOperator, ILogicalOperator>> historyOfOperatorsForFragmentation)
			throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSource == null)
			throw new QueryPartModificationException("The origin source must be not null!");
		else if(copiesOfOriginSource == null)
			throw new QueryPartModificationException("The copied sources must be not null!");
		else if(historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		SDFSchema sourceSchema = originSource.getOutputSchema().clone();
		
		Collection<ILogicalQueryPart> newCopies = Lists.newArrayList();
		for(int copyNo = 0; copyNo < copiesToOrigin.get(originPart).size(); copyNo++) {
		
			ILogicalOperator sourceToRemove = ((List<ILogicalOperator>) copiesOfOriginSource).get(copyNo);
			
			for(LogicalSubscription subscription : sourceToRemove.getSubscriptions()) {
			
				sourceSchema = subscription.getSchema();
				sourceToRemove.unsubscribeSink(subscription);
				
			}
			
			ILogicalQueryPart copy = ((List<ILogicalQueryPart>) copiesToOrigin.get(originPart)).get(copyNo);
			Collection<ILogicalOperator> operatorsWithoutSource = Lists.newArrayList(copy.getOperators());
			operatorsWithoutSource.remove(sourceToRemove);
			if(originPart.getOperators().size() > 1)
				newCopies.add(new LogicalQueryPart(operatorsWithoutSource));
			
		}
		if(originPart.getOperators().size() > 1)
			modifiedCopiesToOrigin.put(originPart, newCopies);
		
		// Keep only one copy of the source
		ILogicalOperator sourceToKeep = copiesOfOriginSource.iterator().next();
		
		// The operator for fragmentation to be inserted
		ILogicalOperator operatorForFragmentation = null;
		try {
			
			operatorForFragmentation = this.getFragmentationClass().newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			throw new QueryPartModificationException("Operator for fragmentation could not be instantiated!");
			
		}
		
		// Subscribe operator for fragmentation to source
		operatorForFragmentation.subscribeToSource(sourceToKeep, 0, 0, sourceSchema);
		
		// Subscribe targets to operator for fragmentation
		for(LogicalSubscription subscription : originSource.getSubscriptions()) {
			
			Optional<ILogicalQueryPart> optPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(copiesToOrigin.keySet(), subscription.getTarget());
			if(!optPartOfOriginTarget.isPresent())
				throw new QueryPartModificationException("Could not determine query part of " + subscription.getTarget() + "!");
			ILogicalQueryPart partOfOriginTarget = optPartOfOriginTarget.get();
			
			int indexOfOriginTarget = 
					((List<ILogicalOperator>) ((Collection<ILogicalOperator>) partOfOriginTarget.getOperators())).indexOf(subscription.getTarget());
			
			for(int copyNo = 0; copyNo < modifiedCopiesToOrigin.get(partOfOriginTarget).size(); copyNo++) {
				
				ILogicalOperator copiedTarget = ((List<ILogicalOperator>) ((Collection<ILogicalOperator>) 
						(((List<ILogicalQueryPart>) ((Collection<ILogicalQueryPart>) 
								modifiedCopiesToOrigin.get(partOfOriginTarget))).get(copyNo).getOperators()))).get(indexOfOriginTarget);
				operatorForFragmentation.subscribeSink(copiedTarget, subscription.getSinkInPort(), copyNo, sourceSchema);
				
			}
			
		}
		
		if(AbstractHorizontalFragmentationQueryPartModificator.log.isDebugEnabled()) {
			
			Collection<ILogicalOperator> copiedTargets = Lists.newArrayList();
			for(LogicalSubscription sub : operatorForFragmentation.getSubscriptions())
				copiedTargets.add(sub.getTarget());
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for fragmentation between {} and {}", 
				sourceToKeep, copiedTargets);
			
		}
		
		// Create the query part for the source and the operator for fragmentation
		Collection<ILogicalOperator> copiesOfOperators = Lists.newArrayList(sourceToKeep, operatorForFragmentation);
		ILogicalQueryPart fragmentationPart = new LogicalQueryPart(copiesOfOperators);
		Collection<ILogicalQueryPart> copiesOfFragmentationPart = Lists.newArrayList(fragmentationPart);
		ILogicalQueryPart newOriginPart = null;
		if(originPart.getOperators().size() == 1)
			newOriginPart = originPart;
		else newOriginPart = fragmentationPart;
		modifiedCopiesToOrigin.put(newOriginPart, copiesOfFragmentationPart);
		historyOfOperatorsForFragmentation.add(new Pair<ILogicalOperator, ILogicalOperator>(operatorForFragmentation, originSource));
		
		return modifiedCopiesToOrigin;
		
	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySubscriptionForReunion(
			ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			Collection<ILogicalQueryPart> relevantParts,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription,
			Collection<IPair<ILogicalOperator, ILogicalOperator>> historyOfOperatorsForFragmentation)
			throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(relevantParts == null)
			throw new QueryPartModificationException("Collection of relevant parts for fragmentation must be not null!");
		else if(originSink == null)
			throw new QueryPartModificationException("The origin sink must be not null!");
		else if(copiesOfOriginSink == null)
			throw new QueryPartModificationException("The copied sinks must be not null!");
		else if(subscription == null)
			throw new QueryPartModificationException("The subscription to modify must be not null!");
		else if(historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Create operator for reunion
		ILogicalOperator operatorForReunion = null;
		try {
		
			operatorForReunion = AbstractHorizontalFragmentationQueryPartModificator.reunionClass.newInstance();
			
		} catch(Exception e) {
			
			AbstractHorizontalFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e.getMessage(), e);
			
		}
		
		// Subscribe the relative sources
		for(int copyNo = 0; copyNo < copiesOfOriginSink.size(); copyNo++) {
			
			ILogicalOperator target = ((List<ILogicalOperator>) copiesOfOriginSink).get(copyNo);
			operatorForReunion.subscribeToSource(target, copyNo, subscription.getSourceOutPort(), subscription.getSchema());
			
		}
		
		// Determine the target operator
		ILogicalOperator originTarget = subscription.getTarget();
		Optional<ILogicalQueryPart> optPartOfTarget = LogicalQueryHelper.determineQueryPart(relevantParts, originTarget);
		ILogicalOperator target = null;
		if(optPartOfTarget.isPresent()) {
			
			for(IPair<ILogicalOperator, ILogicalOperator> entry : historyOfOperatorsForFragmentation) {
				
				if(entry.getE2().equals(originTarget)) {
					
					ILogicalQueryPart partofOperatorForFragmentation = 
							LogicalQueryHelper.determineQueryPart(copiesToOrigin.keySet(), entry.getE1()).get();
					int operatorNo = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) 
							partofOperatorForFragmentation.getOperators()).indexOf(entry.getE1());
					ILogicalQueryPart copy = modifiedCopiesToOrigin.get(partofOperatorForFragmentation).iterator().next(); // There is only one copy
					
					target = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) copy.getOperators()).get(operatorNo);
					break;
					
				}
				
			}
			
		} else target = originTarget;
		
		Collection<ILogicalOperator> operatorsOfReunionPart = Lists.newArrayList(operatorForReunion);
		ILogicalOperator lastOperatorOfPart = operatorForReunion;
		
		// Handling of aggregations
		Optional<AggregateAO> optAggregation = 
				AbstractHorizontalFragmentationQueryPartModificator.handleAggregation(originPart, modifiedCopiesToOrigin);
		if(optAggregation.isPresent()) {
			
			// Subscribe the aggregation to the operator for reunion
			AggregateAO aggregation = optAggregation.get().clone();
			operatorForReunion.subscribeSink(aggregation, 0, 0, operatorForReunion.getOutputSchema());
			operatorsOfReunionPart.add(aggregation);
			lastOperatorOfPart = aggregation;
			
		}
		
		// Subscribe target
		lastOperatorOfPart.subscribeSink(target, subscription.getSinkInPort(), 0, subscription.getSchema());
				
		if(AbstractHorizontalFragmentationQueryPartModificator.log.isDebugEnabled()) {
			
			Collection<ILogicalOperator> copiedTargets = Lists.newArrayList();
			for(LogicalSubscription sub : operatorForReunion.getSubscribedToSource())
				copiedTargets.add(sub.getTarget());
			if(optAggregation.isPresent())
				AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}", 
					copiesOfOriginSink, optAggregation.get());
			else AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}", 
					copiesOfOriginSink, copiedTargets);
			
		}
		
		// Create the query part for the operator for reunion
		ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsOfReunionPart);
		Collection<ILogicalQueryPart> copiesOfReunionPart = Lists.newArrayList(reunionPart);
		modifiedCopiesToOrigin.put(reunionPart, copiesOfReunionPart);
		
		return modifiedCopiesToOrigin;
		
	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyRealSink(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink)
			throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new QueryPartModificationException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new QueryPartModificationException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originSink == null)
			throw new QueryPartModificationException("Origin sink for modification must be not null!");
		else if(copiesOfOriginSink == null)
			throw new QueryPartModificationException("List of copied sinks for modification must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// The operator for reunion to be inserted
		ILogicalOperator operatorForReunion = null;
		try {
			
			operatorForReunion = AbstractHorizontalFragmentationQueryPartModificator.reunionClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			AbstractHorizontalFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw new QueryPartModificationException(e.getMessage(), e);
			
		}
		
		for(int sinkNo = 0; sinkNo < copiesOfOriginSink.size(); sinkNo++) {
		
			ILogicalOperator replicatedSink = ((List<ILogicalOperator>) copiesOfOriginSink).get(sinkNo);
			replicatedSink.subscribeSink(operatorForReunion, sinkNo, 0, replicatedSink.getOutputSchema());
			
		}
		
		AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion after {}.", copiesOfOriginSink);
		Collection<ILogicalOperator> operatorsOfReunionPart = Lists.newArrayList(operatorForReunion);
		
		// Handling of aggregations
		Optional<AggregateAO> optAggregation = 
				AbstractHorizontalFragmentationQueryPartModificator.handleAggregation(originPart, modifiedCopiesToOrigin);
		if(optAggregation.isPresent()) {
			
			// Subscribe the aggregation to the operator for reunion
			AggregateAO aggregation = optAggregation.get().clone();
			operatorForReunion.subscribeSink(aggregation, 0, 0, operatorForReunion.getOutputSchema());
			operatorsOfReunionPart.add(aggregation);
			
		}
		
		// Create the query part for the operator for fragmentation
		ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsOfReunionPart);
		Collection<ILogicalQueryPart> copiesOfReunionPart = Lists.newArrayList(reunionPart);
		modifiedCopiesToOrigin.put(reunionPart, copiesOfReunionPart);
		
		return modifiedCopiesToOrigin;
		
	}
	
	/**
	 * Searches a single aggregation within a given query part and changes it's copies to send partial aggregates. 
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @return The origin aggregation, if there is exactly one within the query part.
	 * @throws NullPointerException if <code>originPart</code> or <code>copiesToOrigin</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 * @throws QueryPartModificationException if there is more than one aggregation.
	 */
	private static Optional<AggregateAO> handleAggregation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin)
			throws NullPointerException, IllegalArgumentException, QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		
		// The return value
		Optional<AggregateAO> optAggregation = Optional.absent();
		
		for(ILogicalOperator operator : originPart.getOperators()) {
			
			if(operator instanceof AggregateAO && !optAggregation.isPresent()) {
				
				AbstractHorizontalFragmentationQueryPartModificator.log.debug("Found {} as an aggregation in {}", operator, originPart);
			
				copiesToOrigin = AbstractHorizontalFragmentationQueryPartModificator.changeAggregation(
						originPart, copiesToOrigin, (AggregateAO) operator);
				optAggregation = Optional.of((AggregateAO) operator);
			
			} else if(operator instanceof AggregateAO)
				throw new QueryPartModificationException("Can not fragment a query part containing more than one aggregation!");
			
		}
		
		return optAggregation;
		
	}

	/**
	 * Changes the copies of a given origin aggregation to send partial aggregates. 
	 * @param originPart The query part to modify.
	 * @param copiesToOrigin A mapping of copies to origin query parts.
	 * @param originAggregation The origin aggregation.
	 * @return The changed mapping of copies to origin query parts.
	 * @throws NullPointerException if <code>originPart</code>, <code>copiesToOrigin</code> or <code>originAggregation</code> is null.
	 * @throws IllegalArgumentException if <code>copiesToOrigin</code> does not contain <code>originPart</code> as a key.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> changeAggregation(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			AggregateAO originAggregation)
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if(originAggregation == null)
			throw new NullPointerException("Origin aggregation must be not null");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		int operatorIndex = ((List<ILogicalOperator>) (Collection<ILogicalOperator>) originPart.getOperators()).indexOf(originAggregation);
		
		for(ILogicalQueryPart copy : copiesToOrigin.get(originPart)) {
			
			AggregateAO copyOfAggregation = (AggregateAO) ((List<ILogicalOperator>) (Collection<ILogicalOperator>) 
					copy.getOperators()).get(operatorIndex);
			
			for(SDFSchema inSchema : copyOfAggregation.getAggregations().keySet()) {
				
				for(AggregateFunction function : copyOfAggregation.getAggregations().get(inSchema).keySet()) {
					
					SDFAttribute oldOutAttr = copyOfAggregation.getAggregations().get(inSchema).get(function);
					SDFAttribute newOutAttr = null;
					
					if(function.getName().toUpperCase().equals("AVG") || function.getName().toUpperCase().equals("COUNT"))
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE);
					else if(function.getName().toUpperCase().equals("COUNT"))
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.COUNT_PARTIAL_AGGREGATE);
					else if(oldOutAttr.getDatatype().isListValue())
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.LIST_PARTIAL_AGGREGATE);
					else newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE);
					
					copyOfAggregation.getAggregations().get(inSchema).put(function, newOutAttr);
					
				}
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}

	@Override
	protected boolean canOptimizeSubscription(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription)
			throws QueryPartModificationException {
		
		try {
		
			return !AbstractHorizontalFragmentationQueryPartModificator.handleAggregation(originPart, copiesToOrigin).isPresent();
			
		} catch(Exception e) {
			
			throw new QueryPartModificationException(e);
			
		}
		
	}
	
	/**
	 * Returns the class of the operator for fragmentation to be used.
	 */
	protected abstract Class<? extends ILogicalOperator> getFragmentationClass();

}