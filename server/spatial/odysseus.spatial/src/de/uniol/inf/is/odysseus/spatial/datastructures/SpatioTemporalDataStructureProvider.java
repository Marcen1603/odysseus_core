package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpatioTemporalDataStructureProvider {

	static Logger logger = LoggerFactory.getLogger(SpatioTemporalDataStructureProvider.class);

	private static SpatioTemporalDataStructureProvider instance;
	private Map<String, IMovingObjectDataStructure> dataStructureMap;

	private SpatioTemporalDataStructureProvider() {
		this.dataStructureMap = new HashMap<String, IMovingObjectDataStructure>();
	}

	public static SpatioTemporalDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new SpatioTemporalDataStructureProvider();
		}
		return instance;
	}

	public IMovingObjectDataStructure getOrCreateDataStructure(String name, String type) {
		// TODO Use OSGi to bind dataStructures
		if (getDataStructure(name) == null) {
			Class<?> dataStructureClass = MovingObjectDataStructuresRegistry.getDataStructureClass(type);
			IMovingObjectDataStructure dataStrucure = null;
			try {
				dataStrucure = (IMovingObjectDataStructure) dataStructureClass.getDeclaredConstructor(String.class)
						.newInstance(name);
				addDataStructure(dataStrucure);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.error("No datastructure of type " + type + " available.");
				e.printStackTrace();
			}
		}
		return getDataStructure(name);
	}

	public void addDataStructure(IMovingObjectDataStructure dataStructure) {
		dataStructureMap.put(dataStructure.getName(), dataStructure);
	}

	public IMovingObjectDataStructure getDataStructure(String name) {
		return this.dataStructureMap.get(name);
	}

}
