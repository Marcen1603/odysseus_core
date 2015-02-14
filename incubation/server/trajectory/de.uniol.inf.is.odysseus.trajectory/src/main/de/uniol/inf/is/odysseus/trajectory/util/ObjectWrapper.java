package de.uniol.inf.is.odysseus.trajectory.util;

public class ObjectWrapper<T> {
	
	private final T wrapped;
	
	public ObjectWrapper(T wrapped) {
		this.wrapped = wrapped;
	}

	public T getWrapped() {
		return wrapped;
	}

	
}
