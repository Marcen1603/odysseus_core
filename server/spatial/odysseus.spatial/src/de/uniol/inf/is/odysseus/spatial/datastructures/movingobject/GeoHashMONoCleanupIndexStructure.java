package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.geotools.referencing.GeodeticCalculator;

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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datastructures.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

/**
 * Difference: Removed the idea of distance as a window predicate and do not
 * remove old elements. The decision, when to create a totally new index
 * structure is up to the user of this index.
 * 
 * @author Tobias Brandt
 *
 */
public class GeoHashMONoCleanupIndexStructure implements IMovingObjectDataStructure {

	private static final int BIT_PRECISION = 64;
	public static final String TYPE = "mo_no_cleanup_geohash";

	// The position of the geometry-attribute within the tuple
	private int geometryAttributePosition;

	// The name of the data structure to access it
	private String name;

	private NavigableMap<GeoHash, List<TrajectoryElement>> pointMap;
	private Map<String, TrajectoryElement> latestTrajectoryElementMap;

	public GeoHashMONoCleanupIndexStructure(String name, int geometryPosition) {
		this.name = name;
		this.geometryAttributePosition = geometryPosition;

		this.pointMap = new TreeMap<>();
		this.latestTrajectoryElementMap = new HashMap<>();
	}

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {
		// Get info for new TrajectoryElement
		GeoHash geoHash = GeoHashHelper.fromLatLong(locationMeasurement.getLatitude(),
				locationMeasurement.getLongitude(), BIT_PRECISION);
		String id = locationMeasurement.getMovingObjectId();

		// Get the previously latest element from that trajectory
		TrajectoryElement latestElement = latestTrajectoryElementMap.get(id);

		// Create the new "latest" element and save it
		TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, id, geoHash, streamElement);
		latestTrajectoryElementMap.put(id, trajectoryElement);

		List<TrajectoryElement> geoHashList = this.pointMap.get(geoHash);
		if (geoHashList == null) {
			/*
			 * Probably we will only have one element in here as two objects on the same
			 * location are unlikely (but depends on the scenario)
			 */
			geoHashList = new ArrayList<>(1);
		}

