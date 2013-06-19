package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public class MultipleSourceExecutor extends Thread implements IEventListener, ISourceExecutor {

	Logger logger = LoggerFactory.getLogger(MultipleSourceExecutor.class);

	private boolean interrupt = false;
	private List<IIterableSource<?>> sources = new CopyOnWriteArrayList<IIterableSource<?>>();
	private SimpleThreadScheduler caller;	

	public MultipleSourceExecutor(List<IIterableSource<?>> sources, SimpleThreadScheduler singleThreadScheduler) {
		String name = "";
		String sep = "";
		for (IIterableSource<?> s : sources) {
			name = name + sep + s.getName();
			sep = ", ";
		}
		this.setName(name);
		this.sources = sources;
		this.caller = singleThreadScheduler;

	}

	@Override
	public void run() {
		logger.debug("Starting source scheduling for "+sources+" - "+this+"("+this.hashCode()+")");
		interrupt = false;
		for (IIterableSource<?> s : sources) {
			logger.debug("Adding Source. " + s + ".Waiting for open ...");
			s.subscribe(this, POEventType.OpenDone);
			synchronized (this) {
				while (!interrupt && !s.isOpen() && !isInterrupted() && caller.isRunning()) {
					try {
						this.wait(1000);
					} catch (InterruptedException ignored) {
					}
				}
			}
			if (!interrupt) {
				logger.debug("Opened " + this.hashCode() + "... Start Processing of Source " + s);
			}
		}
		logger.debug("All sources are open");
		boolean alldone = false;
		while (!interrupt && !isInterrupted() && !alldone) {
			for (IIterableSource<?> s : sources) {
				if (s.isOpen() && !s.isDone()) {
					if (s.hasNext()) {
						long ct1 = -1;
						if (s.getDelay() > 0) {
							ct1 = System.currentTimeMillis();
						}
						s.transferNext();
						if (s.getDelay() > 0) {
							long ct2 = System.currentTimeMillis();
							long diff = ct2 - ct1;
							try {
								if (s.getDelay() - diff > 0) {
									Thread.sleep(s.getDelay() - diff);
								}
							} catch (InterruptedException e) {
								// Exception can be ignored
							}
						}
					}
				}else{
					alldone = true;
					for(IIterableSource<?> others : sources){
						if(!others.isDone()){
							alldone = false;
						}
					}					
				}
			}
		}

		logger.debug("Removing " + this.hashCode() + " Sources " + sources);
		caller.removeSourceThread(this);

	}

	@Override
	public void interrupt() {
		super.interrupt();
		this.interrupt = true;
	}

	@Override
	public boolean hasSource(IIterableSource<?> other) {
		return this.sources.contains(other);
	}

	@Override
	public Collection<IIterableSource<?>> getSources() {
		return Collections.unmodifiableCollection(sources);
	}

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		notifyAll();
	}

}
