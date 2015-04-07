package de.uniol.inf.is.odysseus.peer.util;

public interface IObservableOperator {

	/**
	 * Adds an observer instance if it is not already in the set of observers for this OperatorObservable.
	 * 
	 * @param observer
	 *            an observer instance to be added.
	 */
	public void addObserver(IOperatorObserver observer);

	/**
	 * Removes an observer instance from the set of observers of this OperatorObservable.
	 * 
	 * @param observer
	 *            an observer instance to be removed.
	 */
	public void removeObserver(IOperatorObserver observer);

	/**
	 * Removes all observer instances from the set of observers of this OperatorObservable
	 */
	public void removeObservers();

	/**
	 * Notifies all observer instances in the set of observers of this OperatorObservable
	 * 
	 * @param arg
	 *            An object all the observers will get
	 */
	public void notifyObservers(Object arg);
}
