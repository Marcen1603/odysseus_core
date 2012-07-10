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
package de.uniol.inf.is.odysseus.core.connection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessConnectionHandlerRegistry {
static Logger logger = LoggerFactory.getLogger(AccessConnectionHandlerRegistry.class);
	
	private static Map<String, IAccessConnectionHandler<?>> handlers = new HashMap<String, IAccessConnectionHandler<?>>();
	
	static public void register(IAccessConnectionHandler<?> handler){
		logger.debug("Registering handler  "+handler.getName());
		if (handlers.containsKey(handler.getName().toLowerCase())){
			logger.error("Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		handlers.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(IAccessConnectionHandler<?> handler){
		logger.debug("Remove Handler "+handler.getName());
		handlers.remove(handler.getName().toLowerCase());
	}
	
	public static IAccessConnectionHandler<?> get(String name){
		return handlers.get(name.toLowerCase());
	}
}
