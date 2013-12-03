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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

/**
 * Interface for considering the effort of query sharing
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface IQuerySharing {
	/**
	 * @return the best plan with respect to cost-based priority and query
	 *         sharing effort
	 */
	public IScheduling getNextPlan();

	/**
	 * sets the priority for the given scheduling unit
	 * @param plan the scheduling unit
	 * @param priority the priority of the given scheduling unit
	 */
	public void setPriority(IScheduling plan, double priority);

	/**
	 * updates the data structure storing information about the effort of query
	 * sharing
	 * @param plans list of all plans managed by the scheduler
	 */
	public void refreshEffortTable(List<IScheduling> plans);

}
