package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.ISpatioTemporalDataStructure;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;

@SuppressWarnings("deprecation")
public class SpatialQueryPO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private ISpatioTemporalDataStructure dataStructure;
	private Polygon polygon;
//	private int geometryPosition;

	private DefaultTISweepArea<Tuple<ITimeInterval>> sweepArea;

	public SpatialQueryPO(ISpatioTemporalDataStructure dataStructure, List<Point> polygonPoints, int geometryPosition) {
		this.dataStructure = dataStructure;
		this.sweepArea = new DefaultTISweepArea<>();
//		this.geometryPosition = geometryPosition;
		
		// Create a polygon with the given points
		GeometryFactory factory = new GeometryFactory();
		LinearRing ring = factory.createLinearRing(
				polygonPoints.stream().map(p -> p.getCoordinate()).toArray(size -> new Coordinate[size]));
		this.polygon = factory.createPolygon(ring, null);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// As for now, a new input is just a trigger that the data structure has
		// changed. Therefore, we will put out the new result now.
		// Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) object;
		// List<Tuple<?>> result = dataStructure.queryBoundingBox(polygonPoints,
		// new TimeInterval(tuple.getMetadata().getStart(),
		// tuple.getMetadata().getEnd()));
		// List<Tuple<?>> result = dataStructure.queryBoundingBox(polygonPoints,
		// new TimeInterval(PointInTime.ZERO, PointInTime.INFINITY));
		// Tuple<?> newTuple = ((Tuple<?>) object).append(result);
		// transfer((T) newTuple);

		// I am not sure if we need a data structure for the case that the
		// *polygon does not move*.

		// Hold a list (sweeparea) of all tuples within the given polygon
		// Every time a new tuple arrives:
		// 1. purge elements before in list with timestamps from new tuple
		// 2. check, if new tuple is in the given polygon
		// if yes, add
		// else, not
		// if something changed -> Transfer new tuple with list

		Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) object;
		List<Tuple<ITimeInterval>> elements = sweepArea.extractElementsBeforeAsList(tuple.getMetadata().getStart());

		if (polygon.contains(getGeometry(tuple))) {
			// Insert new element to result list and send new tuple with this
			// list
			sweepArea.insert(tuple);
			transferList(object);
		} else if (elements.size() > 0) {
			// We removed something, so we need to transfer the new list as well
			transferList(object);
		}

	}

	@SuppressWarnings("unchecked")
	private void transferList(T object) {
		List<Tuple<ITimeInterval>> result = sweepArea.queryAllElementsAsList();
		Tuple<?> transferTuple = ((Tuple<?>) object).append(result);
		transfer((T) transferTuple);
	}

	private Geometry getGeometry(Tuple<?> tuple) {
		Object o = tuple.getAttribute(this.dataStructure.getGeometryPosition());
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
