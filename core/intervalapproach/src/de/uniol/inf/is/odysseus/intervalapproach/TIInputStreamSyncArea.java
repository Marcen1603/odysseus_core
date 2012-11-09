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
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TIInputStreamSyncArea<T extends IStreamObject<? extends ITimeInterval>>
		implements IInputStreamSyncArea<T> {

	static Logger _logger;

	static public Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(TIInputStreamSyncArea.class);
		}
		return _logger;
	}

	final protected Map<Integer, PointInTime> minTsForPort = new HashMap<Integer, PointInTime>();
	private PointInTime minTs = null;
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
		this(2);
	}

	public TIInputStreamSyncArea(int inputPortCount) {
		for (int i = 0; i < inputPortCount; i++) {
			minTsForPort.put(i, null);
		}
	}

	public TIInputStreamSyncArea(TIInputStreamSyncArea<T> tiInputStreamSyncArea) {
		for (int i = 0; i < minTsForPort.size(); i++) {
			PointInTime fktn = tiInputStreamSyncArea.minTsForPort.get(i);
			minTsForPort.put(i, fktn != null ? fktn.clone() : null);
		}
		inputQueue.addAll(tiInputStreamSyncArea.inputQueue);
	}

	@Override
	public void init(IProcessInternal<T> po) {
		this.po = po;
		this.inputQueue.clear();
	}

	@Override
	public synchronized void addInputPort(int port) {
		if (minTsForPort.get(port) == null) {
			getLogger().debug(
					"Added new input port " + port + " current min " + minTs);
			this.minTsForPort.put(port, null);
		} else {
			throw new IllegalArgumentException("Port " + port
					+ " is already in use!!");
		}
	}

	@Override
	public synchronized void removeInputPort(int port) {
		getLogger().debug("Removed input port " + port);
		this.minTsForPort.remove(port);
	}

	@Override
	public synchronized void newElement(T object, int inPort) {
		// getLogger().debug("New Element "+object+" Port="+inPort+" current min "+minTs);
		// Remove all elements that are before minTS
		// can happen if a new source is appended at runtime
		PointInTime ts = object.getMetadata().getStart();
		if (minTsForPort.containsKey(inPort)) {
			if (minTs == null || !ts.before(minTs)) {
				inputQueue.add(new Pair<T, Integer>(object, inPort));
				newHeartbeat(ts, inPort);
			} else {
				getLogger().warn(
						"Removed out of time element " + object + " from port "
								+ inPort);
			}
		} else {
			getLogger().warn(
					"Removed element " + object + " for not registered port "
							+ inPort);
		}
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
	public synchronized void newHeartbeat(PointInTime heartbeat, int inPort) {
		if (minTs == null || !heartbeat.before(minTs)) {

			synchronized (minTsForPort) {
				minTsForPort.put(inPort, heartbeat);
				calcMinTs();
				// getLogger().debug("Current minimum "+minimum);
			}
			if (minTs != null) {
				synchronized (this.inputQueue) {
					// don't use an iterator, it does NOT guarantee ordered
					// traversal!
					IPair<T, Integer> elem = this.inputQueue.peek();
					boolean elementsSend = false;
					while (elem != null
							&& elem.getE1().getMetadata().getStart()
									.beforeOrEquals(minTs)) {
						this.inputQueue.poll();

						po.process_internal(elem.getE1(), elem.getE2());
						// getLogger().debug("Process "+elem.getE1()+" on Port "+elem.getE2());
						elem = this.inputQueue.peek();
						elementsSend = true;
					}
//					// Avoid unnecessary punctuations!
					if (!elementsSend) {
						po.process_newHeartbeat(minTs);
					}
				}
			}
		}
	}

	private void calcMinTs() {
		PointInTime minimum = minTsForPort.get(0);
		for (Entry<Integer, PointInTime> p : minTsForPort.entrySet()) {
			// if one element has no value, no element
			// has been read from this input port
			// --> no data can be send
			if (p.getValue() == null) {
				return;
			}
			minimum = PointInTime.min(minimum, p.getValue());
		}
		// If for all initial sources there has ever been calculates
		// a minimal time stamp, the next time stamp is not allowed
		// to be lower!
		if (minTs == null || minTs.beforeOrEquals(minimum)) {
			minTs = minimum;
		} else {
			throw new RuntimeException("Input streams are out of order! "
					+ minTs + " " + minimum);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final TIInputStreamSyncArea<IStreamObject<? extends ITimeInterval>> area = new TIInputStreamSyncArea<IStreamObject<? extends ITimeInterval>>();
		TestClass<IStreamObject<? extends ITimeInterval>> sink = new TestClass<IStreamObject<? extends ITimeInterval>>();
		area.init(sink);

		int maxSleep = 10;

		startSource(area, 0, 0, 1000, maxSleep);
		startSource(area, 10, 1, 1000, maxSleep);

		Thread.sleep(100);
		area.removeInputPort(1);
		Thread.sleep(10);
		area.addInputPort(2);
		startSource(area, 5, 2, 1000, maxSleep);

		Thread.sleep(100);
		area.addInputPort(3);
		area.addInputPort(4);
		startSource(area, 5, 3, 1000, maxSleep);
		startSource(area, 0, 4, 1000, maxSleep);

		Thread.sleep(10);

		Thread.sleep(10);
		area.addInputPort(5);
		area.addInputPort(6);
		startSource(area, 5, 5, 1000, maxSleep);
		startSource(area, 0, 6, 1000, maxSleep);

		area.removeInputPort(3);
	}

	private static void startSource(
			final TIInputStreamSyncArea<IStreamObject<? extends ITimeInterval>> area,
			final long start, final int port, final int elemCount,
			final int maxSleep) {
		new Thread() {
			public void run() {
				Random rnd = new Random();
				for (int i = 0; i < elemCount; i++) {
					Tuple<ITimeInterval> test = new Tuple<ITimeInterval>(1,
							false);
					test.setMetadata(new TimeInterval());
					test.getMetadata().setStart(new PointInTime(start + i));
					area.newElement(test, port);
					try {
						Thread.sleep(rnd.nextInt(maxSleep));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			};
		}.start();
	}

}

class TestClass<R extends IStreamObject<? extends ITimeInterval>> implements
		IProcessInternal<R> {

	PointInTime lastElement = null;

	@Override
	public void process_internal(R event, int port) {
		validate(event.getMetadata().getStart(), event);
		System.out.println("Element processed " + event + " from port " + port);
	}

	@Override
	public void process_newHeartbeat(PointInTime pointInTime) {
		validate(pointInTime, null);
	}

	private void validate(PointInTime start, R event) {
		if (lastElement == null) {
			lastElement = start;
		} else {
			if (lastElement.beforeOrEquals(start)) {
				lastElement = start;
			} else {
				throw new RuntimeException("Elements are out of order!! "
						+ event);
			}
		}
	}

}
