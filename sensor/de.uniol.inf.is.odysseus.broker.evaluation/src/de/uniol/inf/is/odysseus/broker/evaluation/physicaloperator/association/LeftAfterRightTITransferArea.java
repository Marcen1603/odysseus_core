package de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.association;

import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/**
 * The LeftAfterRightTITransferFunction is a special transfer function for the
 * {@link BrokerJoinTIPO}. This transfer functions acts to the assumption that
 * the left input will ever be chronological after the right input. Therefore an
 * element will be transfered if only the right input (and not the left input as
 * well) guarantees that there are no elements before the transfered element.
 * 
 * @author Dennis Geesen
 * 
 * @param <W>
 *            the type of the tuple
 */
public class LeftAfterRightTITransferArea<R extends IMetaAttributeContainer<? extends ITimeInterval>,W extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends TITransferArea<R,W> {

	/**
	 * Instantiates a new transfer function.
	 */
	public LeftAfterRightTITransferArea() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction#newElement
	 * (de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer, int)
	 */
	@Override
	public void newElement(R object, int port) {
		if (port == 0) {
			PointInTime minimum = null;
			synchronized (minTs) {
				minTs[port] = object.getMetadata().getStart();
				if (minTs[0] != null) {
					// if (minTs[0]!=null && minTs[1]!=null){
					// minimum = TimeInterval.startsBefore(minTs[0], minTs[1]) ?
					// minTs[0] : minTs[1];
					minimum = minTs[0];
				}
			}
			if (minimum != null) {
				synchronized (super.outputQueue) {
					// don't use an iterator, it does NOT guarantee ordered
					// traversal!
					W elem = this.outputQueue.peek();
					while (elem != null
							&& elem.getMetadata().getStart().beforeOrEquals(
									minimum)) {
						this.outputQueue.poll();
						po.transfer(elem);
						elem = this.outputQueue.peek();
					}
				}
			}
		} else {
			synchronized (super.outputQueue) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				W elem = this.outputQueue.peek();
				while (elem != null) {
					this.outputQueue.poll();
					po.transfer(elem);
					elem = this.outputQueue.peek();
				}
			}
		}

		// T elem = this.out.peek();
		// while(elem != null){
		// this.out.poll();
		// po.transfer(elem);
		// elem = this.out.peek();
		// }
		//		
	}

}
