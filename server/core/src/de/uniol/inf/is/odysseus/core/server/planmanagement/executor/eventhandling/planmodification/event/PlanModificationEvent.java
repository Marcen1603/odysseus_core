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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;

/**
 * PlanModificationEvent is an event that occurs during modification of
 * the registered execution plan. It refers to an {@link IExecutionPlan}.
 * 
 * @author Wolf Bauer
 *
 */
public class PlanModificationEvent extends
		AbstractPlanModificationEvent<IExecutionPlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1752702553996685067L;
	/**
	 * ID for an event after reoptimizing the execution plan.
	 */
	public static String PLAN_REOPTIMIZE = "PLAN_REOPTIMIZE";

	/**
	 * Constructor of PlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            {@link IExecutionPlan} to which this event refers.
	 */
	public PlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			IExecutionPlan value) {
		super(sender, eventType, value);
	}
}
