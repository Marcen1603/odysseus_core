package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.SpatioTemporalQueryResult;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

/**
 * Difference: Removed the idea of distance as a window predicate and do not
 * remove old elements. The decision, when to create a totally new index
 * structure is up to the user of this index.
 * 
 * @author Tobias Brandt
 *
 */
@Deprecated
public class MONoCleanupNoPointMapIndexStructure implements MovingObjectIndexOld {

	public static final int BIT_PRECISION = 64;
	public static final String TYPE = "mo_no_cleanup_geohash";

	// The position of the geometry-attribute within the tuple
	private int geometryAttributePosition;

	// The name of the data structure to access it
	private String name;

	// protected NavigableMap<GeoHash, List<TrajectoryElement>> pointMap;
	protected Map<String, TrajectoryElement> latestTrajectoryElementMap;

	public MONoCleanupNoPointMapIndexStructure(String name, int geometryPosition) {
		this.name = name;
		this.geometryAttributePosition = geometryPosition;

		// this.pointMap = new TreeMap<>();
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
		TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, id, geoHash,
				locationMeasurement.getMeasurementTime(), streamElement);
		latestTrajectoryElementMap.put(id, trajectoryElement);

		// List<TrajectoryElement> geoHashList = this.pointMap.get(geoHash);
		// if (geoHashList == null) {
		// /*
		// * Probably we will only have one element in here as two objects on the same
		// * location are unlikely (but depends on the scenario)
		// */
		// geoHashList = new ArrayList<>(1);
		// }

