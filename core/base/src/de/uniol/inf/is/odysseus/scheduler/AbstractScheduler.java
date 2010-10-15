package de.uniol.inf.is.odysseus.scheduler;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.event.EventHandler;
import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent.SchedulingEventType;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Base class for scheduler. Contains Methodes for setting the scheduling state
 * and error handling.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractScheduler extends EventHandler implements IScheduler{
	/**
	 * Indicates if the scheduling is started.
	 */
	private boolean isRunning;

	/**
	 * Maximum time each strategy can use (no garantee if strategy)
	 */
	protected volatile long timeSlicePerStrategy = 10;

	/**
	 * The {@link ISchedulingFactory} which will be used for scheduling.
	 * Each PartialPlan will be initialized with a new strategy instance.
	 */
	protected ISchedulingFactory schedulingFactory;

	/**
	 * Object which will be informed if an error will occure.
	 */
	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();

	//--- Events
	
	private SchedulingEvent schedulingStarted = new SchedulingEvent(this,SchedulingEventType.SCHEDULING_STARTED,"");
	private SchedulingEvent schedulingStopped = new SchedulingEvent(this,SchedulingEventType.SCHEDULING_STOPPED,"");
	
	/**
	 * Creates a new scheduler.
	 * 
	 * @param schedulingFactory
	 *            {@link ISchedulingFactory} which will be used for
	 *            scheduling. Each PartialPlan will be initialized with a new
	 *            strategy instance.
	 */
	public AbstractScheduler(
			ISchedulingFactory schedulingFactory) {
		this.schedulingFactory = schedulingFactory;
	}

	/**
	 * Send an ErrorEvent to all registered listeners.
	 * 
	 * @param eventArgs {@link ErrorEvent} which should be send.
	 */
	@Override
	public synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.errorEventOccured(eventArgs);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#startScheduling()
	 */
	@Override
	public synchronized void startScheduling() {
		this.isRunning = true;
		fire(schedulingStarted);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#stopScheduling()
	 */
	@Override
	public synchronized void stopScheduling() {
		this.isRunning = false;
		fire(schedulingStopped);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#setTimeSlicePerStrategy(long)
	 */
	@Override
	public void setTimeSlicePerStrategy(long time) {
		this.timeSlicePerStrategy = time;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#addErrorEventListener(de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		if (!this.errorEventListener.contains(errorEventListener)) {
			this.errorEventListener.add(errorEventListener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#removeErrorEventListener(de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
	
	
}