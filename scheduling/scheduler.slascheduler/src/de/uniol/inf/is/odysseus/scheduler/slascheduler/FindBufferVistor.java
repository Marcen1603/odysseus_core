package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.util.IGraphNodeVisitor;

public class FindBufferVistor implements IGraphNodeVisitor<IPhysicalOperator, IPhysicalOperator> {
	
	private List<IBuffer<?>> buffers;
	
	public FindBufferVistor() {
		this.buffers = new ArrayList<IBuffer<?>>();
	}

	@Override
	public void nodeAction(IPhysicalOperator node) {
		if (node instanceof IBuffer<?>) {
			this.buffers.add((IBuffer<?>)node);
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(IPhysicalOperator sink,
			IPhysicalOperator source) {
	}

	@Override
	public void afterFromSinkToSourceAction(IPhysicalOperator sink,
			IPhysicalOperator source) {
	}

	@Override
	public void beforeFromSourceToSinkAction(IPhysicalOperator source,
			IPhysicalOperator sink) {
	}

	@Override
	public void afterFromSourceToSinkAction(IPhysicalOperator source,
			IPhysicalOperator sink) {
	}

	@Override
	public IPhysicalOperator getResult() {
		return null;
	}
	
	public List<IBuffer<?>> getBuffers() {
		return this.buffers;
	}

}
