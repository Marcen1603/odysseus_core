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

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * MultiQueryPlanModificationEvent is an event that occurs during modification of
 * the registered execution plan. It refers to a list of {@link IQuery}s.
 * 
 * @author Wolf Bauer
 *
 */
public class MultiQueryPlanModificationEvent extends
		AbstractPlanModificationEvent<ArrayList<IQuery>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2252621233834834416L;
	/**
	 * ID for an event after adding queries.
	 */
	public static String QUERIES_ADD = "QUERIES_ADD";
	
	/**
	 * Constructor of MultiQueryPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            List of {@link IQuery} to which this event refers.
	 */
	public MultiQueryPlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			ArrayList<IQuery> value) {
		super(sender,  eventType, value);
	}
}
