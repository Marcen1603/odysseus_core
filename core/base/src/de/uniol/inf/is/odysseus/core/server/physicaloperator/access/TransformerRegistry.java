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

public class TransformerRegistry {

	static Logger logger = LoggerFactory.getLogger(TransformerRegistry.class);

	static private Map<String, ITransformer<?,?>> transformer = new HashMap<String, ITransformer<?,?>>();
	
	public static void register(ITransformer<?,?> handler){
		logger.debug("Register Transformer "+handler.getName());
		if (transformer.containsKey(handler.getName().toLowerCase())){
			logger.error("Input Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		transformer.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(ITransformer<?,?> handler){
		logger.debug("Remove Transformer "+handler.getName());
		transformer.remove(handler.getName().toLowerCase());
	}
	
	public static ITransformer<?,?> getTransformer(String name){
		return transformer.get(name.toLowerCase());
	}
}
