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
package de.uniol.inf.is.odysseus.ruleengine.system;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.usermanagement.User;

public abstract class AbstractWorkingEnvironment<T> implements IWorkingEnvironment<T> {
	
	private T configuration = null;
	private WorkingMemory workingMemory;
	private IRuleFlow ruleFlow;
	
	public AbstractWorkingEnvironment(T config, IRuleFlow ruleflow, User caller, IDataDictionary dd){
		this.configuration = config;
		this.ruleFlow = ruleflow;
		this.workingMemory = new WorkingMemory(this, caller, dd);
	}
	
	@Override
	public T getConfiguration(){
		return this.configuration;
	}

	public void processEnvironment() {
		this.workingMemory.process();		
	}

	@Override
	public WorkingMemory getWorkingMemory() {
		return this.workingMemory;
	}
	
	@Override
	public IRuleFlow getRuleFlow() {
		return this.ruleFlow;
	}
}
