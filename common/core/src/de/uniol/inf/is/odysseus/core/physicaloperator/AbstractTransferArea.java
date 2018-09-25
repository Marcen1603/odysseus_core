package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

abstract public class AbstractTransferArea<R extends IStreamObject<?>, W extends IStreamObject<?>> implements ITransferArea<R, W> {

	private static final long serialVersionUID = 1019179379693815902L;

	@Override
	public abstract ITransferArea<R,W> clone();

	@Override
	public void dump() {
		// nothing to do, no state
	}
	
	@Override
	public long getElementsRead() {
		return -1;
	}
	
	@Override
	public long getElementsWritten() {
		return -1;
	}
	
	@Override
	public long getPunctuationsRead() {
		return -2;
	}
	
	@Override
	public long getPunctuationsWritten() {
		return -1;
	}
	
	@Override
	public long getPunctuationsSuppressed() {
		return -1;
	}
	
}
