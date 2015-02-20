package de.uniol.inf.is.odysseus.trajectory.compare.util;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Implementation of <tt>IPointCreator</tt> for converting a <tt>Coordinate</tt> represented in <i>EPSG:4326</i> spatial format
 * to an <tt>Point</tt> in a <i>UTM Coordinate System</i>.
 * 
 * @author marcus
 *
 */
public class UtmPointCreator implements IPointCreator {
	
	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(UtmPointCreator.class);
	
	/** the <tt>GeometryFactory for creating <tt>Points</tt> */
	private final static GeometryFactory GF = new GeometryFactory();

	/** the zone of this <tt>UtmPointCreator</tt> */
	private final int utmZone;
	
	/** used for transforming coordinates */
	private final BasicCoordinateTransform basicTransform;
	
	/**
	 * 
	 * @param utmZone the utm zone 
	 */
	public UtmPointCreator(final int utmZone) {
		this.utmZone = utmZone;
		final CRSFactory cf = new CRSFactory();
		this.basicTransform = new BasicCoordinateTransform(
				cf.createFromName("EPSG:4326"), 
				cf.createFromParameters("Sumo", "+proj=utm +zone=" + this.utmZone + " +ellps=WGS84 +datum=WGS84 +units=m +no_defs"));
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("UtmPointCreator for zone: " + this.utmZone);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <p>The resulting <tt>Point's</tt> <tt>Coordinate</tt> will be represented
	 * in a <i>UTM Coordinate System</i>.
	 * 
	 * @return @return the Point in a <i>UTM Coordinate System</i> from the passed <tt>Coordinate</tt>
	 */
	@Override
	public Point createPoint(final Coordinate coordinate) throws IllegalArgumentException {
		if(coordinate == null) {
			throw new IllegalArgumentException("coordinate is null");
		}
        final ProjCoordinate pcSrc = new ProjCoordinate(coordinate.x, coordinate.y);
        final ProjCoordinate pcDest = this.basicTransform.transform(
                pcSrc, new ProjCoordinate()
        );

        return GF.createPoint(new Coordinate(pcDest.x, pcDest.y));
	}

}
