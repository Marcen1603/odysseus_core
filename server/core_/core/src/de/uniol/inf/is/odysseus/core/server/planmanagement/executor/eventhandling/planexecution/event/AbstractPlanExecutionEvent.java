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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.event.AbstractEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanScheduling;

/**
 * AbstractPlanExecutionEvent is an Event that occurs during execution of
 * the registered execution plan. This is the base class for concrete
 * execution events.
 * 
 * An event consists of a sender, an id and a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <ValueType>
 *            Type of the stored value which refers to the execution change.
 */
public abstract class AbstractPlanExecutionEvent<ValueType> extends
		AbstractEvent<IPlanScheduling, ValueType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7796234610202092460L;

	/**
	 * Constructor of AbstractPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            value which refers to the execution change.
	 */
	public AbstractPlanExecutionEvent(IPlanScheduling sender, IEventType eventType, ValueType value) {
		super(sender, eventType, value);
	}
}
