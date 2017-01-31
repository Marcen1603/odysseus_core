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
import de.uniol.inf.is.odysseus.core.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractTransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransfer;

/**
 * This area does not tread order. It directly transfers all elements
 *
 * @author Andre Bolles
 */
public class DirectTransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		extends AbstractTransferArea<R, W> {

	private static final long serialVersionUID = 2207860250465543159L;

	protected PointInTime[] minTs;
	protected transient ITransfer<W> po;
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());

	boolean inOrder = true;

	public DirectTransferArea() {
	}

	public DirectTransferArea(DirectTransferArea<R, W> tiTransferFunction) {
		minTs = new PointInTime[tiTransferFunction.minTs.length];
		for (int i = 0; i < minTs.length; i++) {
			minTs[i] = tiTransferFunction.minTs[i] != null ? tiTransferFunction.minTs[i]
					.clone() : null;
		}
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	@Override
	public PointInTime getMinTs(int inPort) {
		return minTs[inPort];
	}

	@Override
	public PointInTime calcMaxEndTs() {
		return null;
	}

	@Override
	public void addNewInput(int port) {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException(
				"Adding of inputs currently not implemented");
	}

	@Override
	public void removeInput(int port) {
		throw new IllegalArgumentException(
				"Removing of inputs currently not implemented");
	}

	@Override
	public void init(ITransfer<W> po, int length) {
		this.po = po;
		minTs = new PointInTime[length];
		for (int i = 0; i < minTs.length; i++) {
			this.minTs[i] = null;
		}
		this.outputQueue.clear();
	}

	@Override
	public void setTransfer(ITransfer<W> po) {
		this.po = po;
	}

	@Override
	public void newElement(IStreamable object, int inPort) {
		// No need here
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		po.sendPunctuation(punctuation);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int toPort) {
		po.sendPunctuation(punctuation, toPort);
	}

	@Override
	public void transfer(W object) {
		this.po.transfer(object);
		// synchronized (this.outputQueue) {
		// outputQueue.add(object);
		// }
	}

	@Override
	public void transfer(W object, int toPort) {
		this.po.transfer(object, toPort);
	}

	@Override
	public void done(int port) {
		// Nothing to do here
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public PointInTime getWatermark() {
		return null;
	}

	@Override
	public DirectTransferArea<R, W> clone() {
		return new DirectTransferArea<R, W>(this);
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
