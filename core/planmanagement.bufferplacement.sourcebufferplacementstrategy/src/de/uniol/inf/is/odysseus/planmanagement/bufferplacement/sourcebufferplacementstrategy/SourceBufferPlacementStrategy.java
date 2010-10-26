package de.uniol.inf.is.odysseus.planmanagement.bufferplacement.sourcebufferplacementstrategy;

import java.util.Collection;

/**
 * Places buffers before metadata creation po
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

public class SourceBufferPlacementStrategy extends AbstractBufferPlacementStrategy{
	         
	
	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(SourceBufferPlacementStrategy.class);
		}
		return _logger;
	}
	
	@Override
	public String getName() {
		return "Source Buffer Placement";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return new BufferedPipe();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing.
	}

	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink, ISink<?> sink) {
		boolean setBuffer = sink instanceof MetadataCreationPO;
		if (setBuffer){
			getLogger().debug(childSink+" "+sink+" true");
		}
		return setBuffer;
	}

}
