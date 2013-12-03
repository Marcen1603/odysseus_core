/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for distributors of {@link SLAViolationEvent}. Required to decouple
 * event generation and event handling, so no processing time of a selected 
 * partial plan is wasted by event lsisteners.
 * 
 * @author Thomas Vogelgesang
 *
 */
public interface ISLAViolationEventDistributor {
	
	/**
	 * adds the given event to the event queue 
	 * @param event the event to buffer
	 */
	public void queueSLAViolationEvent(SLAViolationEvent event);

}
