package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.Comparator;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	private Comparator<? super T> comparator = new MetadataComparator<IPriority>();

	public PriorityBufferedPipe(PriorityBufferedPipe<T> priorityBufferedPipe) {
		super(priorityBufferedPipe);
	}

	@SuppressWarnings("unchecked")
	protected void process_next(T object, int port, boolean isReadOnly) {
		byte prio = object.getMetadata().getPriority();

		if (isReadOnly) {
			object = (T) object.clone();
		}

		if (prio > 0) {
			synchronized (this.buffer) {
				// TODO siehe kommentar OutOfORderBufferedPipe
				prioCount.incrementAndGet();
				ListIterator<T> li = this.buffer.listIterator(this.buffer
						.size());
				while (li.hasPrevious()) {
					if (this.comparator.compare(li.previous(), object) == -1) {
						li.next();
						li.add(object);
						return;
					}
				}
			}
			synchronized (this.buffer) {
				this.buffer.addFirst(object);
			}
		} else {
			synchronized (this.buffer) {
				this.buffer.add(object);
			}
		}
	}

	@Override
	public Byte getTopElementPrio() {
		T obj = this.buffer.peek();
		return obj.getMetadata().getPriority();
	}

	public PriorityBufferedPipe<T> clone() {
		return new PriorityBufferedPipe<T>(this);
	}

}
