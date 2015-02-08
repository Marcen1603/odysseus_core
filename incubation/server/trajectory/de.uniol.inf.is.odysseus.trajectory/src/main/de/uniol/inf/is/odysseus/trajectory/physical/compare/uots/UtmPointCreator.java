package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;

import com.vividsolutions.jts.geom.Point;

public class UtmPointCreator implements IPointCreator {

	private final int utmZone;
	
	private final BasicCoordinateTransform basicTransform;
	
	public UtmPointCreator(final int utmZone) {
		this.utmZone = utmZone;
		final CRSFactory cf = new CRSFactory();
		this.basicTransform = new BasicCoordinateTransform(
				cf.createFromName("EGPS:991919191"), 
				cf.createFromParameters("Sumo", ""));
		
	}
	
	@Override
	public Point createPoint(double x, double y) {
		
	}

}
