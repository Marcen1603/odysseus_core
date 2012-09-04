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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.POEventType;

class SingleSourceExecutor extends Thread implements IEventListener {

	Logger logger = LoggerFactory.getLogger(SingleSourceExecutor.class);

	private IIterableSource<?> s;

	private SimpleThreadScheduler caller;

	private boolean interrupt;

	public SingleSourceExecutor(IIterableSource<?> s,
			SimpleThreadScheduler singleThreadScheduler) {
		this.setName(s.getName());
		this.s = s;
		this.caller = singleThreadScheduler;
	}

	@Override
	public void run() {
		interrupt = false;
		logger.debug("Adding Source. " + s + ".Waiting for open ...");
		s.subscribe(this, POEventType.OpenDone);
		synchronized (this) {
			while (!interrupt && !s.isOpen() && !isInterrupted()) {
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
				s.transferNext();
				Thread.yield();
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

}
