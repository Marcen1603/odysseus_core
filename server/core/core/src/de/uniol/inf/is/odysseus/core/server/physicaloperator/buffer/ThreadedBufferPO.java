package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This is a special version of the buffer operator, where the buffer will not be scheduled
 * but has an own thread for processing
 * 
 * @author Marco Grawunder
 *
 * @param <R>
 */
public class ThreadedBufferPO<R extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<R, R> {

	protected LinkedList<IStreamable> buffer = new LinkedList<>();

	Thread runner;
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		synchronized (this) {
			buffer.add(object);
			notifyAll();
		}
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		buffer.clear();
		// Create new thread and start
		runner = new Thread("ThreadedBuffer "+getName()){
			public void run() {
				while (isOpen()){
					while(!buffer.isEmpty()){
						transferNext();
					}
					synchronized(this){
						try {
							wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		};
		runner.setDaemon(true);
		
	}
	
	@SuppressWarnings("unchecked")
	private void transferNext() {
		// Copy from BufferPO ... 
		if (!this.buffer.isEmpty()) {
			// the transfer might take some time, so pop element first and
			// release lock on buffer instead of transfer(buffer.pop())
			IStreamable element;
			synchronized (this.buffer) {
				element = buffer.pop();
			}

			if (element.isPunctuation()) {
				sendPunctuation((IPunctuation) element);
			} else {
				transfer((R) element);
			}

			// the top element of a buffer must always be
			// an real element, send punctuations immediately
			synchronized (buffer) {
				while (!buffer.isEmpty() && buffer.peek().isPunctuation()) {
					sendPunctuation((IPunctuation) buffer.pop());
				}
			}

			if (isDone()) {
				propagateDone();
			}
		}
	}
	
	@Override
	protected void process_close() {
		drainBuffer();
		
	}
	
	@Override
	protected void process_done(int port) {
		drainBuffer();
	}

	private void drainBuffer() {
		synchronized (buffer) {
			while(!buffer.isEmpty()){
				transferNext();
			}	
		}
	}
	
	
	
}
