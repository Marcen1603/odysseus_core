package de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal;

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
@SuppressWarnings("deprecation")
public class SpatioTemporalDataStructureProvider {

	static Logger logger = LoggerFactory.getLogger(SpatioTemporalDataStructureProvider.class);

	private static SpatioTemporalDataStructureProvider instance;
	private Map<String, ISpatioTemporalDataStructure> dataStructureMap;

	private SpatioTemporalDataStructureProvider() {
		this.dataStructureMap = new HashMap<String, ISpatioTemporalDataStructure>();
	}

	public static SpatioTemporalDataStructureProvider getInstance() {
		if (instance == null) {
			instance = new SpatioTemporalDataStructureProvider();
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
	
	public ISpatioTemporalDataStructure getOrCreateDataStructure(String name, String type, int geometryPosition) {
		if (!dataStructureExists(name)) {
			Class<?> dataStructureClass = SpatioTemporalDataStructuresRegistry.getDataStructureClass(type);
			ISpatioTemporalDataStructure dataStrucure = null;
			try {
				dataStrucure = (ISpatioTemporalDataStructure) dataStructureClass
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
	public void addDataStructure(ISpatioTemporalDataStructure dataStructure) {
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
	public ISpatioTemporalDataStructure getDataStructure(String name) {
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

	/**
	 * Removes the data structure from the provider. If this was the only
	 * reference to the data structure, the data structure will be removed by
	 * the garbage collector.
	 * 
	 * @param name
	 *            The name of the data structure you want to remove
	 * @return true, if there was a data structure with the given name, false if
	 *         not
	 */
	public boolean removeDataStructure(String name) {
		if (this.dataStructureExists(name)) {
			this.dataStructureMap.remove(name);
			return true;
		}
		return false;
	}

}
