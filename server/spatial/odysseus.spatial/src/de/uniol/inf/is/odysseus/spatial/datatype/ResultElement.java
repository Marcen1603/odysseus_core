package de.uniol.inf.is.odysseus.spatial.datatype;

public class ResultElement {

	private TrajectoryElement trajectoryElement;
	// Distance to query object
	private double meters;

	
	public ResultElement(TrajectoryElement trajectoryElement, double meters) {
		this.trajectoryElement = trajectoryElement;
		this.meters = meters;
	}

	public TrajectoryElement getTrajectoryElement() {
		return trajectoryElement;
	}

	public void setTrajectoryElement(TrajectoryElement trajectoryElement) {
		this.trajectoryElement = trajectoryElement;
	}

	public double getMeters() {
		return meters;
	}

	public void setMeters(double meters) {
		this.meters = meters;
	}

}
