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
package de.uniol.inf.is.odysseus.ac.reaction;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionQuerySelector;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class SingleQuerySelector implements IAdmissionQuerySelector {

	@Override
	public ImmutableList<IPhysicalQuery> determineQueriesToStop(IAdmissionControl admissionControl, List<IPhysicalQuery> runningQueries) {
		if( runningQueries == null || runningQueries.isEmpty()) {
			return ImmutableList.of();
		}
		IPhysicalQuery highestCostQuery = determineQueryWithHighestCost(admissionControl, runningQueries);	
		return ImmutableList.of(highestCostQuery);
	}

	@Override
	public ImmutableList<IPhysicalQuery> determineQueriesToStart(IAdmissionControl admissionControl, List<IPhysicalQuery> stoppedQueries) {
		if( stoppedQueries == null || stoppedQueries.isEmpty()) {
			return ImmutableList.of();
		}
		
		IPhysicalQuery highestCostQuery = determineQueryWithHighestCost(admissionControl, stoppedQueries);	
		return ImmutableList.of(highestCostQuery);
	}

	private static IPhysicalQuery determineQueryWithHighestCost(IAdmissionControl admissionControl, List<IPhysicalQuery> runningQueries) {
		IPhysicalQuery highestCostQuery = null;
		ICost highestCost = null;		
		for( IPhysicalQuery query : runningQueries ) {
			ICost queryCost = admissionControl.getCost(query);
			if( highestCostQuery == null || queryCost.compareTo(highestCost) > 0) {
				highestCostQuery = query;
				highestCost = queryCost;
			}
		}
		return highestCostQuery;
	}
}
