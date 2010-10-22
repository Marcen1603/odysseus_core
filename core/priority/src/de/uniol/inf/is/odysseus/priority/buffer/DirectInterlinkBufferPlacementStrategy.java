package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.planmanagement.bufferplacement.standardbufferplacementstrategy.StandardBufferPlacementStrategy;

public class DirectInterlinkBufferPlacementStrategy extends
		StandardBufferPlacementStrategy {
	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return new DirectInterlinkBufferedPipe();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected IBuffer<?> createNewSourceBuffer() {
		return new DirectInterlinkBufferedPipe();
	}
	
	@Override
	public String getName() {
		return "Direct Interlink Buffer Placement";
	}

}
