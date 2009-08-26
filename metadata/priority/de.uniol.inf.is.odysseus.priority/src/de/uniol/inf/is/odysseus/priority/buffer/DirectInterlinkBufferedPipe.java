package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class DirectInterlinkBufferedPipe<T extends IMetaAttribute<? extends IPriority>>
		extends BufferedPipe<T> {
	Lock directLinkLock = new ReentrantLock();

	@Override
	public void transferNext() {
		directLinkLock.lock();
		super.transferNext();
		directLinkLock.unlock();
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected synchronized void process_next(T object, int port,
			boolean isReadOnly) {
		
		byte prio = object.getMetadata().getPriority();
		
		// Load Shedding
		if (prio < 0){
			return;
		}
		
		if (isReadOnly) {
			object = (T) object.clone();
		}

		if (prio > 0) {
			directLinkLock.lock();
			transfer(object);
			directLinkLock.unlock();
		} else {
			synchronized (this.buffer) {
				this.buffer.add(object);
			}
		}
	}
}
