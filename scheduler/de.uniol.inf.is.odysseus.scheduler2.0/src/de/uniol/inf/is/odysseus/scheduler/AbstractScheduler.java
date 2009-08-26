package de.uniol.inf.is.odysseus.scheduler;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;

public abstract class AbstractScheduler implements IScheduler {
	private boolean isRunning;

	protected volatile long timeSlicePerStrategy = 10;

	protected ISchedulingStrategyFactory schedulingStrategieFactory;

	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();
	
	public AbstractScheduler(ISchedulingStrategyFactory schedulingStrategieFactory) {
		this.schedulingStrategieFactory = schedulingStrategieFactory;
	}

	protected synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
		}
	}

	@Override
	public synchronized void startScheduling() {
		this.isRunning = true;
	}

	@Override
	public synchronized void stopScheduling() {
		this.isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void setTimeSlicePerStrategy(long time) {
		this.timeSlicePerStrategy = time;
	}

	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		if (!this.errorEventListener.contains(errorEventListener)) {
			this.errorEventListener.add(errorEventListener);
		}
	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
}