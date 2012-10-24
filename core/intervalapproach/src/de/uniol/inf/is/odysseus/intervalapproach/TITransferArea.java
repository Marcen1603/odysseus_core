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

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TITransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		implements ITransferArea<R, W> {

	// remember the last time stamp for each input port
	// can contain null, if no element is seen
	final protected List<PointInTime> minTs;
	// states the time stamp of the last send object
	private PointInTime watermark = null;
	// the operator that uses this sink
	protected AbstractSource<W> po;
	// Store to reorder elements
	protected PriorityQueue<W> outputQueue = new PriorityQueue<W>(11,
			new MetadataComparator<ITimeInterval>());
	// to which port the data should be send
	private int outputPort = 0;

	public TITransferArea() {
		minTs = new LinkedList<>();
	}

	private TITransferArea(TITransferArea<R, W> tiTransferFunction) {
		minTs = new LinkedList<>(tiTransferFunction.minTs);
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	public void setOutputPort(int outputPort) {
		this.outputPort = outputPort;
	}

	@Override
	public void init(AbstractPipe<R, W> po) {
		synchronized (outputQueue) {
			this.po = po;
			int size = po.getSubscribedToSource().size();
			for (int i = 0; i < size; i++) {
				this.minTs.add(null);
			}
			this.outputQueue.clear();
		}
	}

	@Override
	public void addNewInput(PhysicalSubscription<ISource<? extends R>> sub) {
		minTs.add(null);
	}

	@Override
	public void newElement(R object, int inPort) {
		PointInTime start = object.getMetadata().getStart();
		// watermark is needed if new sources are connected at runtime
		// if watermark == null no object has ever been transferred --> init
		// phase
		// else treat only objects that are at least from time watermark
		if (watermark == null || start.afterOrEquals(watermark)) {
			newHeartbeat(start, inPort);
		}
	}

	@Override
	public void transfer(W object) {
		synchronized (this.outputQueue) {
			// watermark is needed if new sources are connected at runtime
			// if watermark == null no object has ever been transferred --> init
			// phase
			// else treat only objects that are at least from time watermark
			if (watermark == null
					|| object.getMetadata().getStart().afterOrEquals(watermark)) {
				outputQueue.add(object);
			}
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
			minTs.set(inPort,heartbeat);
			minimum = getMinTs();			
		}
		sendData(minimum);
	}

	protected void sendData(PointInTime minimum) {
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
				// Set marker to time stamp of the last send object
				watermark = minimum;
			}
		}
	}

	private PointInTime getMinTs() {
		synchronized (minTs) {
			PointInTime minimum = minTs.get(0);
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

}
