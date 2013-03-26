package de.uniol.inf.is.odysseus.debs2013.spatial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public abstract class ShotOnGoalDetection extends AbstractFunction<Boolean>{

	private static final long serialVersionUID = 8159082937727522598L;

	private LineString goal;
		
	@Override
	public int getArity() {
		return 2;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
    	SDFSpatialDatatype.SPATIAL_POINT
	};

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	@Override
	public Boolean getValue() {
		GeometryFactory gf = new GeometryFactory();
		Point startPoint = (Point) getInputValue(0);
		Point endPoint = (Point) getInputValue(1);
		endPoint.getCoordinate().x += (endPoint.getCoordinate().x - startPoint.getCoordinate().x) * 10000;
		endPoint.getCoordinate().y += (endPoint.getCoordinate().y - startPoint.getCoordinate().y) * 10000;
		LineString shot = gf.createLineString(new Coordinate[] { startPoint.getCoordinate(), endPoint.getCoordinate() });
		System.out.println(shot.toString());
		return shot.intersects(goal);
		
	 }

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}
	
	public void setGoal(LineString goal) {
		this.goal = goal;
	}
}
