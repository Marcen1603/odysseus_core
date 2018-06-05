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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;

/**
 * IPlanModificationListener describes an object which will be informed if the
 * registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanModificationListener {
	/**
	 * The method is called if the registered plan is modified. An
	 * IPlanModificationListener can react to concrete
	 * {@link AbstractPlanModificationEvent} 
	 * 
	 * @param eventArgs {@link AbstractPlanModificationEvent} describes the event that occurs.
	 */
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs);
}
