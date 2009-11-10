package de.uniol.inf.is.odysseus.loadshedding.bufferplacement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

public class LoadSheddingBufferPlacement  extends
AbstractBufferPlacementStrategy {

	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		return subscriptions.size() > 1
		|| childSink.getSubscribedTo().size() > 1;
	}

	@Override
	protected IBuffer<?> createNewBuffer() {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		
	}

}
