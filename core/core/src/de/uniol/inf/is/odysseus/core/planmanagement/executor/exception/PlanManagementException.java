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
package de.uniol.inf.is.odysseus.core.planmanagement.executor.exception;

/**
 * PlanManagementException describes an {@link Exception} which occurs during
 * plan management.
 * 
 * @author Wolf Bauer
 * 
 */
public class PlanManagementException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3420886636303978500L;

	/**
	 * Constructor of PlanManagementException.
	 * 
	 * @param e original {@link Exception} which occurs. 
	 */
	public PlanManagementException(Exception e) {
		this.initCause(e);
		this.setStackTrace(e.getStackTrace());
	}

	/**
	 * Constructor of PlanManagementException.
	 * 
	 * @param details detailed exception message.
	 */
	protected PlanManagementException(String details) {
		super(details);
	}

	public PlanManagementException(String message, Throwable e) {
		super(message,e);
	}
}
