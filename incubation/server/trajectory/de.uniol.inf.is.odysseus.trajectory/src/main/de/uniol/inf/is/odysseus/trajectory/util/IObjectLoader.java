package de.uniol.inf.is.odysseus.trajectory.util;

public interface IObjectLoader<E, P, A> {

	public E load(P param, A additional);
}
