package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;

/**
 * Buffer used for planmigration.
 * 
 * @author Tobias Witt, Marco Grawunder
 *
 * @param <T>
 */
public class BlockingBuffer<T extends IClone> extends BufferedPipe<T> {

	public BlockingBuffer(boolean block) {
		if (block){
			block();
		}else{
			unblock();
		}
	}
	
	
	public BlockingBuffer(BlockingBuffer<T> blockingBuffer) {
		super(blockingBuffer);
	}

	@Override
	public BufferedPipe<T> clone() {
		return new BlockingBuffer<T>(this);
	}
		
}
