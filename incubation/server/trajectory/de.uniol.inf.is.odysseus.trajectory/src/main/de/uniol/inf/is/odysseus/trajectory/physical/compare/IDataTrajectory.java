package de.uniol.inf.is.odysseus.trajectory.physical.compare;

public interface IDataTrajectory {

	public RawDataTrajectory getRawTrajectory();
	
	public void setDistance(double distance);
	
	public double getDistance();
}
