package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;

public interface SpatialIndex2<T extends Object> {
	
	public void add(T object);
	
	public boolean remove(T object);
	
	public List<T> getKNearestNeighbors(T center, int k);
	
	public List<T> getWithinDistance(T center, double distanceInMeters);

}