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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Configuration;

/**
 * ExecutionConfiguration stores the configuration of an {@link IExecutor}.
 * 
 * @author Wolf Bauer
 *
 */
public class ExecutionConfiguration extends Configuration<IExecutionSetting<?>> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -381716411535330966L;

	/**
	 * Creates a new ExecutionConfiguration based on {@link IExecutionSetting} entries.
	 * 
	 * @param entries
	 */
	public ExecutionConfiguration(IExecutionSetting<?>... entries) {
		super(entries);
		
	}

}
