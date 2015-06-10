package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;
import de.uniol.inf.is.odysseus.multithreaded.parameter.MultithreadedOperatorParameter;
import de.uniol.inf.is.odysseus.multithreaded.transform.AbstractParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class InterOperatorParallelizationPreTransformationHandler extends
		AbstractParallelizationPreTransformationHandler {

	private static final String HANDLER_NAME = "InterOperatorParallelizationPreTransformationHandler";
	private final String TYPE = "INTER_OPERATOR";
	private final int MIN_PARAMETER_COUNT = 1;

	private int degreeOfParallelization = 0;

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		if (handlerParameters.size() < MIN_PARAMETER_COUNT) {
			throw new IllegalArgumentException();
		} else {
			// Determine parameters
			for (Pair<String, String> pair : handlerParameters) {
				ParallelizationParameter parameter = ParallelizationParameter
						.getParameterByName(pair.getE1());
				switch (parameter) {
				case DEGREE_OF_PARALLELIZATION:
					try {
						degreeOfParallelization = Integer
								.parseInt(pair.getE2());
					} catch (Exception e) {
						throw new IllegalAccessError();
					}
					break;
				default:
					break;
				}
			}

			List<String> operatorIds = new ArrayList<String>();
			MultithreadedOperatorParameter multithreadedOperatorParameter = config
					.get(MultithreadedOperatorParameter.class);
			if (multithreadedOperatorParameter != null){
				operatorIds.addAll(multithreadedOperatorParameter
						.getOperatorIds());				
			}

			// Transform Plan
			// TODO do it more dynamically for different operators and plans,
			// maybe different rules and so on
			ILogicalOperator logicalPlan = query.getLogicalPlan();
			if (logicalPlan instanceof TopAO) {
				Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
				set.add(AggregateAO.class);
				set.add(JoinAO.class);
				CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
						set, true);
				GenericGraphWalker collectWalker = new GenericGraphWalker();
				collectWalker.prefixWalk(logicalPlan, collVisitor);

				for (ILogicalOperator operatorForTransformation : collVisitor
						.getResult()) {
					if (operatorIds.contains(operatorForTransformation.getUniqueIdentifier()) || operatorIds.isEmpty()){
						if (operatorForTransformation instanceof AggregateAO) {
							transformAggregateOperator((AggregateAO) operatorForTransformation);
						} else if (operatorForTransformation instanceof JoinAO) {
							transformJoinOperator((JoinAO) operatorForTransformation);
						}						
					}
				}

			}

		}
	}

	private void transformAggregateOperator(AggregateAO aggregateOperator) {
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscribedToSource());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscriptions());

		// remove subscriptions
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			ILogicalOperator target = upstreamOperatorSubscription.getTarget();
			target.unsubscribeSink(upstreamOperatorSubscription);
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			ILogicalOperator target = downstreamOperatorSubscription
					.getTarget();
			target.unsubscribeFromSource(downstreamOperatorSubscription);
		}

		List<SDFAttribute> groupingAttributes = aggregateOperator
				.getGroupingAttributes();
		if (!groupingAttributes.isEmpty()) {
			// Fragment operator
			HashFragmentAO fragment = new HashFragmentAO();
			fragment.setAttributes(groupingAttributes);
			fragment.setNumberOfFragments(degreeOfParallelization);
			fragment.setName("Hash Fragment");

			// subscribe new operator
			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				aggregateOperator
						.unsubscribeFromSource(upstreamOperatorSubscription);
				fragment.subscribeToSource(upstreamOperatorSubscription
						.getTarget(), upstreamOperatorSubscription
						.getSinkInPort(), upstreamOperatorSubscription
						.getSourceOutPort(), upstreamOperatorSubscription
						.getTarget().getOutputSchema());
			}

			UnionAO union = new UnionAO();
			union.setName("Union");

			for (int i = 0; i < degreeOfParallelization; i++) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + i);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(10000000);

				AggregateAO newAggregateOperator = aggregateOperator.clone();
				newAggregateOperator.setName(aggregateOperator.getName() + "_"
						+ i);
				newAggregateOperator.setUniqueIdentifier(aggregateOperator
						.getUniqueIdentifier() + "_" + i);

				buffer.subscribeToSource(fragment, 0, i,
						fragment.getOutputSchema());

				newAggregateOperator.subscribeToSource(buffer, 0, 0,
						buffer.getOutputSchema());

				union.subscribeToSource(newAggregateOperator, i, 0,
						newAggregateOperator.getOutputSchema());
			}

			for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
				aggregateOperator
						.unsubscribeSink(downstreamOperatorSubscription);
				downstreamOperatorSubscription.getTarget().subscribeToSource(
						union, downstreamOperatorSubscription.getSinkInPort(),
						downstreamOperatorSubscription.getSourceOutPort(),
						union.getOutputSchema());
			}
		}
	}

	private void transformJoinOperator(JoinAO joinOperator) {
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(joinOperator
				.getSubscribedToSource());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(joinOperator.getSubscriptions());

		// remove subscriptions
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			ILogicalOperator target = upstreamOperatorSubscription.getTarget();
			target.unsubscribeSink(upstreamOperatorSubscription);
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			ILogicalOperator target = downstreamOperatorSubscription
					.getTarget();
			target.unsubscribeFromSource(downstreamOperatorSubscription);
		}

		// TODO Prädikate können auch anders aufgebaut sein
		List<SDFAttribute> attributes = joinOperator.getPredicate()
				.getAttributes();
		int numberOfFragments = 0;

		List<Pair<HashFragmentAO, Integer>> fragmentsSinkInPorts = new ArrayList<Pair<HashFragmentAO, Integer>>();

		for (SDFAttribute sdfAttribute : attributes) {

			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				int indexOfAttribute = upstreamOperatorSubscription.getSchema()
						.indexOf(sdfAttribute);
				if (indexOfAttribute != -1) {
					// if join predicate attribute references on the source from
					// this subscription, create fragment operator
					// Fragment operator
					HashFragmentAO fragment = new HashFragmentAO();
					ArrayList<SDFAttribute> fragmentAttributes = new ArrayList<SDFAttribute>();
					fragmentAttributes.add(sdfAttribute);
					fragment.setAttributes(fragmentAttributes);
					fragment.setNumberOfFragments(degreeOfParallelization);
					fragment.setName("Hash Fragment_" + numberOfFragments);

					Pair<HashFragmentAO, Integer> pair = new Pair<HashFragmentAO, Integer>();
					pair.setE1(fragment);
					pair.setE2(upstreamOperatorSubscription.getSinkInPort());
					fragmentsSinkInPorts.add(pair);

					joinOperator
							.unsubscribeFromSource(upstreamOperatorSubscription);
					fragment.subscribeToSource(upstreamOperatorSubscription
							.getTarget(), 0, upstreamOperatorSubscription
							.getSourceOutPort(), upstreamOperatorSubscription
							.getTarget().getOutputSchema());

					numberOfFragments++;
					break;
				}
			}

		}

		UnionAO union = new UnionAO();
		union.setName("Union");

		int bufferCounter = 0;
		for (int i = 0; i < degreeOfParallelization; i++) {
			JoinAO newJoinOperator = joinOperator.clone();
			newJoinOperator.setName(joinOperator.getName() + "_" + i);
			newJoinOperator.setUniqueIdentifier(joinOperator.getUniqueIdentifier()
					+ "_" + i);

			for (Pair<HashFragmentAO, Integer> pair : fragmentsSinkInPorts) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + bufferCounter);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(10000000);
				bufferCounter++;

				buffer.subscribeToSource(pair.getE1(), 0, i, pair.getE1()
						.getOutputSchema());

				newJoinOperator.subscribeToSource(buffer, pair.getE2(), 0,
						buffer.getOutputSchema());

				union.subscribeToSource(newJoinOperator, i, 0,
						newJoinOperator.getOutputSchema());
			}
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			joinOperator.unsubscribeSink(downstreamOperatorSubscription);
			downstreamOperatorSubscription.getTarget().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}

	}

	@Override
	public String getType() {
		return TYPE;
	}

}
