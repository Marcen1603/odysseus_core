/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * @author Dennis Nowak
 *
 */

public abstract class AbstractFragmentUnionTransformationStrategy<T extends ILogicalOperator>
		extends AbstractParallelTransformationStrategy<T> {

	private UnionAO union;
	protected List<AbstractStaticFragmentAO> fragments;
	protected List<Pair<AbstractStaticFragmentAO, Integer>> fragmentsSinkInPorts;

	@Override
	public TransformationResult transform(T operator, ParallelOperatorConfiguration configurationForOperator) {
		super.operator = operator;
		super.configuration = configurationForOperator;
		super.transformationResult = new TransformationResult(State.SUCCESS);

		try {
			super.doValidation();
			prepareTransformation();
			createAndSubscribeFragments();
		} catch (ParallelizationStrategyException e) {
			transformationResult.setState(State.FAILED);
			return transformationResult;
		}

		createUnionOperator();

		// for every degree, insert buffer, clone operator and connect
		// everything
		int bufferCounter = 0;
		for (int i = 0; i < configuration.getDegreeOfParallelization(); i++) {
			ILogicalOperator newOperator = cloneOperator(i);
			bufferCounter = doSubscriptions(bufferCounter, i, newOperator);
			doPostParallelizationIfNeeded(i, newOperator);
		}
		// connect fragmented data stream with union
		doFinalConnection();

		return transformationResult;

	}

	protected abstract void prepareTransformation();
	
	protected abstract void createAndSubscribeFragments() throws ParallelizationStrategyException;;

	protected void subscribeFragmentOperator(LogicalSubscription upstreamOperatorSubscription,
			AbstractStaticFragmentAO fragmentAO) {
		// unsubscribe the join and subscribe the fragemnt operator
		operator.unsubscribeFromSource(upstreamOperatorSubscription);
		fragmentAO.subscribeToSource(upstreamOperatorSubscription.getSource(), 0,
				upstreamOperatorSubscription.getSourceOutPort(),
				upstreamOperatorSubscription.getSource().getOutputSchema());
	}

	protected void storeFragmentOperators(LogicalSubscription upstreamOperatorSubscription,
			AbstractStaticFragmentAO fragmentAO) {
		// save the fragement operator and the input port in a map
		Pair<AbstractStaticFragmentAO, Integer> pair = new Pair<AbstractStaticFragmentAO, Integer>();
		pair.setE1(fragmentAO);
		pair.setE2(upstreamOperatorSubscription.getSinkInPort());
		fragmentsSinkInPorts.add(pair);
		fragments.add(fragmentAO);
	}

	private void createUnionOperator() {
		union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);
	}

	private ILogicalOperator cloneOperator(int i) {
		ILogicalOperator newOperator = operator.clone();
		newOperator.setName(operator.getName() + "_" + i);
		newOperator.setUniqueIdentifier(operator.getUniqueIdentifier() + "_" + i);
		return newOperator;
	}

	private int doSubscriptions(int bufferCounter, int i, ILogicalOperator newOperator) {
		// for every fragementation operator do subscriptions
		for (Pair<AbstractStaticFragmentAO, Integer> pair : fragmentsSinkInPorts) {
			BufferAO buffer = createBufferOperator(bufferCounter,newOperator.getName());
			bufferCounter++;

			buffer.subscribeToSource(pair.getE1(), 0, i, pair.getE1().getOutputSchema());

			newOperator.subscribeToSource(buffer, pair.getE2(), 0, buffer.getOutputSchema());
		}
		return bufferCounter;
	}

	private BufferAO createBufferOperator(int bufferCounter, String targetName) {
		BufferAO buffer = new BufferAO();
		buffer.setName("Buffer_"+ targetName + "_" + bufferCounter);
		buffer.setThreaded(configuration.isUseThreadedBuffer());
		buffer.setMaxBufferSize(configuration.getBufferSize());

		return buffer;
	}

	private void doPostParallelizationIfNeeded(int i, ILogicalOperator newOperator) {
		if (configuration.getEndParallelizationId() != null && !configuration.getEndParallelizationId().isEmpty()) {
			// check if the way to endpoint is valid,
			checkIfWayToEndPointIsValid(operator, configuration);

			// if endoperator id is set, do post parallelization
			ILogicalOperator lastParallelizedOperator = doPostParallelization(operator, newOperator,
					configuration.getEndParallelizationId(), i);
			union.subscribeToSource(lastParallelizedOperator, i, 0, lastParallelizedOperator.getOutputSchema());
		} else {
			union.subscribeToSource(newOperator, i, 0, newOperator.getOutputSchema());
		}
	}

	private void doFinalConnection() {
		// get the last operator that need to be parallelized. if no end id is
		// set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (configuration.getEndParallelizationId() != null && !configuration.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper
					.findDownstreamOperatorWithId(configuration.getEndParallelizationId(), operator);
		} else {
			lastOperatorForParallelization = operator;
		}

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization.getSubscriptions());

		// unsibscribe existing operator from all sources and include new
		// operator
		lastOperatorForParallelization.unsubscribeFromAllSources();
		lastOperatorForParallelization.unsubscribeFromAllSinks();

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getSink().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(), downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}

		for (AbstractStaticFragmentAO fragmentAO : transformationResult.getFragmentOperators()) {
			fragmentAO.initialize();
		}
	}
}
