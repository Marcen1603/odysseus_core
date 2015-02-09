package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class JoinTIPOState<K extends ITimeInterval, T extends IStreamObject<K>>
		implements Serializable, IOperatorState {

	private static final long serialVersionUID = 3316591729332909753L;

	private ITimeIntervalSweepArea<T>[] sweepAreas;
	private ITransferArea<T, T> transferArea;
	

	public ITimeIntervalSweepArea<T>[] getSweepAreas() {
		return sweepAreas;
	}

	public void setSweepAreas(ITimeIntervalSweepArea<T>[] sweepAreas) {
		this.sweepAreas = sweepAreas;
	}

	public ITransferArea<T, T> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<T, T> transferArea) {
		this.transferArea = transferArea;
	}
}
