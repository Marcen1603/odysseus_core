package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.standardbufferplacementstrategy;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

/**
 *  
 * @author Jonas Jacobi, Marco Grawunder
 *
 */
public class StandardBufferPlacementStrategy 
	   extends	AbstractBufferPlacementStrategy {
 
	Logger logger = LoggerFactory.getLogger(StandardBufferPlacementStrategy.class);;
	
	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		// add buffer, if we are a binary operator or if the bottom
		// operator is a binary one
		return subscriptions.size() > 1
				|| childSink.getSubscribedToSource().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return new BufferedPipe();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing. It's only a standard placement strategy.
	}

	
	
	@Override
	public String getName() {
		return "Standard Buffer Placement";
	}


}
