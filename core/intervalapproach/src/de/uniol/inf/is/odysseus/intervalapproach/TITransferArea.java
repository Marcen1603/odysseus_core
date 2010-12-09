package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TITransferArea<R extends IMetaAttributeContainer<? extends ITimeInterval>, W extends IMetaAttributeContainer<? extends ITimeInterval>>
		implements ITransferArea<R,W> {

	final protected PointInTime[] minTs;
	protected AbstractSource<W> po;
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());;

	public TITransferArea() {
		minTs = new PointInTime[2];
	}

	public TITransferArea(int inputPortCount) {
		minTs = new PointInTime[inputPortCount];
	}

	public TITransferArea(TITransferArea<R,W> tiTransferFunction) {
		minTs = new PointInTime[tiTransferFunction.minTs.length];
		for (int i = 0; i < minTs.length; i++) {
			minTs[i] = tiTransferFunction.minTs[i] != null ? tiTransferFunction.minTs[i]
					.clone() : null;
		}
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	@Override
	public void setSourcePo(AbstractSource<W> po) {
		this.po = po;
	}

	@Override
	public void init(AbstractSource<W> po) {
		this.po = po;
		for (int i = 0; i < minTs.length; i++) {
			this.minTs[i] = null;
		}
		this.outputQueue.clear();
	}

	@Override
	public void newElement(R object, int inPort) {
		newHeartbeat(object.getMetadata().getStart(), inPort);
	}

	@Override
	public void transfer(W object) {
		synchronized (this.outputQueue) {
			outputQueue.add(object);
		}
	}

	@Override
	public void done() {
		for (W element : outputQueue) {
			po.transfer(element);
		}
		outputQueue.clear();
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public TITransferArea<R,W> clone() {
		return new TITransferArea<R,W>(this);
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int inPort) {
		PointInTime minimum = null;
		synchronized (minTs) {
			minTs[inPort] = heartbeat;
			minimum = getMinTs();
		}
		if (minimum != null) {
			synchronized (this.outputQueue) {
				boolean wasElementSent = false;
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				W elem = this.outputQueue.peek();
				while (elem != null
						&& elem.getMetadata().getStart()
								.beforeOrEquals(minimum)) {
					this.outputQueue.poll();
					wasElementSent = true;
					po.transfer(elem);
					elem = this.outputQueue.peek();
				}
				if (wasElementSent) {
					po.sendPunctuation(minimum);
				}
			}
		}
	}

	private PointInTime getMinTs() {
		PointInTime minimum = minTs[0];
		for (PointInTime p : minTs) {
			// if one element has no value, no element
			// has been read from this input port
			// --> no data can be send
			if (p == null) {
				return null;
			}
			minimum = PointInTime.min(minimum, p);
		}
		return minimum;
	}

}
