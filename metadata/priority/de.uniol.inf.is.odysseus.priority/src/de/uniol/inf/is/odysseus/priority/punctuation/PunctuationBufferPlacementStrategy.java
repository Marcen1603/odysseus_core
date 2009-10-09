package de.uniol.inf.is.odysseus.priority.punctuation;

import java.util.Collection;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

/**
 * This strategy makes usage of direct-link buffers with punctuation extension instead of
 * normal BufferedPipes.
 * @author jan
 *
 */
public class PunctuationBufferPlacementStrategy  extends
		AbstractBufferPlacementStrategy {

	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	// add buffer, if we are a binary operator or if the bottom
	// operator is a binary one
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		return subscriptions.size() > 1
				|| childSink.getSubscribedTo().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return  new DirectInterlinkBufferedPipe();
	}
}
