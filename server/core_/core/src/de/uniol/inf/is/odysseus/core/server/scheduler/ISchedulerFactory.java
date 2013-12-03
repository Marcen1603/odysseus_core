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

import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Describes an object which creates specific {@link IScheduler}. Each
 * {@link IScheduler} is initialized with a {@link ISchedulingFactory}.
 * A factory can be identified by a name. This name should be unique.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulerFactory {
	/**
	 * Creates a specific {@link IScheduler}. Each {@link IScheduler} is
	 * initialized with a {@link ISchedulingFactory}.
	 * 
	 * @param schedulingFactory
	 *            {@link ISchedulingFactory} which will be used for
	 *            creating new {@link IScheduler}.
	 * @return A new specific {@link IScheduler} instance.
	 */
	public IScheduler createScheduler(
			ISchedulingFactory schedulingFactoring, int threadCount);

	/**
	 * Returns a name for this factory. This name should be unique.
	 * 
	 * @return Name of this factory.
	 */
	public String getName();
}
