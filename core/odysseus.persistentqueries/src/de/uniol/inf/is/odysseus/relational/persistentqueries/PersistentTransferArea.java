/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.relational.persistentqueries;

import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;

/**
 * This area is for persistent data. It directly transfers all elements, since in
 * persistent data temporal order is not important.
 * @author Andre Bolles
 */
public class PersistentTransferArea<R extends IMetaAttributeContainer<? extends ITimeInterval>, W extends IMetaAttributeContainer<? extends ITimeInterval>>
		implements ITransferArea<R,W> {

	final protected PointInTime[] minTs;
	protected AbstractSource<W> po;
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());;

	public PersistentTransferArea() {
		minTs = new PointInTime[2];
	}

	public PersistentTransferArea(int inputPortCount) {
		minTs = new PointInTime[inputPortCount];
	}

	public PersistentTransferArea(PersistentTransferArea<R,W> tiTransferFunction) {
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
		this.po.transfer(object);
//		synchronized (this.outputQueue) {
//			outputQueue.add(object);
//		}
	}

	@Override
	public void done() {
		while(!this.outputQueue.isEmpty()){
			po.transfer(this.outputQueue.poll());
		}
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public PersistentTransferArea<R,W> clone() {
		return new PersistentTransferArea<R,W>(this);
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
