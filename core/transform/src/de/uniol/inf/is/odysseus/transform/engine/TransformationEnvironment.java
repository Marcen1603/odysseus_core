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
package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.ruleengine.system.AbstractWorkingEnvironment;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * Handles the local state: current loaded configuration for each transformation instance etc.
 * 
 * 
 * @author Dennis Geesen
 *
 */
public class TransformationEnvironment extends AbstractWorkingEnvironment<TransformationConfiguration>{
		
		
	public TransformationEnvironment(TransformationConfiguration transformConfig, IRuleFlow ruleFlow, ISession caller, IDataDictionary dd){
		super(transformConfig, ruleFlow, caller, dd);		
	}				
	
}
