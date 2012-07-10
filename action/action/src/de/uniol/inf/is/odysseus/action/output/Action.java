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
package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.operator.EventTriggerPO;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * An Action is the combination of an {@link IActuator} and
 * a Method that should be executed. In Combination with {@link ActionParameter}s
 * it can be executed by a {@link EventTriggerPO}.
 * @see EventTriggerPO
 * @see IActuator
 * @author Simon Flandergan
 *
 */
public class Action {
	private IActuator actuator;
	private String methodName;
	private ActionMethod method;
	
	/**
	 * Creates a new Action
	 * @param actuator
	 * @param methodName method associated to actuator
	 * @param parameterTypes parameter classtypes of method
	 * @throws ActionException
	 */
	public Action (IActuator actuator, String methodName, Class<?>[] parameterTypes) throws ActionException{
		boolean compatible = false;
		//check for compatibility
		for (ActionMethod method : actuator.getFullSchema()){
			if(method.isCompatibleTo(methodName, parameterTypes)){
				compatible = true;
				this.method = method;
				break;
			}
		}
		if (!compatible){
			throw new ActionException("Error while creating Action: Undefined Method");
		}
		
		this.actuator = actuator;
		this.methodName = methodName;
	}
	
	/**
	 * Invokes associated {@link IActuator} to execute the associated method with
	 * given parameters
	 * @param params
	 */
	public void executeMethod(Object[] params) throws ActuatorException{
		this.actuator.executeMethod(this.methodName, this.method.getParameterTypes(), params);
	}
	
	public IActuator getActuator() {
		return actuator;
	}
	
	public ActionMethod getMethod() {
		return method;
	}
}
