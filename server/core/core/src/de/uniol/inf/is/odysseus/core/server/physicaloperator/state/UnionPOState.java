package de.uniol.inf.is.odysseus.core.server.physicaloperator.state;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;

/**
 * The current state of an {@link UnionPO} is defined by its transfer area
 * and its groups.
 * 
 * @author Chris Tönjes-Deye
 * 
 */
public class UnionPOState<R extends IStreamObject<?>> extends AbstractOperatorState {

	private static final long serialVersionUID = 9088231287860150949L;

	private ITransferArea<R, R> transferArea;

	public ITransferArea<R, R> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<R, R> transferArea) {
		this.transferArea = transferArea;
	}
	
	@Override
	public Serializable getSerializedState() {
		return this;
	}
	

	@Override
	public long estimateSizeInBytes() {
		//As this state does not tend to be really big we return the real size
		return getSizeInBytesOfSerializable(this);
	}
	
}
