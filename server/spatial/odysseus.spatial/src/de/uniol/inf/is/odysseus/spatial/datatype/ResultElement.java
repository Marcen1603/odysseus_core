package de.uniol.inf.is.odysseus.spatial.datatype;

public class ResultElement {

	private TrajectoryElement trajectoryElement;
	// Distance to query object
	private double distanceInMeters;

	
	public ResultElement(TrajectoryElement trajectoryElement, double meters) {
		this.trajectoryElement = trajectoryElement;
		this.distanceInMeters = meters;
	}

	public TrajectoryElement getTrajectoryElement() {
		return trajectoryElement;
	}

	public void setTrajectoryElement(TrajectoryElement trajectoryElement) {
		this.trajectoryElement = trajectoryElement;
	}

	public double getMeters() {
		return distanceInMeters;
	}

	public void setMeters(double meters) {
		this.distanceInMeters = meters;
	}

}
