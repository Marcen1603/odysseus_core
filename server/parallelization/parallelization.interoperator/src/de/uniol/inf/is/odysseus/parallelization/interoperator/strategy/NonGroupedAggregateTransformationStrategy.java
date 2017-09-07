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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
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

	private UnionAO union;
	private AbstractStaticFragmentAO fragmentAO;
	private List<AggregateItem> renamedPAAggregationItems;
	private List<AggregateItem> renamedCombineAggregationItems;

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
	public int evaluateCompatibility(AggregateAO operator) {
		boolean everyAggregationHasOnlyOneInputAttribut = true;
		List<AggregateItem> existingAggregationItems = operator
				.getAggregationItems();
		for (AggregateItem aggregateItem : existingAggregationItems) {
			if (aggregateItem.inAttributes.size() > 1) {
				everyAggregationHasOnlyOneInputAttribut = false;
			}
		}

		if (everyAggregationHasOnlyOneInputAttribut) {
			// Only aggregations with one input attribute are allowed
			// for partial aggregates
			if (operator.getGroupingAttributes().isEmpty()) {
				return 100;
			} else {
				// if the aggregation has an grouping, there is might be a
				// better strategy
				return 80;
			}
		}

		// if at least one aggregation has more than one input port, this
		// strategy is not compatible
		return 0;
	}

	/**
	 * do the specific transformation based on the configuration. Instance need
	 * to be initialized via newInstance method
	 * 
	 * @return
	 */
	@Override
	public TransformationResult transform(AggregateAO operator,
			ParallelOperatorConfiguration configurationForOperator) {
		super.operator = operator;
		super.configuration = configurationForOperator;
		super.transformationResult = new TransformationResult(State.SUCCESS);

		try {
			super.doValidation();
			prepareTransformation();
			createFragmentOperator();
		} catch (ParallelizationStrategyException e) {
			transformationResult.setState(State.FAILED);
			return transformationResult;
		}

		subscribeFragementAO();
		renameAggregations();
		createUnionOperator();

		// for each degree of parallelization
		for (int i = 0; i < configuration.getDegreeOfParallelization(); i++) {
			BufferAO buffer = createBufferOperator(i);
			AggregateAO newAggregateOperator = cloneAggregateOperator(i);
			updateSubscriptions(i, buffer, newAggregateOperator);
		}

		AggregateAO combinePAAggregateOperator = createCombinePAAggregateOperator();
		doFinalConnection(combinePAAggregateOperator);
		return transformationResult;
	}

	/**
	 * connect fragmented data stream with union
	 */
	private void doFinalConnection(AggregateAO combinePAAggregateOperator) {
		// subscribe aggregate operator for combining partial aggregates to
		// union
		combinePAAggregateOperator.subscribeToSource(union, 0, 0,
				union.getOutputSchema());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(operator.getSubscriptions());

		// remove old subscription and subscribe new aggregate operator to
		// existing downstream operators
		operator.unsubscribeFromAllSources();
		operator.unsubscribeFromAllSinks();

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getSink().subscribeToSource(
					combinePAAggregateOperator,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					combinePAAggregateOperator.getOutputSchema());
		}
	}

	/**
	 * creates the aggregate operator which combines the partial aggregates
	 * 
	 * @return aggregate operator
	 */
	private AggregateAO createCombinePAAggregateOperator() {
		// create aggregate operator for combining partial aggregates
		AggregateAO combinePAAggregateOperator = operator.clone();
		combinePAAggregateOperator.setName(operator.getName() + "_combinePA");
		combinePAAggregateOperator.setUniqueIdentifier(UUID.randomUUID()
				.toString());
		combinePAAggregateOperator.clearAggregations();
		combinePAAggregateOperator
				.setAggregationItems(renamedCombineAggregationItems);
		combinePAAggregateOperator.setDrainAtClose(false);
		combinePAAggregateOperator.setNumberOfThreads(1);
		return combinePAAggregateOperator;
	}

	/**
	 * connects fragment to buffer and buffer to aggregate
	 * 
	 * @param i
	 * @param buffer
	 * @param newAggregateOperator
	 */
	private void updateSubscriptions(int i, BufferAO buffer,
			AggregateAO newAggregateOperator) {
		// subscribe buffer to fragment
		buffer.subscribeToSource(fragmentAO, 0, i, fragmentAO.getOutputSchema());

		// subscribe new aggregate operator to buffer
		newAggregateOperator.subscribeToSource(buffer, 0, 0,
				buffer.getOutputSchema());

		union.subscribeToSource(newAggregateOperator, i, 0,
				newAggregateOperator.getOutputSchema());
	}

	/**
	 * clone existing aggregate operator and updates the existing aggregations
	 * to use partial aggregates
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	private AggregateAO cloneAggregateOperator(int i) {
		// create new aggregate operator from existing operator
		AggregateAO newAggregateOperator = operator.clone();
		newAggregateOperator.setName(operator.getName() + "_pa_" + i);
		newAggregateOperator.setUniqueIdentifier(UUID.randomUUID().toString());
		newAggregateOperator.setOutputPA(true); // enable partial aggregates
		newAggregateOperator.clearAggregations();
		// use renamed output name of attributes
		newAggregateOperator.setAggregationItems(renamedPAAggregationItems);
		newAggregateOperator.setDrainAtClose(false);
		return newAggregateOperator;
	}

	/**
	 * creates a new buffer operator
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	private BufferAO createBufferOperator(int i) {
		// create buffer
		BufferAO buffer = new BufferAO();
		buffer.setName("Buffer_" + i);
		buffer.setThreaded(configuration.isUseThreadedBuffer());
		buffer.setMaxBufferSize(configuration.getBufferSize());
		buffer.setDrainAtClose(false);
		return buffer;
	}

	/**
	 * creates a new union operator
	 */
	private void createUnionOperator() {
		union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);
	}

	/**
	 * do renaming of aggregate attributes. this is needed to use partial
	 * aggregates
	 */
	private void renameAggregations() {
		// Renaming of input and output attributes for partial aggregates
		List<AggregateItem> existingAggregationItems = operator
				.getAggregationItems();
		renamedPAAggregationItems = new ArrayList<AggregateItem>();
		renamedCombineAggregationItems = new ArrayList<AggregateItem>();

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
	}

	/**
	 * subscribes the fragment operator to existing upstream operator
	 */
	private void subscribeFragementAO() {
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(operator.getSubscribedToSource());

		// subscribe new operator
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			operator.unsubscribeFromSource(upstreamOperatorSubscription);
			fragmentAO.subscribeToSource(
					upstreamOperatorSubscription.getSource(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getSource().getOutputSchema());
		}
	}

	/**
	 * prepares transformation and validates values
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	private void prepareTransformation()
			throws ParallelizationStrategyException {
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			throw new ParallelizationStrategyException(
					"Definition of Endpoint for strategy " + this.getName()
							+ " is not allowed");
		}

		transformationResult.setAllowsModificationAfterUnion(false);

		// if the option for using parallel operators is set to true, the given
		// operator is modified to use multiple threads
		if (configuration.isUseParallelOperators()) {
			operator.setNumberOfThreads(configuration
					.getDegreeOfParallelization());
		}
	}

	/**
	 * creates a fragment operator dynamically based on type
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	private void createFragmentOperator()
			throws ParallelizationStrategyException {
		try {
			fragmentAO = createFragmentAO(
					configuration.getFragementationType(),
					configuration.getDegreeOfParallelization(), "", null, null,
					null);
			transformationResult.addFragmentOperator(fragmentAO);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParallelizationStrategyException("");
		}
		if (fragmentAO == null) {
			throw new ParallelizationStrategyException("");
		}
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
	 * returns a new instance of this strategy
	 */
	@Override
	public IParallelTransformationStrategy<AggregateAO> getNewInstance() {
		return new NonGroupedAggregateTransformationStrategy();
	}
}
