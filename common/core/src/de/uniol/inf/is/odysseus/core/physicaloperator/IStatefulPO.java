package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

/**
 * Interface to specify if an physical operator is stateful.
 *
 */
public interface IStatefulPO {
	public Serializable getState();
	public void setState(Serializable state);
	public long estimateStateSize(long schemaSizeInBytes);
}
