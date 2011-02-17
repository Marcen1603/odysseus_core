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
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;

/**
 * Factory for creating {@link SingleThreadSchedulerWithStrategy} instances.
 * 
 * @author Wolf Bauer
 * 
 */
public class SingleThreadSchedulerFactoryNoWait extends AbstractSchedulerFactory {

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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory#createScheduler(de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory)
	 */
	@Override
	public IScheduler createScheduler(
			ISchedulingFactory schedulingStrategy) {
		return new SingleThreadSchedulerNoWait(schedulingStrategy);
	}

}
