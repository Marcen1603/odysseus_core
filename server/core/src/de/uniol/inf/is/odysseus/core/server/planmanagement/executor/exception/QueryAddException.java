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
 * QueryAddException describes an {@link Exception} which occurs during
 * adding a query to odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryAddException extends PlanManagementException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	
	/**
	 * Constructor of QueryAddException.
	 * 
	 * @param exception Exception {@link Exception} which occurs. 
	 */
	public QueryAddException(Exception exception) {
		super(exception);
	}

	/**
	 * Constructor of QueryAddException.
	 * 
	 * @param error detailed exception message.
	 */
	public QueryAddException(String error) {
		super(
				"Error while adding query." + (error != null
						&& error.length() > 0 ? "Additional Info:"
						+ AppEnv.LINE_SEPARATOR + error : ""));
		
	}
}
