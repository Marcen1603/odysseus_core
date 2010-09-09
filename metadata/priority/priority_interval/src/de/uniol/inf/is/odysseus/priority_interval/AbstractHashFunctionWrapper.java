package de.uniol.inf.is.odysseus.priority_interval;
public abstract class AbstractHashFunctionWrapper<T> {
	final private T element;

	public AbstractHashFunctionWrapper(T element) {
		this.element = element;
	}

	abstract public int hashCode();

	public boolean equals(Object o) {
		return this.element.equals(o);
	}

	final public T getElement() {
		return this.element;
	}
}
