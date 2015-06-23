package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
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
		extends AbstractPipe<R, R> implements IPhysicalOperatorKeyValueProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadedBufferPO.class);
	
	private long elementsRead;
	private long puncRead;

	final private long limit;
	private boolean done;

	private List<IStreamable> inputBuffer = new ArrayList<>();
	private List<IStreamable> outputBuffer = new ArrayList<>();
	final private ReentrantLock lockInput = new ReentrantLock(true);
	final private ReentrantLock lockOutput = new ReentrantLock(true);

	Runner runner;
	private boolean drainAtClose;
	private AtomicBoolean isClosing;

	class Runner extends Thread {

		private boolean terminate = false;
		private boolean started = false;

		public Runner(String name) {
			super(name);
		}

		public void terminate() {
			this.terminate = true;
		}

		public void run() {
			started = true;
			while (!terminate) {
				synchronized (this) {
					try {
						if (inputBuffer.isEmpty()) {
							wait(1000);
							if (terminate) {
								continue;
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// inputBuffer to outputBuffer
				lockInput.lock();
				List<IStreamable> tmp = outputBuffer;
				outputBuffer = inputBuffer;
				inputBuffer = tmp;
				lockInput.unlock();
				lockOutput.lock();
				Iterator<IStreamable> outIter = outputBuffer.iterator();
				while (outIter.hasNext() && !terminate) {
					transferNext(outIter.next());
					outIter.remove();
				}
				lockOutput.unlock();
			}
			started = false;
		};

	}

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

	public boolean isRunning() {
		return runner != null && runner.started;
	}

	@Override
	protected void process_next(R object, int port) {
		elementsRead++;
		addObjectToBuffer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		puncRead++;
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
			notifyAll();
		}
		lockInput.lock();
		inputBuffer.add(object);
		lockInput.unlock();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		done = false;
		elementsRead = 0;
		inputBuffer.clear();
		outputBuffer.clear();
		this.isClosing = new AtomicBoolean(false);
		// Create new thread and start
		runner = new Runner("ThreadedBuffer " + getName());
		runner.setDaemon(true);
		runner.start();
	}

	@Override
	protected void process_close() {
		LOG.debug("Closing ...."+this.getName());
		this.isClosing.set(true);
		runner.terminate();
		synchronized(runner){
			runner.notify();
		}
		lockOutput.lock();
		if (drainAtClose) {
			drainBuffers();
		} else {
			inputBuffer.clear();
			outputBuffer.clear();
		}
		lockOutput.unlock();
		isClosing.set(false);
		LOG.debug("Closing .... done");
	}

	protected void process_done(int port) {
		if (!isClosing.get()) {
			lockOutput.lock();
			drainBuffers();
			lockOutput.unlock();
			done = true;
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}

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
		LOG.debug("Draining ....");

		// drain buffer (typically at done/close)
		// Copy everything from inputBuffer to outputBuffer
		runner.terminate();
		
		lockInput.lock();
		outputBuffer.addAll(inputBuffer);
		inputBuffer.clear();
		lockInput.unlock();
		for (int i = 0; i < outputBuffer.size(); i++) {
			transferNext(outputBuffer.get(i));
		}
		outputBuffer.clear();
		LOG.debug("Draining .... done");
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("Elements processed", elementsRead + "");
		map.put("Punctuations processed", puncRead + "");
		map.put("CurrentSize", getElementsStored1() + "");
		map.put("InputQueue", getInputBufferSize() + "");
		map.put("OutputQueue", getOutputBufferSize() + "");
		map.put("started", isRunning() + "");
		return map;
	}

	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ThreadedBufferPO)){
			return false;
		}

		@SuppressWarnings("unchecked")
		ThreadedBufferPO<R> po = (ThreadedBufferPO<R>) ipo;
		if (this.drainAtClose != po.drainAtClose){
			return false;
		}
		
		if (this.limit != po.limit){
			return false;
		}
		
		return super.isSemanticallyEqual(ipo);
	}
	
}
