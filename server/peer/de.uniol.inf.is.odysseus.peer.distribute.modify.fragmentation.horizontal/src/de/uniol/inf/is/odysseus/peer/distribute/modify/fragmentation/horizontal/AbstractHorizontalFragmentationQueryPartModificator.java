package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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


// TODO failure for operator cloud
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
	private static final Logger log = LoggerFactory.getLogger(AbstractHorizontalFragmentationQueryPartModificator.class);
	
	/**
	 * The class of the used operator for reunion.
	 */
	private static final Class<? extends ILogicalOperator> reunionClass = UnionAO.class;
	
	@Override
	protected Class<? extends ILogicalOperator> getReunionClass() {
		
		return AbstractHorizontalFragmentationQueryPartModificator.reunionClass;
		
	}
	
	@Override
	protected boolean canOptimizeSubscription(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription)
			throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		
		// The inverse return value
		boolean foundAggregation = false;
		
		for(ILogicalOperator operator : originPart.getOperators()) {
			
			if(operator instanceof AggregateAO && !foundAggregation)
				foundAggregation = true;
			else if(operator instanceof AggregateAO)
				throw new QueryPartModificationException("Can not fragment a query part containing more than one aggregation!");
			
		}
		
		return !foundAggregation;
		
	}
	
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertOperatorForReunion(
			ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin,
			ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink,
			Optional<LogicalSubscription> optSubscription,
			Collection<ILogicalOperator> targets)
			throws NullPointerException, QueryPartModificationException{
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if(copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if(originSink == null)
			throw new NullPointerException("The origin sink must be not null!");
		else if(copiesOfOriginSink == null)
			throw new NullPointerException("The copied sinks must be not null!");
		else if(optSubscription == null)
			throw new NullPointerException("The optional of the subscription to modify must be not null!");
		else if(targets == null)
			throw new NullPointerException("The targets must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);
		
		// Create operator for reunion
		ILogicalOperator operatorForReunion = null;
		try {
		
			operatorForReunion = this.getReunionClass().newInstance();
			
		} catch(Exception e) {
			
			String message = "Could not instantiate operator for reunion!";
			AbstractHorizontalFragmentationQueryPartModificator.log.error(message, e);
			throw new QueryPartModificationException(message, e);
			
		}
		
		int sinkInPort = 0;
		int sourceOutPort = 0;
		SDFSchema schema = originSink.getOutputSchema();
		if(optSubscription.isPresent()) {
			
			sourceOutPort = optSubscription.get().getSourceOutPort();
			sinkInPort = optSubscription.get().getSinkInPort();
			schema = optSubscription.get().getSchema();
			
		}
		
		// Subscribe the operator for reunion to the copied sinks
		for(int sinkNo = 0; sinkNo < copiesOfOriginSink.size(); sinkNo++) {
			
			ILogicalOperator copiedSink = ((List<ILogicalOperator>) copiesOfOriginSink).get(sinkNo);
			copiedSink.subscribeSink(operatorForReunion, sinkNo, sourceOutPort, schema);
			
		}
		
		Collection<ILogicalOperator> operatorsOfReunionPart = Lists.newArrayList(operatorForReunion);
		ILogicalOperator lastOperatorOfReunionPart = operatorForReunion;
		
		// Handling of aggregations
		Optional<AggregateAO> optAggregation = 
				AbstractHorizontalFragmentationQueryPartModificator.handleAggregation(originPart, modifiedCopiesToOrigin);
		if(optAggregation.isPresent()) {
			
			lastOperatorOfReunionPart = optAggregation.get();
			operatorsOfReunionPart.add(lastOperatorOfReunionPart);
			operatorForReunion.subscribeSink(lastOperatorOfReunionPart, sinkInPort, sourceOutPort, schema);
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an aggregation after {}", operatorForReunion);
			
		}
	
		// Subscribe the targets to the last operator of reunion part
		for(ILogicalOperator target : targets)
			lastOperatorOfReunionPart.subscribeSink(target, sinkInPort, 0, schema);		
		
		AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}", 
				copiesOfOriginSink, targets);
		
		// Create the query part for the operator for reunion
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
		
		Collection<ILogicalOperator> copiedAggregations = 
				LogicalQueryHelper.collectCopies(originPart, copiesToOrigin.get(originPart), originAggregation);
		
		for(ILogicalOperator copy : copiedAggregations) {
			
			AggregateAO copyOfAggregation = (AggregateAO) copy;
			
			for(SDFSchema inSchema : copyOfAggregation.getAggregations().keySet()) {
				
				for(AggregateFunction function : copyOfAggregation.getAggregations().get(inSchema).keySet()) {
					
					SDFAttribute oldOutAttr = copyOfAggregation.getAggregations().get(inSchema).get(function);
					SDFAttribute newOutAttr = null;
					
					if(function.getName().toUpperCase().equals("AVG") || function.getName().toUpperCase().equals("COUNT")) {
						
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), 
								SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE);
						
					} else if(function.getName().toUpperCase().equals("COUNT")) {
						
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), 
								SDFDatatype.COUNT_PARTIAL_AGGREGATE);
						
					} else if(oldOutAttr.getDatatype().isListValue()) {
						
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), 
								SDFDatatype.LIST_PARTIAL_AGGREGATE);
						
					} else {
						
						newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), 
								SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE);
					
					}
					
					copyOfAggregation.getAggregations().get(inSchema).put(function, newOutAttr);
					
				}
				
			}
			
		}
		
		return modifiedCopiesToOrigin;
		
	}
	
}