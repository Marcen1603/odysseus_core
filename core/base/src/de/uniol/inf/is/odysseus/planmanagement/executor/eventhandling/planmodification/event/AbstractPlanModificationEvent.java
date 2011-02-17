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
package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.event.AbstractEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

/**
 * AbstractPlanModificationEvent is an Event that occurs during modification of
 * the registered execution plan. This is the base class for concrete
 * modification events.
 * 
 * An event consists of a sender, an id and a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <ValueType>
 *            Type of the stored value which refers to the modification.
 */
public abstract class AbstractPlanModificationEvent<ValueType> extends
		AbstractEvent<IPlanManager, ValueType> {

	/**
	 * Constructor of AbstractPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            value which refers to the modification.
	 */
	public AbstractPlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			ValueType value) {
		super(sender, eventType, value);
	}
}
