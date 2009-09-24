package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractIterablePipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityBufferedPipe2<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractIterablePipe<T, T> implements IBuffer<T>,
		IPrioBuffer<T> {

	private Comparator<? super T> comparator = new MetadataComparator<IPriority>();

	private LinkedList<T> nonPrioBuffer;
	private LinkedList<T> prioBuffer;
	private int maxSize = 0;

	@Override
	protected void process_open() throws OpenFailedException {
		prioBuffer = new LinkedList<T>();
		nonPrioBuffer = new LinkedList<T>();
	}

	@Override
	final public boolean hasNext() {
		if (!isOpen())
			return false;
		return prioBuffer.size() > 0 || nonPrioBuffer.size() > 0;
	}

	public boolean isEmpty() {
		return prioBuffer.isEmpty() && nonPrioBuffer.isEmpty();
	}

	@Override
	public boolean isDone() {
		return super.isDone() && isEmpty();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	protected void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();

		// Load Shedding
		if (prio < 0) {
			return;
		}

		if (maxSize < this.size() + 1) {
			maxSize = this.size() + 1;
		}
		if (prio > 0) {
			synchronized (this.prioBuffer) {

				ListIterator<T> li = this.prioBuffer
						.listIterator(this.prioBuffer.size());
				while (li.hasPrevious()) {
					if (this.comparator.compare(li.previous(), object) == -1) {
						li.next();
						li.add(object);
						return;
					}
				}
				this.prioBuffer.addFirst(object);
				return;
			}
		}
		synchronized (nonPrioBuffer) {
			nonPrioBuffer.add(object);
		}
	}

	@Override
	public int size() {
		return nonPrioBuffer.size() + prioBuffer.size();
	}

	@Override
	public void transferNext() {
		T element = null;
		// the transfer might take some time, so pop element first and release
		// lock on buffer instead of transfer(buffer.pop())
		synchronized (prioBuffer) {
			if (!prioBuffer.isEmpty()) {
				element = prioBuffer.pop();
			}
		}
		if (element == null) { // Damit sich nicht zwei synch-Bloecke kreuzen
			synchronized (nonPrioBuffer) {
				element = nonPrioBuffer.pop();
			}
		}
		// objectsWritten++;
		transfer(element);
		if (isDone()) {
			propagateDone();
		}

	}

	@Override
	public void transferNextBatch(int count) {
		ArrayList<T> out = new ArrayList<T>(count);
		synchronized (this.nonPrioBuffer) {
			synchronized (this.prioBuffer) {
				if (count > size()) {
					throw new IllegalArgumentException(
							"cannot transfer more elements than size()");
				}
				int prioSize = this.prioBuffer.size();
				for (int i = 0; i < prioSize; ++i) {
					out.add(this.prioBuffer.remove());
				}
				for (int i = 0; i < count - prioSize; ++i) {
					out.add(this.nonPrioBuffer.remove());
				}
			}
		}
		transfer(out);
	}

	@Override
	public Byte getTopElementPrio() {
		T obj = this.prioBuffer.peek();
		if (obj != null) {
			return obj.getMetadata().getPriority();
		} else {
			return 0;
		}
	}

	@Override
	public int getPrioritizedCount() {
		return this.prioBuffer.size();
	}

}
