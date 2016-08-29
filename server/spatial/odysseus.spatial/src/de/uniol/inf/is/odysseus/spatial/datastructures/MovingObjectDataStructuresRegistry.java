package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.IHasAlias;

public class MovingObjectDataStructuresRegistry {

	static Logger logger = LoggerFactory.getLogger(MovingObjectDataStructuresRegistry.class);

	static private Map<String, Class<?>> datastructures = new HashMap<String, Class<?>>();

	public static void register(Class<?> datastructure, String type) {
		if (!datastructures.containsKey(type)) {
			datastructures.put(type, datastructure);
		} else {
			logger.warn("Datastructure with name " + type + " already registered");
		}
	}

	static public void remove(IMovingObjectDataStructure datastructure) {
		logger.trace("Remove datastructure " + datastructure.getName());
		datastructures.remove(datastructure.getName().toLowerCase());
		if (datastructure instanceof IHasAlias) {
			datastructures.remove(((IHasAlias) datastructure).getAliasName().toLowerCase());
		}
	}

	static public Class<?> getDataStructureClass(String name) {
		Class<?> datastructure = datastructures.get(name.toLowerCase());
		if (datastructure != null) {
			return datastructure;
		}
		logger.error("No datastrcture with name " + name + " found!");
		if (logger.isDebugEnabled()) {
			logger.debug("Available datastrcture: ");
			for (String k : datastructures.keySet()) {
				logger.debug(k);
			}
		}
		return null;
	}

	public static ImmutableList<String> getDataStructureNames() {
		return ImmutableList.copyOf(datastructures.keySet());
	}

}
