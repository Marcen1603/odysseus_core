/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.scheduler.priorityscheduler;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy.SimpleDynamicPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SimpleThreadScheduler;

public class SimpleDynamicSchedulerFactory extends AbstractSchedulerFactory {

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		
		int executorThreadsCount = (int) OdysseusConfiguration
				.getLong("scheduler_simpleThreadScheduler_executorThreadsCount", 1);
		IPartialPlanScheduling[] scheduling = new SimpleDynamicPriorityPlanScheduling[executorThreadsCount];
		for(int i=0;i<scheduling.length;i++){
			scheduling[i] = new SimpleDynamicPriorityPlanScheduling(0);
		}

		
		return new SimpleThreadScheduler(schedulingFactoring, scheduling);
	}

}
