package de.uniol.inf.is.odysseus.spatial.index;

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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class GeoHashIndex implements SpatialIndex {

	// The precision of the GeoHashes
	public static final int BIT_PRECISION = 64;

	// The latest locations of the moving objects
	protected NavigableMap<GeoHash, List<TrajectoryElement>> locationMap;

	/*
	 * The latest known location of each known moving object (ID of moving object ->
	 * latest known element of the trajectory)
	 */
	protected Map<String, TrajectoryElement> latestTrajectoryElementMap;

	public GeoHashIndex() {
		this.locationMap = new TreeMap<>();
		this.latestTrajectoryElementMap = new HashMap<>();
	}

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {

		// TODO How to clean up with window?

		// Get info for new TrajectoryElement
		GeoHash geoHash = GeoHashHelper.fromLatLong(locationMeasurement.getLatitude(),
				locationMeasurement.getLongitude(), BIT_PRECISION);
		String id = locationMeasurement.getMovingObjectId();

		// Get the previously latest element from that trajectory
		TrajectoryElement latestElement = this.latestTrajectoryElementMap.get(id);

		// Create the new "latest" element and save it
		TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, id, geoHash,
				locationMeasurement.getMeasurementTime(), streamElement);
		this.latestTrajectoryElementMap.put(id, trajectoryElement);

		// Remove previously last element from index
		if (latestElement != null) {
			List<TrajectoryElement> movingObjectList = this.locationMap.get(latestElement.getGeoHash());
			if (movingObjectList.size() <= 1) {
				// In case there's only one element at that point, we can remove the whole entry
				this.locationMap.remove(latestElement.getGeoHash());
			} else {
				/*
				 * There are more elements at this point (uhh, a crash). Only remove the element
				 * that belongs to this moving object id.
				 */
				List<TrajectoryElement> listWithoutElement = movingObjectList.stream()
						.filter(elem -> !elem.getMovingObjectID().equals(id)).collect(Collectors.toList());
				this.locationMap.put(latestElement.getGeoHash(), listWithoutElement);
			}
		}

		// Add new latest location to index
		List<TrajectoryElement> geoHashList = this.locationMap.get(geoHash);
		if (geoHashList == null) {
			/*
			 * Probably we will only have one element in here as two objects on the same
			 * location are unlikely (but depends on the scenario)
			 */
			geoHashList = new ArrayList<>(1);
		}

		// Add the new element to the list
		geoHashList.add(trajectoryElement);

		/*
		 * To have the double linked list correctly add the new element as the next
		 * element in the previous one
		 */
		if (latestElement != null) {
			latestElement.setNextElement(trajectoryElement);
		}
		this.locationMap.put(geoHash, geoHashList);
	}

	@Override
	public Map<String, ResultElement> queryCircleOnLatestElements(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval) {

		MetrticSpatialUtils spatialUtils = MetrticSpatialUtils.getInstance();

		// Get possible elements within the circle
		Map<String, TrajectoryElement> candidates = approximateCircleOnLatestElements(centerLatitude, centerLongitude,
				radius, interval);

		// Refine: calculate actual distance
		Map<String, ResultElement> results = new HashMap<>(candidates.size());
		for (String key : candidates.keySet()) {
			double meters = spatialUtils.calculateDistance(null, new Coordinate(centerLatitude, centerLongitude),
					new Coordinate(candidates.get(key).getLatitude(), candidates.get(key).getLongitude()));
			if (meters <= radius) {
				// The element is within the radius
				ResultElement result = new ResultElement(candidates.get(key), meters);
				results.put(key, result);
			}
		}

		return results;
	}

	@Override
	public Map<String, TrajectoryElement> approximateCircleOnLatestElements(double centerLatitude,
			double centerLongitude, double radius, TimeInterval interval) {
		// Get the rectangular envelope for the circle
		Coordinate centerCoord = new Coordinate(centerLatitude, centerLongitude);
		Envelope env = MetrticSpatialUtils.getInstance().getEnvelopeForRadius(centerCoord, radius);
		GeometryFactory factory = new GeometryFactory();
		Point topLeft = factory.createPoint(new Coordinate(env.getMaxX(), env.getMaxY()));
		Point lowerRight = factory.createPoint(new Coordinate(env.getMinX(), env.getMinY()));

		// Get all elements within that bounding box (filter step, just an
		// approximation)
		Map<GeoHash, List<TrajectoryElement>> candidateCollection = approximateBoundinBox(
				GeoHashHelper.createPolygon(GeoHashHelper.createBox(topLeft, lowerRight)));

		// Transform format of result
		Map<String, TrajectoryElement> result = new HashMap<>();
		for (GeoHash hash : candidateCollection.keySet()) {
			List<TrajectoryElement> elements = candidateCollection.get(hash);
			for (TrajectoryElement element : elements) {
				// Check for time interval if necessary
				if (interval == null) {
					result.put(element.getMovingObjectID(), element);
				} else if (interval.includes(element.getMeasurementTime())) {
					result.put(element.getMovingObjectID(), element);
				}
			}
		}
		return result;
	}

	@Override
	public TrajectoryElement getLatestLocationOfObject(String movingObjectID) {
		return this.latestTrajectoryElementMap.get(movingObjectID);
	}

	@SuppressWarnings("unchecked")
	protected Map<GeoHash, List<TrajectoryElement>> approximateBoundinBox(Polygon polygon) {
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
		Map<GeoHash, List<TrajectoryElement>> allHashes = new HashMap<>();
		for (GeoHash hash : searchHashes) {
			// Query all our hashes and use those which have the same prefix
			// The whole list of hashes is used in "getByPrefix"
			allHashes.putAll((Map<? extends GeoHash, ? extends List<TrajectoryElement>>) GeoHashHelper
					.getByPreffix(hash, this.locationMap));
		}
		return allHashes;
	}

}