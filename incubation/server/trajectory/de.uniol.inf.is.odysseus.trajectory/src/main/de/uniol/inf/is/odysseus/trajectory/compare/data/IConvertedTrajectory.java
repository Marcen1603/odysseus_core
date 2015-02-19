package de.uniol.inf.is.odysseus.trajectory.compare.data;

/**
 * A trajectory which has been converted from its raw representation to
 * a processable representation. This is utilized by various <tt>SpatialDistance</tt>
 * implementations. So <tt>E</tt> could be of whatever type that is useful
 * for further processing.
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 * @param <T> the type of the encapsulated <tt>RawQueryTrajectory</tt>
 */
public interface IConvertedTrajectory<E, T extends RawQueryTrajectory> extends IHasTextualAttributes {

	/**
	 * Returns the data of the converted trajectory.
	 * 
	 * @return the data of the converted trajectory
	 */
	public E getData();
}
