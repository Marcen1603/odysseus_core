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
public abstract class AbstractConvertedTrajectory<E, T extends RawQueryTrajectory> implements IConvertedTrajectory<E, T> {
	
	/** the encapsulated <tt>RawQueryTrajectory</tt>  */
	private final T rawTrajectory;
	
	/** the converted data */
	private final E convertedData;
	
	/**
	 * Creates an <tt>AbstractConvertedTrajectory</tt>
	 * 
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * 
	 * @throws IllegalArgumentException if <tt>rawTrajectory == null</tt>
	 *         or <tt>convertedData == null</tt>
	 */
	protected AbstractConvertedTrajectory(final T rawTrajectory, final E convertedData) {
		if(rawTrajectory == null) {
			throw new IllegalArgumentException("rawTrajectory is null");
		}
		if(convertedData == null) {
			throw new IllegalArgumentException("convertedData is null");
		}
		this.rawTrajectory = rawTrajectory;
		this.convertedData = convertedData;
	}
	
	@Override
	public T getRawTrajectory() {
		return this.rawTrajectory;
	}
	
	@Override
	public E getData() {
		return this.convertedData;
	}
}
