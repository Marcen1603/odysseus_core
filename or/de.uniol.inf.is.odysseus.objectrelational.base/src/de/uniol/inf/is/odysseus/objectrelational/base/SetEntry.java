package de.uniol.inf.is.odysseus.objectrelational.base;

/**
 * @author Jendrik Poloczek
 * @param <T> type of set entry
 */
public class SetEntry<T> implements Comparable<T> {
	private static final long serialVersionUID = 1L;

	private T value;
	
	public SetEntry(T entry) {
		this.value = entry;
	}

	public T getValue() {
		return value;
	}
	
	public String toString() {
		return this.value.toString();
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
