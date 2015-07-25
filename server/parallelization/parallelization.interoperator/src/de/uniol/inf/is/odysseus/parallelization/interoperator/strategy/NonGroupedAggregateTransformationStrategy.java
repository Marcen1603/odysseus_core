/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

/**
 * Inter operator parallelization strategy for AggregateAO operators, which
 * contains no grouping. this fragmentation uses roundRobin or
 * shuffleFragmentation and partial aggregates. The data stream is splitted with
 * the fragmentation operator. After this a buffer is inserted. the existing
 * operator is cloned and configured to use partial aggregates. after this the
 * data stream is combined with an union operator. the created partial
 * aggregates are combined with an new aggregate operator after the union.
 * 
 * @author ChrisToenjesDeye
 *
 */
public class NonGroupedAggregateTransformationStrategy extends
		AbstractParallelTransformationStrategy<AggregateAO> {

	@Override
	public String getName() {
		return "NonGroupedAggregateTransformationStrategy";
	}

	/**
	 * evaluates the compatibility of this strategy with a given operator. to
	 * use this strategy it is possible if the aggregation has grouping or not.
	 * the aggregations need to be one input attribute to use partial aggregates
	 * 
	 * @param operator
	 * @return
	 */
	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		if (operator instanceof AggregateAO) {
			AggregateAO aggregateOperator = (AggregateAO) operator;
			if (aggregateOperator.getGroupingAttributes().isEmpty()) {
				// if aggregation has no grouping this strategy works good

				boolean everyAggregationHasOnlyOneInputAttribut = true;
				List<AggregateItem> existingAggregationItems = aggregateOperator
						.getAggregationItems();
				for (AggregateItem aggregateItem : existingAggregationItems) {
					if (aggregateItem.inAttributes.size() > 1) {
						everyAggregationHasOnlyOneInputAttribut = false;
					}
				}

				if (everyAggregationHasOnlyOneInputAttribut) {
					// Only aggregations with one input attribute are allowed
					// for partial aggregates
					return 100;
				}
			} else {
				// if the aggregation has an grouping, there is might be a
				// better strategy
				return 50;
			}
		}
		// if the operator is no aggregation, this strategy is incompatible
		return 0;
	}

	/**
	 * do the specific transformation based on the configuration
	 * 
	 * @param operator
	 * @param configurationForOperator
	 * @return
	 */
	@Override
	public TransformationResult transform(ILogicalOperator operator,
			ParallelOperatorConfiguration configurationForOperator) {
		if (!super.areSettingsValid(configurationForOperator)) {
			return new TransformationResult(State.FAILED);
		}
		if (configurationForOperator.getEndParallelizationId() != null
				&& !configurationForOperator.getEndParallelizationId().isEmpty()) {
			throw new IllegalArgumentException(
					"Definition of Endpoint for strategy " + this.getName()
							+ " is not allowed");
		}

		TransformationResult transformationResult = new TransformationResult(
				State.SUCCESS);
		transformationResult.setAllowsModificationAfterUnion(false);

		AggregateAO aggregateOperator = (AggregateAO) operator;

		// create fragment operator
		AbstractStaticFragmentAO fragmentAO;
		try {
			fragmentAO = createFragmentAO(
					configurationForOperator.getFragementationType(),
					configurationForOperator.getDegreeOfParallelization(), "", null,
					null, null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return new TransformationResult(State.FAILED);
		}

		if (fragmentAO == null) {
			return new TransformationResult(State.FAILED);
		}
		transformationResult.addFragmentOperator(fragmentAO);

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscribedToSource());

		// subscribe new operator
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			aggregateOperator
					.unsubscribeFromSource(upstreamOperatorSubscription);
			fragmentAO.subscribeToSource(
					upstreamOperatorSubscription.getTarget(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getTarget().getOutputSchema());
		}

		// Renaming of input and output attributes for partial aggregates
		List<AggregateItem> existingAggregationItems = aggregateOperator
				.getAggregationItems();
		List<AggregateItem> renamedPAAggregationItems = new ArrayList<AggregateItem>();
		List<AggregateItem> renamedCombineAggregationItems = new ArrayList<AggregateItem>();

		for (AggregateItem aggregateItem : existingAggregationItems) {
			SDFAttribute attr = aggregateItem.outAttribute;

			// output attributes
			String newPAAttributeName = "pa_" + attr.getQualName();
			SDFAttribute outAttribute = new SDFAttribute(attr.getSourceName(),
					newPAAttributeName, SDFDatatype.PARTIAL_AGGREGATE, null,
					attr.getDtConstraints(), null);
			AggregateItem newOutItem = new AggregateItem(
					aggregateItem.aggregateFunction.toString(),
					aggregateItem.inAttributes, outAttribute);
			renamedPAAggregationItems.add(newOutItem);

			// input attributes
			List<SDFAttribute> inAttributes = new ArrayList<SDFAttribute>();
			SDFAttribute inAttribute = outAttribute.clone();
			inAttributes.add(inAttribute);

			AggregateItem newInItem = new AggregateItem(
					aggregateItem.aggregateFunction.toString(), inAttributes,
					aggregateItem.outAttribute);
			renamedCombineAggregationItems.add(newInItem);
		}

		// create union operator for merging fragmented datastreams
		UnionAO union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);

		// for each degree of parallelization
		for (int i = 0; i < configurationForOperator.getDegreeOfParallelization(); i++) {
			// create buffer
			BufferAO buffer = new BufferAO();
			buffer.setName("Buffer_" + i);
			buffer.setThreaded(configurationForOperator.isUseThreadedBuffer());
			buffer.setMaxBufferSize(configurationForOperator.getBufferSize());
			buffer.setDrainAtClose(false);

			// create new aggregate operator from existing operator
			AggregateAO newAggregateOperator = aggregateOperator.clone();
			newAggregateOperator.setName(aggregateOperator.getName() + "_pa_"
					+ i);
			newAggregateOperator.setUniqueIdentifier(aggregateOperator
					.getUniqueIdentifier() + "_pa_" + i);
			newAggregateOperator.setOutputPA(true); // enable partial aggregates
			newAggregateOperator.clearAggregations();
			newAggregateOperator.setAggregationItems(renamedPAAggregationItems); // use
																					// renamed
																					// output
																					// name
																					// of
																					// attributes
			newAggregateOperator.setDrainAtClose(true);

			// subscribe buffer to fragment
			buffer.subscribeToSource(fragmentAO, 0, i,
					fragmentAO.getOutputSchema());

			// subscribe new aggregate operator to buffer
			newAggregateOperator.subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());

			union.subscribeToSource(newAggregateOperator, i, 0,
					newAggregateOperator.getOutputSchema());

		}

		// create aggregate operator for combining partial aggregates
		AggregateAO combinePAAggregateOperator = aggregateOperator.clone();
		combinePAAggregateOperator.setName(aggregateOperator.getName()
				+ "_combinePA");
		combinePAAggregateOperator.setUniqueIdentifier(aggregateOperator
				.getUniqueIdentifier() + "_combinePA");
		combinePAAggregateOperator.clearAggregations();
		combinePAAggregateOperator
				.setAggregationItems(renamedCombineAggregationItems);
		combinePAAggregateOperator.setDrainAtClose(true);

		// subscribe aggregate operator for combining partial aggregates to
		// union
		combinePAAggregateOperator.subscribeToSource(union, 0, 0,
				union.getOutputSchema());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscriptions());

		// remove old subscription and subscribe new aggregate operator to
		// existing downstream operators
		aggregateOperator.unsubscribeFromAllSources();
		aggregateOperator.unsubscribeFromAllSinks();

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getTarget().subscribeToSource(
					combinePAAggregateOperator,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					combinePAAggregateOperator.getOutputSchema());
		}
		return transformationResult;
	}

	/**
	 * returns a list of compatible fragmentation types (e.g. ShuffleFragmentAO)
	 * 
	 * @return
	 */
	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractStaticFragmentAO>>();
		allowedFragmentTypes.add(RoundRobinFragmentAO.class);
		allowedFragmentTypes.add(ShuffleFragmentAO.class);
		return allowedFragmentTypes;
	}

	/**
	 * returns the preferred fragementation type. this is needed if the user
	 * doesnt select a fragmentation
	 * 
	 * @return
	 */
	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return RoundRobinFragmentAO.class;
	}

	/**
	 * abstract method to allow strategy specific post parallelization works or
	 * validations. this method is used in postParalleliaztion for each operator
	 * between start and end
	 * 
	 * @param parallelizedOperator
	 * @param currentExistingOperator
	 * @param currentClonedOperator
	 * @param iteration
	 * @param fragments
	 * @param settingsForOperator
	 */
	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractStaticFragmentAO> fragments,
			ParallelOperatorConfiguration settingsForOperator) {
		// no operation needed, because post parallelization is not allowed for
		// this strategy
	}

}
