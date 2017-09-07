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
package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoPlanAdaption;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.IElementCloningUpdater;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 *
 * @author Wolf Bauer, Tobias Witt, Marco Grawunder
 *
 */
public class StandardOptimizer extends AbstractOptimizer {

	static private final ISession superUser =  SessionManagement.instance.loginSuperUser(null);

	protected static Logger LOG = LoggerFactory.getLogger(StandardOptimizer.class);;

	public static final long MONITORING_PERIOD = 30000;

	public StandardOptimizer() {
	}

	@Override
	public List<IPhysicalQuery> optimize(IServerExecutor executor, IExecutionPlan currentExecPlan,  List<ILogicalQuery> queries,
			OptimizationConfiguration parameter, IDataDictionaryWritable dd)
			{
		List<IPhysicalQuery> optimizedQueries = new ArrayList<IPhysicalQuery>();
		if (!queries.isEmpty()) {
			ParameterDoPlanAdaption adaption = parameter.getParameterDoPlanAdaption();
			for (ILogicalQuery query : queries) {
				IPhysicalQuery optimized = this.queryOptimizer.optimizeQuery(executor, query, parameter, dd);
				doPostOptimizationActions(optimized, parameter, currentExecPlan);
				optimizedQueries.add(optimized);
				// set the adaption parameter for each query
				if(optimized != null && adaption != null && adaption == ParameterDoPlanAdaption.TRUE) {
					optimized.setParameter("noAdaption", false);
				} else if(optimized != null) {
					optimized.setParameter("noAdaption", true);
				}
			}

			IQuerySharingOptimizer qso = getQuerySharingOptimizer();
			if (qso != null
					&& parameter.getParameterPerformQuerySharing() != null
					&& parameter.getParameterPerformQuerySharing().getValue()) {
				qso.applyQuerySharing(currentExecPlan.getQueries(superUser), optimizedQueries, parameter, dd);
			}

			// Add new queries to the execution plan and optimize new plan
			currentExecPlan.addQueries(optimizedQueries, superUser);
		}
		IElementCloningUpdater equ = getElementCloningUpdater();
		if (equ != null){
			equ.updateCloning(currentExecPlan);
		}
		return optimizedQueries;
	}

	@Override
	public void beforeQueryRemove(IPhysicalQuery removedQuery,
			IExecutionPlan executionPlan, OptimizationConfiguration parameter,
			IDataDictionary dd)  {
	}

	@Override
	public void handleFinishedMigration(IPhysicalQuery query) {
		// TODO Auto-generated method stub

	}

}
