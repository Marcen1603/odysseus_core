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
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.strategy.RoundRobinPlanScheduling;

/**
 * Factory for creating {@link AbstractSimpleThreadScheduler} instances.
 * 
 * @author Wolf Bauer
 * 
 */
public class STSchedulerFactoryMS_RR_LimitSourcesPThread extends
		AbstractSchedulerFactory {

	final static Logger LOG = LoggerFactory
			.getLogger(STSchedulerFactoryMS_RR_LimitSourcesPThread.class);
	private OdysseusConfiguration config;

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 * 
	 * @param context
	 *            OSGi {@link ComponentContext} provides informations about the
	 *            OSGi environment.
	 */
	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulerFactory#
	 * createScheduler
	 * (de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory
	 * .ISchedulingFactory)
	 */
	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingStrategy,
			int threadCount) {
		IPhysicalQueryScheduling[] scheduling = new RoundRobinPlanScheduling[threadCount];
		for (int i = 0; i < scheduling.length; i++) {
			scheduling[i] = new RoundRobinPlanScheduling();
		}
		long sourcesPerThread = config.getLong("Scheduler.Simplethreaded.SourcesPerThread", -1);
		return new SimpleThreadSchedulerMSLimitSourcesPerThread(
				schedulingStrategy, scheduling, sourcesPerThread, config);
	}
	public void setConfig(OdysseusConfiguration config) {
		this.config = config;
	}

}
