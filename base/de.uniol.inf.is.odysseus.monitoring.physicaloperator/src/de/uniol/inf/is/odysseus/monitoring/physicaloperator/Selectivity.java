package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.IObservablePhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;


public abstract class Selectivity extends AbstractMonitoringData<Double>
		implements POEventListener {

	private Integer writeCount;
	private int[] readCount;
	private int readCountSum;
	private int sourceCount;

	public Selectivity(IObservablePhysicalOperator target, int sourceCount) {
		super(target);
		this.sourceCount = sourceCount;
		reset();
		target.subscribe(this, POEventType.PushDone);
		target.subscribe(this, POEventType.ProcessDone);
	}

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

	public void poEventOccured(POEvent poEvent) {
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

	@Override
	public AbstractMonitoringData<Double> clone() {
		Selectivity ret = null;
		ret = (Selectivity) super.clone();
		ret.writeCount = writeCount;
		ret.readCount = new int[readCount.length];
		System.arraycopy(readCount, 0, ret.readCount, 0, readCount.length);
		ret.readCountSum = readCountSum;
		
		return ret;
	}
}
