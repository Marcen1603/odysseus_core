/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.planmanagement.optimization.query.standardqueryoptimizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoPlanGeneration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * QueryRestructOptimizer is the standard query optimizer for odysseus. This
 * optimizer creates the physical plan for queries. Based on
 * {@link OptimizationConfiguration} a Rewrite is used and buffer are placed by
 * an {@link IBufferPlacementStrategy}.
 * 
 * @author Wolf Bauer, Tobias Witt, Marco Grawunder
 * 
 */
public class StandardQueryOptimizer implements IQueryOptimizer {

	protected static final Logger LOG = LoggerFactory.getLogger(StandardQueryOptimizer.class);;	

	final private Map<IPhysicalQuery, QueryBuildConfiguration> buildConfig = new HashMap<IPhysicalQuery, QueryBuildConfiguration>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer
	 * #optimizeQuery(de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.
	 * IQueryOptimizable, de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery,
	 * de
	 * .uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration
	 * .OptimizationConfiguration, Set<String>)
	 */
	@Override
	public IPhysicalQuery optimizeQuery(IServerExecutor executor,
			ILogicalQuery query, OptimizationConfiguration parameters,
			IDataDictionary dd)  {

		ICompiler compiler = executor.getCompiler();
		QueryBuildConfiguration cb = executor.getBuildConfigForQuery(query);

		if (query == null) {
			throw new QueryOptimizationException("Query has no logical plan!");
		}

		IPhysicalQuery physicalQuery = null;

		ParameterDoRewrite restruct = parameters.getParameterDoRewrite();
		
		ParameterDoPlanGeneration planGeneration = parameters.getParameterDoPlanGeneration();

		// if a logical rewrite should be processed.
		ILogicalPlan originalPlan = query.getLogicalPlan();
		ILogicalPlan copiedPlan = originalPlan.copyPlan();
		
		boolean createAlternativePlans = copiedPlan != null && planGeneration != null && query.getAlternativeLogicalPlans().isEmpty()
				&& planGeneration == ParameterDoPlanGeneration.TRUE;
		if(createAlternativePlans) {
		
			if( copiedPlan.findOpsFromType(JoinAO.class).isEmpty()) {
				LOG.debug("Query can not be adapted because it does not contain any joins");
				// query can not be adapted because there are no alternative plans
				query.setParameter("noAdaption", true);
			} else {
				LOG.debug("Creating alternative logical plans");
				PlanGenerationConfiguration generationConfig = parameters
						.getPlanGenerationConfiguration();
				List<ILogicalPlan> alternativePlans = compiler
						.generatePlans(copiedPlan, generationConfig, query);
				query.setAlternativeLogicalPlans(alternativePlans);
				// this should be the best
				if (!alternativePlans.isEmpty()) {
					LOG.debug("generatePlans has returned {} plans",
							alternativePlans.size());
					copiedPlan = alternativePlans.get(0);
				} else {
					LOG.warn(
							"generatePlans has returned an empty list.");
				}
			}
		}
		
		boolean queryShouldBeRewritten = copiedPlan != null && restruct != null
				&& restruct == ParameterDoRewrite.TRUE;
		if (queryShouldBeRewritten) {
			LOG.debug("Start rewriting of query...");
			RewriteConfiguration rewriteConfig = parameters.getRewriteConfiguration();
			rewriteConfig.setQueryBuildConfiguration(cb);
			ILogicalPlan newPlan = compiler.rewritePlan(copiedPlan,rewriteConfig, query.getUser(), dd);
			LOG.debug("Rewriting of query done.");
			// set new logical plan.
			query.setLogicalPlan(newPlan, false);
			// do the same for all alternative plans if any
			// TODO: nochmal von marco gegenchecken lassen.
			if(createAlternativePlans) {
				List<ILogicalPlan> alternativePlans = query.getAlternativeLogicalPlans();
				alternativePlans.set(0, query.getLogicalPlan());
				for(int i = 1; i < alternativePlans.size(); i++) {
					ILogicalPlan alternativePlan = alternativePlans.get(i);
					alternativePlans.set(i, compiler.rewritePlan(alternativePlan, rewriteConfig, query.getUser(), dd));
				}
			}
		}else{
			LOG.debug("Skip rewriting, because it is deactivated.");
		}

		try {
			// create the physical plan
			LOG.debug("Starting transformation for the logical query...");
			physicalQuery = compiler.transform(query,
					cb.getTransformationConfiguration(), query.getUser(), dd);

			LOG.debug("Transformation into a physical query done.");
			buildConfig.put(physicalQuery, cb);

			postTransformationInit(physicalQuery);
			buildConfig.remove(physicalQuery);
		} catch (Throwable e) {
			throw e;
		}

		return physicalQuery;
	}

	@Override
	public void postTransformationInit(IPhysicalQuery query)
			throws  OpenFailedException {
		QueryBuildConfiguration bc = buildConfig.get(query);
		IBufferPlacementStrategy strat = bc.getBufferPlacementStrategy();
		if (strat != null) {
			addBuffers(query, strat);
		}
	}

	private static void addBuffers(IPhysicalQuery query,
			IBufferPlacementStrategy bufferPlacementStrategy)
		 {

		// add Buffer
		if (bufferPlacementStrategy != null) {
			try {
				LOG.debug(
						"Adding Buffers with Strategy "
								+ bufferPlacementStrategy.getName());
				bufferPlacementStrategy.addBuffers(query);
			} catch (Exception e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}

}
