package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All spatial data structures can be accessed or created with this provider.
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialDataStructureProvider {

	static Logger logger = LoggerFactory.getLogger(SpatialDataStructureProvider.class);

	private static SpatialDataStructureProvider instance;
	private Map<String, IMovingObjectDataStructure> dataStructureMap;

	private SpatialDataStructureProvider() {
		this.dataStructureMap = new HashMap<String, IMovingObjectDataStructure>();
	}

	public static SpatialDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new SpatialDataStructureProvider();
		}
		return instance;
	}

	/**
	 * Creates a new data structure from the given type if there does not exist
	 * one with the given name. If there already exists a data structure with
	 * the given name, it is return and the type is ignored.
	 * 
	 * @param name
	 *            The name of the data structure you want to create or get
	 * @param type
	 *            The type of the data structure. You can ask the
	 *            MovingObjectDataStructuresRegistry for the available data
	 *            structures
	 * @param geometryPosition
	 *            The position in the incoming tuples where the geometry
	 *            attribute is.
	 * @return A spatial data structure
	 */
	public IMovingObjectDataStructure getOrCreateDataStructure(String name, String type, int geometryPosition) {
		if (!dataStructureExists(name)) {
			Class<?> dataStructureClass = MovingObjectDataStructuresRegistry.getDataStructureClass(type);
			IMovingObjectDataStructure dataStrucure = null;
			try {
				dataStrucure = (IMovingObjectDataStructure) dataStructureClass
						.getDeclaredConstructor(String.class, int.class).newInstance(name, geometryPosition);
				addDataStructure(dataStrucure);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.error("No datastructure of type " + type + " available.");
				e.printStackTrace();
			}
		}

		return getDataStructure(name);
	}

	/**
	 * Adds a data structure so that you can access this data structure from
	 * this provider. If a data structure with the same name already exists, the
	 * old data structure is removed and the given new one replaces the old one.
	 * 
	 * @param dataStructure
	 *            The data structure to add
	 */
	public void addDataStructure(IMovingObjectDataStructure dataStructure) {
		dataStructureMap.put(dataStructure.getName(), dataStructure);
	}

	/**
	 * Method to get the data structure with the given name. If the data
	 * structure does not exist, null is returned.
	 * 
	 * @param name
	 *            The name of the data structure
	 * @return The data structure or null, if it does not exist
	 */
	public IMovingObjectDataStructure getDataStructure(String name) {
		return this.dataStructureMap.get(name);
	}

	/**
	 * Checks if the data structure already exists
	 * 
	 * @param name
	 *            Name of the data structure you want to check
	 * @return true, if data structure already exists, false if not
	 */
	public boolean dataStructureExists(String name) {
		return getDataStructure(name) != null;
	}

}
