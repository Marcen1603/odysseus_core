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
package de.uniol.inf.is.odysseus.core.objecthandler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;

public class InputDataHandlerRegistry{

	static Logger logger = LoggerFactory.getLogger(InputDataHandlerRegistry.class);
	
	private static Map<String, IInputDataHandler<?, ?>> inputDataHandlers = new HashMap<String, IInputDataHandler<?, ?>>();
	
	static public void register(IInputDataHandler<?, ?> handler){
		logger.debug("Registering handler  "+handler.getName());
		if (inputDataHandlers.containsKey(handler.getName().toLowerCase())){
			logger.error("Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		inputDataHandlers.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(IInputDataHandler<?, ?> handler){
		logger.debug("Remove Handler "+handler.getName());
		inputDataHandlers.remove(handler.getName().toLowerCase());
	}
	
	public static IInputDataHandler<?, ?> get(String name){
		return inputDataHandlers.get(name.toLowerCase());
	}
}
