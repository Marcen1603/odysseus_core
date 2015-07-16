package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

/**
 * Interface to specify if an physical operator is stateful.
 *
 */
public interface IStatefulPO {
	public IOperatorState getState();
	public void setState(Serializable state);
}
