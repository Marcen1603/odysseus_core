package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This is a special version of the buffer operator, where the buffer will not
 * be scheduled but has an own thread for processing
 * 
 * @author Marco Grawunder
 *
 * @param <R>
 */
public class ThreadedBufferPO<R extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractPipe<R, R> {

	protected LinkedList<IStreamable> buffer = new LinkedList<>();

	Thread runner;

	private long limit;

	public ThreadedBufferPO(long l) {
		this.limit = l;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		synchronized (this) {
			if (limit > 0) {
				while (buffer.size() > limit) {
					try {
						wait(1000);
					} catch (InterruptedException e) {
					}
				}
			}
			buffer.add(object);
			notifyAll();
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		buffer.clear();
		// Create new thread and start
		runner = new Thread("ThreadedBuffer " + getName()) {
			public void run() {
				while (isOpen()) {
					synchronized (buffer) {
						while (!(buffer.peek() == null)) {
							transferNext(buffer.pop());
						}
					}
					synchronized (this) {
						try {
							wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				// drain buffer at done/close
				while (!(buffer.peek() == null)) {
					transferNext(buffer.pop());
				}
			};

			@SuppressWarnings("unchecked")
			private void transferNext(IStreamable element) {

				if (element.isPunctuation()) {
					sendPunctuation((IPunctuation) element);
				} else {
					transfer((R) element);
					if (limit > 0) {
						synchronized (this) {
							notifyAll();
						}
					}
				}

				// the top element of a buffer must always be
				// an real element, send punctuations immediately

				IStreamable peek;
				while ((peek = buffer.peek()) != null
						&& peek.isPunctuation()) {
					sendPunctuation((IPunctuation) buffer.pop());
				}

				if (isDone()) {
					propagateDone();
				}
			}
		};
		runner.setDaemon(true);
		runner.start();
	}

}
