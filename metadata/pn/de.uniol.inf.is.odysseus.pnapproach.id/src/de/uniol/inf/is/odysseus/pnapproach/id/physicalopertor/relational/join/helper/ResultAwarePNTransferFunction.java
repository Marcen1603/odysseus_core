package de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join.helper;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join.ResultAwareJoinPNIDPO;
import de.uniol.inf.is.odysseus.pnapproach.id.sweeparea.ResultAwarePNIDSweepArea;

public class ResultAwarePNTransferFunction<M extends IPosNeg, T extends IMetaAttributeContainer<M>> implements ITransferFunction<T> {

	private ResultAwareJoinPNIDPO<M, T> po;
	private ResultAwarePNIDSweepArea<T> sweepArea;
	final protected PointInTime[] minTs;
	private int counter;
	
	public ResultAwarePNTransferFunction() {
		minTs = new PointInTime[2];
		this.sweepArea = new ResultAwarePNIDSweepArea<T>();
		this.counter = 0;
	}
	
	public ResultAwarePNTransferFunction(
			ResultAwarePNTransferFunction<M, T> resultAwarePNTransferFunction)  {
		minTs = new PointInTime[2];
		minTs[0] = resultAwarePNTransferFunction.minTs[0].clone();
		minTs[1] = resultAwarePNTransferFunction.minTs[1].clone();
		sweepArea = resultAwarePNTransferFunction.sweepArea.clone();;
		counter = resultAwarePNTransferFunction.counter;
		po = resultAwarePNTransferFunction.po;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setSourcePo(AbstractSource<T> source) {
		po = (ResultAwareJoinPNIDPO<M,T>) source;
	}

	public void init(AbstractSource<T> po){
		throw new UnsupportedOperationException("Method not supported for this kind of transfer function.\n" +
				"Use init(ResultAwareJoinPNIDPO instead.");
	}
	
	public void init(ResultAwareJoinPNIDPO<M, T> po) {
		this.po = po;
		this.minTs[0] = PointInTime.getZeroTime();
		this.minTs[1] = PointInTime.getZeroTime();
		this.sweepArea.clear();
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
			this.counter += this.sweepArea.insertAndRemovedWrongScheduled(object);

		}
	}
	
	public void transferForce(T object){
		synchronized(this.sweepArea){
			this.sweepArea.insert(object);
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
	
	public ResultAwarePNTransferFunction<M,T> clone() {
		return new ResultAwarePNTransferFunction<M,T>(this);
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebug(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
