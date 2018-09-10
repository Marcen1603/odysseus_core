package de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.vividsolutions.jts.index.quadtree.Quadtree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

@Deprecated
public class QuadTreeSTDataStructure implements ISpatioTemporalDataStructure {

	public static final String TYPE = "quadtree";

	protected Quadtree quadTree;
	private int geometryPosition;
	private String name;
	private PointInTime previousEndTS;
	private PointInTime previousStartTS;
	protected DefaultTISweepArea<Tuple<ITimeInterval>> sweepArea;

	/*
	 * We want to avoid to calculate the polygon on every query. Therefore we
	 * store the polygon for a certain list of coordinates. If we have the same
	 * coordinates we can just use the existing polygon. This could especially
	 * be time-saving when we always query for the same polygon.
	 * 
	 * But we need to be careful: If we always change the polygon, the map could
	 * get huge. Therefore we should completely delete it once in a while to
	 * avoid overflows.
	 * 
	 */
	private Map<Integer, Polygon> queryPolygons;
	private static final int MAX_NUMBER_POLYGON = 1000;

	private static Logger _logger = LoggerFactory.getLogger(FastQuadTreeSTDataStructure.class);

	public QuadTreeSTDataStructure(String name, int geometryPosition) {
		this.quadTree = new Quadtree();
		this.geometryPosition = geometryPosition;
		this.name = name;
		this.sweepArea = new DefaultTISweepArea<>();
		this.queryPolygons = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;

			// Check the order
			checkEndTSOrder(tuple);
			checkStartTSOrder(tuple);

			// Insert in QuadTree
			Geometry geom = getGeometry((Tuple<ITimeInterval>) o);
			this.quadTree.insert(geom.getEnvelopeInternal(), o);

			// Insert in SweepArea
			this.sweepArea.insert(tuple);
		}
	}

	private void checkEndTSOrder(Tuple<ITimeInterval> tuple) {
		// Check if we are out of order
		if (previousEndTS == null) {
			previousEndTS = tuple.getMetadata().getEnd();
		}

		if (previousEndTS.after(tuple.getMetadata().getEnd())) {
			_logger.debug("End-Timestamps not ordered!");
			_logger.debug("Previous: " + previousEndTS + " current: " + tuple.getMetadata().getEnd());
		}
		previousEndTS = tuple.getMetadata().getEnd();
	}

	private void checkStartTSOrder(Tuple<ITimeInterval> tuple) {
		// Check if we are out of order
		if (previousStartTS == null) {
			previousStartTS = tuple.getMetadata().getStart();
		}

		if (previousStartTS.after(tuple.getMetadata().getStart())) {
			_logger.debug("Start-Timestamps not ordered!");
			_logger.debug("Previous: " + previousStartTS + " current: " + tuple.getMetadata().getStart());
		}
		previousStartTS = tuple.getMetadata().getStart();
	}

	@Override
	public void cleanUp(PointInTime timestamp) {
		// Remove old elements from sweepArea
		List<Tuple<ITimeInterval>> removed = this.sweepArea.extractElementsBeforeAsList(timestamp);

		// Warn
		if (removed.size() > 50000) {
			_logger.warn("Remove " + removed.size() + " elements from QuadTree. This can take a while!");
		}

		// Remove the extracted elements from the quadTree
		removed.stream().forEach(e -> this.quadTree.remove(getGeometry(e).getEnvelopeInternal(), e));
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<Tuple<ITimeInterval>> queryKNN(Geometry geometry, int k, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tuple<ITimeInterval>> queryCircle(Geometry geometry, double rangeMeters, ITimeInterval t) {

		// Get the rectangular envelope for the circle
		Envelope env = MetricSpatialUtils.getInstance().getEnvelopeForRadius(geometry.getCoordinate(), rangeMeters);

		// Query the quadTree for all objects that may be in the envelope
		List<Tuple<ITimeInterval>> envelopeItems = quadTree.query(env);

		List<Tuple<ITimeInterval>> result = envelopeItems.parallelStream()
				// spatial filter
				.filter(e -> e != null && MetricSpatialUtils.getInstance().calculateDistance(geometry.getCoordinate(),
						getGeometry((Tuple<ITimeInterval>) e).getCoordinate()) <= rangeMeters)
				// temporal filter
				.filter(f -> f.getMetadata().getStart().before(t.getEnd())
						&& f.getMetadata().getEnd().after(t.getStart()))
				.collect(Collectors.toList());

		return (List<Tuple<ITimeInterval>>) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tuple<ITimeInterval>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {

		// Check if the first and the last point is equal. If not, we have to
		// add the first point at the end to have a closed ring.
		Point firstPoint = polygonPoints.get(0);
		Point lastPoint = polygonPoints.get(polygonPoints.size() - 1);
		if (!firstPoint.equals(lastPoint)) {
			polygonPoints.add(firstPoint);
		}

		// Create an envelope with all the coordinates of the polygon
		Envelope env = new Envelope();
		polygonPoints.stream().forEach(c -> env.expandToInclude(c.getCoordinate()));

		// Query the quadTree for all objects that may be in the envelope
		List<Tuple<ITimeInterval>> envelopeItems = quadTree.query(env);

		/*
		 * Create the actual polygon we search for: 1. We already have
		 * calculated the polygon in the past -> use polygon from the map 2. We
		 * did not calculate it in the past -> create a new polygon and save it
		 * in the map
		 */
		int hashCode = polygonPoints.hashCode();
		Polygon polygon = null;

		if (queryPolygons.containsKey(hashCode)) {
			polygon = queryPolygons.get(hashCode);
		} else {
			GeometryFactory factory = new GeometryFactory();
			LinearRing ring = factory.createLinearRing(
					polygonPoints.stream().map(p -> p.getCoordinate()).toArray(size -> new Coordinate[size]));
			polygon = factory.createPolygon(ring, null);
			queryPolygons.put(hashCode, polygon);

			// Clean up if the HashMap gets too big
			if (queryPolygons.size() > MAX_NUMBER_POLYGON) {
				this.queryPolygons = new HashMap<>();
			}
		}

		final Polygon queryPolygon = polygon;
		// Filter for the objects which are really in the polygon
		List<Tuple<ITimeInterval>> result = envelopeItems.parallelStream()
				// spatial filter
				.filter(e -> queryPolygon.contains(getGeometry(e)))
				// temporal filter
				.filter(f -> f.getMetadata().getStart().before(t.getEnd())
						&& f.getMetadata().getEnd().after(t.getStart()))
				.collect(Collectors.toList());

		return (List<Tuple<ITimeInterval>>) result;
	}

	@Override
	public int getGeometryPosition() {
		return this.geometryPosition;
	}

	protected Geometry getGeometry(Tuple<ITimeInterval> tuple) {
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
}
