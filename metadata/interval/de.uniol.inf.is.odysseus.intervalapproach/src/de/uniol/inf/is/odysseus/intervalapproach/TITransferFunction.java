package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;

/**
 * @author Jonas Jacobi
 */
public class TITransferFunction<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		implements ITransferFunction<T> {

	final protected ITimeInterval[] minTs;
	protected AbstractSource<T> po;
	// Initiale Groesse da Comperator uebergeben werden muss
	final protected PriorityQueue<T> out = new PriorityQueue<T>(11,
			new MetadataComparator<ITimeInterval>());

	public TITransferFunction() {
		minTs = new ITimeInterval[2];
	}

	public TITransferFunction(TITransferFunction<T> tiTransferFunction)  {
		minTs = new ITimeInterval[2];
		minTs[0] = tiTransferFunction.minTs[0].clone();
		minTs[1] = tiTransferFunction.minTs[1].clone();
		out.addAll(tiTransferFunction.out);
	}

	public void setSourcePo(AbstractSource<T> po) {
		this.po = po;
	}

	@Override
	public void init(AbstractSource<T> po) {
		this.po = po;
		TimeInterval ti = new TimeInterval(PointInTime.getZeroTime(),
				PointInTime.getInfinityTime());
		this.minTs[0] = ti;
		this.minTs[1] = ti;
		this.out.clear();
	}

	@Override
	public void newElement(T object, int port) {
		ITimeInterval minimum = null;
		synchronized (minTs) {
			minTs[port] = object.getMetadata();
			if (minTs[0]!=null && minTs[1]!=null){
				minimum = TimeInterval.startsBefore(minTs[0], minTs[1]) ? minTs[0]
				                                                               : minTs[1];
			}
		}
		if (minimum != null){
			synchronized (this.out) {
				// don't use an iterator, it does NOT guarantee ordered traversal!
				T elem = this.out.peek();
				while(elem != null && TimeInterval.startsBeforeOrEqual(elem.getMetadata(), minimum)){
					this.out.poll();
					po.transfer(elem);
					elem = this.out.peek();
				}
			}
		}
	}

	@Override
	public void transfer(T object) {
		synchronized (this.out) {
			out.add(object);
		}
	}

	@Override
	public void done() {
		for (T element : out) {
			po.transfer(element);
		}
		out.clear();
	}

	@Override
	public int size() {
		return out.size();
	}
	
	public TITransferFunction<T> clone() {
		return new TITransferFunction<T>(this);
	}

	
}
