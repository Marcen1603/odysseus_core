package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashBoundingBoxQuery;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

public class GeoHashSTDataStructure implements IMovingObjectDataStructure {

	private int geometryPosition;

	private NavigableMap<GeoHash, Tuple<?>> geoHashes;

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

			// Insert into map
			GeoHash geoHash = GeoHash.withBitPrecision(getGeometry(tuple).getCentroid().getX(),
					getGeometry(tuple).getCentroid().getY(), 64);
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
		// TODO This is a complete guess. See which coordinate is which. ->
		// First test seems to be ok. Other possibility: expand BoundingBoxQuery
		// with all polygonPoints
		WGS84Point point1 = new WGS84Point(envelope.getCoordinates()[0].x, envelope.getCoordinates()[0].y);
		WGS84Point point2 = new WGS84Point(envelope.getCoordinates()[2].x, envelope.getCoordinates()[2].y);
		BoundingBox bBox = new BoundingBox(point1, point2);
		GeoHashBoundingBoxQuery bbQuery = new GeoHashBoundingBoxQuery(bBox);
		List<GeoHash> searchHashes = bbQuery.getSearchHashes();

		// Get all hashes that we have to calculate the distance for
		Map<GeoHash, Tuple<?>> allHashes = new HashMap<>();
		for (GeoHash hash : searchHashes) {
			// Query all our hashes and use those which have the same prefix
			// The whole list of hashes is used in "getByPrefix"
			allHashes.putAll(getByPreffix(hash));
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

	private SortedMap<GeoHash, Tuple<?>> getByPreffix(GeoHash preffix) {
		// We have to add 1 to the given hash, as we search all between the
		// given hash and the next one
		return this.geoHashes.subMap(preffix, preffix.next());
	}

}
