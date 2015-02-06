package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.ArrayList;
import java.util.List;

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

	private List<IStreamable> inputBuffer = new ArrayList<>();
	private List<IStreamable> outputBuffer = new ArrayList<>();

	Thread runner;
	boolean started = false;

	final private long limit;

	public ThreadedBufferPO(long l) {
		this.limit = l;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public long getElementsStored1() {
		return inputBuffer.size() + outputBuffer.size();
	}

	public long getInputBufferSize() {
		return inputBuffer.size();
	}

	public long getOutputBufferSize() {
		return outputBuffer.size();
	}

	@Override
	protected void process_next(R object, int port) {
		// Start thread
		if (!started) {
			runner.start();
			started = true;
		}
		addObjectToBuffer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		addObjectToBuffer(punctuation);
	}

	private void addObjectToBuffer(IStreamable object) {
		synchronized (this) {
			if (limit > 0) {
				while (getElementsStored1() > limit) {
					try {
						wait(1000);
					} catch (InterruptedException e) {						
					}
				}
			}
			synchronized (inputBuffer) {
				inputBuffer.add(object);
			}
			notifyAll();
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		inputBuffer.clear();
		outputBuffer.clear();
		// Create new thread and start
		runner = new Thread("ThreadedBuffer " + getName()) {
			public void run() {
				while (isOpen()) {
					synchronized (this) {
						try {
							if (inputBuffer.isEmpty()) {
								wait(1000);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// inputBuffer to outputBuffer
					synchronized (inputBuffer) {
						List<IStreamable> tmp = outputBuffer;
						outputBuffer = inputBuffer;						
						inputBuffer = tmp;
					}
					for (int i=0;i<outputBuffer.size();i++){
						transferNext(outputBuffer.get(i));
					}
					outputBuffer.clear();
				}
			};

		};
		runner.setDaemon(true);
	}

	@Override
	protected void process_close() {
		synchronized (inputBuffer) {
			drainBuffers();
		}
	}

	protected void process_done(int port) {
		synchronized (inputBuffer) {
			drainBuffers();
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
		
	}

	private void drainBuffers() {
		// drain buffer at done/close
		// Copy everything from inputBuffer to outputBuffer
		synchronized (inputBuffer) {
			outputBuffer.addAll(inputBuffer);
			inputBuffer.clear();
		}
		for (int i=0;i<outputBuffer.size();i++){
			transferNext(outputBuffer.get(i));
		}
		outputBuffer.clear();
	}

}
