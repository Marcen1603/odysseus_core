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
package de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

/**
 * Describes a Factory for creating specific {@link IScheduling}. Used
 * for OSGi services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulingFactory {
	/**
	 * Create a specific {@link IScheduling}. Initialize it with a
	 * physical plan which should be scheduled with a priority.
	 * 
	 * @param plan
	 *            Physical plan which should be scheduled.
	 * @param priority
	 *            Priority with which physical plan which should be scheduled.
	 * @return New created a specific {@link IScheduling}.
	 */
	public IScheduling create(IPartialPlan plan, long priority);

	/**
	 * ID of this factory. Should be unique.
	 * 
	 * @return ID of this factory. Should be unique.
	 */
	public String getName();
}
