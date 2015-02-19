package de.uniol.inf.is.odysseus.trajectory.compare.data;


/**
 * Abstract implementation of <tt>IConvertedTrajectory</tt>. This class encapsulated
 * the <tt>RawQueryTrajectory</tt> specified by <tt>IConvertedTrajectory</tt>
 * 
 * @author marcus
 *
 * @param <E> the type of the trajectory data
 * @param <T> the type of the encapsulated <tt>RawQueryTrajectory</tt>
 */
public abstract class AbstractTrajectory<E, T extends RawQueryTrajectory> implements IConvertedTrajectory<E, T> {
	
	/** the encapsulated <tt>RawQueryTrajectory</tt>  */
	private final T rawTrajectory;
	
	/**
	 * Creates an <tt>AbstractTrajectory</tt>
	 * 
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * 
	 * @throws IllegalArgumentException if <tt>rawTrajectory == null</tt>
	 */
	protected AbstractTrajectory(final T rawTrajectory) {
		if(rawTrajectory == null) {
			throw new IllegalArgumentException("rawTrajectory is null");
		}
		this.rawTrajectory = rawTrajectory;
	}
	
	/**
	 * Returns the encapsulated <tt>RawQueryTrajectory</tt>.
	 * 
	 * @return the encapsulated <tt>RawQueryTrajectory</tt>
	 */
	public T getRawTrajectory() {
		return this.rawTrajectory;
	}
}
