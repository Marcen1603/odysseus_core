package de.uniol.inf.is.odysseus.trajectory.physical.compare;


public abstract class AbstractAdvancedTrajectory {

	private final RawTrajectory rawTrajectory;
	
	private double distance = 0;
	
	
	protected AbstractAdvancedTrajectory(RawTrajectory rawTrajectory) {
		this.rawTrajectory = rawTrajectory;
	}
	
	public RawTrajectory getRawTrajectory() {
		return this.rawTrajectory;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return "[ " + this.rawTrajectory.getVehicleId() + ": " + this.getDistance() + "]";
	}
}
