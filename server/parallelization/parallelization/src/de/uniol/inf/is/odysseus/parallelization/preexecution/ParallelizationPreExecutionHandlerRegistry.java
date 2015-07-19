package de.uniol.inf.is.odysseus.parallelization.preexecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		LOG.debug("Register new PreTransformationHandler "
				+ preExecutionHandler.getType());
		if (!handler.containsKey(preExecutionHandler.getType()
				.toLowerCase())) {
			handler.put(preExecutionHandler.getType().toLowerCase(),
					preExecutionHandler);
		} else {
			LOG.error("PreTransformationHandler with name "
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
		LOG.debug("Remove PreTransformationHandler "
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

	public static List<String> getValidTypes() {
		return new ArrayList<String>(handler.keySet());
	}

	public static boolean isValidType(String type) {
		for (String handlerName : handler.keySet()) {
			if (handlerName.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
}
