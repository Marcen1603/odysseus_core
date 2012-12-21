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
package de.uniol.inf.is.odysseus.persistentqueries;

import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * This area is for persistent data. It directly transfers all elements, since
 * in persistent data temporal order is not important.
 * 
 * @author Andre Bolles
 */
public class PersistentTransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		implements ITransferArea<R, W> {

	protected PointInTime[] minTs;
	protected AbstractSource<W> po;
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());

	boolean inOrder = true; 
	
	public PersistentTransferArea() {
	}

	public PersistentTransferArea(
			PersistentTransferArea<R, W> tiTransferFunction) {
		minTs = new PointInTime[tiTransferFunction.minTs.length];
		for (int i = 0; i < minTs.length; i++) {
			minTs[i] = tiTransferFunction.minTs[i] != null ? tiTransferFunction.minTs[i]
					.clone() : null;
		}
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	@Override
	public void addNewInput(PhysicalSubscription<ISource<? extends R>> sub) {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException(
				"Adding of inputs currently not implemented");
	}
	
	@Override
	public void removeInput(PhysicalSubscription<ISource<? extends R>> sub) {
		throw new IllegalArgumentException(
				"Removing of inputs currently not implemented");
	}

	@Override
	public void init(AbstractPipe<R, W> po) {
		this.po = po;
		minTs = new PointInTime[po.getSubscribedToSource().size()];
		for (int i = 0; i < minTs.length; i++) {
			this.minTs[i] = null;
		}
		this.outputQueue.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newElement(IStreamable object, int inPort) {
		if (object.isPunctuation()) {
			newHeartbeat(((IPunctuation) object).getTime(), inPort);
		} else {
			newHeartbeat(((R) object).getMetadata().getStart(), inPort);
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		po.sendPunctuation(punctuation);
	}

	@Override
	public void transfer(W object) {
		this.po.transfer(object);
		// synchronized (this.outputQueue) {
		// outputQueue.add(object);
		// }
	}

	@Override
	public void done() {
		while (!this.outputQueue.isEmpty()) {
			po.transfer(this.outputQueue.poll());
		}
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public PersistentTransferArea<R, W> clone() {
		return new PersistentTransferArea<R, W>(this);
	}

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
				if (wasElementSent && isInOrder()) {
					po.sendPunctuation(Heartbeat.createNewHeartbeat(minimum));
				}
			}
		}
	}

	public PointInTime getMinTs() {
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

	@Override
	public boolean isInOrder() {
		return inOrder;
	}
	
	@Override
	public void setInOrder(boolean isInOrder) {
		this.inOrder = isInOrder;
	}
	
	
}
