package de.uniol.inf.is.odysseus.trajectory.compare.util;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class UtmPointCreator implements IPointCreator {

	private final int utmZone;
	
	private final BasicCoordinateTransform basicTransform;
	
	private final GeometryFactory gf = new GeometryFactory();
	
	public UtmPointCreator(final int utmZone) {
		this.utmZone = utmZone;
		final CRSFactory cf = new CRSFactory();
		this.basicTransform = new BasicCoordinateTransform(
				cf.createFromName("EPSG:4326"), 
				cf.createFromParameters("Sumo", "+proj=utm +zone=" + this.utmZone + " +ellps=WGS84 +datum=WGS84 +units=m +no_defs"));
		
	}
	
	@Override
	public Point createPoint(double x, double y) {
        final ProjCoordinate pcSrc = new ProjCoordinate(x, y);
        final ProjCoordinate pcDest = this.basicTransform.transform(
                pcSrc, new ProjCoordinate()
        );

        return this.gf.createPoint(new Coordinate(pcDest.x, pcDest.y));
	}

}
