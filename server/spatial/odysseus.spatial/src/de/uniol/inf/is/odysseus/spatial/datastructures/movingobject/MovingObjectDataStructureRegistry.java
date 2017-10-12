package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.IHasAlias;

public class MovingObjectDataStructureRegistry {

	static Logger logger = LoggerFactory.getLogger(MovingObjectDataStructureRegistry.class);

	private static Map<String, Class<?>> datastructures = new HashMap<String, Class<?>>();

	/**
	 * Registers a new type of spatial data structure so that it can be accessed
	 * by others.
	 * 
	 * @param datastructure
	 *            The class which provides the data structure. Is needs to
	 *            implement IMovingObjectDataStructure.
	 * @param type
	 *            The name of the type of the data structure under which it can
	 *            be used (e.g. in PQL).
	 */
	public static void register(Class<?> datastructure, String type) {

		// Is it a spatial data structure?
		if (!MovingObjectIndex.class.isAssignableFrom(datastructure)) {
			logger.warn("Given class with type " + type + " does not implement IMovingObjectDataStructure.");
			return;
		}

		if (!datastructures.containsKey(type)) {
			datastructures.put(type, datastructure);
		} else {
			logger.warn("Datastructure with name " + type + " already registered");
		}
	}

	/**
	 * Removes a data structure from the registry.
	 * 
	 * @param datastructure
	 *            The data structure that needs to be removed.
	 */
	static public void remove(MovingObjectIndex datastructure) {
		logger.trace("Remove datastructure " + datastructure.getName());
		datastructures.remove(datastructure.getName().toLowerCase());
		if (datastructure instanceof IHasAlias) {
			datastructures.remove(((IHasAlias) datastructure).getAliasName().toLowerCase());
		}
	}

	/**
	 * Returns the data structure class for the given data structure type (name
	 * of the type of data structure, not the instance). This class can be used
	 * to create an instance of it.
	 * 
	 * @param type
	 *            The type of data structure you want to get the class for
	 * @return The class of the data structure so that an instance can be
	 *         created
	 */
	static public Class<?> getDataStructureClass(String type) {
		Class<?> datastructure = datastructures.get(type.toLowerCase());
		if (datastructure != null) {
			return datastructure;
		}
		logger.error("No datastrcture with name " + type + " found!");
		if (logger.isDebugEnabled()) {
			logger.debug("Available datastructures: ");
			for (String k : datastructures.keySet()) {
				logger.debug(k);
			}
		}
		return null;
	}

	/**
	 * You may want to choose a data structure from this list. These are all
	 * registered types of data structures.
	 * 
	 * @return A list of all types of data structures
	 */
	public static ImmutableList<String> getDataStructureTypes() {
		return ImmutableList.copyOf(datastructures.keySet());
	}
	
}
