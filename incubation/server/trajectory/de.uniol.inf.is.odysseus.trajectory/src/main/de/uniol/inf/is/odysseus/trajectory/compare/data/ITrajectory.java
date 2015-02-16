package de.uniol.inf.is.odysseus.trajectory.compare.data;

public interface ITrajectory<E, T extends RawQueryTrajectory> extends IHasConvertedData<E>, IHasTextualAttributes {

	public T getRawTrajectory();
}
