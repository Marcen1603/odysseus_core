package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
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
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

/**
 * Uses GeoHashes for a spatial indexing.
 * 
 * @author Tobias Brandt
 *
 */
public class GeoHashSTDataStructure implements IMovingObjectDataStructure {

	private static Logger _logger = LoggerFactory.getLogger(GeoHashSTDataStructure.class);

	public static final String TYPE = "geohash";

	private static final int BIT_PRECISION = 64;

	private int geometryPosition;
	private String name;

	// The number of elements currently in the index
	protected long elementCounter;

	/*
	 * All the objects. As there can be more than one object at the exact same
	 * location, we need a list. Probably most lists are only of size 1.
	 */
	protected NavigableMap<GeoHash, List<Tuple<ITimeInterval>>> geoHashes;

	// Index by time
	protected DefaultTISweepArea<Tuple<ITimeInterval>> sweepArea;

	public GeoHashSTDataStructure(String name, int geometryPosition) {
		this.geometryPosition = geometryPosition;
		this.name = name;

		/*
		 * TODO Think about if such a tree is the fastest option or if there
		 * should be a HashMap. Pro this solution: When searching, finding
		 * similar values (based on geohashes) is simple and fast.
		 */
		this.geoHashes = new TreeMap<>();
		this.sweepArea = new DefaultTISweepArea<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;

			// Insert into map
			GeoHash geoHash = this.fromGeometry(getGeometry(tuple));
			List<Tuple<ITimeInterval>> geoHashList = this.geoHashes.get(geoHash);
			if (geoHashList == null) {
				/*
				 * Probably we will only have one element in here as two objects
				 * on the same location are unlikely (but depends on the
				 * scenario)
				 */
				geoHashList = new ArrayList<>(1);
			}

			// Add the new tuple to the list
			geoHashList.add(tuple);
			this.geoHashes.put(geoHash, geoHashList);

			// Insert in SweepArea
			this.sweepArea.insert(tuple);

			// Count
			elementCounter++;
		}
	}

	@Override
	public void cleanUp(PointInTime timestamp) {
		// Remove old elements from sweepArea
		List<Tuple<ITimeInterval>> removed = this.sweepArea.extractElementsBeforeAsList(timestamp);

		// Update the counter
		elementCounter -= removed.size();

		// Warn
		if (removed.size() > 50000) {
			_logger.warn("Remove " + removed.size() + " elements from GeoHashIndex. This can take a while!");
		}

		// Remove the extracted elements from the map
		for (Tuple<ITimeInterval> removedTuple : removed) {
			GeoHash hash = this.fromGeometry(this.getGeometry(removedTuple));
			List<Tuple<ITimeInterval>> geoHashList = this.geoHashes.get(hash);
			if (geoHashList.size() == 1) {
				// We don't have to compare or iterate, we can directly remove
				// the list
				this.geoHashes.remove(hash);
			} else {
				// We have to iterate through the list to find the right tuple
				// to delete
				geoHashList.remove(removedTuple);
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<Tuple<ITimeInterval>> queryKNN(Geometry geometry, int k, ITimeInterval t) {

		// TODO Question to think about: First do temporal filter or first do
		// spatial filter? Which is faster? I think spatial could be faster
		// here.

		// This is a bit more tricky
		// - how big should be starting area be? That's based on the density of
		// the data points there.
		// - What if there are not enough elements?

		// Case 1: The total number of elements is smaller or equal to k -> We
		// can return everything
		// Hint: We cannot use the size of "geoHashes" as there may be multiple
		// entries per hash
		if (elementCounter <= k) {

			// get all tuples
			List<Tuple<ITimeInterval>> result = new ArrayList<>();
			geoHashes.values().stream().forEach(e -> result.addAll(result));

			// Nevertheless, we have to filter out the elements which do
			// not overlap on a timely level
			List<Tuple<ITimeInterval>> filter = result.parallelStream()
					// temporal filter
					.filter(f -> f.getMetadata().getStart().before(t.getEnd())
							&& f.getMetadata().getEnd().after(t.getStart()))
					.collect(Collectors.toList());

			return filter;
		}

		/*
		 * Case 2: We have to search the k elements. We will search for a
		 * certain radius and subsequently increase the radius. This
		 * implementation is probably not ideal in case of efficiency but lowers
		 * the risk of a wrong result set.
		 */

		// We have to guess a radius in which we search. We don't have an idea
		// how far the elements may be away.
		// TODO Give the user a chance to set the radius or calculate a good
		// guess from the data we have
		int guessRadius = 100;

		// The max radius not totally necessary but useful to avoid an endless
		// search
		// TODO This maybe leads to wrong results
		int maxRadius = 1000000;

		// Query the guessed radius
		List<Tuple<ITimeInterval>> circleResult = null;
		do {
			// Get the results for the current radius
			circleResult = queryCircle(geometry, guessRadius, t);

			// Double the radius. This is needed for the next round if the don't
			// find enough elements
			guessRadius += guessRadius;
		} while (circleResult.size() < k && guessRadius < maxRadius);

		// Now we have enough elements, but we only need the k closest
		// Sort the elements we need
		List<Tuple<ITimeInterval>> sortedTuples = circleResult;
		// and sort the list by the distance to the given point
		sortedTuples.sort(new Comparator<Tuple<?>>() {

			@Override
			public int compare(Tuple<?> o1, Tuple<?> o2) {
				// Calculate the distance of both tuples to the center (here we
				// can use this distance calculation, as we are only interested
				// in the distance comparison, not in the real distances)
				double distance1 = getGeometry(o1).distance(geometry);
				double distance2 = getGeometry(o2).distance(geometry);

				if (distance1 < distance2) {
					return -1;
				} else if (distance1 > distance2) {
					return 1;
				} else {
					return 0;
				}

			}

			private Geometry getGeometry(Tuple<?> tuple) {
				Object o = tuple.getAttribute(geometryPosition);
				GeometryWrapper geometryWrapper = null;
				if (o instanceof GeometryWrapper) {
					geometryWrapper = (GeometryWrapper) o;
					Geometry geometry = geometryWrapper.getGeometry();
					return geometry;
				} else {
					return null;
				}
			}

		});

		// Now we can return the first k elements from the sorted list
		if (sortedTuples.size() < k) {
			// Maybe we have less than k neighbors because the number of
			// elements is too small
			return sortedTuples;
		}

		// Very important: Do not return the subList itself as it is only a view
		return new ArrayList<Tuple<ITimeInterval>>(sortedTuples.subList(0, k));
	}

	@Override
	public List<Tuple<ITimeInterval>> queryCircle(Geometry geometry, double radius, ITimeInterval t) {

		// This is basically a bounding box but for a circle

		// Get the rectangular envelope for the circle
		Envelope env = MetrticSpatialUtils.getInstance().getEnvelopeForRadius(geometry.getCentroid().getCoordinate(),
				radius);
		GeometryFactory factory = new GeometryFactory(geometry.getPrecisionModel(), geometry.getSRID());
		Point topLeft = factory.createPoint(new Coordinate(env.getMaxX(), env.getMaxY()));
		Point lowerRight = factory.createPoint(new Coordinate(env.getMinX(), env.getMinY()));

		// Get all elements within that bounding box (filter step)
		// TODO This results in doing the distance calculation twice as it is
		// already done within the BoundingBox-method. Create another method
		// that returns roughly the right coordinates and then do the distance
		// calculation only once.
		Map<GeoHash, List<Tuple<ITimeInterval>>> candidateCollection = approximateBoundinBox(
				createPolygon(createBox(topLeft, lowerRight)));

		MetrticSpatialUtils spatialUtils = MetrticSpatialUtils.getInstance();

		// TODO Think about: maybe it is faster to first check if the point is
		// within a polygon (cheap) and reduce the number of distance
		// calculations (expensive)

		// For every point in our list ask JTS if the points lies within the
		// polygon (refinement step)
		// TODO Do performance tests? stream vs parallelStream vs foreach
		// (http://stackoverflow.com/questions/18290935/flattening-a-collection)
		List<Tuple<ITimeInterval>> result = candidateCollection.keySet().stream()
				/*
				 * spatial filter (it's enough to do it only with the first
				 * element as all elements in this list are on the exact same
				 * position (have the same geohash)
				 */
				.filter(f -> spatialUtils.calculateDistance(null, geometry.getCentroid().getCoordinate(),
						getGeometry(this.geoHashes.get(f).get(0)).getCentroid().getCoordinate()) <= radius)
				/*
				 * We have a list of tuples but need to do this for every tuple.
				 * Therefore, we need to "unnest" the list, which is a flatMap
				 * operation.
				 */
				.flatMap(tupleList -> candidateCollection.get(tupleList).stream())
				/*
				 * Temporal filter -> Valid time needs to overlap
				 */
				.filter(tuple -> tuple.getMetadata().getStart().before(t.getEnd())
						&& tuple.getMetadata().getEnd().after(t.getStart()))
				/*
				 * Write result into a list
				 */
				.collect(Collectors.toList());

		// TODO Maybe faster with haversine. give choice?
		// List<Tuple<ITimeInterval>> result =
		// candidateCollection.parallelStream()
		// // temporal filter
		// .filter(e -> e.getMetadata().getStart().before(t.getEnd())
		// && e.getMetadata().getEnd().after(t.getStart()))
		// // spatial filter
		// .filter(f ->
		// spatialUtils.calculateHaversineDistance(geometry.getCentroid().getCoordinate(),
		// getGeometry(f).getCentroid().getCoordinate()) <= radius)
		// .collect(Collectors.toList());
		return result;
	}

	public List<Tuple<ITimeInterval>> queryBoundingBox(Point topLeft, Point lowerRight, ITimeInterval t) {
		// Create the polygon to query
		List<Point> polygonPoints = createBox(topLeft, lowerRight);

		return queryBoundingBox(polygonPoints, t);
	}

	@Override
	public List<Tuple<ITimeInterval>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {
		Polygon polygon = createPolygon(polygonPoints);

		// Get all hashes that we have to calculate the distance for
		Map<GeoHash, List<Tuple<ITimeInterval>>> allHashes = approximateBoundinBox(polygon);

		// For every point in our list ask JTS if the points lies within the
		// polygon
		// TODO Do performance tests? stream vs parallelStream vs foreach
		// (http://stackoverflow.com/questions/18290935/flattening-a-collection)
		List<Tuple<ITimeInterval>> result = allHashes.keySet().stream()
				/*
				 * spatial filter (it's enough to do it only with the first
				 * element as all elements in this list are on the exact same
				 * position (have the same geohash)
				 */
				.filter(e -> polygon.contains(getGeometry(allHashes.get(e).get(0))))
				/*
				 * We have a list of tuples but need to do this for every tuple.
				 * Therefore, we need to "unnest" the list, which is a flatMap
				 * operation.
				 */
				.flatMap(tupleList -> allHashes.get(tupleList).stream())
				/*
				 * Temporal filter -> Valid time needs to overlap
				 */
				.filter(tuple -> tuple.getMetadata().getStart().before(t.getEnd())
						&& tuple.getMetadata().getEnd().after(t.getStart()))
				/*
				 * Write result into a list
				 */
				.collect(Collectors.toList());

		return result;
	}

	private Map<GeoHash, List<Tuple<ITimeInterval>>> approximateBoundinBox(Polygon polygon) {
		Geometry envelope = polygon.getEnvelope();

		// Get hashes that we have to search for
		// TODO This is guessed. See which coordinate is which. ->
		// First test seems to be OK. Other possibility: expand BoundingBoxQuery
		// with all polygonPoints
		WGS84Point point1 = new WGS84Point(envelope.getCoordinates()[0].x, envelope.getCoordinates()[0].y);
		WGS84Point point2 = new WGS84Point(envelope.getCoordinates()[2].x, envelope.getCoordinates()[2].y);
		BoundingBox bBox = new BoundingBox(point1, point2);
		GeoHashBoundingBoxQuery bbQuery = new GeoHashBoundingBoxQuery(bBox);
		List<GeoHash> searchHashes = bbQuery.getSearchHashes();

		// Get all hashes that we have to calculate the distance for
		Map<GeoHash, List<Tuple<ITimeInterval>>> allHashes = new HashMap<>();
		for (GeoHash hash : searchHashes) {
			// Query all our hashes and use those which have the same prefix
			// The whole list of hashes is used in "getByPrefix"
			allHashes.putAll(getByPreffix(hash));
		}
		return allHashes;
	}

	private Polygon createPolygon(List<Point> polygonPoints) {
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
		return polygon;
	}

	@Override
	public int getGeometryPosition() {
		return this.geometryPosition;
	}

	/**
	 * Get the geometry from a tuple
	 * 
	 * @param tuple
	 *            The tuple with the geometry
	 * @return The geometry
	 */
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

	private SortedMap<GeoHash, List<Tuple<ITimeInterval>>> getByPreffix(GeoHash preffix) {
		// We have to add 1 to the given hash, as we search all between the
		// given hash and the next one
		return this.geoHashes.subMap(preffix, preffix.next());
	}

	protected GeoHash fromGeometry(Geometry geometry) {
		Point centroid = geometry.getCentroid();
		GeoHash hash = GeoHash.withBitPrecision(centroid.getX(), centroid.getY(), BIT_PRECISION);
		return hash;
	}

	private List<Point> createBox(Point topLeft, Point lowerRight) {
		// Create the polygon
		GeometryFactory factory = new GeometryFactory(topLeft.getPrecisionModel(), topLeft.getSRID());
		Point topRight = factory.createPoint(new Coordinate(lowerRight.getX(), topLeft.getY()));
		Point lowerLeft = factory.createPoint(new Coordinate(topLeft.getX(), lowerRight.getY()));

		List<Point> polygonPoints = new ArrayList<>(5);
		polygonPoints.add(topLeft);
		polygonPoints.add(topRight);
		polygonPoints.add(lowerRight);
		polygonPoints.add(lowerLeft);
		polygonPoints.add(topLeft);

		return polygonPoints;
	}

}
