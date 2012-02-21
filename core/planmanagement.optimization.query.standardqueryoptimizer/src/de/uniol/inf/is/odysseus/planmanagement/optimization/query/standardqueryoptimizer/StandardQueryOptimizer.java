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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer
	 * #optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.
	 * IQueryOptimizable, de.uniol.inf.is.odysseus.planmanagement.query.IQuery,
	 * de
	 * .uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration
	 * .OptimizationConfiguration, Set<String>)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalQuery optimizeQuery(ICompiler compiler,
			ILogicalQuery query, OptimizationConfiguration parameters,
			IDataDictionary dd) throws QueryOptimizationException {

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
			physicalQuery = compiler.transform(query, query.getBuildParameter()
					.getTransformationConfiguration(), query.getUser(), dd);

			postTransformationInit(physicalQuery);
		} catch (Throwable e) {
			throw new QueryOptimizationException(
					"Exeception while initialize query.", e);
		}

		return physicalQuery;
	}

	@Override
	public void postTransformationInit(IPhysicalQuery query)
			throws QueryOptimizationException, OpenFailedException {

		addBuffers(query);
	}

	private void addBuffers(IPhysicalQuery query)
			throws QueryOptimizationException {
		IBufferPlacementStrategy bufferPlacementStrategy = query
				.getBuildParameter().getBufferPlacementStrategy();

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
