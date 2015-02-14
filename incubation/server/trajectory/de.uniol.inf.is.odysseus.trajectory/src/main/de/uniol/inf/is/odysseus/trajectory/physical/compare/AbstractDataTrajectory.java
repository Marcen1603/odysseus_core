package de.uniol.inf.is.odysseus.trajectory.physical.compare;


public abstract class AbstractDataTrajectory implements IDataTrajectory {

	private final RawDataTrajectory rawTrajectory;
	
	private double distance = 0;
	
	protected AbstractDataTrajectory(RawDataTrajectory rawTrajectory) {
		this.rawTrajectory = rawTrajectory;
	}
	
	@Override
	public RawDataTrajectory getRawTrajectory() {
		return this.rawTrajectory;
	}
	
	@Override
	public double getDistance() {
		return distance;
	}

	@Override
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return "[ " + this.rawTrajectory.getVehicleId() + ": " + this.getDistance() + "]";
	}
}
