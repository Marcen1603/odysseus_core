package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

public interface IObjectLoader<E, P, A> {

	public E load(P param, A additional);
}
