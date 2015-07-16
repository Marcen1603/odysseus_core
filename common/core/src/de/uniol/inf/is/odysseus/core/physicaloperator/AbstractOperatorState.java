package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

public abstract class AbstractOperatorState implements IOperatorState, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985394656242726017L;

	@Override
	public Serializable getSerializedState() {
		return this;
	}

}
