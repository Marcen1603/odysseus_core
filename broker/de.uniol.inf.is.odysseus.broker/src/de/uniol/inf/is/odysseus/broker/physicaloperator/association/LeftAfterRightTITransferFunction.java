package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * The LeftAfterRightTITransferFunction is a special transfer function for the
 * {@link BrokerJoinTIPO}. This transfer functions acts to the assumption that
 * the left input will ever be chronological after the right input. Therefore an
 * element will be transfered if only the right input (and not the left input as
 * well) guarantees that there are no elements before the transfered element.
 * 
 * @author Dennis Geesen
 * 
 * @param <T>
 *            the type of the tuple
 */
public class LeftAfterRightTITransferFunction<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends TITransferFunction<T> {

	/**
	 * Instantiates a new transfer function.
	 */
	public LeftAfterRightTITransferFunction() {
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
	public void newElement(T object, int port) {
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
				synchronized (super.out) {
					// don't use an iterator, it does NOT guarantee ordered
					// traversal!
					T elem = this.out.peek();
					while (elem != null
							&& elem.getMetadata().getStart().beforeOrEquals(
									minimum)) {
						this.out.poll();
						po.transfer(elem);
						elem = this.out.peek();
					}
				}
			}
		} else {
			synchronized (super.out) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				T elem = this.out.peek();
				while (elem != null) {
					this.out.poll();
					po.transfer(elem);
					elem = this.out.peek();
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
