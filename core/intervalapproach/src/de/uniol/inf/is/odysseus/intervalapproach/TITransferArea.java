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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TITransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		implements ITransferArea<R, W> {

	static final Logger logger = LoggerFactory.getLogger(TITransferArea.class);

	// remember the last time stamp for each input port
	// can contain null, if no element is seen
	final protected Map<Integer, PointInTime> minTs;
	// states the time stamp of the last send object
	private PointInTime watermark = null;
	// the operator that uses this sink
	protected AbstractSource<W> po;
	// Store to reorder elements
	protected PriorityQueue<IStreamable> outputQueue = new PriorityQueue<IStreamable>(
			11, new Comparator<IStreamable>() {
				@SuppressWarnings("unchecked")
				@Override
				public int compare(IStreamable left, IStreamable right) {
					PointInTime l = left.isPunctuation() ? ((IPunctuation) left)
							.getTime() : ((W) left).getMetadata().getStart();
					PointInTime r = right.isPunctuation() ? ((IPunctuation) right)
							.getTime() : ((W) right).getMetadata().getStart();

					return l.compareTo(r);
				}
			});
	// to which port the data should be send
	private int outputPort = 0;
	
	private boolean inOrder = true; 

	public TITransferArea() {
		minTs = new HashMap<>();
	}

	private TITransferArea(TITransferArea<R, W> tiTransferFunction) {
		minTs = new HashMap<>(tiTransferFunction.minTs);
		outputQueue.addAll(tiTransferFunction.outputQueue);
	}

	public void setOutputPort(int outputPort) {
		this.outputPort = outputPort;
	}

	@Override
	public void init(AbstractPipe<R, W> po) {
		synchronized (outputQueue) {
			this.po = po;
			for (PhysicalSubscription<ISource<? extends R>>  sub : po.getSubscribedToSource()){
				this.minTs.put(sub.getSinkInPort(),null);
			}
			this.outputQueue.clear();
		}
	}

	@Override
	public void addNewInput(PhysicalSubscription<ISource<? extends R>> sub) {
		this.minTs.put(sub.getSinkInPort(),null);
	}
	
	@Override
	public void removeInput(PhysicalSubscription<ISource<? extends R>> sub) {
		this.minTs.remove(sub.getSinkInPort());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newElement(IStreamable object, int inPort) {
		PointInTime start;
		if (object.isPunctuation()) {
			start = ((IPunctuation) object).getTime();
		} else {
			start = ((R) object).getMetadata().getStart();
		}
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
	public void sendPunctuation(IPunctuation punctuation) {
		synchronized (this.outputQueue) {
			logger.debug("New Punctuation " + punctuation);
			// watermark is needed if new sources are connected at runtime
			// if watermark == null no object has ever been transferred --> init
			// phase
			// else treat only objects that are at least from time watermark
			if (watermark == null
					|| punctuation.getTime().afterOrEquals(watermark)) {
				outputQueue.add(punctuation);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void done() {
		while (!this.outputQueue.isEmpty()) {
			IStreamable elem = this.outputQueue.poll();
			if (elem.isPunctuation()) {
				po.sendPunctuation((IPunctuation) elem);
			} else {
				po.transfer((W) this.outputQueue.poll(), outputPort);
			}
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
			minTs.put(inPort, heartbeat);
			minimum = getMinTs();
		}
		sendData(minimum);
	}

	@SuppressWarnings("unchecked")
	protected void sendData(PointInTime minimum) {
		if (minimum != null) {
			synchronized (this.outputQueue) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				IStreamable elem = this.outputQueue.peek();
				boolean elementsSend = false;
				while (elem != null) {
					if (elem.isPunctuation()) {
						if (((IPunctuation) elem).getTime().beforeOrEquals(
								minimum)) {
							this.outputQueue.poll();
							// Avoid sending "outdated" heartbeats
							while (((IPunctuation) elem).isHeartbeat()) {
								IStreamable nextElem = outputQueue.peek();
								if (nextElem.isPunctuation()
										&& ((IPunctuation) elem).isHeartbeat()
										&& ((IPunctuation) elem).getTime()
												.afterOrEquals(minimum)) {
									elem = nextElem;
								} else {
									break;
								}
							}
							po.sendPunctuation((IPunctuation) elem);
							elem = this.outputQueue.peek();
						} else {
							elem = null;
						}
					} else {
						if (((W) elem).getMetadata() != null
								&& ((W) elem).getMetadata().getStart()
										.beforeOrEquals(minimum)) {
							this.outputQueue.poll();
							elementsSend = true;
							po.transfer((W) elem);
							elem = this.outputQueue.peek();
						} else {
							elem = null;
						}
					}
				}
				// Avoid unnecessary punctuations
				if (!elementsSend && isInOrder()) {
					po.sendPunctuation(Heartbeat.createNewHeartbeat(minimum), outputPort);
				}
				// Set marker to time stamp of the last send object
				watermark = minimum;
			}
		}
	}

	private PointInTime getMinTs() {
		synchronized (minTs) {
			PointInTime minimum = minTs.get(0);
			for (PointInTime p : minTs.values()) {
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

	@Override
	public boolean isInOrder() {
		return inOrder;
	}
	
	@Override
	public void setInOrder(boolean isInOrder) {
		this.inOrder = isInOrder;
	}
	
}
