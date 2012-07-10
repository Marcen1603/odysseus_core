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
package de.uniol.inf.is.odysseus.action.actuator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActuator {
	private String name;
	private byte adress;
	private double result;
	private Logger logger;
	
	public TestActuator (String name){
		this.name = name;	
		this.logger = LoggerFactory.getLogger(TestActuator.class);
	}
	
	public TestActuator (String name, byte adress){
		this.name = name;
		this.adress = adress;
	}
	
	public void doSomething(byte a, double b, double c, int d){
		this.result = a+b+c+d;
	}
	
	public byte getAdress() {
		return adress;
	}
	
	public String getName() {
		return name;
	}
	
	public double giveSomething() {
		return this.result;
	}
	
	public void setFields(String name, byte adress){
		this.name = name;
		this.adress = adress;
		this.logger.debug(this.name+":"+adress);
	}
	
	
}
