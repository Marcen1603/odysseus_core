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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;

/**
 * ExecutorInitializeException describes an {@link Exception} which occurs
 * during initializing an {@link IExecutor} .
 * 
 * @author Wolf Bauer
 * 
 */
public class ExecutorInitializeException extends PlanManagementException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	/**
	 * Constructor of ExecutorInitializeException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 */
	public ExecutorInitializeException(String m) {
		super(m);
	}
}
