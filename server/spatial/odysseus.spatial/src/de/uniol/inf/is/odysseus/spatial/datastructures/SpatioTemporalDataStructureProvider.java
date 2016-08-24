package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.HashMap;
import java.util.Map;

public class SpatioTemporalDataStructureProvider {

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

	public void addDataStructure(IMovingObjectDataStructure dataStructure) {
		dataStructureMap.put(dataStructure.getName(), dataStructure);
	}

	public IMovingObjectDataStructure getDataStructure(String name) {
		return this.dataStructureMap.get(name);
	}

}
