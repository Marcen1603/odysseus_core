package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.event.POPortEvent;


public abstract class Selectivity extends AbstractMonitoringData<Double>
		implements IPOEventListener {

	private Integer writeCount;
	private int[] readCount;
	private int readCountSum;
	private int sourceCount;

	public Selectivity(IPhysicalOperator target, int sourceCount) {
		super(target);
		this.sourceCount = sourceCount;
		reset();
		target.subscribe(this, POEventType.PushDone);
		target.subscribe(this, POEventType.ProcessDone);
	}
	
	public Selectivity(Selectivity other){
		super(other);
		this.writeCount = other.writeCount;
		this.readCount = new int[other.readCount.length];
		System.arraycopy(readCount, 0, this.readCount, 0, other.readCount.length);
		this.readCountSum = other.readCountSum;
	}
	
	

	@Override
	public void reset() {
		this.writeCount = 0;
		if (this.readCount == null){
			this.readCount = new int[sourceCount];
		}
		for (int i = 0; i < sourceCount; i++) {
			this.readCount[i] = 0;
		}
	}

	final protected double getWriteCount() {
		return (double) this.writeCount;
	}

	final protected double getReadCount(int port) {
		return (double) this.readCount[port];
	}

	final protected double getReadCountSum() {
		return (double) this.readCountSum;
	}

	final protected double getReadCountProduct() {
		int c = 0;
		for (int i = 0; i < readCount.length; i++) {
			c += readCount[i];
		}
		return (double) c;
	}

	final protected int getReadCountLength() {
		return readCount.length;
	}

	@Override
	public void eventOccured(IEvent<?,?> event) {
		POEvent poEvent = (POEvent) event;
		synchronized (this.writeCount) {
			synchronized (this.readCount) {
				if (poEvent.getPOEventType() == POEventType.PushDone) {
					++writeCount;
				} else if (poEvent.getPOEventType() == POEventType.ProcessDone) {
					this.readCount[((POPortEvent) poEvent).getPort()]++;
					readCountSum++;
				}
			}
		}
	}

}
