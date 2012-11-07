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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.ac.StandardPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
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

	@Override
	public List<IPossibleExecution> getPossibleExecutions(IAdmissionControl ac, Map<IPhysicalQuery, ICost> queryCosts, ICost overallCost, ICost maxCost) {

		List<IPossibleExecution> poss = Lists.newArrayList();

		ICost highestCost = null;
		IPhysicalQuery highestQuery = queryWithHighestCost(queryCosts, highestCost);

		if( highestQuery != null ) {
			poss.add(StandardPossibleExecution.stopOneQuery(queryCosts, highestQuery));
		}

		return poss;
	}

	private static IPhysicalQuery queryWithHighestCost(Map<IPhysicalQuery, ICost> queryCosts, ICost highestCost) {
		IPhysicalQuery highestQuery = null;
		for (IPhysicalQuery query : queryCosts.keySet()) {
			ICost cost = queryCosts.get(query);

			if( highestCost == null || cost.compareTo(highestCost) > 0) {
				highestCost = cost;
				highestQuery = query;
			} 
		}
		return highestQuery;
	}
}
