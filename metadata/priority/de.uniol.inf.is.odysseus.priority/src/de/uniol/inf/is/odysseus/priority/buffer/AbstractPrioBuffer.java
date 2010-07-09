package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public abstract class AbstractPrioBuffer<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BufferedPipe<T> implements IPrioBuffer<T> {
	final protected AtomicInteger prioCount = new AtomicInteger(0);

	public AbstractPrioBuffer() {
	}
	
	public AbstractPrioBuffer(AbstractPrioBuffer<T> abstractBufferedPipe) {
		super(abstractBufferedPipe);
		prioCount.set(abstractBufferedPipe.prioCount.get());
	}

	@Override
	final public int getPrioritizedCount() {
		return this.prioCount.get();
	}

	@Override
	protected void process_transfer(T object) {
		if (object.getMetadata().getPriority() > 0) {
				prioCount.decrementAndGet();	
		}
		super.process_transfer(object);
	}

}
