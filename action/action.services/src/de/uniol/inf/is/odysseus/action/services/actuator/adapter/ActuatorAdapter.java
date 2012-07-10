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
package de.uniol.inf.is.odysseus.action.services.actuator.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * Actuator bases on reflections. Allows the use of any java-class as 
 * Actuator when extending this class. It does not have to implements any
 * special framework specific functionality! Schema and method execution is
 * retrieved dynamically through reflections technology
 * @author Simon Flandergan
 *
 */
public class ActuatorAdapter implements IActuator {
	private Object adapter;
	private List<ActionMethod> methods;
	
	public ActuatorAdapter(Object adapter) {
		this.adapter = adapter;
		this.methods = new ArrayList<ActionMethod>();
		for (Method method : this.adapter.getClass().getMethods()) {
			boolean show = false;
			String[] paramNames = new String[0];
			if (method.isAnnotationPresent(ActuatorAdapterSchema.class)){
				if (!method.getAnnotation(ActuatorAdapterSchema.class).provide()){
					//do not provide this method
					continue;
				}
				show = method.getAnnotation(ActuatorAdapterSchema.class).show();
				paramNames = method.getAnnotation(ActuatorAdapterSchema.class).paramNames();
			}
			
			this.methods.add(
					new ActionMethod(method.getName(), method.getParameterTypes(), paramNames, show));
		}
	}
	
	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Method methodToExecute = this.adapter.getClass().getMethod(method, types);
			methodToExecute.invoke(this.adapter, params);
		} catch (SecurityException e) {
			throw new ActuatorException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ActuatorException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new ActuatorException(e.getMessage());
		}catch (NullPointerException e){
			throw new ActuatorException(e.getMessage());
		}
	}
	
	
	@Override
	public List<ActionMethod> getFullSchema() {
		return this.methods;
	}

	@Override
	public List<ActionMethod> getReducedSchema() {
		ArrayList<ActionMethod> reducedSchema = new ArrayList<ActionMethod>();
		for (ActionMethod m : this.methods){
			if (m.isShowInSchema()){
				reducedSchema.add(m);
			}
			
		}
		return reducedSchema;
	}

}
