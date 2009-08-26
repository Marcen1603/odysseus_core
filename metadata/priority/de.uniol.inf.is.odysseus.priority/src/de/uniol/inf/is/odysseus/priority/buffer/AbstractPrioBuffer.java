package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public abstract class AbstractPrioBuffer<T extends IMetaAttribute<? extends IPriority>>
		extends BufferedPipe<T> implements IPrioBuffer<T> {
	final protected AtomicInteger prioCount = new AtomicInteger(0);

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
