package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 * Default implementation of <tt>IRawTrajectory</tt>. The <tt>List</tt> of
 * <tt>Points</tt> is passed to constructor and internally transformed to a
 * <i>unmodifiable</i> <tt>List</tt>.
 * 
 * @author marcus
 *
 */
public class RawQueryTrajectory implements IRawTrajectory {

	/** the points list */
	private final List<Point> points;
	
	/**
	 * Creates an <tt>RawQueryTrajectory</tt>. A new <i>unmodifiable</i> <tt>List</tt>
	 * of <tt>Points</tt> is created from the passed <tt>List</tt>. An 
	 * <tt>IllegalArgumentException</tt> will be thrown if <i>points</i> is <tt>null</tt>
	 * or has not <i>at least one</i> element.
	 * 
	 * @param points the <tt>Points</tt> from which the internal <i>unmodifiable</i> 
	 *        <tt>List</tt> will be created
	 * 
	 * @throws IllegalArgumentException if <tt>points</tt> is <tt>null</tt>
	 *         or has not <i>at least one</i> element
	 * 
	 */
	public RawQueryTrajectory(final List<Point> points) {
		if(points == null) {
			throw new IllegalArgumentException("Argument points is null");
		}
		if(points.size() == 0) {
			throw new IllegalArgumentException("points must have more than 0 elements");
		}
		this.points = Collections.unmodifiableList(points);
	}

	/**
	 * {@inheritDoc}
	 * The returned <tt>List</tt> is <i>unmodifiable</i>.
	 * 
	 * @return {@inheritDoc} as an <i>unmodifiable</i> <tt>List</tt>
	 */
	public List<Point> getPoints() {
		return this.points;
	}


}
