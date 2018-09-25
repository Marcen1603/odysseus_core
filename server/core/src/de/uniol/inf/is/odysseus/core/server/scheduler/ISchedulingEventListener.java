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
package de.uniol.inf.is.odysseus.core.server.scheduler;

import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

/**
 * Interface to send scheduling events
 * @author Marco Grawunder
 *
 */

public interface ISchedulingEventListener {

	/**
	 * Called if the scheduling for sched is started
	 * @param sched
	 */
	public void scheddulingPossible(IScheduling sched);
	public void nothingToSchedule(IScheduling sched);	
}
