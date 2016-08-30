package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public interface IMovingObjectDataStructure {

	public void add(Object o);

	public void addListener(ISpatialListener listener);

	public void removeListener(ISpatialListener listener);

	public String getName();
	
	public List<Tuple<?>> getKNN(Tuple<?> tuple, int geometryPosition, int k);
}
