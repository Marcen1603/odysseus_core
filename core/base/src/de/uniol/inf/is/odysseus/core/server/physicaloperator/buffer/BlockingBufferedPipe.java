package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import de.uniol.inf.is.odysseus.core.IClone;

public class BlockingBufferedPipe<T extends IClone> extends BufferedPipe<T> {

	final long maxBufferSize;

	public BlockingBufferedPipe(long maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	@Override
	public void transferNext() {
		super.transferNext();
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if (size() < maxBufferSize) {
			super.process_next(object, port);
		} else {
			synchronized (this) {
				while (size() < maxBufferSize) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
