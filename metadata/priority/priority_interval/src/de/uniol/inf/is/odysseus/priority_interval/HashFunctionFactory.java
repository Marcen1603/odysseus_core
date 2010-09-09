package de.uniol.inf.is.odysseus.priority_interval;

public interface HashFunctionFactory<T> {
	public AbstractHashFunctionWrapper<T> createHashFunction(T element);
}
