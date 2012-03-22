package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

public interface IInput<T> {

	/**
	 * Is called to initialize an input (e.g. to open a file or a connection)
	 */
	void init();

	/**
	 * Returns true if there is another object to read, false else
	 * @return
	 */
	boolean hasNext();

	
	/**
	 * Returns the next value. Should only be called if hasNext() was true!
	 * @return
	 */
	T getNext();

	/**
	 * Is called to clean up the input
	 */
	void terminate();


}
