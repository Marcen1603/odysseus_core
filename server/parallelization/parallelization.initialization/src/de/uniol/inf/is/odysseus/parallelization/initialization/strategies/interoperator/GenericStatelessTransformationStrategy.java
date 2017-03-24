/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractFragmentUnionTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

/**
 * @author Dennis Nowak
 *
 */
public class GenericStatelessTransformationStrategy<T extends ILogicalOperator>
		extends AbstractFragmentUnionTransformationStrategy<T> {

	public static final String NAME = "GenericStatelessTransformationStrategy";

	@Override
	public String getName() {
		return GenericStatelessTransformationStrategy.NAME;
	}

	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> list = new ArrayList<>();
		list.add(RoundRobinFragmentAO.class);
		list.add(ShuffleFragmentAO.class);
		return list;
	}

	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return RoundRobinFragmentAO.class;
	}

	@Override
	public int evaluateCompatibility(T operator) {
		switch (operator.getStateType()) {
		case STATELESS:
			return 90;
		default:
			return 0;
		}
	}

	@Override
	public IParallelTransformationStrategy<T> getNewInstance() {
		return new GenericStatelessTransformationStrategy<>();
	}

	@Override
	protected void prepareTransformation() {

	}

	@Override
	protected void createAndSubscribeFragments() throws ParallelizationStrategyException {
		int numberOfFragments = 0;
		fragmentsSinkInPorts = new ArrayList<Pair<AbstractStaticFragmentAO, Integer>>();
		fragments = new ArrayList<AbstractStaticFragmentAO>();

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(operator.getSubscribedToSource());

		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {

			AbstractStaticFragmentAO fragmentAO = null;
			try {
				fragmentAO = createFragmentAO(configuration.getFragementationType(),
						configuration.getDegreeOfParallelization(), numberOfFragments + "", null, null, null);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ParallelizationStrategyException("");
			}
			if (fragmentAO == null) {
				throw new ParallelizationStrategyException("");
			}
			transformationResult.addFragmentOperator(fragmentAO);
			storeFragmentOperators(upstreamOperatorSubscription, fragmentAO);
			subscribeFragmentOperator(upstreamOperatorSubscription, fragmentAO);
			numberOfFragments++;
		}
	}

}
