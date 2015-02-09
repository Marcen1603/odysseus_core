package de.uniol.inf.is.odysseus.server.intervalapproach.window.state;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class SlidingElementWindowTIPOState<T extends IStreamObject<ITimeInterval>>
		implements Serializable, IOperatorState {

	private static final long serialVersionUID = -3048670939120301707L;

	private IGroupProcessor<T, T> groupProcessor;

	private ITransferArea<T, T> transferArea;

	private Map<Long, List<T>> buffers;

	private PointInTime lastTs;
	
	
	public IGroupProcessor<T, T> getGroupProcessor() {
		return groupProcessor;
	}

	public void setGroupProcessor(IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	public ITransferArea<T, T> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<T, T> transferArea) {
		this.transferArea = transferArea;
	}

	public Map<Long, List<T>> getBuffers() {
		return buffers;
	}

	public void setBuffers(Map<Long, List<T>> buffers) {
		this.buffers = buffers;
	}

	public PointInTime getLastTs() {
		return lastTs;
	}

	public void setLastTs(PointInTime lastTs) {
		this.lastTs = lastTs;
	}
}
