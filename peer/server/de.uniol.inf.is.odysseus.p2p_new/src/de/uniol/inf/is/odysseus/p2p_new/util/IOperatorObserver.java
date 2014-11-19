package de.uniol.inf.is.odysseus.p2p_new.util;

public interface IOperatorObserver {

	/**
	 * Updates the state of this OperatorObserver. The argument should usually be an indication of the aspects that
	 * changed in the ObservableOperator.
	 *
	 * @param observable
	 *            an IObservableOperator instance.
	 * @param arg
	 *            an arbitrary argument to be passed.
	 */
	public void update(IObservableOperator observable, Object arg);

}
