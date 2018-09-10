/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractFragmentUnionTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

/**
 * @author Dennis Nowak
 *
 */
public class GenericGroupedTransformationStrategy<T extends ILogicalOperator>
		extends AbstractFragmentUnionTransformationStrategy<T> {

	public static final String NAME = "GenericGroupedTransformationStrategy";

	private Map<Integer, List<SDFAttribute>> groupingAttributes = new HashMap<>();

	@Override
	public String getName() {
		return GenericGroupedTransformationStrategy.NAME;
	}

	@Override
	public int evaluateCompatibility(T operator) {
		switch (operator.getStateType()) {
		case PARTITIONED_STATE:
			return 90;
		default:
			return 0;
		}
	}

	private void getGroupAttributes(T operator) {
		if (operator instanceof JoinAO) {
			JoinAO join = (JoinAO) operator;
			this.groupingAttributes = SDFAttributeHelper.getInstance()
					.getSDFAttributesFromEqualPredicates(this.groupingAttributes, join);
		} else if (operator instanceof AggregateAO) {
			this.groupingAttributes.put(0, ((AggregateAO) operator).getGroupingAttributes());
		} else {
			throw new IllegalArgumentException("Operator " + operator.getClass() + " not supported.");
		}
	}

	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> list = new ArrayList<>();
		list.add(HashFragmentAO.class);
		return list;
	}

	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return HashFragmentAO.class;
	}

	@Override
	public IParallelTransformationStrategy<T> getNewInstance() {
		return new GenericGroupedTransformationStrategy<>();
	}

	@Override
	protected void prepareTransformation() {
		getGroupAttributes(operator);

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

			List<SDFAttribute> attributesForSource = groupingAttributes
					.get(upstreamOperatorSubscription.getSinkInPort());
			try {
				fragmentAO = createFragmentAO(configuration.getFragementationType(),
						configuration.getDegreeOfParallelization(), numberOfFragments + "", attributesForSource, null,
						null);
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
