/** Copyright [2011] [The Odysseus Team]
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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;

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

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(StandardQueryOptimizer.class);
		}
		return _logger;
	}

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
	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalQuery optimizeQuery(IServerExecutor executor,
			ILogicalQuery query, OptimizationConfiguration parameters,
			IDataDictionary dd) throws QueryOptimizationException {

		ICompiler compiler = executor.getCompiler();
		QueryBuildConfiguration cb = executor.getBuildConfigForQuery(query);

		if (query == null) {
			throw new QueryOptimizationException("Query has no logical plan!");
		}

		IPhysicalQuery physicalQuery = null;

		ParameterDoRewrite restruct = parameters.getParameterDoRewrite();

		// if a logical rewrite should be processed.
		ILogicalOperator originalPlan = query.getLogicalPlan();

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
				query);
		@SuppressWarnings("rawtypes")
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(originalPlan, copyVisitor);
		ILogicalOperator copiedPlan = copyVisitor.getResult();

		boolean queryShouldBeRewritten = copiedPlan != null && restruct != null
				&& restruct == ParameterDoRewrite.TRUE;
		if (queryShouldBeRewritten) {
			ILogicalOperator newPlan = compiler.rewritePlan(copiedPlan,
					parameters.getRewriteConfiguration());
			// set new logical plan.
			query.setLogicalPlan(newPlan, false);
		}

		try {
			// create the physical plan

			physicalQuery = compiler.transform(query,
					cb.getTransformationConfiguration(), query.getUser(), dd);

			buildConfig.put(physicalQuery, cb);

			postTransformationInit(physicalQuery);
			buildConfig.remove(physicalQuery);
		} catch (Throwable e) {
			throw new QueryOptimizationException(
					"Exeception while initialize query.", e);
		}

		return physicalQuery;
	}

	@Override
	public void postTransformationInit(IPhysicalQuery query)
			throws QueryOptimizationException, OpenFailedException {
		QueryBuildConfiguration bc = buildConfig.get(query);
		IBufferPlacementStrategy strat = bc.getBufferPlacementStrategy();
		if (strat != null) {
			addBuffers(query, strat);
		}
	}

	private static void addBuffers(IPhysicalQuery query,
			IBufferPlacementStrategy bufferPlacementStrategy)
			throws QueryOptimizationException {

		// add Buffer
		if (bufferPlacementStrategy != null) {
			try {
				getLogger().debug(
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
