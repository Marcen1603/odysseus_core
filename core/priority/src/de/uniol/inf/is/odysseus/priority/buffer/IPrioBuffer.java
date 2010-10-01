package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;

public interface IPrioBuffer<T> extends IBuffer<T> {
	public Byte getTopElementPrio();
	public int getPrioritizedCount();
}
