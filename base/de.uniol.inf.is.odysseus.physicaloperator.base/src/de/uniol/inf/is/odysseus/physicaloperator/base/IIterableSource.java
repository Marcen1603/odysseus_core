package de.uniol.inf.is.odysseus.physicaloperator.base;

/**
 * An iterator interface for plain sources or buffers.
 * While {@link #hasNext()} may block until data is available
 * for plain sources,
 * it is not allowed to block for buffers.
 * @author Jonas Jacobi
 */
public interface IIterableSource<T> extends ISource<T> {
	/**
	 * Get whether a call to transferNext() will be successful.
	 * @return true, if an element can be transfered. 
	 */
	public boolean hasNext();
	/**
	 * Call {@link #transfer(Object)} with the next element. May only be called
	 * if a call to hasNext() returns true.
	 */
	public void transferNext();
	
	/**
	 * Returns true, if ISource has all Input processed
	 */
	public boolean isDone();
}
