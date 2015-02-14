package de.uniol.inf.is.odysseus.trajectory.physical.compare;

public abstract class AbstractQueryTrajectory<E> implements IQueryTrajectory<E> {

	private final E data;
	
	protected AbstractQueryTrajectory(final E data) {
		this.data = data;
	}
	
	@Override
	public E getData() {
		return this.data;
	}
}
