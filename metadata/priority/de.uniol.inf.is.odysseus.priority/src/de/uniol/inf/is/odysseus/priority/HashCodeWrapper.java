package de.uniol.inf.is.odysseus.priority;

public class HashCodeWrapper<T> {
	private final T element;
	private final int hashCode;

	public HashCodeWrapper(T element, int hashCode){
		this.element = element;
		this.hashCode = hashCode;
	}
	
	public T getElement() {
		return element;
	}
	
	@Override
	public int hashCode() {
		return this.hashCode;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object arg0) {
		HashCodeWrapper<T> other = (HashCodeWrapper<T>)arg0;
		return other.hashCode == this.hashCode;
	}
}
