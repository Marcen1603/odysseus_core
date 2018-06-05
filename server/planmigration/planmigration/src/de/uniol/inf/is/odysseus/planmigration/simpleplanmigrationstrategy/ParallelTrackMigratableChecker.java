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
package de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingTimeWindowTIPO;

/**
 * Checks if a given plan is migratable with the SimplePlanMigrationStrategy
 * 
 * @author Merlin Wasmann
 * 
 */
public class ParallelTrackMigratableChecker {

	public static boolean isMigratable(IPhysicalQuery query) {
		Set<IPhysicalOperator> operators = getOperators(query);
		if (containsStatefulOperators(operators)) {
			Set<IPhysicalOperator> windows = getWindows(operators);
			return containsOnlyTimeWindows(windows);
		}
		return true;
	}

	private static Set<IPhysicalOperator> getOperators(IPhysicalQuery query) {
		return query.getAllOperators();
	}

	private static boolean containsStatefulOperators(
			Set<IPhysicalOperator> operators) {
		for (IPhysicalOperator op : operators) {
			if (op instanceof IStatefulOperator) {
				return true;
			}
		}
		return false;
	}

	private static Set<IPhysicalOperator> getWindows(
			Set<IPhysicalOperator> operators) {
		Set<IPhysicalOperator> windows = new HashSet<IPhysicalOperator>();
		for (IPhysicalOperator op : operators) {
			if (op instanceof AbstractWindowTIPO) {
				windows.add(op);
			}
		}
		return windows;
	}
	
	private static boolean containsOnlyTimeWindows(Set<IPhysicalOperator> windows) {
		Set<IPhysicalOperator> timeWindows = new HashSet<IPhysicalOperator>();
		for(IPhysicalOperator window : windows) {
			if(window instanceof AbstractWindowTIPO) {
				if(window instanceof SlidingAdvanceTimeWindowTIPO || window instanceof SlidingTimeWindowTIPO) {
					timeWindows.add(window);
				}
			}
		}
		windows.removeAll(timeWindows);
		return windows.isEmpty();
	}

}
