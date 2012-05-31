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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TIInputStreamSyncArea<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		implements IInputStreamSyncArea<T> {

	static Logger _logger;

	static public Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(TIInputStreamSyncArea.class);
		}
		return _logger;
	}

	final protected PointInTime[] minTs;
	protected IProcessInternal<T> po;
	private PriorityQueue<IPair<T, Integer>> inputQueue = new PriorityQueue<IPair<T, Integer>>(
			10, new Comparator<IPair<T, Integer>>() {
				@Override
				public int compare(IPair<T, Integer> left,
						IPair<T, Integer> right) {
					return left.getE1().getMetadata()
							.compareTo(right.getE1().getMetadata());
				}
			});

	public TIInputStreamSyncArea() {
		minTs = new PointInTime[2];
	}

	public TIInputStreamSyncArea(int inputPortCount) {
		minTs = new PointInTime[inputPortCount];
	}

	public TIInputStreamSyncArea(TIInputStreamSyncArea<T> tiTransferFunction) {
		minTs = new PointInTime[tiTransferFunction.minTs.length];
		for (int i = 0; i < minTs.length; i++) {
			minTs[i] = tiTransferFunction.minTs[i] != null ? tiTransferFunction.minTs[i]
					.clone() : null;
		}
		inputQueue.addAll(tiTransferFunction.inputQueue);
	}

	@Override
	public void setSink(IProcessInternal<T> po) {
		this.po = po;
	}

	@Override
	public void init(IProcessInternal<T> po) {
		this.po = po;
		for (int i = 0; i < minTs.length; i++) {
			this.minTs[i] = null;
		}
		this.inputQueue.clear();
	}

	@Override
	public void newElement(T object, int inPort) {
		// getLogger().debug("New Element "+object+" "+inPort);
		inputQueue.add(new Pair<T, Integer>(object, inPort));
		newHeartbeat(object.getMetadata().getStart(), inPort);
	}

	@Override
	public void done() {
		for (IPair<T, Integer> element : inputQueue) {
			po.process_internal(element.getE1(), element.getE2());
		}
		inputQueue.clear();
	}

	@Override
	public int size() {
		return inputQueue.size();
	}

	@Override
	public TIInputStreamSyncArea<T> clone() {
		return new TIInputStreamSyncArea<T>(this);
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int inPort) {
		if (po == null)
			return;
		PointInTime minimum = null;
		synchronized (minTs) {
			minTs[inPort] = heartbeat;
			minimum = getMinTs();
			// getLogger().debug("Current minimum "+minimum);
		}
		if (minimum != null) {
			synchronized (this.inputQueue) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				IPair<T, Integer> elem = this.inputQueue.peek();
				while (elem != null
						&& elem.getE1().getMetadata().getStart()
								.beforeOrEquals(minimum)) {
					this.inputQueue.poll();

					po.process_internal(elem.getE1(), elem.getE2());
					// getLogger().debug("Process "+elem.getE1()+" on Port "+elem.getE2());
					elem = this.inputQueue.peek();
				}
				po.process_newHeartbeat(minimum);
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
