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
		return 4;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
    	SDFSpatialDatatype.SPATIAL_POINT,
    	SDFDatatype.LONG
	};

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	@Override
	public Boolean getValue() {
		GeometryFactory gf = new GeometryFactory();
		Coordinate startCoordinate = ((Point) getInputValue(0)).getCoordinate();
		Coordinate endCoordinate = ((Point) getInputValue(1)).getCoordinate();	
		Long tsShot = (Long) getInputValue(2);
		Long ts = (Long) getInputValue(3);
		if((ts - tsShot) != 0) {
			Double speedX = (endCoordinate.x - startCoordinate.x) / (ts - tsShot);
			Double speedY = (endCoordinate.y - startCoordinate.y) / (ts - tsShot);
			// Progrnose der Flubbahn in 1,5 Sekunden = 1500000000000 Picosekunden
			Coordinate newEndCoordinate = new Coordinate( endCoordinate.x + (speedX * 1500000000000.0 * 2), 
															endCoordinate.y + (speedY * 1500000000000.0 * 2));
			LineString shot = gf.createLineString(new Coordinate[] { startCoordinate, newEndCoordinate });
			return shot.intersects(goal);
		} else {
			return false;
		}
	 }

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}
	
	public void setGoal(LineString goal) {
		this.goal = goal;
	}
}
