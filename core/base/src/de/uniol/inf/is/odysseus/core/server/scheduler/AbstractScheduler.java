/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.scheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulingEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulingEvent.SchedulingEventType;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Base class for scheduler. Contains Methodes for setting the scheduling state
 * and error handling.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractScheduler implements
		IScheduler {
	/**
	 * Indicates if the scheduling is started.
	 */
	private boolean isRunning;

	/**
	 * Maximum time each strategy can use (no garantee if strategy)
	 */
	protected volatile long timeSlicePerStrategy = OdysseusConfiguration.getLong(
			"scheduler_TimeSlicePerStrategy", 10);

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
	final boolean outputDebug = Boolean.parseBoolean(OdysseusConfiguration
			.get("debug_Scheduler"));

	FileWriter file;
	final long limitDebug = OdysseusConfiguration.getLong(
			"debug_Scheduler_maxLines", 1048476);
	long linesWritten;
	StringBuffer toPrint = new StringBuffer();

	private List<IIterableSource<?>> sources;

	private Collection<IPhysicalQuery> partialPlans;
	
	private final EventHandler eventHandler;

	/**
	 * Creates a new scheduler.
	 * 
	 * @param schedulingFactory
	 *            {@link ISchedulingFactory} which will be used for scheduling.
	 *            Each PartialPlan will be initialized with a new strategy
	 *            instance.
	 */
	public AbstractScheduler(ISchedulingFactory schedulingFactory) {
		eventHandler = EventHandler.getInstance(this);
		this.schedulingFactory = schedulingFactory;
	}

//	@SuppressWarnings("rawtypes")
//	public int print(IScheduling s) {
//		List<IQuery> queries = s.getPlan().getQueries();
//		int linesPrinted = queries.size();
//		for (IQuery q : queries) {
//			toPrint.append(System.currentTimeMillis()).append(";");
//			toPrint.append(s.getPlan().getId())
//					.append(";")
//					.append(q.getID() + 1)
//					// sieht besser aus :-)
//					.append(";").append(s.getPlan().getCurrentPriority())
//					.append(";")
//					.append(("" + q.getPenalty()).replace('.', ','))
//					.append(";");
//			// Written Objects
//			IPlanMonitor mon = q.getPlanMonitor("Root Monitor");
//			if (mon != null) {
//				toPrint.append(((ProcessCallsMonitor) mon)
//						.getOverallProcessCallCount());
//			} else {
//				toPrint.append("-1");
//			}
//			toPrint.append(";");
//			ScheduleMeta h = s.getPlan().getScheduleMeta();
//			if (h != null) {
//				h.csvPrint(toPrint);
//			}
//			toPrint.append("\n");
//		}
//		return linesPrinted;
//	}

	public void savePrint() {
		try {
			file.write(toPrint.toString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		linesWritten += value;
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
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#startScheduling()
	 */
	@Override
	public void startScheduling() {
		this.isRunning = true;
		if (outputDebug) {
			try {
				file = new FileWriter(OdysseusConfiguration.getHomeDir()
						+ OdysseusConfiguration.get("scheduler_DebugFileName") + "_"
						+ System.currentTimeMillis() + ".csv");
				file.write("Timestamp;PartialPlan;Query;Priority;Penalty;ObjectsWritten;DiffToLastCall;InTimeCalls;AllCalls;Factor;HistorySize\n");
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
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#stopScheduling()
	 */
	@Override
	public void stopScheduling() {
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
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#setTimeSlicePerStrategy
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
	 * de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler#addErrorEventListener
	 * (de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener)
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
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler#
	 * removeErrorEventListener
	 * (de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}

	@Override
	final public void setLeafSources(List<IIterableSource<?>> sources) {
		this.sources = sources;
		process_setLeafSources(sources);
	}

	@Override
	final public List<IIterableSource<?>> getLeafSources() {
		if (sources != null){
			return Collections.unmodifiableList(sources);
		}
		return null;
	}

	abstract protected void process_setLeafSources(
			List<IIterableSource<?>> sources);

	@Override
	final public void setPartialPlans(Collection<IPhysicalQuery> partialPlans) {
		this.partialPlans = partialPlans;
		process_setPartialPlans(partialPlans);
	}

	abstract protected void process_setPartialPlans(
			Collection<IPhysicalQuery> partialPlans);

	@Override
	final public Collection<IPhysicalQuery> getPartialPlans() {
		if (partialPlans != null){
			return Collections.unmodifiableCollection(partialPlans);
		}
		
		return null;
	}

	@Override
    public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(this,listener, type);
	}

	@Override
    public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(this,listener, type);
	}

	@Override
    public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(this,listener);
	}

	@Override
    public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(this, listener);
	}

	@Override
    public final void fire(IEvent<?, ?> event) {
		eventHandler.fire(this, event);
	}
	
	
}