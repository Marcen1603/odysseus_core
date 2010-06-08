package de.uniol.inf.is.odysseus.pnapproach.pn.physicaloperator.relational.join.helper;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.sweeparea.DefaultPNSweepArea;

public class PNTransferFunction<T extends IMetaAttributeContainer<? extends IPosNeg>> implements ITransferFunction<T>{

	final protected PointInTime[] minTs;
	private AbstractSource<T> po;
	final protected DefaultPNSweepArea<T> sweepArea;
	private int counter;

	public PNTransferFunction() {
		minTs = new PointInTime[2];
		this.sweepArea = new DefaultPNSweepArea<T>();
		this.counter = 0;
	}

	public PNTransferFunction(PNTransferFunction<T> pnTransferFunction) {
		minTs = new PointInTime[2];
		this.minTs[0] = pnTransferFunction.minTs[0].clone();
		this.minTs[1] = pnTransferFunction.minTs[1].clone();
		this.sweepArea = pnTransferFunction.sweepArea.clone();
	}

	public void init(AbstractSource<T> po) {
		this.po = po;
		this.minTs[0] = PointInTime.getZeroTime();
		this.minTs[1] = PointInTime.getZeroTime();
		this.sweepArea.clear();
	}

	@Override
	public void setSourcePo(AbstractSource<T> source) {
		this.po = source;
	}
	
	@Override
	public void newElement(T object, int port) {
		PointInTime minimum;
		synchronized (minTs) {
			minTs[port] = object.getMetadata().getTimestamp();
			minimum = PointInTime.before(minTs[0], minTs[1]) ? minTs[0] : minTs[1];
		}
		synchronized (this.sweepArea) {
			Iterator<T> elements = this.sweepArea.extractElementsBefore(minimum);
			while (elements.hasNext()) {
				po.transfer(elements.next());
			}
		}
	}

	@Override
	public void transfer(T object) {
		synchronized (this.sweepArea) {
			this.counter += sweepArea.insertAndRemovedWrongScheduled(object);	
		}
	}

	@Override
	public void done() {
		for (T element : sweepArea) {
			po.transfer(element);
		}
		sweepArea.clear();
	}
	
	public int getWrongResultCount(){
		return counter;
	}
	
	public int size(){
		return sweepArea.size();
	}
	
	public PNTransferFunction<T> clone() {
		return new PNTransferFunction<T>(this);
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int port) {
		
	}

	@Override
	public void setDebug(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
