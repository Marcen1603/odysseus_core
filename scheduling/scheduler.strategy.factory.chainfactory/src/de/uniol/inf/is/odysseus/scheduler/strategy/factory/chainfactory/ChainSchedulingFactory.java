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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory.impl.ChainScheduling;

public class ChainSchedulingFactory extends AbstractSchedulingFactory {
	

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		return new ChainScheduling(plan);
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
	
}
