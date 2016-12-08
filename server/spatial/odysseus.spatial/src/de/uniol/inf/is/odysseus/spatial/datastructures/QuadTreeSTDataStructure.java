package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public class QuadTreeSTDataStructure implements IMovingObjectDataStructure {

	@Override
	public void add(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ISpatialListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ISpatialListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<?>> getKNN(Geometry geometry, int k, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<?>> getNeighborhood(Geometry geometry, double range, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<?>> queryBoundingBox(List<Point> coordinates, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGeometryPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

}
