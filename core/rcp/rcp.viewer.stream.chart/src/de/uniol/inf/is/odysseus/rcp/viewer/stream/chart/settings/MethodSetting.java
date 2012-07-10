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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings;

import java.lang.reflect.Method;

public class MethodSetting {	
	
	private Method getter;
	private Method setter;
	private String name;
	private Method list;
	
	public MethodSetting(String name, Method getter, Method setter) {
		this.getter = getter;
		this.setter = setter;
		this.name = name;			
	}

	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public Class<?> getSetterValueType() {
		return this.getSetter().getParameterTypes()[0];
	}
	
	public Class<?> getGetterValueType() {
		return this.getGetter().getReturnType();
	}

	public Method getListGetter() {
		return list;
	}

	public void setListGetter(Method list) {
		this.list = list;
	}		
	
	

}
