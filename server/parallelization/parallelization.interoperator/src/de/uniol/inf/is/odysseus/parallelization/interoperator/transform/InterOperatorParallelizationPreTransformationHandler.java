package de.uniol.inf.is.odysseus.parallelization.interoperator.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.ParallelOperatorParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.ParallelOperatorSettings;
import de.uniol.inf.is.odysseus.parallelization.interoperator.postoptimization.PostOptimizationHandler;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry.ParallelTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.transform.AbstractParallelizationPreTransformationHandler;

public class InterOperatorParallelizationPreTransformationHandler extends
		AbstractParallelizationPreTransformationHandler {

	public static final String HANDLER_NAME = "InterOperatorParallelizationPreTransformationHandler";
	private final String TYPE = "INTER_OPERATOR";
	private final int PARAMETER_COUNT = 3;

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = 0;
	private boolean optimizationAllowed;

	private ParallelOperatorParameter parallelOperatorParameter;
	private List<String> operatorIds = new ArrayList<String>();

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {

		// get global parameters
		getGlobalParameters(handlerParameters);

		// get individual parameters for each operator
		getOperatorSpecificParameters(config);

		// Get logical plan and copy it, needed for revert if transformation
		// fails
		ILogicalOperator logicalPlan = query.getLogicalPlan();

		// do transformations
		List<TransformationResult> transformationResults = new ArrayList<TransformationResult>();
		transformationResults = doTransformations(query, logicalPlan,
				transformationResults);

		// if all transformations are done, we try to optimize the
		// transformed plan (remove union, fragment combinations if
		// possible)
		PostOptimizationHandler.doPostOptimization(logicalPlan, query,
				transformationResults, optimizationAllowed);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<TransformationResult> doTransformations(ILogicalQuery query,
			ILogicalOperator logicalPlan,
			List<TransformationResult> transformationResults) {
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
				query);
		GenericGraphWalker copyWalker = new GenericGraphWalker();
		copyWalker.prefixWalk(logicalPlan, copyVisitor);
		ILogicalOperator savedPlan = copyVisitor.getResult();

		// do transformation of logical graph
		try {
			if (logicalPlan instanceof TopAO) {
				if (parallelOperatorParameter == null
						|| (parallelOperatorParameter != null && parallelOperatorParameter
								.getOperatorIds().isEmpty())) {
					// if no operator specific parameters are set, each operator
					// which has an compatible strategy is parallelized
					transformationResults
							.addAll(doAutomaticTransformation(logicalPlan));
				} else if (parallelOperatorParameter != null
						&& !parallelOperatorParameter.getOperatorIds()
								.isEmpty()) {
					transformationResults
							.addAll(doCustomTransformation(logicalPlan));
				}
			}
		} catch (Exception e) {
			// if something went wrong, revert plan and throw exception
			query.setLogicalPlan(savedPlan, true);
			throw e;
		}
		return transformationResults;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<TransformationResult> doCustomTransformation(
			ILogicalOperator logicalPlan) {

		List<TransformationResult> transformationResults = new ArrayList<TransformationResult>();

		// create graph walker with id visitor, returns a map with the id and
		// the corresponding operator
		OperatorIdLogicalGraphVisitor<ILogicalOperator> operatorIdVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				parallelOperatorParameter.getOperatorIds());
		GenericGraphWalker idWalker = new GenericGraphWalker();
		idWalker.prefixWalk(logicalPlan, operatorIdVisitor);
		Map<String, ILogicalOperator> result = operatorIdVisitor.getResult();

		// iterate over result of id visitor
		for (String operatorId : result.keySet()) {
			ILogicalOperator operatorForTransformation = result.get(operatorId);
			ParallelOperatorSettings settingsForOperator = parallelOperatorParameter
					.getSettingsForOperator(operatorId);
			if (operatorForTransformation != null
					&& settingsForOperator != null) {
				// only do transformation if operator is found and settings
				// exists
				if (settingsForOperator.hasMultithreadingStrategy()) {
					// if setting has an user defined Parallel strategy
					IParallelTransformationStrategy<? extends ILogicalOperator> selectedStrategy = ParallelTransformationStrategyRegistry
							.getStrategiesByName(settingsForOperator
									.getParallelStrategy());
					// do post calculations and validations for settings
					settingsForOperator.doPostCalculationsForSettings(
							selectedStrategy, globalDegreeOfParallelization,
							globalBufferSize);
					if (selectedStrategy.getOperatorType() == operatorForTransformation
							.getClass()) {
						// evaluate compatibility of strategy and operator
						int compatibility = selectedStrategy
								.evaluateCompatibility(operatorForTransformation);
						if (compatibility > 0) {
							// if compatibility is greater than 0,
							// transformation is possible
							transformationResults.add(selectedStrategy
									.transform(operatorForTransformation,
											settingsForOperator));
						} else {
							// if the selected strategy corresponds to the given
							// operator type, but is not compatible
							throw new IllegalArgumentException(
									"Strategy "
											+ selectedStrategy.getName()
											+ " is not compatible with selected operator with id "
											+ operatorForTransformation
													.getUniqueIdentifier());
						}
					} else {
						// if the selected strategy is valid for this operator
						// type (selected by id), throw exception
						throw new IllegalArgumentException(
								"Strategy with name "
										+ settingsForOperator
												.getParallelStrategy()
										+ " is not compatible with Operator of type "
										+ operatorForTransformation.getClass());
					}

				} else {
					// if no user defined strategy is given, get the best
					// strategy for this operator
					IParallelTransformationStrategy<? extends ILogicalOperator> bestStrategy = getBestStrategy(operatorForTransformation);
					// do post calculations and validations for settings
					settingsForOperator.doPostCalculationsForSettings(
							bestStrategy, globalDegreeOfParallelization,
							globalBufferSize);
					transformationResults.add(bestStrategy.transform(
							operatorForTransformation, settingsForOperator));
				}
			}
		}
		return transformationResults;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<TransformationResult> doAutomaticTransformation(
			ILogicalOperator logicalPlan) {

		List<TransformationResult> transformationResults = new ArrayList<TransformationResult>();

		// get all strategies which are for an specific logical operator
		Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
		List<Class<? extends ILogicalOperator>> validTypes = ParallelTransformationStrategyRegistry
				.getValidTypes();
		if (validTypes != null) {
			// if we have strategies, we need to add the logical operators to
			// graph visitor
			set.addAll(validTypes);
			CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
					set, true);
			GenericGraphWalker collectWalker = new GenericGraphWalker();
			collectWalker.prefixWalk(logicalPlan, collVisitor);

			// result of graph visitor contains all operators, which have one or
			// more available strategies
			for (ILogicalOperator operatorForTransformation : collVisitor
					.getResult()) {
				// get best strategy
				IParallelTransformationStrategy<? extends ILogicalOperator> bestStrategy = getBestStrategy(operatorForTransformation);
				// create default settings for this transformation
				ParallelOperatorSettings settings = ParallelOperatorSettings
						.createDefaultSettings(bestStrategy,
								globalDegreeOfParallelization, globalBufferSize);
				transformationResults.add(bestStrategy.transform(
						operatorForTransformation, settings));
			}
		}
		return transformationResults;
	}

	private void getOperatorSpecificParameters(QueryBuildConfiguration config) {
		operatorIds = new ArrayList<String>();
		parallelOperatorParameter = config
				.get(ParallelOperatorParameter.class);
		if (parallelOperatorParameter != null) {
			operatorIds.addAll(parallelOperatorParameter.getOperatorIds());
		}
	}

	private void getGlobalParameters(
			List<Pair<String, String>> handlerParameters) {
		if (handlerParameters.size() != PARAMETER_COUNT) {
			throw new IllegalArgumentException();
		} else {
			// Determine parameters
			for (Pair<String, String> pair : handlerParameters) {
				ParallelizationKeywordParameter parameter = ParallelizationKeywordParameter
						.getParameterByName(pair.getE1());
				switch (parameter) {
				case DEGREE_OF_PARALLELIZATION:
					try {
						globalDegreeOfParallelization = Integer.parseInt(pair
								.getE2());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException();
					}
					break;
				case BUFFERSIZE:
					try {
						globalBufferSize = Integer.parseInt(pair.getE2());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException();
					}
					break;
				case OPTIMIZATION:
					optimizationAllowed = Boolean.parseBoolean(pair.getE2());
					break;
				default:
					break;
				}
			}
		}
	}

	private IParallelTransformationStrategy<? extends ILogicalOperator> getBestStrategy(
			ILogicalOperator operatorForTransformation) {
		List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = ParallelTransformationStrategyRegistry
				.getStrategiesForOperator(operatorForTransformation.getClass());
		if (!strategiesForOperator.isEmpty()) {
			// evaluate compatibility of the different
			// strategies
			List<FESortedPair<Integer, IParallelTransformationStrategy<? extends ILogicalOperator>>> strategiesWithCompatibility = new ArrayList<FESortedPair<Integer, IParallelTransformationStrategy<? extends ILogicalOperator>>>();
			for (IParallelTransformationStrategy<? extends ILogicalOperator> strategy : strategiesForOperator) {
				FESortedPair<Integer, IParallelTransformationStrategy<? extends ILogicalOperator>> strategyWithCompatibility = new FESortedPair<Integer, IParallelTransformationStrategy<? extends ILogicalOperator>>(
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
