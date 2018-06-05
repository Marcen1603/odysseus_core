/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

class SingleSourceExecutor extends Thread implements IEventListener, ISourceExecutor {

	Logger logger = LoggerFactory.getLogger(SingleSourceExecutor.class);

	final private IIterableSource<?> s;
	final private long delay;
	final private int yieldRate;
	final private int nanoyYieldDuration;
	private int yieldCounter = 0;

	final private AbstractSimpleThreadScheduler caller;

	private boolean interrupt;

	public SingleSourceExecutor(IIterableSource<?> s,
			AbstractSimpleThreadScheduler singleThreadScheduler) {
		this.setName(s.getName());
		this.s = s;
		this.caller = singleThreadScheduler;
		this.delay = s.getDelay();
		this.yieldRate = s.getYieldRate();
		this.nanoyYieldDuration = s.getYieldDurationNanos();
	}

	

	@Override
	public void run() {
		interrupt = false;
		logger.debug("Adding Source. " + s + ".Waiting for open ...");
		s.subscribe(this, POEventType.OpenDone);
		synchronized (this) {
			while (!interrupt && !s.isOpen() && !isInterrupted()
					&& caller.isRunning()) {
				try {
					this.wait(1000);
				} catch (InterruptedException ignored) {
				}
			}
		}
		if (!interrupt) {
			logger.debug("Opened " + this.hashCode()
					+ "... Start Processing of Source " + s);
		}
		while (!interrupt && !isInterrupted() && s.isOpen() && !s.isDone()) {
			if (s.hasNext()) {
				long ct1 = -1;
				if (delay > 0) {
					ct1 = System.currentTimeMillis();
				}
				s.transferNext();
				if (delay > 0) {
					long ct2 = System.currentTimeMillis();
					long diff = ct2 - ct1;
					try {
						if (delay - diff > 0) {
							Thread.sleep(delay - diff);
						}
					} catch (InterruptedException e) {
						// Exception can be ignored
					}
				}
				if (yieldRate > 0) {
					yieldCounter++;
					if (yieldRate == yieldCounter) {
						yieldCounter = 0;
						try {
							this.setPriority(2);
							Thread.sleep(0L, nanoyYieldDuration);
							this.setPriority(Thread.NORM_PRIORITY);
						} catch (InterruptedException e) {
						}
					}
				}
				//Thread.yield();
			}
		}

		logger.debug("Removing " + this.hashCode() + " Source " + s);
		caller.removeSourceThread(this);
	}

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		notifyAll();
	}

	@Override
	public void interrupt() {
		super.interrupt();
		this.interrupt = true;
	}

	@Override
	public boolean hasSource(IIterableSource<?> other) {
		return this.s.equals(other);
	}

	@Override
	public Collection<IIterableSource<?>> getSources() {
		List<IIterableSource<?>> sources = new ArrayList<IIterableSource<?>>();
		sources.add(s);
		return sources;		
	}

}
