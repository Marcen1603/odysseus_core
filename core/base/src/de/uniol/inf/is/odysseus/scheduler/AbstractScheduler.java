package de.uniol.inf.is.odysseus.scheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.event.EventHandler;
import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent.SchedulingEventType;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Base class for scheduler. Contains Methodes for setting the scheduling state
 * and error handling.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractScheduler extends EventHandler implements
		IScheduler {
	/**
	 * Indicates if the scheduling is started.
	 */
	private boolean isRunning;

	/**
	 * Maximum time each strategy can use (no garantee if strategy)
	 */
	protected volatile long timeSlicePerStrategy = OdysseusDefaults.getLong("scheduler_TimeSlicePerStrategy",10);

	/**
	 * The {@link ISchedulingFactory} which will be used for scheduling. Each
	 * PartialPlan will be initialized with a new strategy instance.
	 */
	protected ISchedulingFactory schedulingFactory;

	/**
	 * Object which will be informed if an error will occure.
	 */
	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();

	// --- Events

	private SchedulingEvent schedulingStarted = new SchedulingEvent(this,
			SchedulingEventType.SCHEDULING_STARTED, "");
	private SchedulingEvent schedulingStopped = new SchedulingEvent(this,
			SchedulingEventType.SCHEDULING_STOPPED, "");

	// ---- Evaluations ----
	final boolean outputDebug = Boolean.parseBoolean(OdysseusDefaults
			.get("debug_Scheduler"));

	FileWriter file;
	final long limitDebug = OdysseusDefaults.getLong("debug_Scheduler_maxLines",1048476);
	long linesWritten;

	/**
	 * Creates a new scheduler.
	 * 
	 * @param schedulingFactory
	 *            {@link ISchedulingFactory} which will be used for scheduling.
	 *            Each PartialPlan will be initialized with a new strategy
	 *            instance.
	 */
	public AbstractScheduler(ISchedulingFactory schedulingFactory) {
		this.schedulingFactory = schedulingFactory;
	}

	public int print(IScheduling s) {
		StringBuffer toPrint = new StringBuffer();
		List<IQuery> queries = s.getPlan().getQueries();
		int linesPrinted = queries.size();
		for (IQuery q : queries) {
			toPrint.append(System.currentTimeMillis()).append(";");
			toPrint.append(s.getPlan().getId()).append(";").append(q.getID())
					.append(";").append(s.getPlan().getCurrentPriority())
					.append(";").append((""+q.getPenalty()).replace('.', ','))
					.append(";");
			ScheduleMeta h = s.getPlan().getScheduleMeta();
			if (h!=null){
				h.csvPrint(toPrint);
			}
			toPrint.append("\n");
		}
		// logger.debug(toPrint.toString());
		// System.out.println(toPrint);
		try {
			file.write(toPrint.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linesPrinted;
	}

	public boolean isOutputDebug() {
		return outputDebug;
	}

	public long getLimitDebug() {
		return limitDebug;
	}

	public long getLinesWritten() {
		return linesWritten;
	}

	public void incLinesWritten(int value) {
		linesWritten+=value;
	}

	/**
	 * Send an ErrorEvent to all registered listeners.
	 * 
	 * @param eventArgs
	 *            {@link ErrorEvent} which should be send.
	 */
	@Override
	public synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.errorEventOccured(eventArgs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#startScheduling()
	 */
	@Override
	public synchronized void startScheduling() {
		this.isRunning = true;
		if (outputDebug) {
			try {
				file = new FileWriter(OdysseusDefaults.odysseusHome
						+ "SchedulerLog" + System.currentTimeMillis() + ".csv");
				file.write("Timestamp;PartialPlan;Query;Priority;Penalty;DiffToLastCall;InTimeCalls;AllCalls;Factor;HistorySize\n");
				linesWritten = 1; // Header!
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			file = null;
		}
		fire(schedulingStarted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#stopScheduling()
	 */
	@Override
	public synchronized void stopScheduling() {
		this.isRunning = false;
		if (outputDebug) {
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fire(schedulingStopped);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.IScheduler#setTimeSlicePerStrategy
	 * (long)
	 */
	@Override
	public void setTimeSlicePerStrategy(long time) {
		this.timeSlicePerStrategy = time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#addErrorEventListener
	 * (de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		if (!this.errorEventListener.contains(errorEventListener)) {
			this.errorEventListener.add(errorEventListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#
	 * removeErrorEventListener
	 * (de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}

}