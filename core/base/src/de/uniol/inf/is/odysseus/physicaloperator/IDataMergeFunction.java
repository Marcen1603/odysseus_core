package de.uniol.inf.is.odysseus.physicaloperator;

/**
 * @author Jonas Jacobi
 */
public interface IDataMergeFunction<T> {
	public T merge(T left, T right);
	public void init();
	public IDataMergeFunction<T> clone();
}
