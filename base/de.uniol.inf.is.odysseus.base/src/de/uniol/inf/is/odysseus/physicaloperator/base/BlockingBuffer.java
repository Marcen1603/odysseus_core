package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * 
 * @author Tobias Witt, Marco Grawunder
 *
 * @param <T>
 */
public class BlockingBuffer<T extends IClone> extends BufferedPipe<T> {

	public BlockingBuffer() {
	}
	
	
	public BlockingBuffer(BlockingBuffer<T> blockingBuffer) {
		super(blockingBuffer);	
	}


	// Should be done by the scheduler, not by the operator
//	@Override
//	final public boolean hasNext() {
//		// pretend being empty, so that hasNext() is false, if blocked
//		return isBlocked() ? false : super.hasNext();
//	}
	
	@Override
	public BufferedPipe<T> clone() throws CloneNotSupportedException {
		return new BlockingBuffer<T>(this);
	}
	
		
}
