package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public abstract class AbstractPrioBuffer<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BufferedPipe<T> implements IPrioBuffer<T> {
	final protected AtomicInteger prioCount = new AtomicInteger(0);

	public AbstractPrioBuffer() {
		super();
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
	public void transfer(T object) {
		if (object.getMetadata().getPriority() > 0) {
				prioCount.decrementAndGet();	
		}
		super.transfer(object);
	}

}
