/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.ac.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.ac.StandardPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Standardimplementierung von {@link IPossibleExecutionGenerator}. Erstellt aus
 * der Menge aller Anfragen eine Liste möglicher Anfragekombinationen nach
 * Brute-Force.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardPossibleExecutionGenerator implements IPossibleExecutionGenerator {

	private static Logger LOGGER = LoggerFactory.getLogger(StandardPossibleExecutionGenerator.class);

	@Override
	public List<IPossibleExecution> getPossibleExecutions(IAdmissionControl ac, Map<IPhysicalQuery, ICost> queryCosts, ICost maxCost) {

		List<IPossibleExecution> poss = Lists.newArrayList();

		// first solution: all queries running!
		ICost costSum = sum(queryCosts.values());

		int cmp = costSum.compareTo(maxCost);
		if (cmp == -1 || cmp == 0) {
			poss.add(new StandardPossibleExecution(queryCosts.keySet(), new ArrayList<IPhysicalQuery>(), costSum));
			LOGGER.debug("Possible Execution: execute all queries");
		}

		if (poss.isEmpty()) {
			StandardAC stdAC = (StandardAC) ac;
			ICostModel cm = stdAC.getSelectedCostModelInstance();
			
			List<IPhysicalQuery> runningQueries = Lists.newArrayList();
			List<IPhysicalQuery> stoppingQueries = Lists.newArrayList();

			ICost actSum = cm.getZeroCost();
			for (IPhysicalQuery query : queryCosts.keySet()) {
				ICost cost = queryCosts.get(query);
				ICost newSum = actSum.merge(cost);
				if (newSum.compareTo(maxCost) < 0) {
					runningQueries.add(query);
					actSum = newSum;
				} else {
					stoppingQueries.add(query);
				}
			}

			poss.add(new StandardPossibleExecution(runningQueries, stoppingQueries, actSum));
		}

		return poss;
	}

	private static ICost sum(Iterable<ICost> queryCosts) {
		ICost costSum = null;
		for (ICost cost : queryCosts) {
			if (costSum == null) {
				costSum = cost;
			} else {
				costSum = costSum.merge(cost);
			}
		}
		return costSum;
	}
}
