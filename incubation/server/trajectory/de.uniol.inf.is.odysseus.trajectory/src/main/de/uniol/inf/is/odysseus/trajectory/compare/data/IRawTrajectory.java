package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 * Interface for a trajectory which only consists of a <tt>List</tt> of raw
 * <tt>Points</tt> in the <i>EPSG:4326</i> spatial format. The points in the
 * <tt>List</tt> are <i>chronologically ordered</i> so that the measurement 
 * time of the first point is before the measurement time of the last point
 * in the list. The <tt>List</tt> must at least consist of <i>2</i> 
 * <tt>Points</tt>.
 * 
 * @author marcus
 *
 */
public interface IRawTrajectory {
	
	/**
	 * Returns the by <i>measurement time ascending chronologically ordered</i>
	 * <tt>Points</tt> of this trajectory. The <tt>Points</tt> are represented in
	 * the <i>EPSG:4326</i> format.
	 * 
	 * @return the <tt>Points</tt> of this trajectory
	 */
	public List<Point> getPoints();
}
