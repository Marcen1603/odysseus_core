package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class DirectInterlinkBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BufferedPipe<T> {
	Lock directLinkLock = new ReentrantLock();

	public DirectInterlinkBufferedPipe(
			DirectInterlinkBufferedPipe<T> directInterlinkBufferedPipe) {
		super(directInterlinkBufferedPipe);
	}

	public DirectInterlinkBufferedPipe() {
	}

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
		byte prio = object.getMetadata().getPriority();

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

	public DirectInterlinkBufferedPipe<T> clone() {
		return new DirectInterlinkBufferedPipe<T>(this);
	}

}
