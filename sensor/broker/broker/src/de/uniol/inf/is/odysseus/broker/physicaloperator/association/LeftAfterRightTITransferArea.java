/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;

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
	 * (de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer, int)
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
