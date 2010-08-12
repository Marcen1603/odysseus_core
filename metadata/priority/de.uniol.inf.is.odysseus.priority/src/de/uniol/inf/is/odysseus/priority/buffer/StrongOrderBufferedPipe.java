package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class StrongOrderBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	private LinkedList<T>[] buffers;

	@SuppressWarnings("unchecked")
	public StrongOrderBufferedPipe(int maxPrio) {
		super();
		buffers = new LinkedList[maxPrio+1];
		for (int i = 0; i < maxPrio+1; ++i) {
			buffers[i] = new LinkedList<T>();
		}
	}

	public StrongOrderBufferedPipe(
			StrongOrderBufferedPipe<T> strongOrderBufferedPipe) {
		this(strongOrderBufferedPipe.buffers.length);
	}

	protected void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();
		synchronized (this.buffer) {
			if (prio > 0) {
				this.prioCount.incrementAndGet();
			}
			this.buffers[prio].add(object);
			this.heartbeat.set(null);
		}
	};

	@Override
	public void transferNext() {
		transferLock.lock();
		boolean transfered = false;
		for (int i = buffers.length-1; i > -1; --i) {
			if (!buffers[i].isEmpty()) {
				transfered = true;
				T element;
				synchronized (this.buffer) {
					element = buffers[i].pop();
				}
				// logger.debug(this+" transferNext() "+element);
				transfer(element);
				if (isDone()) {
					propagateDone();
				}
				break;
			}
		}
		PointInTime hearbeat = this.heartbeat.get();
		if (!transfered && hearbeat != null) {
			sendPunctuation(heartbeat.getAndSet(null));
		}
		transferLock.unlock();
	}

	@Override
	public boolean hasNext() {
		if (!isOpen()) {
			getLogger().error("hasNext call on not opened buffer!");
			return false;
		}

		return !isBuffersEmpty() || this.heartbeat.get() != null;
	}

	@Override
	public boolean isDone() {
		transferLock.lock();
		boolean returnValue;
		synchronized (this.buffer) {
			returnValue = super.isDone() && isBuffersEmpty();
		}
		transferLock.unlock();
		return returnValue;
	}

	private boolean isBuffersEmpty() {
		for (List<T> l : this.buffers) {
			if (!l.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Byte getTopElementPrio() {
		for (int i = buffers.length-1; i > -1; --i) {
			if (!buffers[i].isEmpty()) {
				return (byte) i;
			}
		}
		return 0;
	}

	@Override
	public StrongOrderBufferedPipe<T> clone() {
		return new StrongOrderBufferedPipe<T>(this);
	}

}
