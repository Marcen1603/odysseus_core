package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public interface IDataTrajectory<E> extends IHasConvertedData<E>, IHasTextualAttributes {

	public RawWithId getRawTrajectory();
	
	public TimeInterval getTimeInterval();
	
	public double getDistance();
	
	public void setDistance(double distance);
}
