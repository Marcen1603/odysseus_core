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

	private SingleThreadSchedulerWithStrategy caller;

	public SingleSourceExecutor(IIterableSource<?> s,
			SingleThreadSchedulerWithStrategy singleThreadScheduler) {
		this.setName(s.getName());
		this.s = s;
		this.caller = singleThreadScheduler;
	}

	@Override
	public void run() {

		logger.debug("Adding Source " + s);
		logger.debug("Waiting for open");
		s.subscribe(this, POEventType.OpenDone);
		synchronized (this) {
			while (!s.isOpen()) {
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("Opened ... Start Processing");
		while (!isInterrupted() && s.isOpen() && !s.isDone() && s.hasNext()) {
			s.transferNext();
			Thread.yield();
		}
		logger.debug("Removing Source " + s);
		caller.removeSourceThread(this);
	}

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		notifyAll();
	}

}