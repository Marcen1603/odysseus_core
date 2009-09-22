package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.standardbufferplacementstrategy;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

/**
 * TODO: sollte von Jonas oder Marco dokumentiert werden.
 * 
 * @author Wolf Bauer
 *
 */
public class StandardBufferPlacementStrategy extends
		AbstractBufferPlacementStrategy {

	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	// add buffer, if we are a binary operator or if the bottom
	// operator is a binary one
	protected boolean bufferNeeded(
			List<? extends Subscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		return subscriptions.size() > 1
				|| childSink.getSubscribedTo().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		// TODO Warnings
		return new BufferedPipe();
	}
}
