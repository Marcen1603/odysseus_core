package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class OutOfOrderBufferedPipe<T extends IMetaAttribute<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	@SuppressWarnings("unchecked")
	protected synchronized void process_next(T object, int port, boolean isReadOnly) {
		byte prio = object.getMetadata().getPriority();
		
		// Load Shedding
		if (prio < 0){
			return;
		}
		if (isReadOnly) {
			object = (T) object.clone();
		}

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

}
