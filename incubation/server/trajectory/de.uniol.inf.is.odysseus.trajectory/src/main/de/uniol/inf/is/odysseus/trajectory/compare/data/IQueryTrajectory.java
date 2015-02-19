package de.uniol.inf.is.odysseus.trajectory.compare.data;

/**
 * An specification of <tt>IConvertedTrajectory</tt>. This trajectory is converted from 
 * a <tt>RawQueryTrajectory</tt> which may have been constructed from a CSV file. It is 
 * the base interface for all <i>converted</i> trajectories.
 * 
 * @author marcus
 *
 * @param <E> the type of the query trajectory data
 * @param <T> the type of the encapsulated <tt>RawQueryTrajectory</tt>
 */
public interface IQueryTrajectory<E> extends IConvertedTrajectory<E, RawQueryTrajectory> {

}
