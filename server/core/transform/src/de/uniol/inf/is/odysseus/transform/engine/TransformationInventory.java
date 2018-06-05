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
package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;

/**
 * Handles the global state: current loaded rules and current workflow
 * 
 * 
 * @author Dennis Geesen
 * 
 */
public class TransformationInventory extends AbstractInventory {

	private static TransformationInventory instance = null;

	private TransformationInventory(){	
		super();		
	}
	
	public TransformationInventory(AbstractInventory inventory) {
		super(inventory);
	}

	public static synchronized TransformationInventory getInstance() {
		if (instance == null) {
			instance = new TransformationInventory();
		}
		return instance;
	}
	
	@Override
	public AbstractInventory getCurrentInstance(){
		return getInstance();
	}
	
	@Override
	public String getInventoryName(){
		return "Transform";
	}	
}
