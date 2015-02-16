package de.uniol.inf.is.odysseus.trajectory.compare.data;


public abstract class AbstractTrajectory<E, T extends RawQueryTrajectory> implements ITrajectory<E, T> {
	
	private final T rawTrajectory;
	
	protected AbstractTrajectory(final T rawTrajectory) {
		this.rawTrajectory = rawTrajectory;
	}
	
	public T getRawTrajectory() {
		return this.rawTrajectory;
	}
}
