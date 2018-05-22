package de.uniol.inf.odysseus.spatiotemporal.types.region;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

public class LinearMovingRegionFunction implements TemporalFunction<GeometryWrapper> {

	private List<TemporalFunction<GeometryWrapper>> movingPoints;
	private GeometryFactory geometryFactory;

	public LinearMovingRegionFunction(List<TemporalFunction<GeometryWrapper>> movingPoints) {
		this.movingPoints = movingPoints;
		this.geometryFactory = new GeometryFactory();
	}
	
	public LinearMovingRegionFunction(LinearMovingRegionFunction other) {
		this.movingPoints = other.movingPoints;
		this.geometryFactory = new GeometryFactory();
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {

		Coordinate[] points = new Coordinate[movingPoints.size()];
		int i = 0;
		for (TemporalFunction<GeometryWrapper> tpoint : movingPoints) {
			points[i] = tpoint.getValue(time).getGeometry().getCoordinate();
			i++;
		}

		/*
		 * This is maybe an old solution, but the JTS version in Odysseus is old and I
		 * don't want to break old code.
		 * 
		 * https://stackoverflow.com/questions/6570017/how-to-create-a-polygon-in-jts-
		 * when-we-have-list-of-coordinate
		 */
		LinearRing linear = geometryFactory.createLinearRing(points);
		Polygon poly = new Polygon(linear, null, geometryFactory);

		return new GeometryWrapper(poly);
	}
	
	@Override
	public LinearMovingRegionFunction clone() {
		return new LinearMovingRegionFunction(this);
	}
	
	@Override
	public String toString() {
		String sample = movingPoints.size() > 0 ? movingPoints.get(0).toString() : "";
		return movingPoints.size() + " vertices; sample-function: " + sample;
	}

}
