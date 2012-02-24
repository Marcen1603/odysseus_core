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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;


/**
 * Superclass for all exceptions which can occur during a call to open
 * on a {@link ISource} or {@link ISink}.
 * @author Jonas Jacobi
 */
public class OpenFailedException extends PlanManagementException {

	private static final long serialVersionUID = 762295616036628102L;
	
	public OpenFailedException(String message) {
		super(message);
	}
	
	public OpenFailedException(Exception e){
		super(e);
	}

}
