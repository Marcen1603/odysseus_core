package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.ISubscriber;
import de.uniol.inf.is.odysseus.monitoring.MonitoringDataScheduler;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;

public class Datarate extends AbstractPeriodicalMonitoringData<Double>
		implements IPOEventListener {

	private Integer writeCount;
	private long lastTimestamp;
	private Double value;

	public Datarate(IPhysicalOperator target) {
		super(target, MonitoringDataTypes.DATARATE.name);
		reset();
		target.subscribe(this, POEventType.ProcessDone);
	}

	public Datarate(Datarate datarate) {
		super(datarate.getTarget(), MonitoringDataTypes.DATARATE.name);
		this.writeCount = datarate.writeCount;
		this.lastTimestamp = datarate.lastTimestamp;
		this.value = datarate.value;
		((IPhysicalOperator) datarate.getTarget()).subscribe(this,
				POEventType.ProcessDone);
	}

	@Override
	public void reset() {
		this.writeCount = 0;
		this.lastTimestamp = System.currentTimeMillis();
		this.value = new Double(0);
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void run() {
		synchronized (this) {
			long currentTime = System.currentTimeMillis();

			this.value = (double) writeCount / (currentTime - lastTimestamp);
			notifySubscribers(value);
			lastTimestamp = currentTime;
			this.writeCount = 0;
		}
	}

	@Override
	public void subscribe(ISubscriber<Double> subscriber) {
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).subscribe(this, POEventType.PushDone);
		}
		super.subscribe(subscriber);
	}

	@Override
	public void unsubscribe(ISubscriber<Double> subscriber) {
		super.unsubscribe(subscriber);
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).unsubscribe(this, POEventType.PushDone);
			MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(
					this);
		}
	}

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event) {
		++writeCount;
	}

	@Override
	public AbstractPeriodicalMonitoringData<Double> clone() {
		return new Datarate(this);
	}
}
