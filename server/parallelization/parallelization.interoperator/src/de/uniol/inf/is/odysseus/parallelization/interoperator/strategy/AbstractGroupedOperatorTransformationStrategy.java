package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

/**
 * abstract class for inter operator parallelization strategies, which uses
 * grouping of the operator for fragmentation of the datastream (e.g.
 * aggregation or fastmedian)
 * 
 * @author ChrisToenjesDeye
 */
public abstract class AbstractGroupedOperatorTransformationStrategy<T extends ILogicalOperator>
		extends AbstractParallelTransformationStrategy<T> {

	protected AbstractStaticFragmentAO fragmentAO;
	protected UnionAO union;
	protected List<SDFAttribute> groupingAttributes;

	/**
	 * connect fragmented data stream with union
	 */
	protected void doFinalConnection() {
		// get the last operator that need to be parallelized. if no end id
		// is set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper
					.findDownstreamOperatorWithId(
							configuration.getEndParallelizationId(), operator);
		} else {
			lastOperatorForParallelization = operator;
		}

		// remove subscriptions to sink from this operator and connect the
		// union
		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization
				.getSubscriptions());

		lastOperatorForParallelization.unsubscribeFromAllSources();
		lastOperatorForParallelization.unsubscribeFromAllSinks();

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getSink().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}
	}

	/**
	 * transform plan until endoperator is reached
	 * 
	 * @param i
	 *            portnumber / iteration
	 * @param newOperator
	 */
	protected void doPostParallelizationIfNeeded(int i,
			ILogicalOperator newOperator) {
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {

			// check the way to endpoint
			checkIfWayToEndPointIsValid(operator, configuration);

			// if a end operator id is set, do post parallelization
			List<AbstractStaticFragmentAO> fragments = new ArrayList<AbstractStaticFragmentAO>();
			fragments.add(fragmentAO);
			ILogicalOperator lastParallelizedOperator = doPostParallelization(
					operator, newOperator,
					configuration.getEndParallelizationId(), i);
			union.subscribeToSource(lastParallelizedOperator, i, 0,
					lastParallelizedOperator.getOutputSchema());
		} else {
			union.subscribeToSource(newOperator, i, 0,
					newOperator.getOutputSchema());
		}
	}

	/**
	 * creates a new Buffer operator
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	protected BufferAO createBufferOperator(int i) {
		BufferAO buffer = new BufferAO();
		buffer.setName("Buffer_" + i);
		buffer.setThreaded(configuration.isUseThreadedBuffer());
		buffer.setMaxBufferSize(configuration.getBufferSize());
		buffer.setDrainAtClose(false);
		return buffer;
	}

	/**
	 * creates a new Union Operator
	 */
	protected void createUnionOperator() {
		union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);
	}

	/**
	 * subscribes the created fragment operator to existing upstream operator
	 */
	protected void subscribeFragementAO() {
		// subscribe new operator
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(operator.getSubscribedToSource());

		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			// unsubscribe from all sources
			operator.unsubscribeFromSource(upstreamOperatorSubscription);
			// subscribe new fragement operator to these sources
			fragmentAO.subscribeToSource(
					upstreamOperatorSubscription.getSink(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getSink().getOutputSchema());
		}
	}

	/**
	 * creates a fragment operator dynamically based on type
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	protected void createFragmentOperator()
			throws ParallelizationStrategyException {
		fragmentAO = null;
		try {
			fragmentAO = createFragmentAO(
					configuration.getFragementationType(),
					configuration.getDegreeOfParallelization(), "",
					groupingAttributes, null, null);
			transformationResult.addFragmentOperator(fragmentAO);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParallelizationStrategyException(
					"Error creating fragment operator");
		}
		if (fragmentAO == null) {
			throw new ParallelizationStrategyException(
					"Error creating fragment operator");
		}
	}

	/**
	 * returns a list of compatible fragmentation types (e.g. HashFragementAO)
	 * 
	 * @return
	 */
	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractStaticFragmentAO>>();
		allowedFragmentTypes.add(HashFragmentAO.class);
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
		return HashFragmentAO.class;
	}
}
