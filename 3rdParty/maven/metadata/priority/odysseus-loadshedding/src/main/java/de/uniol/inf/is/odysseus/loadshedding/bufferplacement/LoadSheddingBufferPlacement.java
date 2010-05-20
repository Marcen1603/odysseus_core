package de.uniol.inf.is.odysseus.loadshedding.bufferplacement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.loadshedding.DirectLoadSheddingBuffer;
import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;
import de.uniol.inf.is.odysseus.priority.PriorityPO;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(immediate = true)
@Service(value = IBufferPlacementStrategy.class)
public class LoadSheddingBufferPlacement  extends
AbstractBufferPlacementStrategy {

	boolean placeLoadShedder = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		
		if(childSink instanceof PriorityPO || childSink instanceof PostPriorisationPO) {
			placeLoadShedder = true;
		} else {
			placeLoadShedder = false;
		}
		
		if(placeLoadShedder) {
			return true;
		}
		
		return subscriptions.size() > 1
		|| childSink.getSubscribedToSource().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		if(!placeLoadShedder) {
			return  new DirectInterlinkBufferedPipe();
		}
		
		return new DirectLoadSheddingBuffer();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		if(buffer instanceof DirectLoadSheddingBuffer) {
			LoadManager.getInstance(null).addLoadShedder((DirectLoadSheddingBuffer)buffer);
		}
	}

}
