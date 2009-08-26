package de.uniol.inf.is.odysseus.physicaloperator.base;

/**
 * Marker interface for all buffer pipes. A call to {@link #hasNext()} may not
 * block for a buffer.
 * 
 * @author Jonas Jacobi
 */
public interface IBuffer<T> extends IIterableSource<T>, IPipe<T, T> {
	public int size();
	public void transferNextBatch(int count);
}
