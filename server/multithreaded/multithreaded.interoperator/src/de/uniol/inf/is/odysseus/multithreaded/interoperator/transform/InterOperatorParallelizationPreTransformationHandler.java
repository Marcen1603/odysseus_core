package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorParameter;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.IMultithreadedTransformationStrategy;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.registry.MultithreadedTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;
import de.uniol.inf.is.odysseus.multithreaded.transform.AbstractParallelizationPreTransformationHandler;

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
			if (multithreadedOperatorParameter != null) {
				operatorIds.addAll(multithreadedOperatorParameter
						.getOperatorIds());
			}

			// Transform Plan
			ILogicalOperator logicalPlan = query.getLogicalPlan();
			if (logicalPlan instanceof TopAO) {
				Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
				List<Class<? extends ILogicalOperator>> validTypes = MultithreadedTransformationStrategyRegistry
						.getValidTypes();
				if (validTypes != null) {
					set.addAll(validTypes);
					CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
							set, true);
					GenericGraphWalker collectWalker = new GenericGraphWalker();
					collectWalker.prefixWalk(logicalPlan, collVisitor);

					for (ILogicalOperator operatorForTransformation : collVisitor
							.getResult()) {
						if (operatorIds.contains(operatorForTransformation
								.getUniqueIdentifier())
								|| operatorIds.isEmpty()) {
							List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = MultithreadedTransformationStrategyRegistry
									.getStrategiesForOperator(operatorForTransformation
											.getClass());
							if (!strategiesForOperator.isEmpty()) {
								// evaluate compatibility of the different
								// strategies
								List<FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>> strategiesWithCompatibility = new ArrayList<FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>>();
								for (IMultithreadedTransformationStrategy<? extends ILogicalOperator> strategy : strategiesForOperator) {
									FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategyWithCompatibility = new FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>(
											strategy.evaluateCompatibility(operatorForTransformation),
											strategy);
									strategiesWithCompatibility
											.add(strategyWithCompatibility);
								}
								Collections.sort(strategiesWithCompatibility,
										Collections.reverseOrder());
								if (strategiesWithCompatibility.get(0).getE1() > 0) {
									strategiesWithCompatibility
											.get(0)
											.getE2()
											.transform(
													operatorForTransformation,
													degreeOfParallelization);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
