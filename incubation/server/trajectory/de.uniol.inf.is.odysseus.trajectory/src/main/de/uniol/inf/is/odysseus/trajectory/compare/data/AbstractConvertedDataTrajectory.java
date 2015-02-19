package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Map;

/**
 * Abstract base class for all <i>converted data trajectories</i>. This class encapsulated 
 * the <i>converted data</i> and the <i>distance</i>.
 * @author marcus
 *
 * @param <E> the type of the data trajectory data
 */
public abstract class AbstractConvertedDataTrajectory<E> extends AbstractTrajectory<E, RawDataTrajectory> implements IConvertedDataTrajectory<E> {

	/** the converted data */
	private final E convertedData;
	
	/** the distance */
	private double distance;
	
	/**
	 * Creates an instance of <tt>AbstractConvertedDataTrajectory</tt>.
	 * 
	 * @param rawTrajectory the <tt>RawDataTrajectory</tt> to encapsulate
	 * @param convertedData the converted data
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt>
	 */
	protected AbstractConvertedDataTrajectory(RawDataTrajectory rawTrajectory, E convertedData) {
		super(rawTrajectory);
		
		if(convertedData == null) {
			throw new IllegalArgumentException("convertedData is null");
		}
		this.convertedData = convertedData;
	}
	
	@Override
	public E getData() {
		return this.convertedData;
	}

	@Override
	public double getDistance() {
		return this.distance;
	}

	@Override
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.getRawTrajectory().getTextualAttributes();
	}
	
	@Override
	public String toString() {
		return "[VehId: " + this.getRawTrajectory().getVehicleId() + "|" 
				+ "TrajId: " + this.getRawTrajectory().getTrajectoryNumber() + "|"
				+ "Dist: " + this.distance + "]";
	}

	@Override
	public int numAttributes() {
		return this.getRawTrajectory().numAttributes();
	}
}