		// Add the new element to the list
		// geoHashList.add(trajectoryElement);
		// this.pointMap.put(geoHash, geoHashList);
	}

	@Override
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t) {
		return queryCircle(geometry, radius, t, null);
	}

	@Override
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t,
			String movingObjectIdToIgnore) {
		// Get the rectangular envelope for the circle
		Envelope env = MetricSpatialUtils.getInstance().getEnvelopeForRadius(geometry.getCentroid().getCoordinate(),
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

		// MetrticSpatialUtils spatialUtils = MetrticSpatialUtils.getInstance();
		//
		// // TODO Maybe faster with Haversine. Give choice?
		// // TODO Think about: maybe it is faster to first check if the point is
		// // within a polygon (cheap) and reduce the number of distance
		// // calculations (expensive)
		//
		// Map<String, List<ResultElement>> resultMap = new HashMap<>();
		//
		// // Check all candidates
		// for (GeoHash key : candidateCollection.keySet()) {
		//
		// double meters = spatialUtils.calculateDistance(null,
		// geometry.getCentroid().getCoordinate(),
		// new Coordinate(key.getPoint().getLatitude(), key.getPoint().getLongitude()));
		//
		// // Check if they are in the given radius
		// if (meters <= radius) {
		// // All elements that are at this point (key) need to be added to
		// // the result
		// for (TrajectoryElement element : pointMap.get(key)) {
		//
		// if (movingObjectIdToIgnore != null && !movingObjectIdToIgnore.isEmpty()
		// && element.getMovingObjectID().equals(movingObjectIdToIgnore)) {
		// // This is probably the moving object we are searching for, hence, do not put
		// it
		// // into the results
		// continue;
		// }
		//
		// // Check if the result is within the given time interval
		// if (element.getStreamElement().getMetadata() instanceof ITimeInterval) {
		// ITimeInterval time = (ITimeInterval)
		// element.getStreamElement().getMetadata();
		// if (!(time.getStart().before(t.getEnd()) &&
		// time.getEnd().after(t.getStart()))) {
		// // It's not in the right time interval
		// continue;
		// }
		// }
		//
		// // Check if we already have results for this key
		// List<ResultElement> listOfMOInRadius =
		// resultMap.get(element.getMovingObjectID());
		// if (listOfMOInRadius == null) {
		// // No: Add a result list
		// listOfMOInRadius = new ArrayList<ResultElement>();
		// resultMap.put(element.getMovingObjectID(), listOfMOInRadius);
		// }
		// ResultElement resultElement = new ResultElement(element, meters);
		// listOfMOInRadius.add(resultElement);
		// }
		// }
		// }
		//
		// return resultMap;
		return null;
	}

	@Override
	public Map<String, SpatioTemporalQueryResult> queryCircle(String movingObjectID, double radius) {
		Map<String, SpatioTemporalQueryResult> results = new HashMap<>();

		// Get the latest known location of the moving object to search the neighbors
		// for
		TrajectoryElement centerElement = this.latestTrajectoryElementMap.get(movingObjectID);
		if (centerElement == null) {
			return results;
		}

		GeodeticCalculator calculator = new GeodeticCalculator();
		PointInTime measurementTime = centerElement.getMeasurementTime();

		/*
		 * For all other objects make a prediction for the time of the center object and
		 * see, if they are close enough
		 */
		for (String otherMovingObjectID : this.latestTrajectoryElementMap.keySet()) {
			if (otherMovingObjectID.equals(movingObjectID)) {
				continue;
			}
			WGS84Point otherLocation = this.predictLocation(otherMovingObjectID, measurementTime);
			calculator.setStartingGeographicPoint(centerElement.getLongitude(), centerElement.getLatitude());
			calculator.setDestinationGeographicPoint(otherLocation.getLongitude(), otherLocation.getLatitude());
			double distanceMeters = calculator.getOrthodromicDistance();
			if (distanceMeters <= radius) {
				// Add to result map
				SpatioTemporalQueryResult queryResultElement = new SpatioTemporalQueryResult(distanceMeters,
						new WGS84Point(centerElement.getLatitude(), centerElement.getLongitude()), otherLocation,
						measurementTime);
				results.put(otherMovingObjectID, queryResultElement);
			}
		}

		return results;
	}

	@Override
	public Map<String, List<SpatioTemporalQueryResult>> queryCircleTrajectory(String movingObjectID, double radius) {
		// TODO Maybe add start and end time for the query?

		Map<String, List<SpatioTemporalQueryResult>> results = new HashMap<>();

		// Get the trajectory of the given moving object ID
		TrajectoryElement centerElement = this.latestTrajectoryElementMap.get(movingObjectID);
		if (centerElement == null) {
			return results;
		}

		GeodeticCalculator calculator = new GeodeticCalculator();

		// For each trajectory element, make a whole circle query
		do {
			PointInTime measurementTime = centerElement.getMeasurementTime();
			for (String otherMovingObjectID : this.latestTrajectoryElementMap.keySet()) {
				if (otherMovingObjectID.equals(movingObjectID)) {
					// The own trajectory doesn't count, we will be close to ourself
					continue;
				}
				WGS84Point otherLocation = this.predictLocation(otherMovingObjectID, measurementTime);
				calculator.setStartingGeographicPoint(centerElement.getLongitude(), centerElement.getLatitude());
				calculator.setDestinationGeographicPoint(otherLocation.getLongitude(), otherLocation.getLatitude());
				double distanceMeters = calculator.getOrthodromicDistance();
				if (distanceMeters <= radius) {
					// Add to result map
					SpatioTemporalQueryResult queryResultElement = new SpatioTemporalQueryResult(distanceMeters,
							new WGS84Point(centerElement.getLatitude(), centerElement.getLongitude()), otherLocation,
							measurementTime);
					if (!results.containsKey(otherMovingObjectID)) {
						List<SpatioTemporalQueryResult> resultsForOneMovingObject = new ArrayList<>();
						results.put(otherMovingObjectID, resultsForOneMovingObject);
					}
					results.get(otherMovingObjectID).add(queryResultElement);
				}
			}

			// Go on with the previous / older element we know
			centerElement = centerElement.getPreviousElement();
		} while (centerElement != null);

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

	protected Map<GeoHash, List<Tuple<ITimeInterval>>> approximateBoundinBox(Polygon polygon) {
		// Geometry envelope = polygon.getEnvelope();
		//
		// // Get hashes that we have to search for
		// // TODO This is guessed. See which coordinate is which. ->
		// // First test seems to be OK. Other possibility: expand BoundingBoxQuery
		// // with all polygonPoints
		// WGS84Point point1 = new WGS84Point(envelope.getCoordinates()[0].x,
		// envelope.getCoordinates()[0].y);
		// WGS84Point point2 = new WGS84Point(envelope.getCoordinates()[2].x,
		// envelope.getCoordinates()[2].y);
		// BoundingBox bBox = new BoundingBox(point1, point2);
		// GeoHashBoundingBoxQuery bbQuery = new GeoHashBoundingBoxQuery(bBox);
		// List<GeoHash> searchHashes = bbQuery.getSearchHashes();
		//
		// // Get all hashes that we have to calculate the distance for
		// Map<GeoHash, List<Tuple<ITimeInterval>>> allHashes = new HashMap<>();
		// for (GeoHash hash : searchHashes) {
		// // Query all our hashes and use those which have the same prefix
		// // The whole list of hashes is used in "getByPrefix"
		// allHashes.putAll((Map<? extends GeoHash, ? extends
		// List<Tuple<ITimeInterval>>>) GeoHashHelper
		// .getByPreffix(hash, pointMap));
		// }
		// return allHashes;
		return null;
	}

	private Map<String, List<TrajectoryElement>> convertToTrajectoryMap(List<GeoHash> unsortedList) {
		// Map<String, List<TrajectoryElement>> resultMap = new HashMap<>();
		//
		// for (GeoHash hash : unsortedList) {
		// for (TrajectoryElement element : pointMap.get(hash)) {
		// List<TrajectoryElement> listOfMOInBoundingBox =
		// resultMap.get(element.getMovingObjectID());
		// if (listOfMOInBoundingBox == null) {
		// listOfMOInBoundingBox = new ArrayList<TrajectoryElement>();
		// resultMap.put(element.getMovingObjectID(), listOfMOInBoundingBox);
		// }
		// listOfMOInBoundingBox.add(element);
		// }
		// }
		//
		// return resultMap;
		return null;
	}

	private WGS84Point predictLocation(String id, PointInTime time) {

		TrajectoryElement elementBefore = searchClostestElementBeforeOrEquals(id, time);
		TrajectoryElement elementAfter = searchClostestElementAfterOrEquals(id, time);

		if (elementBefore != null && elementBefore == elementAfter) {
			// We have the exact location in our index, use it
			return new WGS84Point(elementBefore.getLatitude(), elementBefore.getLongitude());
		} else if (elementBefore == null) {
			/*
			 * We want to predict a location in the past, before we know anything: use the
			 * first two known elements to calculate speed and direction
			 */
			elementBefore = elementAfter;
			elementAfter = searchClostestElementAfterOrEquals(id, elementBefore.getMeasurementTime().plus(1));
			if (elementAfter == null) {
				/*
				 * We only have one location in the trajectory, we cannot say anything about the
				 * speed or direction -> we have to return the only known location
				 */
				return new WGS84Point(elementBefore.getLatitude(), elementBefore.getLongitude());
			}
		} else if (elementAfter == null) {
			/*
			 * We want to predict a location in the future: use the two last elements to
			 * calculate speed and direction
			 */

			/*
			 * elementBefore is the last known location -> use if as the elementAfter and
			 * put the last but one location as the elementBefore
			 */
			elementAfter = elementBefore;
			elementBefore = searchClostestElementBeforeOrEquals(id, elementAfter.getMeasurementTime().plus(-1));
			if (elementBefore == null) {
				/*
				 * We only have one location in the trajectory, we cannot say anything about the
				 * speed or direction -> we have to return the only known location
				 */
				return new WGS84Point(elementAfter.getLatitude(), elementAfter.getLongitude());
			}
		}

		// Use them to interpolate location
		// Calculate speed and direction

		// Distance
		double distanceMeters = MetricSpatialUtils.getInstance().calculateDistance(null,
				new Coordinate(elementBefore.getLatitude(), elementBefore.getLongitude()),
				new Coordinate(elementAfter.getLatitude(), elementAfter.getLongitude()));

		// Time
		long timeDistanceMs = Math.abs(
				elementAfter.getMeasurementTime().getMainPoint() - elementBefore.getMeasurementTime().getMainPoint());

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

	@Override
	public Map<String, List<ResultElement>> queryCircleWOPrediction(double centerLatitude, double centerLongitude,
			double radius, TimeInterval t) {
		// TODO Implement this method
		return null;
	}

	@Override
	public Map<String, List<ResultElement>> queryCircleWOPrediction(String movingObjectID, double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<ResultElement>> queryCircleWOPrediction(String movingObjectCenterID, double radius,
			TimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

}
