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
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.IMultithreadedTransformationStrategy;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.registry.MultithreadedTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;
import de.uniol.inf.is.odysseus.multithreaded.transform.AbstractParallelizationPreTransformationHandler;

public class InterOperatorParallelizationPreTransformationHandler extends
		AbstractParallelizationPreTransformationHandler {

	public static final String HANDLER_NAME = "InterOperatorParallelizationPreTransformationHandler";
	private final String TYPE = "INTER_OPERATOR";
	private final int PARAMETER_COUNT = 2;

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = 0;

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		if (handlerParameters.size() != PARAMETER_COUNT) {
			throw new IllegalArgumentException();
		} else {
			// Determine parameters
			for (Pair<String, String> pair : handlerParameters) {
				ParallelizationParameter parameter = ParallelizationParameter
						.getParameterByName(pair.getE1());
				switch (parameter) {
				case GLOBAL_DEGREE_OF_PARALLELIZATION:
					try {
						globalDegreeOfParallelization = Integer.parseInt(pair
								.getE2());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException();
					}
					break;
				case GLOBAL_BUFFERSIZE:
					try {
						globalBufferSize = Integer.parseInt(pair.getE2());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException();
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
						// operator has no custom settings or no settings are
						// available, then choose best strategy and transform
						if (!operatorIds.isEmpty()
								&& (operatorForTransformation
										.getUniqueIdentifier() == null || !operatorIds
										.contains(operatorForTransformation
												.getUniqueIdentifier()
												.toLowerCase()))) {
							// if we have an set of operatorIds, only this
							// operators should be transformed. If then the id
							// of the given operator is null or not in the set
							// of operatorIds, no transformation is done
							continue;
						} else if (operatorIds.isEmpty()) {
							IMultithreadedTransformationStrategy<? extends ILogicalOperator> bestStrategy = getBestStrategy(operatorForTransformation);
							MultithreadedOperatorSettings settings = MultithreadedOperatorSettings
									.createDefaultSettings(bestStrategy,
											globalDegreeOfParallelization,
											globalBufferSize);
							bestStrategy.transform(operatorForTransformation,
									settings);
						} else if (operatorIds
								.contains(operatorForTransformation
										.getUniqueIdentifier().toLowerCase())) {
							// if custom settings for operator
							// are available
							MultithreadedOperatorSettings settingsForOperator = multithreadedOperatorParameter
									.getSettingsForOperator(operatorForTransformation
											.getUniqueIdentifier());
							if (settingsForOperator.hasMultithreadingStrategy()) {
								IMultithreadedTransformationStrategy<? extends ILogicalOperator> selectedStrategy = MultithreadedTransformationStrategyRegistry
										.getStrategiesByName(settingsForOperator
												.getMultithreadingStrategy());
								settingsForOperator
										.doPostCalculationsForSettings(
												selectedStrategy,
												globalDegreeOfParallelization,
												globalBufferSize);

								if (selectedStrategy.getOperatorType() == operatorForTransformation
										.getClass()) {
									int compatibility = selectedStrategy
											.evaluateCompatibility(operatorForTransformation);
									if (compatibility > 0) {
										selectedStrategy.transform(
												operatorForTransformation,
												settingsForOperator);
									} else {
										throw new IllegalArgumentException(
												"Strategy "
														+ selectedStrategy
																.getName()
														+ " is not compatible with selected operator with id "
														+ operatorForTransformation
																.getUniqueIdentifier());
									}
								} else {
									throw new IllegalArgumentException(
											"Strategy with name "
													+ settingsForOperator
															.getMultithreadingStrategy()
													+ " is not compatible with Operator of type "
													+ operatorForTransformation
															.getClass());
								}

							} else {
								IMultithreadedTransformationStrategy<? extends ILogicalOperator> bestStrategy = getBestStrategy(operatorForTransformation);
								settingsForOperator
										.doPostCalculationsForSettings(
												bestStrategy,
												globalDegreeOfParallelization,
												globalBufferSize);
								bestStrategy.transform(
										operatorForTransformation,
										settingsForOperator);
							}
						}
					}
				}
			}
		}
	}

	private IMultithreadedTransformationStrategy<? extends ILogicalOperator> getBestStrategy(
			ILogicalOperator operatorForTransformation) {
		List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = MultithreadedTransformationStrategyRegistry
				.getStrategiesForOperator(operatorForTransformation.getClass());
		if (!strategiesForOperator.isEmpty()) {
			// evaluate compatibility of the different
			// strategies
			List<FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>> strategiesWithCompatibility = new ArrayList<FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>>();
			for (IMultithreadedTransformationStrategy<? extends ILogicalOperator> strategy : strategiesForOperator) {
				FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategyWithCompatibility = new FESortedPair<Integer, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>(
						strategy.evaluateCompatibility(operatorForTransformation),
						strategy);
				strategiesWithCompatibility.add(strategyWithCompatibility);
			}
			Collections.sort(strategiesWithCompatibility,
					Collections.reverseOrder());
			if (strategiesWithCompatibility.get(0).getE1() > 0) {
				return strategiesWithCompatibility.get(0).getE2();
			}
		}
		return null;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
