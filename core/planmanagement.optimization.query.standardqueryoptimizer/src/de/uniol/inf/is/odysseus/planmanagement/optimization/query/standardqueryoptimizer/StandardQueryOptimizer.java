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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

/**
 * QueryRestructOptimizer is the standard query optimizer for odysseus. This
 * optimizer creates the physical plan for queries. Based on
 * {@link OptimizationConfiguration} a Rewrite is used and buffer are placed by
 * an {@link IBufferPlacementStrategy}.
 * 
 * @author Wolf Bauer, Tobias Witt
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

	// /* (non-Javadoc)
	// * @see
	// de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable,
	// de.uniol.inf.is.odysseus.planmanagement.query.IQuery,
	// de.uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration.OptimizationConfiguration)
	// */
	// @Override
	// public void optimizeQuery(IQueryOptimizable sender, IQuery query,
	// OptimizationConfiguration parameters) throws QueryOptimizationException {
	// optimizeQuery(sender, query, parameters, null);
	// }

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
	public IPhysicalQuery optimizeQuery(IQueryOptimizable sender, ILogicalQuery query,
			OptimizationConfiguration parameters, IDataDictionary dd)
			throws QueryOptimizationException {
		ICompiler compiler = null;
		IPhysicalQuery physicalQuery = null;

		try {
			compiler = sender.getCompiler();
		} catch (Exception e) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

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

	@Override
	public Map<IPhysicalOperator, ILogicalOperator> createAlternativePlans(
			IQueryOptimizable sender, ILogicalQuery query,
			OptimizationConfiguration parameters)
			throws QueryOptimizationException {

		throw new RuntimeException(
				"Does not work at the moment. At Marco: Kannst du das bitte so anpassen, "
						+ "dass jetzt berücksichtigt wird, dass bei der Transformation jetzt alle Roots (können jetzt ja"
						+ "auch mehrere sein) eines physischen Anfrageplans zurückgeliefert werden.");
		// ICompiler compiler = sender.getCompiler();
		// if (compiler == null) {
		// throw new QueryOptimizationException("Compiler is not loaded.");
		// }
		//
		// ParameterDoRestruct restruct = parameters.getParameterDoRestruct();
		// if (restruct == null || restruct != ParameterDoRestruct.TRUE) {
		// // no restruct allowed
		// return new HashMap<IPhysicalOperator, ILogicalOperator>(0);
		// }
		//
		// // create working copy of plan
		// CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new
		// CopyLogicalGraphVisitor<ILogicalOperator>();
		// AbstractGraphWalker walker = new AbstractGraphWalker();
		// walker.prefixWalk(query.getLogicalPlan(), copyVisitor);
		// ILogicalOperator logicalPlanCopy = copyVisitor.getResult();
		//
		// // create logical alternatives
		// List<ILogicalOperator> logicalAlternatives =
		// compiler.createAlternativePlans(logicalPlanCopy,
		// rulesToUse);
		//
		// try {
		// Map<IPhysicalOperator,ILogicalOperator> alternatives = new
		// HashMap<IPhysicalOperator,ILogicalOperator>();
		//
		// for (ILogicalOperator logicalPlan : logicalAlternatives) {
		// // create alternative physical plans
		// List<IPhysicalOperator> physicalPlans =
		// compiler.transformWithAlternatives(logicalPlan,
		// query.getBuildParameter().getTransformationConfiguration());
		//
		// for (IPhysicalOperator physicalPlan : physicalPlans) {
		// addBuffers(query, physicalPlan);
		//
		// // put last sink on top
		// IPhysicalOperator oldRoot = query.getRoot();
		// if (oldRoot.isSource()) {
		// throw new QueryOptimizationException(
		// "Migration needs a sink only as operator root.");
		// }
		// IPhysicalOperator newRoot = oldRoot.clone();
		// ((ISink)newRoot).subscribeToSource(physicalPlan, 0, 0,
		// physicalPlan.getOutputSchema());
		// physicalPlan = newRoot;
		//
		// alternatives.put(physicalPlan, logicalPlan);
		// }
		// }
		//
		// return alternatives;
		//
		// } catch (Throwable e) {
		// throw new QueryOptimizationException(
		// "Exeception while initialize query.", e);
		// }
	}

}
