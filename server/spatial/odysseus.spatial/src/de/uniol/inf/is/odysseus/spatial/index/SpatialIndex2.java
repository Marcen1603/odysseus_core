package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;

public interface SpatialIndex2<T extends Object> {
	
	public void add(double latitude, double longitude, T object);
	
	public List<T> getKNearestNeighbors(T center, int k);
	
	public List<T> getWithinDistance(T center, double distanceInMeters);

}