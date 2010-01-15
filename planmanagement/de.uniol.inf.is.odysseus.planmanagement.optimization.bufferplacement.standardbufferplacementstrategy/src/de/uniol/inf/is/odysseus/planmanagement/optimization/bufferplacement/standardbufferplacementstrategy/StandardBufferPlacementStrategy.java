package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.standardbufferplacementstrategy;

import java.util.Collection;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

/**
 * TODO: sollte von Jonas oder Marco dokumentiert werden.
 * 
 * @author Wolf Bauer
 *
 */
public class StandardBufferPlacementStrategy extends
		AbstractBufferPlacementStrategy {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	// add buffer, if we are a binary operator or if the bottom
	// operator is a binary one
	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		return subscriptions.size() > 1
				|| childSink.getSubscribedToSource().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		// TODO Warnings
		return new BufferedPipe();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing. It's only a standard placement strategy.
	}
}
