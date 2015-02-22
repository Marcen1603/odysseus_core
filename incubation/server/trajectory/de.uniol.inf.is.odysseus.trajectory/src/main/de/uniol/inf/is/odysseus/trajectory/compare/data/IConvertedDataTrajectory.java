package de.uniol.inf.is.odysseus.trajectory.compare.data;


/**
 * An extension of <tt>IConvertedTrajectory</tt>. This trajectory is converted from 
 * a <tt>RawDataTrajectory</tt> which was built from a <tt>Tuple</tt> in a stream of
 * trajectories. This kind of trajectory has a <i>distance</i> which can be read and set.
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 */
public interface IConvertedDataTrajectory<E> extends IConvertedTrajectory<E, RawDataTrajectory> {

	/**
	 * Returns the distance of this <tt>IConvertedDataTrajectory</tt>.
	 * 
	 * @return the distance of this <tt>IConvertedDataTrajectory</tt>.
	 */
	public double getDistance();
	
	/**
	 * Sets the distance of this <tt>IConvertedDataTrajectory</tt>.
	 */
	public void setDistance(double distance);
}
