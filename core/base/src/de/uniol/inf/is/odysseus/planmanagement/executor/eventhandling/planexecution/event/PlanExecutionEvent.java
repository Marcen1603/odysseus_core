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
package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

/**
 * PlanExecutionEvent is an event that occurs during changes of plan execution.
 * It refers to no value.
 * 
 * @author Wolf Bauer
 * 
 */
public class PlanExecutionEvent extends AbstractPlanExecutionEvent<PlanExecutionEventType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3096579196303382242L;

	/**
	 * Constructor of PlanExecutionEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 */
	public PlanExecutionEvent(IPlanScheduling sender, PlanExecutionEventType eventType) {
		super(sender, eventType, eventType);
	}
}
