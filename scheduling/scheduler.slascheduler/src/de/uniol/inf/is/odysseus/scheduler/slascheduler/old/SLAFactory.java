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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.old;

import java.util.Dictionary;

import javax.management.RuntimeErrorException;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy.AbstractSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy.PrioSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy.TimebasedSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.old.strategy.AbstractSLAScheduler.PrioCalcMethod;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;


public class SLAFactory extends AbstractSchedulerFactory{

	private PrioCalcMethod method;
	String kind = "";
	
	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
		@SuppressWarnings("rawtypes")
		Dictionary properties = context.getProperties();
		this.method = PrioCalcMethod.valueOf((String)properties.get("calcMethod"));
		this.kind = (String)properties.get("kind");
		super.setName(properties);
	}

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		if ("time".equals(kind)){
			return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new TimebasedSLAScheduler(method));
		}
		if ("prio".equals(kind)){
			return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new PrioSLAScheduler(method));			
		}
		throw new RuntimeException("Scheduler "+kind+" "+method+" could not be created ");
	}


}
