package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

/**
 * Marker Interface for operator states
 * 
 * @author ChrisToenjesDeye,Carsten Cordes
 *
 */
public interface IOperatorState {
	Serializable getSerializedState();
	long estimateSizeInBytes();
}
