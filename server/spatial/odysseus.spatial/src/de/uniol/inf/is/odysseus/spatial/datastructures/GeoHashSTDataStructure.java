package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public class GeoHashSTDataStructure implements IMovingObjectDataStructure {

	private int geometryPosition;

	public GeoHashSTDataStructure(int geometryPosition) {
		this.geometryPosition = geometryPosition;
	}

	@Override
	public void add(Object o) {
		//
		if (o instanceof Tuple<?>) {
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;

			// Insert in QuadTree
			Geometry geom = getGeometry((Tuple<?>) o);
			GeoHash hash = GeoHash.withBitPrecision(1, 1, 64);

			// Insert in SweepArea
			// this.sweepArea.insert(tuple);
		}
	}

	@Override
	public void cleanUp(PointInTime timestamp) {
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
	public List<Tuple<?>> queryKNN(Geometry geometry, int k, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<?>> queryNeighborhood(Geometry geometry, double range, ITimeInterval t) {
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
		return this.geometryPosition;
	}

	protected Geometry getGeometry(Tuple<?> tuple) {
		Object o = tuple.getAttribute(getGeometryPosition());
		GeometryWrapper geometryWrapper = null;
		if (o instanceof GeometryWrapper) {
			geometryWrapper = (GeometryWrapper) o;
			Geometry geometry = geometryWrapper.getGeometry();
			return geometry;
		} else {
			return null;
		}
	}

}
