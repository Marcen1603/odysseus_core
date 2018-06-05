/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.preexecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGI Registry for PreExecutionHandlers (allows easy extensibility of parallelization types)
 * @author ChrisToenjesDeye
 *
 */
public class ParallelizationPreExecutionHandlerRegistry {

	private static Logger LOG = LoggerFactory
			.getLogger(ParallelizationPreExecutionHandlerRegistry.class);

	private static Map<String, IParallelizationPreExecutionHandler> handler = new HashMap<String, IParallelizationPreExecutionHandler>();

	/**
	 * OSGi method - registers all PreTransformationHandler which implements IParallelizationPreTransformationHandler
	 * 
	 * @param IParallelizationPreTransformationHandler
	 */
	public static void registerHandler(
			IParallelizationPreExecutionHandler preExecutionHandler) {
		LOG.debug("Register new PreExecutionHandler "
				+ preExecutionHandler.getType());
		if (!handler.containsKey(preExecutionHandler.getType()
				.toLowerCase())) {
			handler.put(preExecutionHandler.getType().toLowerCase(),
					preExecutionHandler);
		} else {
			LOG.error("PreExecutionHandler with name "
					+ preExecutionHandler.getType()
					+ " already registered");
		}
	}

	/**
	 * OSGi method - unregisters all PreExecutionHandler which implements IParallelizationPreExecutionHandler
	 * 
	 * @param IParallelizationPreExecutionHandler
	 */
	public static void unregisterHandler(
			IParallelizationPreExecutionHandler preExecutionHandler) {
		LOG.debug("Remove PreExecutionHandler "
				+ preExecutionHandler.getType());
		handler.remove(preExecutionHandler.getType().toLowerCase());
	}
	
	public static IParallelizationPreExecutionHandler getPreExecutionHandlerByType(
			String type) {
		if (!handler.containsKey(type.toLowerCase())){
			LOG.error("PreExecutionHandler with name "+type+" does not exist");
			return null;			
		} else {
			return handler.get(type.toLowerCase());
		}
	}

	/**
	 * returns a list of names of all registered preExecutionHandlers
	 * @return
	 */
	public static List<String> getValidTypes() {
		return new ArrayList<String>(handler.keySet());
	}

	/**
	 * checks if the string contains a valid parallelization type
	 * @param type
	 * @return
	 */
	public static boolean isValidType(String type) {
		for (String handlerName : handler.keySet()) {
			if (handlerName.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
}
