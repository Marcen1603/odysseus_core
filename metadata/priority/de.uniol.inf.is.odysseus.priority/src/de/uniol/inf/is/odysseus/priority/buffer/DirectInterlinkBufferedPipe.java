package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationBuffer;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class DirectInterlinkBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPunctuationBuffer<T,T> {
	Lock directLinkLock = new ReentrantLock();

	@Override
	public void transferNext() {
		directLinkLock.lock();
		super.transferNext();
		directLinkLock.unlock();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	final protected synchronized void process_next(T object, int port) {
		
		storage.setCurrentPort(port);
		
		byte prio = object.getMetadata().getPriority();
		
		// Load Shedding
		if (prio < 0){
			return;
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

	@Override
	public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {
		return true;
	}
}
