package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class LeftJoinTITransferArea <R extends IMetaAttributeContainer<? extends ITimeInterval>, W extends IMetaAttributeContainer<? extends ITimeInterval>>
	extends TITransferArea<R, W>{

	
	/**
	 * removes all elements whose start timestamp is before heartbeat
	 */
	public void newHeartbeat(PointInTime heartbeat, int inPort) {
		PointInTime minimum = heartbeat;
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
}
