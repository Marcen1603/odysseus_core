/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

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
 * This is a threaded buffer that uses a {@link BlockingQueue}.
 * 
 * @author Cornelius Ludmann
 */
public class BlockingQueueThreadedBufferPO<R extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<R, R>
		implements IPhysicalOperatorKeyValueProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(BlockingQueueThreadedBufferPO.class);

	private final BlockingQueue<IStreamable> queue;
	private final AtomicBoolean done = new AtomicBoolean(true);
	private final boolean drainAtClose;
	private final int limit;
	private final int threadPriority;

	private final IStreamable closeMarker = new CloseMarker();
	private Thread thread;

	private final class CloseMarker implements IStreamable {

		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uniol.inf.is.odysseus.core.metadata.IStreamable#isPunctuation()
		 */
		@Override
		public boolean isPunctuation() {
			return true;
		}
	}

	public BlockingQueueThreadedBufferPO(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
		this.limit = -1;
		this.queue = new LinkedBlockingQueue<>();
		this.threadPriority = Thread.NORM_PRIORITY;
	}

	public BlockingQueueThreadedBufferPO(boolean drainAtClose, int limit) {
		this.drainAtClose = drainAtClose;
		this.limit = limit;
		if (limit > 0) {
			this.queue = new ArrayBlockingQueue<>(limit);
		} else {
			this.queue = new LinkedBlockingQueue<>();
		}
		this.threadPriority = Thread.NORM_PRIORITY;
	}

	public BlockingQueueThreadedBufferPO(boolean drainAtClose, int limit, int threadPriority) {
		this.drainAtClose = drainAtClose;
		this.limit = limit;
		if (limit > 0) {
			this.queue = new ArrayBlockingQueue<>(limit);
		} else {
			this.queue = new LinkedBlockingQueue<>();
		}
		if (threadPriority > Thread.MAX_PRIORITY) {
			this.threadPriority = Thread.MAX_PRIORITY;
		} else if (threadPriority < Thread.MIN_PRIORITY) {
			this.threadPriority = Thread.MIN_PRIORITY;
		} else {
			this.threadPriority = threadPriority;
		}
	}

	public BlockingQueueThreadedBufferPO(BlockingQueueThreadedBufferPO<R> other) {
		this.drainAtClose = other.drainAtClose;
		this.limit = other.limit;
		if (limit > 0) {
			this.queue = new ArrayBlockingQueue<>(limit);
		} else {
			this.queue = new LinkedBlockingQueue<>();
		}
		this.threadPriority = other.threadPriority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation(
	 * de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		try {
			queue.put(punctuation);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("Size", "" + queue.size());
		map.put("Thread Priority", "" + threadPriority);
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(R object, int port) {
		try {
			queue.put(object);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@SuppressWarnings("unchecked")
	private void transferNext(IStreamable element) {
		if (element.isPunctuation()) {
			sendPunctuation((IPunctuation) element);
		} else {
			transfer((R) element);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_open()
	 */
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		queue.clear();
		done.set(false);
		Runnable runner = new Runnable() {

			@Override
			public void run() {
				try {
					IStreamable element = null;
					while (element != closeMarker) {
						element = queue.take();
						if (element != closeMarker) {
							transferNext(element);
						}
					}
					done.set(true);
				} catch (InterruptedException e) {
					done.set(true);
					Thread.currentThread().interrupt();
				}

			}
		};
		thread = new Thread(runner, "ThreadedBuffer " + getName());
		thread.setPriority(this.threadPriority);
		thread.start();
		LOG.debug("Threaded buffer '{}' with priority '{}' started.", getName(), threadPriority);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_close()
	 */
	@Override
	protected void process_close() {
		super.process_close();
		if (drainAtClose) {
			queue.add(closeMarker);
		} else {
			thread.interrupt();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#isDone
	 * ()
	 */
	@Override
	public boolean isDone() {
		return done.get();
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof BlockingQueueThreadedBufferPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		BlockingQueueThreadedBufferPO<R> po = (BlockingQueueThreadedBufferPO<R>) ipo;
		if (this.drainAtClose != po.drainAtClose) {
			return false;
		}

		if (this.limit != po.limit) {
			return false;
		}
		
		if(this.threadPriority != po.threadPriority) {
			return false;
		}

		return true;
	};
}
