package de.uniol.inf.is.odysseus.trajectory.compare.data;


public interface IDataTrajectory<E> extends ITrajectory<E, RawIdTrajectory> {

	public RawIdTrajectory getRawTrajectory();
		
	public double getDistance();
	
	public void setDistance(double distance);
}
