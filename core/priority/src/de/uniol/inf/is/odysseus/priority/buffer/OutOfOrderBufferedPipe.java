package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class OutOfOrderBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	public OutOfOrderBufferedPipe() {
		super();
	}
	
	public OutOfOrderBufferedPipe(
			OutOfOrderBufferedPipe<T> outOfOrderBufferedPipe) {
		super(outOfOrderBufferedPipe);
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();

		if (prio > 0) {
			synchronized (this.buffer) {
				// TODO das adden zum puffer kapseln, so dass das incrementieren
				// auch automatisch im AbstractPrioBuffer stattfinden kann
				// es kann nicht in einer dort ueberschriebenen process_next
				// stattfinden,
				// da nicht sichergestellt ist, dass das element zum puffer
				// hinzugefuegt wird
				this.prioCount.incrementAndGet();
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
		return this.buffer.element().getMetadata().getPriority();
	}

	public OutOfOrderBufferedPipe<T> clone() {
		return new OutOfOrderBufferedPipe<T>(this);
	}

}
