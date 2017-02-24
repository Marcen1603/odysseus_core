package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashBoundingBoxQuery;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.spatial.datastructures.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class GeoHashMODataStructure implements IMovingObjectDataStructure {

	private static final int BIT_PRECISION = 64;
	public static final String TYPE = "mo_geohash";

	private int idPosition;
	private double distancePerMovingObject;

	// The position of the geometry-attribute within the tuple
	private int geometryAttributePosition;

	// The name of the data structure to access it
	private String name;

	private NavigableMap<GeoHash, List<TrajectoryElement>> pointMap;
	private Map<String, TrajectoryElement> latestTrajectoryElementMap;
	private Map<String, Double> movingObjectDistances;

	public GeoHashMODataStructure(String name, int geometryPosition, int idPosition, double distancePerMovingObject) {
		this.name = name;
		this.geometryAttributePosition = geometryPosition;
		this.idPosition = idPosition;
		this.distancePerMovingObject = distancePerMovingObject;

		this.pointMap = new TreeMap<>();
		this.latestTrajectoryElementMap = new HashMap<>();
		this.movingObjectDistances = new HashMap<>();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			// Get info for new TrajectoryElement
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;
			GeoHash geoHash = GeoHashHelper.fromGeometry(GeoHashHelper.getGeometry(tuple, getGeometryPosition()),
					BIT_PRECISION);
			String id = getMovingObjectId(tuple);

			// Get the previously latest element from that trajectory
			TrajectoryElement latestElement = latestTrajectoryElementMap.get(id);

			// Create the new "latest" element and save it
			TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, geoHash, tuple);
			latestTrajectoryElementMap.put(id, trajectoryElement);

			List<TrajectoryElement> geoHashList = this.pointMap.get(geoHash);
			if (geoHashList == null) {
				/*
				 * Probably we will only have one element in here as two objects
				 * on the same location are unlikely (but depends on the
				 * scenario)
				 */
				geoHashList = new ArrayList<>(1);
			}

			// Add the new element to the list
			geoHashList.add(trajectoryElement);
			this.pointMap.put(geoHash, geoHashList);

			// Calculate the new total distance of this trajectory
			double newDistance = (movingObjectDistances.get(id) == null ? 0.0 : movingObjectDistances.get(id))
					+ trajectoryElement.getDistanceToPreviousElement();
			movingObjectDistances.put(id, newDistance);

			// Search for the last but one element
			TrajectoryElement element = trajectoryElement;
			while (element != null && element.getPreviousElement() != null
					&& element.getPreviousElement().getPreviousElement() != null) {
				element = element.getPreviousElement();
			}

			if (newDistance - element.getDistanceToPreviousElement() > distancePerMovingObject) {
				// We can delete the last element
				element.setPreviousElement(null);
				movingObjectDistances.put(id, newDistance - element.getDistanceToPreviousElement());
			}
		}
	}

	@Override
	public Map<String, List<TrajectoryElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t) {

		// Get the rectangular envelope for the circle
		Envelope env = MetrticSpatialUtils.getInstance().getEnvelopeForRadius(geometry.getCentroid().getCoordinate(),
				radius);
		GeometryFactory factory = new GeometryFactory(geometry.getPrecisionModel(), geometry.getSRID());
		Point topLeft = factory.createPoint(new Coordinate(env.getMaxX(), env.getMaxY()));
		Point lowerRight = factory.createPoint(new Coordinate(env.getMinX(), env.getMinY()));

		// Get all elements within that bounding box (filter step, just an
		// approximation)
		Map<GeoHash, List<Tuple<ITimeInterval>>> candidateCollection = approximateBoundinBox(
				GeoHashHelper.createPolygon(GeoHashHelper.createBox(topLeft, lowerRight)));

		return queryCircle(geometry, radius, t, candidateCollection);

	}

	/**
	 * A circle query with a given candidate collection.
	 * 
	 * @param geometry
	 *            The center of the circle to query
	 * @param radius
	 *            The radius of the circle to query
	 * @param t
	 *            The time interval that the returned elements have to intersect
	 * @param candidateCollection
	 *            The list of candidates. Should cover the whole are from the
	 *            circle you want to query. You save a lookup of the candidates
	 *            with this method, e.g., if you already have the list of
	 *            candidates.
	 * @return All elements from the candidates that are in the given circle.
	 */
	public Map<String, List<TrajectoryElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t,
			Map<GeoHash, List<Tuple<ITimeInterval>>> candidateCollection) {

		MetrticSpatialUtils spatialUtils = MetrticSpatialUtils.getInstance();

		// TODO Do performance tests? stream vs parallelStream vs foreach
		// (http://stackoverflow.com/questions/18290935/flattening-a-collection)
		// TODO Maybe faster with Haversine. Give choice?
		// TODO Think about: maybe it is faster to first check if the point is
		// within a polygon (cheap) and reduce the number of distance
		// calculations (expensive)

		List<GeoHash> result = candidateCollection.keySet().stream()
				/*
				 * spatial filter (it's enough to do it only with the first
				 * element as all elements in this list are on the exact same
				 * position (have the same geohash)
				 */
				.filter(e -> spatialUtils.calculateDistance(null, geometry.getCentroid().getCoordinate(),
						new Coordinate(e.getPoint().getLongitude(), e.getPoint().getLatitude())) <= radius)
				/*
				 * Write result into a list
				 */
				.collect(Collectors.toList());

		return convertToResultMap(result);
	}

	public Map<String, List<TrajectoryElement>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {
		Polygon polygon = GeoHashHelper.createPolygon(polygonPoints);

		// Get all hashes that we have to calculate the distance for
		Map<GeoHash, List<Tuple<ITimeInterval>>> allHashes = approximateBoundinBox(polygon);

		GeometryFactory factory = new GeometryFactory();

		// For every point in our list ask JTS if the points lies within the
		// polygon
		// TODO Do performance tests? stream vs parallelStream vs foreach
		// (http://stackoverflow.com/questions/18290935/flattening-a-collection)
		List<GeoHash> result = allHashes.keySet().stream()
				/*
				 * spatial filter (it's enough to do it only with the first
				 * element as all elements in this list are on the exact same
				 * position (have the same geohash)
				 */
				.filter(e -> polygon.contains(
						factory.createPoint(new Coordinate(e.getPoint().getLongitude(), e.getPoint().getLatitude()))))
				/*
				 * Write result into a list
				 */
				.collect(Collectors.toList());

		return convertToResultMap(result);
	}

	@SuppressWarnings("unchecked")
	protected Map<GeoHash, List<Tuple<ITimeInterval>>> approximateBoundinBox(Polygon polygon) {
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
			allHashes.putAll((Map<? extends GeoHash, ? extends List<Tuple<ITimeInterval>>>) GeoHashHelper
					.getByPreffix(hash, pointMap));
		}
		return allHashes;
	}

	private Map<String, List<TrajectoryElement>> convertToResultMap(List<GeoHash> unsortedList) {
		Map<String, List<TrajectoryElement>> resultMap = new HashMap<>();

		for (GeoHash hash : unsortedList) {
			for (TrajectoryElement element : pointMap.get(hash)) {
				List<TrajectoryElement> listOfMOInBoundingBox = resultMap.get(element.getMovingObjectID());
				if (listOfMOInBoundingBox == null) {
					listOfMOInBoundingBox = new ArrayList<TrajectoryElement>();
					resultMap.put(element.getMovingObjectID(), listOfMOInBoundingBox);
				}
				listOfMOInBoundingBox.add(element);
			}
		}

		return resultMap;
	}

	private String getMovingObjectId(Tuple<?> tuple) {
		Object o = tuple.getAttribute(this.idPosition);
		return o.toString();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Map<String, TrajectoryElement> queryKNN(Geometry geometry, int k, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGeometryPosition() {
		return this.geometryAttributePosition;
	}

}
