package de.uniol.inf.is.odysseus.trajectory.compare.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Interface for converting a <tt>Coordinate</tt> represented in <i>EPSG:4326</i> spatial format
 * to an <tt>Point</tt> in a coordinate system specified by the implementation.
 * 
 * @author marcus
 *
 */
public interface IPointCreator {

	/**
	 * Creates and returns a <tt>Point</tt> from the passed
	 * <tt>Coordinate</tt> in <i>EPSG:4326</i> spatial format.
	 * 
	 * @param coordinate the <tt>Coordinate</tt> in <i>EPSG:4326</i> spatial format
	 * @return the Point in a specified coordinate system from the passed <tt>Coordinate</tt>
	 * @throws IllegalArgumentException if <tt>coordinate == null</tt>
	 */
	public Point createPoint(final Coordinate coordinate) throws IllegalArgumentException;
}
