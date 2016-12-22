package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.davidmoten.geo.Base32;
import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public class GeoHashSTDataStructure implements IMovingObjectDataStructure {

	private int geometryPosition;

	private NavigableMap<Long, Tuple<?>> geoHashes;

	public GeoHashSTDataStructure(int geometryPosition) {
		this.geometryPosition = geometryPosition;
		this.geoHashes = new TreeMap<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		//
		if (o instanceof Tuple<?>) {
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;

			// TODO If you fork the library you could directly get long instead
			// of encode and decode with string

			// Insert into map
			long geoHash = Base32.decodeBase32(GeoHash.encodeHash(1, 1));
			geoHashes.put(geoHash, tuple);

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
	public List<Tuple<?>> queryCircle(Geometry geometry, double radius, ITimeInterval t) {

		return null;
	}

	@Override
	public List<Tuple<?>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {

		// Check if the first and the last point is equal. If not, we have to
		// add the first point at the end to have a closed ring.
		Point firstPoint = polygonPoints.get(0);
		Point lastPoint = polygonPoints.get(polygonPoints.size() - 1);
		if (!firstPoint.equals(lastPoint)) {
			polygonPoints.add(firstPoint);
		}

		// Find the ones which are really within the box
		// Create a polygon with the given points
		GeometryFactory factory = new GeometryFactory();
		LinearRing ring = factory.createLinearRing(
				polygonPoints.stream().map(p -> p.getCoordinate()).toArray(size -> new Coordinate[size]));
		Polygon polygon = factory.createPolygon(ring, null);
		Geometry envelope = polygon.getEnvelope();

		// Get hashes that we have to search for

		// TODO This is a complete guess. See which coordinate is which.
		Coverage coverage = GeoHash.coverBoundingBox(envelope.getCoordinates()[0].x, envelope.getCoordinates()[0].y,
				envelope.getCoordinates()[2].x, envelope.getCoordinates()[2].y);
		Set<String> hashes = coverage.getHashes();

		// Get all hashes that we have to calculate the distance for
		Map<Long, Tuple<?>> allHashes = new HashMap<>();
		Iterator<String> iter = hashes.iterator();
		while (iter.hasNext()) {
			// Query all our hashes and use those which have the same prefix
			// The whole list of hashes is used in "getByPrefix"
			allHashes.putAll(getByPreffix(Base32.decodeBase32(iter.next())));
		}

		// For every point in our list ask JTS if the points lies within the
		// polygon
		List<Tuple<?>> result = allHashes.keySet().parallelStream()
				.filter(e -> polygon.contains(getGeometry(allHashes.get(e)))).map(e -> allHashes.get(e))
				.collect(Collectors.toList());

		return result;
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

	private SortedMap<Long, Tuple<?>> getByPreffix(long preffix) {
		return this.geoHashes.subMap(preffix, preffix + Character.MAX_VALUE);
	}

}
