package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A abstract modifier of {@link ILogicalQueryPart}s, which fragments data
 * streams horizontally from a given source into parallel query parts and
 * inserts operators to merge the result sets of the parallel fragments for each
 * relative sink within every single query part.
 * 
 * @author Michael Brand
 */
public abstract class AbstractHorizontalFragmentationQueryPartModificator extends AbstractFragmentationQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractHorizontalFragmentationQueryPartModificator.class);

	@Override
	protected ILogicalOperator createOperatorForReunion() {

		return new UnionAO();

	}

	@Override
	protected boolean canOptimizeSubscription(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink, Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription, List<String> modificationParameters) throws QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new QueryPartModificationException("Origin query part for modification must be not null!");
		else if (modificationParameters == null)
			throw new QueryPartModificationException("Modification parameters must be not null!");

		// The inverse return value
		boolean foundAggregation = false;

		for (ILogicalOperator operator : originPart.getOperators()) {

			if (operator instanceof AggregateAO) {

				AggregateAO aggregation = (AggregateAO) operator;
				boolean needsPartialAggregates = this.needPartialAggregates(aggregation, modificationParameters);

				if (!needsPartialAggregates)
					continue;
				else if (!foundAggregation)
					foundAggregation = true;
				else
					throw new QueryPartModificationException("Can not fragment a query part containing more than one aggregation!");

			}

		}

		return !foundAggregation;

	}

	/**
	 * Determines, if a given aggregation needs to be changed due to a creation
	 * of partial aggregates.
	 * 
	 * @param aggregation
	 *            The aggregation to be checked.
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return True, if the list of key attributes given by the user (
	 *         {@link #determineKeyAttributes(List)}) contains any
	 *         group-by-attributes of the aggregation; false, else
	 */
	private boolean needPartialAggregates(AggregateAO aggregation, List<String> modificationParameters) {

		Preconditions.checkNotNull(modificationParameters, "Modification parameters must be not null!");
		Preconditions.checkNotNull(aggregation, "Aggregation must be not null!");

		Optional<List<String>> attributes = this.determineKeyAttributes(modificationParameters);

		if (!attributes.isPresent())
			return true;

		for (SDFAttribute attribute : aggregation.getGroupingAttributes()) {

			if (attributes.get().contains(attribute.getSourceName() + "." + attribute.getAttributeName()))
				return false;

		}

		return true;
	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertOperatorForReunion(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink, Optional<LogicalSubscription> optSubscription, Collection<ILogicalOperator> targets,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, 
			Collection<ILogicalOperator> historyOfOperatorsForReunion, Optional<ILogicalQueryPart> originPartOfTarget, 
			List<ILogicalQueryPart> queryPartsOfCopiedTargets, List<String> modificationParameters) throws NullPointerException, QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (originSink == null)
			throw new NullPointerException("The origin sink must be not null!");
		else if (copiesOfOriginSink == null)
			throw new NullPointerException("The copied sinks must be not null!");
		else if (optSubscription == null)
			throw new NullPointerException("The optional of the subscription to modify must be not null!");
		else if (targets == null)
			throw new NullPointerException("The targets must be not null!");
		else if (modificationParameters == null)
			throw new NullPointerException("Modification parameters must be not null!");
		else if (historyOfOperatorsForReunion == null)
			throw new NullPointerException("The history of inserted operator for reunion must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Create operator for reunion
		ILogicalOperator operatorForReunion = this.createOperatorForReunion();

		int sinkInPort = 0;
		int sourceOutPort = 0;
		SDFSchema schema = originSink.getOutputSchema();
		if (optSubscription.isPresent()) {

			sourceOutPort = optSubscription.get().getSourceOutPort();
			sinkInPort = optSubscription.get().getSinkInPort();
			schema = optSubscription.get().getSchema();

		}

		// Subscribe the operator for reunion to the copied sinks
		for (int sinkNo = 0; sinkNo < copiesOfOriginSink.size(); sinkNo++) {

			ILogicalOperator copiedSink = ((List<ILogicalOperator>) copiesOfOriginSink).get(sinkNo);
			copiedSink.subscribeSink(operatorForReunion, sinkNo, sourceOutPort, schema);

		}

		Collection<ILogicalOperator> operatorsOfReunionPart = Lists.newArrayList(operatorForReunion);
		ILogicalOperator lastOperatorOfReunionPart = operatorForReunion;
		historyOfOperatorsForReunion.add(operatorForReunion);

		// Handling of aggregations
		Optional<AggregateAO> optAggregation = this.handleAggregation(originPart, modifiedCopiesToOrigin, modificationParameters);
		if (optAggregation.isPresent()) {

			lastOperatorOfReunionPart = optAggregation.get();
			operatorsOfReunionPart.add(lastOperatorOfReunionPart);
			operatorForReunion.subscribeSink(lastOperatorOfReunionPart, sinkInPort, sourceOutPort, schema);
			AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an aggregation after {}", operatorForReunion);

		}

		// Subscribe the targets to the last operator of reunion part
		for (ILogicalOperator target : targets)
			lastOperatorOfReunionPart.subscribeSink(target, sinkInPort, 0, schema);

		AbstractHorizontalFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}", copiesOfOriginSink, targets);
		
		if(targets.size() == 1 && originPartOfTarget.isPresent() && !queryPartsOfCopiedTargets.isEmpty()) {
			
			final ILogicalQueryPart partOfCopiedTarget = queryPartsOfCopiedTargets.get(0);
		
			// Create modified query part
			Collection<ILogicalOperator> operatorsWithReunion = Lists.newArrayList(partOfCopiedTarget.getOperators());
			operatorsWithReunion.add(operatorForReunion);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			for(ILogicalQueryPart part : modifiedCopiesToOrigin.get(originPartOfTarget.get())) {
				
				if(!part.equals(partOfCopiedTarget))
					modifiedQueryParts.add(part);
				
			}
				
			ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsWithReunion);
			modifiedQueryParts.add(reunionPart);
			modifiedCopiesToOrigin.put(originPartOfTarget.get(), modifiedQueryParts);
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(originPartOfTarget.get(), modifiedCopiesToOrigin, operatorForReunion, historyOfOperatorsForFragmentation);
			
		} else {

			// Create the query part for the operator for reunion
			ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsOfReunionPart);
			Collection<ILogicalQueryPart> copiesOfReunionPart = Lists.newArrayList(reunionPart);
			modifiedCopiesToOrigin.put(reunionPart, copiesOfReunionPart);
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(reunionPart, modifiedCopiesToOrigin, operatorForReunion, historyOfOperatorsForFragmentation);
			
		}
		
		return modifiedCopiesToOrigin;

	}

	/**
	 * Searches a single aggregation within a given query part and changes it's
	 * copies to send partial aggregates.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @return The origin aggregation, if there is exactly one within the query
	 *         part.
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code> or
	 *             <code>modificationParameters</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>copiesToOrigin</code> does not contain
	 *             <code>originPart</code> as a key.
	 * @throws QueryPartModificationException
	 *             if there is more than one aggregation.
	 */
	private Optional<AggregateAO> handleAggregation(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, List<String> modificationParameters) throws NullPointerException, IllegalArgumentException,
			QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if (modificationParameters == null)
			throw new NullPointerException("Modification parameters must be not null!");

		// The return value
		Optional<AggregateAO> optAggregation = Optional.absent();

		for (ILogicalOperator operator : originPart.getOperators()) {

			if (operator instanceof AggregateAO) {

				AggregateAO aggregation = (AggregateAO) operator;
				boolean needsPartialAggregates = this.needPartialAggregates(aggregation, modificationParameters);

				if (!needsPartialAggregates)
					continue;
				else if (!optAggregation.isPresent()) {

					AbstractHorizontalFragmentationQueryPartModificator.log.debug("Found {} as an aggregation, which needs to be changed in {}", operator, originPart);

					optAggregation = Optional.of(AbstractHorizontalFragmentationQueryPartModificator.changeAggregation(originPart, copiesToOrigin, (AggregateAO) operator));

				} else
					throw new QueryPartModificationException("Can not fragment a query part containing more than one aggregation!");

			}

		}

		return optAggregation;

	}

	/**
	 * Changes the copies of a given origin aggregation to send partial
	 * aggregates.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts. Will be muted.
	 * @param originAggregation
	 *            The origin aggregation.
	 * @return The aggregation for merging the partial aggregates.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code> or
	 *             <code>originAggregation</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>copiesToOrigin</code> does not contain
	 *             <code>originPart</code> as a key.
	 */
	private static AggregateAO changeAggregation(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, AggregateAO originAggregation) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if (originAggregation == null)
			throw new NullPointerException("Origin aggregation must be not null");

		// The return value
		AggregateAO reunionAggregate = new AggregateAO();
		reunionAggregate.setGroupingAttributes(originAggregation.getGroupingAttributes());
		reunionAggregate.setDumpAtValueCount(originAggregation.getDumpAtValueCount());
		reunionAggregate.setDrainAtDone(originAggregation.isDrainAtDone());
		reunionAggregate.setDrainAtClose(originAggregation.isDrainAtClose());
		
		List<AggregateItem> aggItems = Lists.newArrayList();

		Collection<ILogicalOperator> copiedAggregations = LogicalQueryHelper.collectCopies(originPart, copiesToOrigin.get(originPart), originAggregation);

		for (int copyNo = 0; copyNo < copiedAggregations.size(); copyNo++) {

			((AggregateAO) ((List<ILogicalOperator>) copiedAggregations).get(copyNo)).setOutputPA(true);

			if (copyNo == 0) {

				for (SDFSchema inSchema : originAggregation.getAggregations().keySet()) {

					for (AggregateFunction function : originAggregation.getAggregations().get(inSchema).keySet()) {

						SDFAttribute attr = originAggregation.getAggregations().get(inSchema).get(function);
						aggItems.add(new AggregateItem(function.getName(), attr, attr));

					}

				}

			}

		}

		reunionAggregate.setAggregationItems(aggItems);

		// Creating PQL string of aggregation items
		StringBuffer pql = new StringBuffer();
		for (AggregateItem a : aggItems) {

			List<String> value = Lists.newArrayList(a.aggregateFunction.getName());
			if(a.inAttributes.size() == 1)
				value.add(a.inAttributes.get(0).getURI());
			else {	// multiple attributes
				
				String attributes = "[";
				for(SDFAttribute inAttribute : a.inAttributes)
					attributes += "'" + inAttribute.getURI() + "',";
				attributes = attributes.substring(0, attributes.length() - 1);
				attributes += "]";
				
				value.add(attributes);
				
			}
			
			pql.append(AggregateItemParameter.getPQLString(value));
			pql.append(",");

		}
		String pqlString = "[" + pql.substring(0, pql.length() - 1) + "]";

		reunionAggregate.addParameterInfo(AggregateAO.AGGREGATIONS, pqlString);

		return reunionAggregate;

	}

	@Override
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachOtherParts(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, Collection<ILogicalQueryPart> partsToBeFragmented,
			String sourceName, Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation) throws NullPointerException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for attachment must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (partsToBeFragmented == null)
			throw new NullPointerException("Collection of parts to be fragmented must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Collect all relative sources
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = LogicalQueryHelper.collectRelativeSources(originPart, modifiedCopiesToOrigin.get(originPart));

		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));

		// Process each relative source
		for (ILogicalOperator originSource : copiedToOriginSources.keySet()) {

			if (originSource.getSubscribedToSource().isEmpty()) {
				continue;
			}

			for (LogicalSubscription subToSource : originSource.getSubscribedToSource()) {

				if (originPart.getOperators().contains(subToSource.getTarget()))
					continue;

				Collection<ILogicalOperator> copiedSources = copiedToOriginSources.get(originSource);

				ILogicalOperator target = subToSource.getTarget();
				Collection<ILogicalOperator> targets = Lists.newArrayList();
				Optional<ILogicalQueryPart> optPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
				if (!optPartOfOriginTarget.isPresent())
					targets.add(target);
				else if (partsToBeFragmented.contains(optPartOfOriginTarget.get())) {

					// Done by the insertion of the operator for reunion
					continue;

				} else
					targets.addAll(LogicalQueryHelper.collectCopies(optPartOfOriginTarget.get(), modifiedCopiesToOrigin.get(optPartOfOriginTarget.get()), target));

				modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, targets, subToSource, copiedSources);

			}

		}

		// Process each relative sink
		for (ILogicalOperator originSink : copiedToOriginSinks.keySet()) {

			if (originSink.getSubscriptions().isEmpty()) {
				continue;
			}

			for (LogicalSubscription subToSink : originSink.getSubscriptions()) {

				if (originPart.getOperators().contains(subToSink.getTarget()))
					continue;

				Collection<ILogicalOperator> copiedSinks = copiedToOriginSinks.get(originSink);

				ILogicalOperator target = subToSink.getTarget();
				Collection<ILogicalOperator> targets = Lists.newArrayList();
				Optional<ILogicalQueryPart> optPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
				if (!optPartOfOriginTarget.isPresent())
					targets.add(target);
				else if(AbstractFragmentationQueryPartModificator.isSourceOperator(originSink, sourceName) || 
						(originSink instanceof AbstractWindowAO && !((AbstractWindowAO) originSink).getWindowType().equals(WindowType.TIME))) {
					// Assumption: RenameAO has exact one input

					LogicalSubscription subscription = new LogicalSubscription(originSink, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());

					IPair<ILogicalOperator, ILogicalQueryPart> operatorForFragmentation = AbstractFragmentationQueryPartModificator.searchOperatorForFragmentation(modifiedCopiesToOrigin.keySet(), historyOfOperatorsForFragmentation, subscription,
							target);

					targets.add(operatorForFragmentation.getE1());
					modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, copiedSinks, subToSink, targets);
					targets.iterator().next().initialize();

					modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(originPart, modifiedCopiesToOrigin, originSink, historyOfOperatorsForFragmentation);

					continue;

				} else
					targets.addAll(LogicalQueryHelper.collectCopies(optPartOfOriginTarget.get(), modifiedCopiesToOrigin.get(optPartOfOriginTarget.get()), target));

				modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, copiedSinks, subToSink, targets);

			}

		}

		return modifiedCopiesToOrigin;

	}

	@Override
	protected Collection<ILogicalOperator> determineRelevantOperators(ILogicalQueryPart queryPart, String sourceName) throws NullPointerException {

		// The return value
		Collection<ILogicalOperator> relevantOperators = Lists.newArrayList();

		for (ILogicalOperator operator : super.determineRelevantOperators(queryPart, sourceName)) {

			if (!(operator instanceof AbstractWindowAO) || ((AbstractWindowAO) operator).getWindowType().equals(WindowType.TIME))
				relevantOperators.add(operator);

		}

		return relevantOperators;

	}

}