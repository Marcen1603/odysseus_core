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
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelInterOperatorSetting;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.postoptimization.PostOptimizationHandler;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry.ParallelTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;

/**
 * PreTransformation handler for inter operator paralleization. this handler is
 * used to transform the existing logical plan.
 * 
 * @author ChrisToenjesDeye
 *
 */
public class InterOperatorParallelizationPreTransformationHandler implements
		IPreTransformationHandler {

	public static final String HANDLER_NAME = "InterOperatorParallelizationPreTransformationHandler";
	private final int PARAMETER_COUNT = 4;

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = 0;
	private boolean optimizationAllowed;
	private boolean useThreadedBuffer;

	private ParallelInterOperatorSetting parallelOperatorParameter;
	private List<String> operatorIds = new ArrayList<String>();

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	/**
	 * do the transformation of the logical plan
	 */
	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {

		// get global parameters
		getGlobalParameters(handlerParameters);

		// get individual parameters for each operator
		getOperatorSpecificParameters(config);

		// Get logical plan and copy it, needed for revert if transformation
		// fails
		ILogicalPlan logicalPlan = query.getLogicalPlan();

		if (query.getInitialLogicalPlan() == null) {
			query.setInitialLogicalPlan(logicalPlan.copyPlan());
		}

		// do transformations
		List<TransformationResult> transformationResults = new ArrayList<TransformationResult>();
		transformationResults = doTransformations(query, logicalPlan.getRoot(), transformationResults);

		cleanupResults(transformationResults);

		// if all transformations are done, we try to optimize the
		// transformed plan (remove union, fragment combinations if
		// possible)
		PostOptimizationHandler.doPostOptimization(logicalPlan, query, transformationResults, optimizationAllowed);
	}

	/**
	 * cleans the results of the transformations. Removes the results with a
	 * state = FAILED. this is needed for post optimization
	 * 
	 * @param transformationResults
	 */
	private void cleanupResults(List<TransformationResult> transformationResults) {
		List<TransformationResult> copyOfResults = new ArrayList<TransformationResult>(transformationResults);
		for (TransformationResult transformationResult : copyOfResults) {
			if (transformationResult.getState() == State.FAILED) {
				transformationResults.remove(transformationResult);
			}
		}
	}

	/**
	 * do all transformations of the plan
	 * 
	 * @param query
	 * @param logicalPlan
	 * @param transformationResults
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<TransformationResult> doTransformations(ILogicalQuery query, ILogicalOperator logicalPlan,
			List<TransformationResult> transformationResults) {
		
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(query);
		GenericGraphWalker copyWalker = new GenericGraphWalker();
		copyWalker.prefixWalk(logicalPlan, copyVisitor);
		ILogicalOperator savedPlan = copyVisitor.getResult();

		// do transformation of logical graph
		try {
			if (logicalPlan instanceof TopAO) {
				if (parallelOperatorParameter == null || (parallelOperatorParameter != null
						&& parallelOperatorParameter.getOperatorIds().isEmpty())) {
					// if no operator specific parameters are set, each operator
					// which has an compatible strategy is parallelized
					doAutomaticTransformation(logicalPlan, transformationResults);
				} else if (parallelOperatorParameter != null && !parallelOperatorParameter.getOperatorIds().isEmpty()) {
					doCustomTransformation(logicalPlan, transformationResults);
				}
			}
		} catch (Exception e) {
			// if something went wrong, revert plan and throw exception
			query.setLogicalPlan(new LogicalPlan(savedPlan), true);
			throw e;
		}
		return transformationResults;
	}

	/**
	 * if there exists custom configurations no automatic transformation is
	 * done. Only the operators definied in odysseus script are transformed with
	 * the selected strategy and fragmentation. if the stragety or fragmantation
	 * are not selected, they are detectetd automatically
	 * 
	 * @param logicalPlan
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doCustomTransformation(ILogicalOperator logicalPlan,
			List<TransformationResult> transformationResults) {
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
			ParallelOperatorConfiguration configurationForOperator = parallelOperatorParameter
					.getConfigurationForOperator(operatorId);
			if (operatorForTransformation != null && configurationForOperator != null) {
				// only do transformation if operator is found and settings
				// exists
				if (configurationForOperator.hasParallelizationStrategy()) {
					doCustomTransformationWithSelectedStrategy(transformationResults, operatorForTransformation,
							configurationForOperator);

				} else {
					doCustomTransformationWithBestStrategy(transformationResults, operatorForTransformation,
							configurationForOperator);
				}
			}
		}
	}

	private void doCustomTransformationWithBestStrategy(List<TransformationResult> transformationResults,
			ILogicalOperator operatorForTransformation, ParallelOperatorConfiguration configurationForOperator) {
		// if no user defined strategy is given, get the best
		// strategy for this operator
		IParallelTransformationStrategy<ILogicalOperator> bestStrategy = getPreferredStrategy(
				operatorForTransformation);
		// do post calculations and validations for settings
		configurationForOperator.doPostCalculationsForConfiguration(bestStrategy, globalDegreeOfParallelization,
				globalBufferSize, useThreadedBuffer);
		Class<? extends ILogicalOperator> operatorClass = bestStrategy.getOperatorType();
		if (operatorClass == operatorForTransformation.getClass()) {
			transformationResults
					.add(bestStrategy.getNewInstance().transform(operatorForTransformation, configurationForOperator));
		}
	}

	private void doCustomTransformationWithSelectedStrategy(List<TransformationResult> transformationResults,
			ILogicalOperator operatorForTransformation, ParallelOperatorConfiguration configurationForOperator) {
		// if setting has an user defined Parallel strategy
		IParallelTransformationStrategy<ILogicalOperator> selectedStrategy = ParallelTransformationStrategyRegistry
				.getStrategiesByName(configurationForOperator.getParallelStrategy());
		// do post calculations and validations for settings
		configurationForOperator.doPostCalculationsForConfiguration(selectedStrategy, globalDegreeOfParallelization,
				globalBufferSize, useThreadedBuffer);

		Class<? extends ILogicalOperator> operatorClass = selectedStrategy.getOperatorType();
		if (operatorClass.isAssignableFrom(operatorForTransformation.getClass())) {
			// evaluate compatibility of strategy and operator
			int compatibility = selectedStrategy.evaluateCompatibility(operatorForTransformation);
			if (compatibility > 0) {
				// if compatibility is greater than 0,
				// transformation is possible
				transformationResults.add(selectedStrategy.getNewInstance().transform(operatorForTransformation,
						configurationForOperator));
			} else {
				// if the selected strategy corresponds to the given
				// operator type, but is not compatible
				throw new IllegalArgumentException(
						"Strategy " + selectedStrategy.getName() + " is not compatible with selected operator with id "
								+ operatorForTransformation.getUniqueIdentifier());
			}
		} else {
			// if the selected strategy is valid for this operator
			// type (selected by id), throw exception
			throw new IllegalArgumentException("Strategy with name " + configurationForOperator.getParallelStrategy()
					+ " is not compatible with Operator of type " + operatorForTransformation.getClass());
		}
	}

	/**
	 * do automatic transformations if no custom operator ids and configurations
	 * are set. the operators are automatically detected from the given logical
	 * plan and the preferred strategies and fragmentations are used
	 * 
	 * @param logicalPlan
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doAutomaticTransformation(ILogicalOperator logicalPlan,
			List<TransformationResult> transformationResults) {

		// get all strategies which are for an specific logical operator
		Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
		List<Class<? extends ILogicalOperator>> validTypes = ParallelTransformationStrategyRegistry.getValidTypes();
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
			for (ILogicalOperator operatorForTransformation : collVisitor.getResult()) {
				// get best strategy
				IParallelTransformationStrategy<ILogicalOperator> bestStrategy = getPreferredStrategy(
						operatorForTransformation);
				// create default settings for this transformation
				ParallelOperatorConfiguration configuration = ParallelOperatorConfiguration.createDefaultConfiguration(
						bestStrategy, globalDegreeOfParallelization, globalBufferSize, useThreadedBuffer);
				Class<? extends ILogicalOperator> operatorClass = bestStrategy.getOperatorType();
				if (operatorClass == operatorForTransformation.getClass()) {
					transformationResults
							.add(bestStrategy.getNewInstance().transform(operatorForTransformation, configuration));
				}
			}
		}
	}

	/**
	 * get operator specific configurations
	 * 
	 * @param config
	 */
	private void getOperatorSpecificParameters(QueryBuildConfiguration config) {
		operatorIds = new ArrayList<String>();
		parallelOperatorParameter = config.get(ParallelInterOperatorSetting.class);
		if (parallelOperatorParameter != null) {
			operatorIds.addAll(parallelOperatorParameter.getOperatorIds());
		}
	}

	/**
	 * gets the gobal parameters which are definied in the PARALLELIZATION
	 * keyword
	 * 
	 * @param handlerParameters
	 */
	private void getGlobalParameters(List<Pair<String, String>> handlerParameters) {
		if (handlerParameters.size() != PARAMETER_COUNT) {
			throw new IllegalArgumentException("Number of paramters is invalid");
		} else {
			// Determine parameters
			for (Pair<String, String> pair : handlerParameters) {
				InterOperatorGlobalKeywordParameter parameter = InterOperatorGlobalKeywordParameter
						.getParameterByName(pair.getE1());
				switch (parameter) {
				case DEGREE_OF_PARALLELIZATION:
					try {
						globalDegreeOfParallelization = Integer.parseInt(pair.getE2());
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
				case THREADEDBUFFER:
					useThreadedBuffer = Boolean.parseBoolean(pair.getE2());
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * detects the preferred strategy for the given operator
	 * 
	 * @param operatorForTransformation
	 * @return
	 */
	private IParallelTransformationStrategy<ILogicalOperator> getPreferredStrategy(
			ILogicalOperator operatorForTransformation) {
		List<IParallelTransformationStrategy<ILogicalOperator>> strategiesForOperator = ParallelTransformationStrategyRegistry
				.getStrategiesForOperator(operatorForTransformation.getClass());
		if (!strategiesForOperator.isEmpty()) {
			// evaluate compatibility of the different
			// strategies
			List<FESortedPair<Integer, IParallelTransformationStrategy<ILogicalOperator>>> strategiesWithCompatibility = new ArrayList<FESortedPair<Integer, IParallelTransformationStrategy<ILogicalOperator>>>();
			for (IParallelTransformationStrategy<ILogicalOperator> strategy : strategiesForOperator) {
				Class<ILogicalOperator> operatorType = strategy.getOperatorType();
				if (operatorForTransformation.getClass() == operatorType) {
					FESortedPair<Integer, IParallelTransformationStrategy<ILogicalOperator>> strategyWithCompatibility = new FESortedPair<Integer, IParallelTransformationStrategy<ILogicalOperator>>(
							strategy.evaluateCompatibility(operatorType.cast(operatorForTransformation)), strategy);
					strategiesWithCompatibility.add(strategyWithCompatibility);
				}
			}
			Collections.sort(strategiesWithCompatibility, Collections.reverseOrder());
			if (strategiesWithCompatibility.get(0).getE1() > 0) {
				return strategiesWithCompatibility.get(0).getE2();
			}
		}
		return null;
	}
}
