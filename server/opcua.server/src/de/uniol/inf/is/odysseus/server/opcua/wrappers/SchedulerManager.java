/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.NotCacheable;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.server.opcua.binding.ExecutorServiceBinding;

/**
 * The scheduler manager of Odysseus.
 */
@Description("The scheduler manager for Odysseus")
public class SchedulerManager {

	/**
	 * Gets the scheduling strategies.
	 *
	 * @return the scheduling strategies
	 */
	@NotCacheable
	@Description("The registered scheduling strategies")
	public String[] getSchedulingStrategies() {
		return getSchedulerManager().getSchedulingStrategy().toArray(new String[0]);
	}

	/**
	 * Gets the schedulers.
	 *
	 * @return the schedulers
	 */
	@NotCacheable
	@Description("The registered schedulers")
	public String[] getSchedulers() {
		return getSchedulerManager().getScheduler().toArray(new String[0]);
	}

	/**
	 * Gets the scheduler manager.
	 *
	 * @return the scheduler manager
	 */
	private ISchedulerManager getSchedulerManager() {
		return ((IServerExecutor) ExecutorServiceBinding.getExecutor()).getSchedulerManager();
	}
}