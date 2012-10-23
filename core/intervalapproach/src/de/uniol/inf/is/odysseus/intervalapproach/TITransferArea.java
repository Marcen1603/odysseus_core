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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TITransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		implements ITransferArea<R, W> {

	private ReentrantLock lock = new ReentrantLock();
	protected PointInTime[] minTs;
	protected AbstractSource<W> po;
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());

	private int outputPort = 0;

	public TITransferArea() {
	}

	private TITransferArea(TITransferArea<R, W> tiTransferFunction) {
		minTs = new PointInTime[tiTransferFunction.minTs.length];
		for (int i = 0; i < minTs.length; i++) {
			minTs[i] = tiTransferFunction.minTs[i] != null ? tiTransferFunction.minTs[i]
					.clone() : null;
		}
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	@Override
	public void init(AbstractSource<W> po) {
		lock.lock();
		this.po = po;
		this.minTs = new PointInTime[po.getSubscriptions().size()];
		for (int i = 0; i < minTs.length; i++) {
			this.minTs[i] = null;
		}
		this.outputQueue.clear();
		lock.unlock();
	}

	@Override
	public void addNewInput(PhysicalSubscription<ISource<? extends R>> sub) {
		lock.lock();

		lock.unlock();
		throw new IllegalArgumentException(
				"Adding of inputs currently not implemented");
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
		while (!this.outputQueue.isEmpty()) {
			po.transfer(this.outputQueue.poll(), outputPort);
		}
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public TITransferArea<R, W> clone() {
		return new TITransferArea<R, W>(this);
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
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				W elem = this.outputQueue.peek();
				while (elem != null
						&& elem.getMetadata().getStart()
								.beforeOrEquals(minimum)) {
					this.outputQueue.poll();
					po.transfer(elem, outputPort);
					elem = this.outputQueue.peek();
				}
				po.sendPunctuation(minimum, outputPort);
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

	public int getOutputPort() {
		return outputPort;
	}

	public void setOutputPort(int outputPort) {
		this.outputPort = outputPort;
	}

}
