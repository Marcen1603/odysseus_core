package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.standardbufferplacementstrategy.StandardBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class StrongOrderBufferedPlacementStrategy extends
		StandardBufferPlacementStrategy {

	private final int maxPrio;

	public StrongOrderBufferedPlacementStrategy() {
		this.maxPrio = 1;
	}

	@Override
	protected IBuffer<?> createNewBuffer() {
		return new StrongOrderBufferedPipe<IMetaAttributeContainer<? extends IPriority>>(
				this.maxPrio);
	}
	
	@Override
	protected IBuffer<?> createNewSourceBuffer() {
		return createNewBuffer();
	}


	@Override
	public String getName() {
		return "Strong Order Buffered Placement Strategy (fixed priorities)";
	}

}
