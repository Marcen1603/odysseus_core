package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public class SlidingElementWindowPNPO<T extends IMetaAttribute<IPosNeg>>
		extends AbstractPipe<T, T> {

	List<T> buffer = null;
	boolean forceElement = true;
	long windowSize;

	public SlidingElementWindowPNPO(long windowSize) {
		this.windowSize = windowSize;
		buffer = new LinkedList<T>();
	}

	public SlidingElementWindowPNPO(SlidingElementWindowPNPO<T> name) {
		this.windowSize = name.windowSize;
		this.buffer = name.buffer;
	}

	@Override
	protected synchronized void process_next(T object, int port,
			boolean isReadOnly) {
		if (isReadOnly) {
			object = (T) object.clone();
		}

		buffer.add(object);
		if (buffer.size() == this.windowSize + 1) {
			T toReturn = buffer.remove(0);
			toReturn.getMetadata().setTimestamp(
					object.getMetadata().getTimestamp());
			this.transfer(toReturn);
		}
	}

	public final void process_open() {
	}

	public final void process_close() {
	}

}
