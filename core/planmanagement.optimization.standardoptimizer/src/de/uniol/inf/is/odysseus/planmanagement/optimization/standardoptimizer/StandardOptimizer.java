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
package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * 
 * @author Wolf Bauer, Tobias Witt, Marco Grawunder
 * 
 */
public class StandardOptimizer extends AbstractOptimizer {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(StandardOptimizer.class);
		}
		return _logger;
	}

	public static final long MONITORING_PERIOD = 30000;

	public StandardOptimizer() {
	}

	@Override
	public List<IPhysicalQuery> optimize(ICompiler compiler, IExecutionPlan currentExecPlan,  List<ILogicalQuery> queries,
			OptimizationConfiguration parameter, IDataDictionary dd)
			throws QueryOptimizationException {
		List<IPhysicalQuery> optimizedQueries = new ArrayList<IPhysicalQuery>();
		if (!queries.isEmpty()) {
			for (ILogicalQuery query : queries) {
				IPhysicalQuery optimized = this.queryOptimizer.optimizeQuery(compiler, query, parameter, dd);
				doPostOptimizationActions(optimized, parameter);
				optimizedQueries.add(optimized);
			}

			IQuerySharingOptimizer qso = getQuerySharingOptimizer();
			if (qso != null
					&& parameter.getParameterPerformQuerySharing() != null
					&& parameter.getParameterPerformQuerySharing().getValue()) {
				qso.applyQuerySharing(optimizedQueries, parameter);
			}
			// Add new queries to the execution plan and optimize new plan
			currentExecPlan.addQueries(optimizedQueries);
			this.planOptimizer.optimizePlan(parameter, currentExecPlan, dd);

		}
		return optimizedQueries;
	}

	@Override
	public void beforeQueryRemove(IPhysicalQuery removedQuery,
			IExecutionPlan executionPlan, OptimizationConfiguration parameter,
			IDataDictionary dd) throws QueryOptimizationException {
		this.planOptimizer.optimizePlan(parameter, executionPlan, dd);
	}



}
