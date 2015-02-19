package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Map;


public abstract class AbstractDataTrajectory<E> extends AbstractTrajectory<E, RawIdTrajectory> implements IDataTrajectory<E> {

	private final E convertedData;
		
	private double distance;
	
	protected AbstractDataTrajectory(RawIdTrajectory rawTrajectory, E convertedData) {
		super(rawTrajectory);
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
