package de.uniol.inf.is.odysseus.parallelization.transform.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parallelization.transform.IParallelizationPreTransformationHandler;

public class ParallelizationPreTransformationHandlerRegistry {

	private static Logger LOG = LoggerFactory
			.getLogger(ParallelizationPreTransformationHandlerRegistry.class);

	private static Map<String, IParallelizationPreTransformationHandler> handler = new HashMap<String, IParallelizationPreTransformationHandler>();

	/**
	 * OSGi method - registers all PreTransformationHandler which implements IParallelizationPreTransformationHandler
	 * 
	 * @param IParallelizationPreTransformationHandler
	 */
	public static void registerHandler(
			IParallelizationPreTransformationHandler preTransformationHandler) {
		LOG.debug("Register new PreTransformationHandler "
				+ preTransformationHandler.getType());
		if (!handler.containsKey(preTransformationHandler.getType()
				.toLowerCase())) {
			handler.put(preTransformationHandler.getType().toLowerCase(),
					preTransformationHandler);
		} else {
			LOG.error("PreTransformationHandler with name "
					+ preTransformationHandler.getType()
					+ " already registered");
		}
	}

	/**
	 * OSGi method - unregisters all PreTransformationHandler which implements IParallelizationPreTransformationHandler
	 * 
	 * @param IParallelizationPreTransformationHandler
	 */
	public static void unregisterHandler(
			IParallelizationPreTransformationHandler preTransformationHandler) {
		LOG.debug("Remove PreTransformationHandler "
				+ preTransformationHandler.getType());
		handler.remove(preTransformationHandler.getType().toLowerCase());
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static IParallelizationPreTransformationHandler getPreTransformationHandlerByType(
			String type) {
		if (!handler.containsKey(type.toLowerCase())){
			LOG.error("PreTransformationHandler with name "+type+" does not exist");
			return null;			
		} else {
			return handler.get(type.toLowerCase());
		}
	}

	public static List<String> getValidTypes() {
		return new ArrayList<String>(handler.keySet());
	}

}
