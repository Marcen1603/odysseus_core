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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;

/**
 * SchedulerException stellt einen Fehler bezogen auf das Scheduling dar.
 * @author wolf
 *
 */
public class SchedulerException extends PlanManagementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7047933396969916561L;
	
	/**
	 * Standard-Konstruktor
	 */
	public SchedulerException() {
		super("SchedulerException: Scheduler plugin is not loaded.");
	}

	/**
	 * Konstruktor. Die Information
	 * @param exception
	 */
	public SchedulerException(Exception exception) {
		super(exception);
	}

	/**
	 * Constructor of SchedulerException.
	 * 
	 * @param details detailed exception message.
	 */
	public SchedulerException(String details) {
		super("SchedulerException:Scheduler plugin is not loaded. (" + AppEnv.LINE_SEPARATOR + details + ")");
	}
}
