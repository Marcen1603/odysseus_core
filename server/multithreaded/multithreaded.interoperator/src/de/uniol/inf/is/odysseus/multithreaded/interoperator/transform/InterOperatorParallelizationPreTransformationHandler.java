package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;
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

			// Transform Plan
			// TODO do it more dynamically for different operators and plans,
			// maybe different rules and so on
			ILogicalOperator logicalPlan = query.getLogicalPlan();
			if (logicalPlan instanceof TopAO) {
				// get last operator of the plan
				Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
				set.add(AggregateAO.class);
				CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
						set, true);
				GenericGraphWalker collectWalker = new GenericGraphWalker();
				collectWalker.prefixWalk(logicalPlan, collVisitor);

				for (ILogicalOperator operatorForTransformation : collVisitor
						.getResult()) {
					transformOperator(operatorForTransformation);
				}

			}

		}
	}

	private void transformOperator(ILogicalOperator operatorForTransformation) {
		// remove subscriptions
		Collection<LogicalSubscription> upstreamOperatorSubscriptions = operatorForTransformation
				.getSubscribedToSource();
		Collection<LogicalSubscription> downstreamOperatorSubscriptions = operatorForTransformation
				.getSubscriptions();

		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			ILogicalOperator target = upstreamOperatorSubscription.getTarget();
			target.unsubscribeSink(upstreamOperatorSubscription);
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			ILogicalOperator target = downstreamOperatorSubscription
					.getTarget();
			target.unsubscribeFromSource(downstreamOperatorSubscription);
		}

		if (operatorForTransformation instanceof AggregateAO) {
			AggregateAO aggregate = (AggregateAO) operatorForTransformation;
			List<SDFAttribute> groupingAttributes = aggregate
					.getGroupingAttributes();
			if (!groupingAttributes.isEmpty()) {
				// Fragment operator
				HashFragmentAO fragment = new HashFragmentAO();
				fragment.setAttributes(groupingAttributes);
				fragment.setNumberOfFragments(degreeOfParallelization);
				fragment.setName("Hash Fragment");

				// subscribe new operator
				for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
					operatorForTransformation
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
					buffer.setName("Buffer_"+i);
					buffer.setThreaded(true);
					buffer.setMaxBufferSize(10000000);
					
					AggregateAO newPartialAggregate = (AggregateAO) operatorForTransformation
							.clone();
					newPartialAggregate.setName(operatorForTransformation
							.getName() + "_" + i);

					buffer.subscribeToSource(fragment, 0, i,
							fragment.getOutputSchema());
					
					newPartialAggregate.subscribeToSource(buffer, 0, 0,
							buffer.getOutputSchema());
					
					union.subscribeToSource(newPartialAggregate, i, 0,
							newPartialAggregate.getOutputSchema());
				}

				for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
					operatorForTransformation
							.unsubscribeSink(downstreamOperatorSubscription);
					downstreamOperatorSubscription.getTarget()
							.subscribeToSource(
									union,
									downstreamOperatorSubscription
											.getSinkInPort(),
									downstreamOperatorSubscription
											.getSourceOutPort(),
									union.getOutputSchema());
				}
			}
		}

	}

	@Override
	public String getType() {
		return TYPE;
	}

}
