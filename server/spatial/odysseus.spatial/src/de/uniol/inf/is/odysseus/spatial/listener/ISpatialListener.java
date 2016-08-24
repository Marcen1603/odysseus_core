package de.uniol.inf.is.odysseus.spatial.listener;

import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;

public interface ISpatialListener {
	
	public void onMovingObjectDataStructureChange(IMovingObjectDataStructure dataStructure);

}
