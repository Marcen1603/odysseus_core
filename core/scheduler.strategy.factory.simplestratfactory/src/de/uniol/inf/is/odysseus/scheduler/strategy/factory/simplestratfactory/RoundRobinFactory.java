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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.RoundRobinScheduling;


public class RoundRobinFactory extends AbstractSchedulingFactory {

	protected Logger logger;
	
	public RoundRobinFactory() {
		this.logger = LoggerFactory.getLogger(RoundRobinFactory.class);
		//logger.info("Round Robin Factory created");
	}
	
	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		return new RoundRobinScheduling(plan);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
