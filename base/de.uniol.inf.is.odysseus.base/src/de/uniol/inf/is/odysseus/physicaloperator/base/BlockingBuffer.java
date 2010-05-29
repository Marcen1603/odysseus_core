package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * 
 * @author Tobias Witt
 *
 * @param <T>
 */
public class BlockingBuffer<T extends IClone> extends BufferedPipe<T> {

	@Override
	final public boolean hasNext() {
		// pretend being empty, so that hasNext() is false, if blocked
		return isBlocked() ? false : super.hasNext();
	}
		
}
