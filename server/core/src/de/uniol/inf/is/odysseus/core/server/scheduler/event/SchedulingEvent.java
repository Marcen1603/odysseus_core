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
package de.uniol.inf.is.odysseus.core.server.scheduler.event;

import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.event.AbstractEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;

public class SchedulingEvent extends AbstractEvent<IScheduler, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3730813628316861446L;

	public enum SchedulingEventType implements IEventType {
		SCHEDULING_STARTED, SCHEDULING_STOPPED
	}
	
	public SchedulingEvent(IScheduler sender, IEventType eventType,
			String value) {
		super(sender, eventType, value);
	}

}
