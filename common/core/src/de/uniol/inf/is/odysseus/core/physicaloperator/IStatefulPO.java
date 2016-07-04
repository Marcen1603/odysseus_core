package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

/**
 * Interface to specify if an physical operator is stateful. <br />
 * <br />
 * Note that process_open methods typically clear/initialize operator states.
 * For stateful operators, the process_open method should check, if an operator
 * state has been loaded ({@link #setState(Serializable)} has been called). If
 * that is the case, the operator state should not be changed. That "state
 * loaded" flag can be set to false, if process_close is called, because after
 * that the operator state is no longer valid.
 *
 */
public interface IStatefulPO {
	public IOperatorState getState();
	public void setState(Serializable state);
}
