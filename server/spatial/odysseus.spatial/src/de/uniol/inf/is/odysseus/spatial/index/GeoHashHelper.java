package de.uniol.inf.is.odysseus.spatial.index;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashBoundingBoxQuery;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

public class GeoHashHelper {

	/**
	 * Get the geometry from a tuple
	 * 
	 * @param tuple
	 *            The tuple with the geometry
	 * @return The geometry
	 */
	public static Geometry getGeometry(Tuple<?> tuple, int geometryPosition) {
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

	public static SortedMap<GeoHash, ?> getByPreffix(GeoHash preffix, NavigableMap<GeoHash, ?> searchMap) {
		// We have to add 1 to the given hash, as we search all between the
		// given hash and the next one
		return searchMap.subMap(preffix, preffix.next());
	}

	public static GeoHash fromGeometry(Geometry geometry, int numberOfBits) {
		Point centroid = geometry.getCentroid();
		GeoHash hash = GeoHash.withBitPrecision(centroid.getX(), centroid.getY(), numberOfBits);
		return hash;
	}

	public static GeoHash fromLatLong(double latitude, double longitude, int numberOfBits) {
		return GeoHash.withBitPrecision(latitude, longitude, numberOfBits);
	}

	/**
	 * 
	 * @param centerLatitude
	 * @param centerLongitude
	 * @param radius
	 * @param srid
	 * @return A list of GeoHashes that fully cover the area defined by the center
	 *         and the radius. Could cover more (likely!) as this is only an
	 *         approximation. Probably more the shape of a box.
	 */
	public static List<GeoHash> approximateCircle(double centerLatitude, double centerLongitude, double radius,
			int srid) {
		// Get the rectangular envelope for the circle
		Envelope env = MetricSpatialUtils.getInstance().getEnvelopeForRadius(centerLatitude, centerLongitude, radius);
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), srid);
		Point topLeft = factory.createPoint(new Coordinate(env.getMaxX(), env.getMaxY()));
		Point lowerRight = factory.createPoint(new Coordinate(env.getMinX(), env.getMinY()));

		// Get the hashes which approximately cover the given area
		List<GeoHash> geoHashes = GeoHashHelper
				.approximateBoundingBox(GeoHashHelper.createPolygon(GeoHashHelper.createBox(topLeft, lowerRight)));
		return geoHashes;
	}

	/**
	 * 
	 * @param polygon
	 *            The bounding box. First and third coordinate (upper left, lower
	 *            right) are used to create the bounding box for GeoHash
	 *            calculation.
	 * @return A list of hashes that fully cover the given bounding box. Could cover
	 *         more, this is only an approximation!
	 */
	public static List<GeoHash> approximateBoundingBox(Polygon polygon) {
		Geometry envelope = polygon.getEnvelope();
		WGS84Point point1 = new WGS84Point(envelope.getCoordinates()[0].x, envelope.getCoordinates()[0].y);
		WGS84Point point2 = new WGS84Point(envelope.getCoordinates()[2].x, envelope.getCoordinates()[2].y);
		BoundingBox bBox = new BoundingBox(point1, point2);
		GeoHashBoundingBoxQuery bbQuery = new GeoHashBoundingBoxQuery(bBox);
		List<GeoHash> searchHashes = bbQuery.getSearchHashes();
		return searchHashes;
	}

	public static List<Point> createBox(Point topLeft, Point lowerRight) {
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

	public static Polygon createPolygon(List<Point> polygonPoints) {
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

}