		// Add the new element to the list
		geoHashList.add(trajectoryElement);
		this.pointMap.put(geoHash, geoHashList);
	}

	@Override
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t) {
		return queryCircle(geometry, radius, t, null);
	}

	@Override
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t,
			String movingObjectIdToIgnore) {
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

		return queryCircle(geometry, radius, t, candidateCollection, movingObjectIdToIgnore);
	}

	private Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t,
			Map<GeoHash, List<Tuple<ITimeInterval>>> candidateCollection, String movingObjectIdToIgnore) {

		MetrticSpatialUtils spatialUtils = MetrticSpatialUtils.getInstance();

		// TODO Maybe faster with Haversine. Give choice?
		// TODO Think about: maybe it is faster to first check if the point is
		// within a polygon (cheap) and reduce the number of distance
		// calculations (expensive)

		Map<String, List<ResultElement>> resultMap = new HashMap<>();

		// Check all candidates
		for (GeoHash key : candidateCollection.keySet()) {

			double meters = spatialUtils.calculateDistance(null, geometry.getCentroid().getCoordinate(),
					new Coordinate(key.getPoint().getLatitude(), key.getPoint().getLongitude()));

			// Check if they are in the given radius
			if (meters <= radius) {
				// All elements that are at this point (key) need to be added to
				// the result
				for (TrajectoryElement element : pointMap.get(key)) {

					if (movingObjectIdToIgnore != null && !movingObjectIdToIgnore.isEmpty()
							&& element.getMovingObjectID().equals(movingObjectIdToIgnore)) {
						// This is probably the moving object we are searching for, hence, do not put it
						// into the results
						continue;
					}

					// Check if the result is within the given time interval
					if (element.getStreamElement().getMetadata() instanceof ITimeInterval) {
						ITimeInterval time = (ITimeInterval) element.getStreamElement().getMetadata();
						if (!(time.getStart().before(t.getEnd()) && time.getEnd().after(t.getStart()))) {
							// It's not in the right time interval
							continue;
						}
					}

					// Check if we already have results for this key
					List<ResultElement> listOfMOInRadius = resultMap.get(element.getMovingObjectID());
					if (listOfMOInRadius == null) {
						// No: Add a result list
						listOfMOInRadius = new ArrayList<ResultElement>();
						resultMap.put(element.getMovingObjectID(), listOfMOInRadius);
					}
					ResultElement resultElement = new ResultElement(element, meters);
					listOfMOInRadius.add(resultElement);
				}
			}
		}

		return resultMap;
	}

	public Map<String, List<ResultElement>> queryCircleTrajectory(String movingObjectID, double radius) {
		// TODO Maybe add start and end time for the query

		Map<String, List<ResultElement>> results = new HashMap<>();

		// Get the trajectory of the given moving object ID
		TrajectoryElement element = this.latestTrajectoryElementMap.get(movingObjectID);

		GeodeticCalculator calculator = new GeodeticCalculator();

		// For each trajectory element, make a whole circle query
		PointInTime measurementTime = element.getMeasurementTime();
		for (String otherMovingObjectID : this.latestTrajectoryElementMap.keySet()) {
			WGS84Point otherLocation = this.predictLocation(otherMovingObjectID, measurementTime);
			calculator.setStartingGeographicPoint(element.getLongitude(), element.getLatitude());
			calculator.setDestinationGeographicPoint(otherLocation.getLongitude(), otherLocation.getLatitude());
			double distanceMeters = calculator.getOrthodromicDistance();
			if (distanceMeters <= radius) {
				// Add to result map
				TrajectoryElement trajectoryElement = new TrajectoryElement(null, otherMovingObjectID, GeoHashHelper
						.fromLatLong(otherLocation.getLatitude(), otherLocation.getLongitude(), BIT_PRECISION), null);
				ResultElement resultElement = new ResultElement(trajectoryElement, distanceMeters);
				if (!results.containsKey(otherMovingObjectID)) {
					List<ResultElement> resultsForOneMovingObject = new ArrayList<>();
					results.put(otherMovingObjectID, resultsForOneMovingObject);
				}
				results.get(otherMovingObjectID).add(resultElement);
			}
		}

		return results;

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
				 * spatial filter (it's enough to do it only with the first element as all
				 * elements in this list are on the exact same position (have the same geohash)
				 */
				.filter(e -> polygon.contains(
						factory.createPoint(new Coordinate(e.getPoint().getLongitude(), e.getPoint().getLatitude()))))
				/*
				 * Write result into a list
				 */
				.collect(Collectors.toList());

		return convertToTrajectoryMap(result);
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

	private Map<String, List<TrajectoryElement>> convertToTrajectoryMap(List<GeoHash> unsortedList) {
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

	private WGS84Point predictLocation(String id, PointInTime time) {

		TrajectoryElement elementBefore = searchClostestElementBeforeOrEquals(id, time);
		TrajectoryElement elementAfter = searchClostestElementAfterOrEquals(id, time);

		// Use them to interpolate location
		// Calculate speed and direction

		// Distance
		double distanceMeters = MetrticSpatialUtils.getInstance().calculateDistance(null,
				new Coordinate(elementBefore.getLatitude(), elementBefore.getLongitude()),
				new Coordinate(elementAfter.getLatitude(), elementAfter.getLongitude()));

		// Time
		long timeDistanceMs = Math.abs(
				elementBefore.getMeasurementTime().getMainPoint() - elementAfter.getMeasurementTime().getMainPoint());

		// Speed
		double speedMetersPerSecond = distanceMeters / (timeDistanceMs * 1000);

		// Distance to interpolated point
		long timeBetweenStartAndPointMs = time.minus(elementBefore.getMeasurementTime()).getMainPoint();
		double distanceToPointMeters = speedMetersPerSecond * (timeBetweenStartAndPointMs / 1000);

		// Direction
		double startingLatitude = elementBefore.getLatitude();
		double startingLongitude = elementBefore.getLongitude();
		double destinationLatitude = elementAfter.getLatitude();
		double destinationLongitude = elementAfter.getLongitude();

		GeodeticCalculator calculator = new GeodeticCalculator();
		calculator.setStartingGeographicPoint(startingLongitude, startingLatitude);
		calculator.setDestinationGeographicPoint(destinationLongitude, destinationLatitude);
		// The azimuth, in decimal degrees from -180° to +180°.
		double azimuth = calculator.getAzimuth();

		calculator.setDirection(azimuth, distanceToPointMeters);
		Point2D interpolatedDestination = calculator.getDestinationGeographicPoint();
		double longitude = interpolatedDestination.getX();
		double latitude = interpolatedDestination.getY();

		WGS84Point resultPoint = new WGS84Point(latitude, longitude);
		return resultPoint;
	}

	private TrajectoryElement searchClostestElementBeforeOrEquals(String id, PointInTime time) {
		TrajectoryElement iterator = this.latestTrajectoryElementMap.get(id);
		while (iterator != null && iterator.getMeasurementTime().after(time)) {
			iterator = iterator.getPreviousElement();
		}
		return iterator;
	}

	private TrajectoryElement searchClostestElementAfterOrEquals(String id, PointInTime time) {
		TrajectoryElement iterator = this.latestTrajectoryElementMap.get(id);
		while (iterator != null && iterator.getMeasurementTime().before(time)) {
			iterator = iterator.getNextElement();
		}
		return iterator;
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

	@Override
	public Set<String> getAllMovingObjectIds() {
		return this.latestTrajectoryElementMap.keySet();
	}

}
