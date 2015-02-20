package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An object which can convert a <tt>Tuple</tt> that represents a <i>trajectory</i> to
 * a <tt>RawDataTrajectory</tt>. Implementations have to decide which schema to use
 * for a <tt>Tuple</tt>.
 * 
 * @author marcus
 *
 */
public interface ITupleToRawTrajectoryConverter {

	/**
	 * Converts and returns a <tt>RawDataTrajectory</tt> from a <tt>Tuple</tt> 
	 * representing a <i>trajectory</i> within the passed <tt>utmZone</tt>. 
	 * 
	 * @param tuple the <tt>Tuple</tt> representing a <i>trajectory</i>
	 * @param utmZone the <tt>utm zone</tt> to in which the to be created <tt>RawDataTrajectory</tt>
	 *        is supposed to be
	 * 
	 * @return a <tt>RawDataTrajectory</tt> from the passed <tt>Tuple</tt> 
	 *         representing a <i>trajectory</i>
	 *         
	 * @throws IllegalArgumentException if <tt>tuple == null</tt> or <tt>utmZone</tt> is
	 *         out of bounds
	 */
	public RawDataTrajectory convert(final Tuple<ITimeInterval> tuple, final int utmZone) throws IllegalArgumentException;
}
