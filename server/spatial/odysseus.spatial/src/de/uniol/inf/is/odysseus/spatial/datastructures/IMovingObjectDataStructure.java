package de.uniol.inf.is.odysseus.spatial.datastructures;

import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public interface IMovingObjectDataStructure {

	public void add(Object o);
	
	public String getName();

	public void addListener(ISpatialListener listener);

	public void removeListener(ISpatialListener listener);

}
