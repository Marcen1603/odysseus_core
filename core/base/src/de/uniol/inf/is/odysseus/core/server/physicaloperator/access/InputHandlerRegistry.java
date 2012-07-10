/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInputHandler;

public class InputHandlerRegistry {
	
	static Logger logger = LoggerFactory.getLogger(InputHandlerRegistry.class);

	static private Map<String, IInputHandler<?>> inputHandler = new HashMap<String, IInputHandler<?>>();
	
	public static void registerHandler(IInputHandler<?> handler){
		logger.debug("Register Input Handler "+handler.getName());
		if (inputHandler.containsKey(handler.getName().toLowerCase())){
			logger.error("Input Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		inputHandler.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void removeHandler(IInputHandler<?> handler){
		logger.debug("Remove Input Handler "+handler.getName());
		inputHandler.remove(handler.getName().toLowerCase());
	}
	
	public static IInputHandler<?> getInputHandler(String name){
		return inputHandler.get(name.toLowerCase());
	}
	
}
