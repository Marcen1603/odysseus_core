package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.planmanagement.bufferplacement.standardbufferplacementstrategy.StandardBufferPlacementStrategy;

public class WeakOrderBufferPlacementStrategey extends
		StandardBufferPlacementStrategy {
	@Override
	public String getName() {
		return "Weak Order Buffer Placement";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return new OutOfOrderBufferedPipe();
	}

}
