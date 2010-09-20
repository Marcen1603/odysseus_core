package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.base.IEvent;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class AvgProcessingTime extends AbstractMonitoringData<Double> implements
		IPOEventListener {

	private long start = 0;
	private long lastRun = -1;
	private double runSum = 0;
	private long runCount = 0;

	public AvgProcessingTime(){
		super();
	}
	
	public AvgProcessingTime(IPhysicalOperator target) {
		setTarget(target);
	}

	public AvgProcessingTime(AvgProcessingTime avgProcessingTime) {
		setTarget(avgProcessingTime.getTarget());
		this.start = avgProcessingTime.start;
		this.lastRun = avgProcessingTime.lastRun;
		this.runSum = avgProcessingTime.runSum;
		this.runCount = avgProcessingTime.runCount;
	}
	
	public void setTarget(IPhysicalOperator target) {
		super.setTarget(target);
		target.subscribe(this, POEventType.ProcessInit);
		target.subscribe(this, POEventType.ProcessDone);
	}

	@Override
	public void eventOccured(IEvent<?,?> poEvent) {
		if (poEvent.getEventType().equals(POEventType.ProcessInit)) {
			start = System.nanoTime();
		} else {
			lastRun = System.nanoTime() - start;
			runCount++;
			runSum += lastRun;
		}
	}

	@Override
	public String getType() {
		return MonitoringDataTypes.PROCESSING_COST.name;
	}

	@Override
	public Double getValue() {
		if (runCount > 0) {
			return runSum / runCount;
		}
		return null;
	}

	@Override
	public void reset() {
		start = 0;
		lastRun = -1;
		runSum = 0;
		runCount = 0;
	}

	@Override
	public AvgProcessingTime clone() {
		return new AvgProcessingTime(this);
	}

}
