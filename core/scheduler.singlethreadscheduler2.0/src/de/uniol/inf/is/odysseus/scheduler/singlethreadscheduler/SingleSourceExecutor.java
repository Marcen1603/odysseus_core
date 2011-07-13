package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;

class SingleSourceExecutor extends Thread {

	Logger logger = LoggerFactory.getLogger(SingleSourceExecutor.class);

	private IIterableSource<?> s;

	private SingleThreadSchedulerWithStrategy caller;

	public SingleSourceExecutor(IIterableSource<?> s,
			SingleThreadSchedulerWithStrategy singleThreadScheduler) {
		this.s = s;
		this.caller = singleThreadScheduler;
	}

	@Override
	public void run() {
		logger.debug("Added Source " + s);
		while (!isInterrupted() && s.isOpen() && !s.isDone()) {
			while (s.hasNext()) {
				s.transferNext();
			}
			Thread.yield();
		}
		logger.debug("Remove Source " + s);
		caller.removeSourceThread(this);
	}

}