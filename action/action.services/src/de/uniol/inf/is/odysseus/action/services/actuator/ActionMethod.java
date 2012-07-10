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
package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Method of an {@link IActuator}. 
 * Used to provide schema for the parser and user.
 * @author Simon Flandergan
 *
 */
public class ActionMethod {
	private String name;
	private List<ActionParameter> parameters;
	private boolean showInSchema;
		
	/**
	 * Public constructor
	 * @param name name of the method
	 * @param classes class types of each parameter in order
	 * @param showInSchema flag for applications to reduce number of shown methods
	 */
	public ActionMethod (String name, Class<?>[] classes, String[] attributes, boolean showInSchema){
		this.name = name;
		this.showInSchema = showInSchema;
		this.parameters = new ArrayList<ActionParameter>();
		
		for (int i=0; i<classes.length; i++){
			ActionParameter param = null;
			try{
				param = new ActionParameter(attributes[i], classes[i]);
			}catch(IndexOutOfBoundsException e){
				param = new ActionParameter("var", classes[i]);
			}
			
			this.parameters.add(param);
		}
		
	}
	
	/**
	 * return method name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns mapping of parameterClasses and name.
	 * Mainly relevant for displaying schema
	 * @return
	 */
	public List<ActionParameter> getParameters() {
		return parameters;
	}
	
	/**
	 * return classes for each parameter in order
	 * @return
	 */
	public Class<?>[] getParameterTypes() {
		Class<?>[] params = new Class<?>[this.parameters.size()];
		
		int index=0;
		for (ActionParameter param : this.parameters){
			params[index] = param.getType();
			index++;
		}
		return params;
	}
	
	/**
	 * checks if this method is compatible to another method with 
	 * given name and parameters
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public boolean isCompatibleTo(String methodName, Class<?>[] parameters){
		if (!this.name.equals(methodName) || parameters.length != this.parameters.size()){
			//method name differs
			return false;
		}
		for (int i=0; i < parameters.length; i++){
			if (!PrimitivTypeComparator.sameType(parameters[i], this.parameters.get(i).getType())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns if schema should be shown
	 * @return
	 */
	public boolean isShowInSchema() {
		return showInSchema;
	}

}
