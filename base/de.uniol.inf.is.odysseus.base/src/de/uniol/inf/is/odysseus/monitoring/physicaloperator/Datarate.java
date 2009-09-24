package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.monitoring.AbstractPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.ISubscriber;
import de.uniol.inf.is.odysseus.monitoring.MonitoringDataScheduler;
import de.uniol.inf.is.odysseus.physicaloperator.base.IObservablePhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class Datarate extends AbstractPeriodicalMonitoringData<Double> implements POEventListener {

	private Integer writeCount;
	private long lastTimestamp;
	private Double value;

	public Datarate(IObservablePhysicalOperator po) {
		super(po);
		reset();
	}

	public Datarate(Datarate datarate) {
		super(datarate.getTarget());
		this.writeCount = datarate.writeCount;
		this.lastTimestamp = datarate.lastTimestamp;
		this.value = datarate.value;
	}


	public void reset() {
		this.writeCount = 0;
		this.lastTimestamp = System.currentTimeMillis();
		this.value = new Double(0);
	}

	public String getType() {
		return MonitoringDataTypes.DATARATE.name;
	}

	public Double getValue() {
		return this.value;
	}

	public void run() {
		synchronized (this.value) {
			long currentTime = System.currentTimeMillis();
			
			System.out.println("datarate update");
			this.value = (double) writeCount
					/ (currentTime - lastTimestamp);
			notifySubscribers(value);
			lastTimestamp = currentTime;
			this.writeCount = 0;
		}
	}

	@Override
	public void subscribe(ISubscriber<Double> subscriber) {
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).subscribe(this,POEventType.PushDone);
		}
		super.subscribe(subscriber);
	}
	
	@Override
	public void unsubscribe(ISubscriber<Double> subscriber) {
		super.unsubscribe(subscriber);
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).unsubscribe(this,POEventType.PushDone);
			MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(this);
		}
	}

	public void poEventOccured(POEvent poEvent) {
		synchronized (this.value) {
			++writeCount;
		}
	}
	
	@Override
	public AbstractPeriodicalMonitoringData<Double> clone() {
		return new Datarate(this);
	}
}
