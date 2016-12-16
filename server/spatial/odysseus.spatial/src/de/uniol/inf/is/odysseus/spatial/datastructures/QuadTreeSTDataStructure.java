package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;
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
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class QuadTreeSTDataStructure implements IMovingObjectDataStructure {

	public static final String TYPE = "quadtree";

	protected Quadtree quadTree;
	private int geometryPosition;
	private String name;
	protected ExtendedTISweepArea<Tuple<ITimeInterval>> sweepArea;

	private static Logger _logger = LoggerFactory.getLogger(FastQuadTreeSTDataStructure.class);

	public QuadTreeSTDataStructure(String name, int geometryPosition) {
		this.quadTree = new Quadtree();
		this.geometryPosition = geometryPosition;
		this.name = name;
		this.sweepArea = new ExtendedTISweepArea<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			
			// Remove old elements from sweepArea
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;			
			List<Tuple<ITimeInterval>> removed = this.sweepArea
					.extractElementsBeforeAsList(tuple.getMetadata().getStart());

			// Warn
			if (removed.size() > 50000) {
				_logger.warn("Remove " + removed.size() + " elements from QuadTree. This can take a while!");
			}

			// Remove the extracted elements from the quadTree
			removed.parallelStream().forEach(e -> this.quadTree.remove(getGeometry(e).getEnvelopeInternal(), e));

			// Insert in QuadTree
			Geometry geom = getGeometry((Tuple<?>) o);
			this.quadTree.insert(geom.getEnvelopeInternal(), o);

			// Insert in SweepArea
			this.sweepArea.insert(tuple);
		}
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
		return this.name;
	}

	@Override
	public List<Tuple<?>> getKNN(Geometry geometry, int k, ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tuple<?>> getNeighborhood(Geometry geometry, double rangeMeters, ITimeInterval t) {

		// Get the rectangular envelope for the circle
		Envelope env = MetrticSpatialUtils.getInstance().getEnvelopeForRadius(geometry.getCoordinate(), rangeMeters);

		// Query the quadTree for all objects that may be in the envelope
		List<?> envelopeItems = quadTree.query(env);

		List<?> result = envelopeItems.parallelStream().filter(e -> e != null && MetrticSpatialUtils.getInstance()
				.calculateDistance(geometry.getCoordinate(), getGeometry((Tuple<?>) e).getCoordinate()) <= rangeMeters)
				.collect(Collectors.toList());

		return (List<Tuple<?>>) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tuple<?>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {

		// Create an envelope with all the coordinates of the polygon
		Envelope env = new Envelope();
		polygonPoints.stream().forEach(c -> env.expandToInclude(c.getCoordinate()));

		// Query the quadTree for all objects that may be in the envelope
		List<?> envelopeItems = quadTree.query(env);

		// Create the actual polygon we search for
		// TODO We could use a hash for the polygon so that it is not necessary
		// to recalculate it every time
		GeometryFactory factory = new GeometryFactory();
		LinearRing ring = factory.createLinearRing(
				polygonPoints.stream().map(p -> p.getCoordinate()).toArray(size -> new Coordinate[size]));
		Polygon polygon = factory.createPolygon(ring, null);

		// Filter for the objects which are really in the polygon
		List<?> result = envelopeItems.parallelStream().filter(e -> polygon.contains(getGeometry((Tuple<?>) e)))
				.collect(Collectors.toList());

		return (List<Tuple<?>>) result;
	}

	@Override
	public int getGeometryPosition() {
		return this.geometryPosition;
	}

	protected Geometry getGeometry(Tuple<?> tuple) {
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
