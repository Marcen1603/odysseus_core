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

public class ObjectHandlerRegistry{

	static Logger logger = LoggerFactory.getLogger(ObjectHandlerRegistry.class);
	
	private static Map<String, IObjectHandler<?>> objectHandlers = new HashMap<String, IObjectHandler<?>>();
	
	static public void register(IObjectHandler<?> handler){
		logger.trace("Registering new object handler  "+handler.getName());
		if (objectHandlers.containsKey(handler.getName().toLowerCase())){
			logger.error("Object Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		objectHandlers.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(IObjectHandler<?> handler){
		logger.trace("Remove object Handler "+handler.getName());
		objectHandlers.remove(handler.getName().toLowerCase());
	}
	
	public static IObjectHandler<?> get(String name){
		return objectHandlers.get(name.toLowerCase());
	}
}
