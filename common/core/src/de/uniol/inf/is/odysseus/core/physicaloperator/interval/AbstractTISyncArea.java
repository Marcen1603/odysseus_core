package de.uniol.inf.is.odysseus.core.physicaloperator.interval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IHasName;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISyncArea;

abstract public class AbstractTISyncArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		implements ISyncArea<R, W> {

	private static final long serialVersionUID = 1314105668558226625L;

	private transient IHasName operator;

	protected long elementsRead;
	protected long puncRead;
	protected long elementsWritten;
	protected long puncWritten;
	protected long puncSuppressed;

	protected class OutputQueueComparator implements Comparator<SerializablePair<IStreamable, Integer>>, Serializable {

		private static final long serialVersionUID = 5321893137959979782L;

		@SuppressWarnings("unchecked")
		@Override
		public int compare(SerializablePair<IStreamable, Integer> left, SerializablePair<IStreamable, Integer> right) {
			PointInTime l = left.getE1().isPunctuation() ? ((IPunctuation) left.getE1()).getTime()
					: ((W) left.getE1()).getMetadata().getStart();
			PointInTime r = right.getE1().isPunctuation() ? ((IPunctuation) right.getE1()).getTime()
					: ((W) right.getE1()).getMetadata().getStart();

			int c = l.compareTo(r);
			if (c == 0) {
				return Long.compare(l.tiBreaker, r.tiBreaker);
			}
			return c;
		}

	}

	protected final OutputQueueComparator outputQueueComp = new OutputQueueComparator();

	static final transient Logger logger = LoggerFactory.getLogger(TITransferArea.class);

	// remember the last time stamp for each input port
	// can contain null, if no element is seen
	final protected Map<Integer, PointInTime> minTs;
	final protected Map<Integer, Boolean> isDone;
	// states the time stamp of the last send object
	protected PointInTime watermark = null;

	// Store to reorder elements
	final protected PriorityQueue<SerializablePair<IStreamable, Integer>> outputQueue;

	// to which port the data should be send
	private int outputPort = 0;

	protected boolean inOrder = true;

	protected AbstractTISyncArea() {
		this.minTs = new HashMap<>();
		this.isDone = new HashMap<>();
		this.outputQueue = new PriorityQueue<>(11, outputQueueComp);
	}

	protected AbstractTISyncArea(AbstractTISyncArea<R, W> other) {
		minTs = new HashMap<>(other.minTs);
		isDone = new HashMap<>(other.isDone);
		outputQueue = new PriorityQueue<>(11, outputQueueComp);
		outputQueue.addAll(other.outputQueue);

	}

	@Override
	public abstract AbstractTISyncArea<R, W> clone();

	protected void init(IHasName po, int inputPortCount) {
		this.minTs.clear();
		this.isDone.clear();
		elementsRead = 0;
		elementsWritten = 0;
		synchronized (outputQueue) {
			this.watermark = null;
			this.operator = po;
			for (int port = 0; port < inputPortCount; port++) {
				this.minTs.put(port, null);
				this.isDone.put(port, false);
			}
			this.outputQueue.clear();
		}
	}

	@Override
	public void addNewInput(int port) {
		this.minTs.put(port, null);
		this.isDone.put(port, false);
	}

	@Override
	public void removeInput(int port) {
		this.minTs.remove(port);
		this.isDone.remove(port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newElement(IStreamable object, int inPort) {
		synchronized (this.outputQueue) {

			PointInTime start;
			if (object.isPunctuation()) {
				start = ((IPunctuation) object).getTime();
			} else {
				start = ((R) object).getMetadata().getStart();
			}
			newHeartbeat(start, inPort);
		}
	}

	public void setOutputPort(int outputPort) {
		this.outputPort = outputPort;
	}

	@Override
	public void transfer(W object) {
		transfer(object, outputPort);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		sendPunctuation(punctuation, outputPort);
	}

	@Override
	public void transfer(W object, int toPort) {
		synchronized (this.outputQueue) {
			elementsRead++;
			// watermark is needed if new sources are connected at runtime
			// if watermark == null no object has ever been transferred --> init
			// phase
			// else treat only objects that are at least from time watermark
			if (watermark == null || object.getMetadata().getStart().afterOrEquals(watermark)) {
				if (watermark != null && object.isPunctuation() && object.getMetadata().getStart().equals(watermark)) {
					// ignore puncutations that do not change the watermark!
				} else {
					outputQueue.add(new SerializablePair<IStreamable, Integer>(object, toPort));
					sendData();
				}
			} else {
				if (!isAllDone()) {
					logger.warn("Out of order element read " + object + " (" + object.hashCode() + ")"
							+ " before last send element " + watermark + " ! Ignoring" + " - (" + this.operator + "("
							+ operator.getName() + ")) d=" + isDone);
				}
			}
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int toPort) {
		synchronized (this.outputQueue) {
			puncRead++;
			// watermark is needed if new sources are connected at runtime
			// if watermark == null no object has ever been transferred --> init
			// phase
			// else treat only objects that are at least from time watermark
			if (watermark == null || punctuation.getTime().afterOrEquals(watermark)) {
				outputQueue.add(new SerializablePair<IStreamable, Integer>(punctuation, toPort));
				sendData();
			} else {
				logger.warn("Out of order element read " + punctuation + " before last send element " + watermark
						+ " ! Ignoring" + " - (" + this.operator + "(" + operator.getName() + "))");
			}
		}
	}

	private void purge() {
		synchronized (outputQueue) {
			while (!this.outputQueue.isEmpty()) {
				Pair<IStreamable, Integer> elem = this.outputQueue.poll();
				if (elem.getE1().isPunctuation()) {
					transferPunctuation(elem);
				} else {
					elementsWritten++;
					transfer(elem);
				}
			}
		}
	}

	abstract protected void transfer(Pair<IStreamable, Integer> elem);

	abstract protected void transferPunctuation(Pair<IStreamable, Integer> elem);

	@Override
	public void done(int port) {
		isDone.put(port, true);
		if (isAllDone()) {
			purge();
		} else {
			sendData();
		}
	}

	private boolean isAllDone() {
		for (Boolean b : isDone.values()) {
			if (!b)
				return false;
		}
		return true;
	}

	@Override
	public int size() {
		return outputQueue.size();
	}

	@Override
	public PointInTime getWatermark() {
		return watermark;
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int inPort) {
		synchronized (this.outputQueue) {

			// watermark is needed if new sources are connected at runtime
			// if watermark == null no object has ever been transferred --> init
			// phase
			// else treat only objects that are at least from time watermark
			if (watermark == null || heartbeat.afterOrEquals(watermark)) {

				synchronized (minTs) {
					PointInTime curHB = minTs.get(inPort);
					if (curHB == null || heartbeat.after(curHB)) {
						minTs.put(inPort, heartbeat);
					} else {
						if (logger.isTraceEnabled()) {
							if (heartbeat.before(curHB)) {
								logger.trace(
										"Heart beat " + heartbeat + " ignored . Was older than last heartbeat " + curHB + " - (" + this.operator + "(" + operator.getName() + "))");
							}
						}
					}
				}
				sendData();
			}
			// else {
			// if (!isAllDone()) {
			// logger.warn("Out of order element read " + heartbeat + " from
			// port " + inPort
			// + " before last send element " + watermark + " ! Ignoring" + " -
			// (" + this.operator +" "+this.operator.getName() + ")");
			// }
			// }
		}
	}

	private void sendData() {
		PointInTime minimum = null;
		synchronized (minTs) {
			minimum = getMinTs();
		}
		sendData(minimum);
	}

	@SuppressWarnings("unchecked")
	protected void sendData(PointInTime minimum) {
		PointInTime lastSendObject = null;
		if (minimum != null && outputQueue.size() > 0) {
			synchronized (this.outputQueue) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				Pair<IStreamable, Integer> elem = this.outputQueue.peek();
				while (elem != null) {
					if (elem.getE1().isPunctuation()) {
						PointInTime ts = ((IPunctuation) elem.getE1()).getTime();

						if ((ts.beforeOrEquals(minimum))) {
							this.outputQueue.poll();
							puncWritten++;
							// suppress following heartbeats
							if (((IPunctuation) elem.getE1()).isHeartbeat()) {
								Pair<IStreamable, Integer> elem2;
								while (((elem2 = outputQueue.peek()) != null) && elem2.getE1().isPunctuation()
										&& ((IPunctuation) elem2.getE1()).isHeartbeat()
										&& ((IPunctuation) elem2.getE1()).getTime().beforeOrEquals(minimum)) {
									elem = this.outputQueue.poll();
									puncSuppressed++;
								}
							}
							transferPunctuation(elem);
							lastSendObject = ((IPunctuation) elem.getE1()).getTime();
							elem = this.outputQueue.peek();
						} else {
							elem = null;
						}
					} else {
						ITimeInterval metadata = ((W) elem.getE1()).getMetadata();
						if (metadata != null && (metadata.getStart().beforeOrEquals(minimum))) {
							this.outputQueue.poll();
							lastSendObject = ((W) elem.getE1()).getMetadata().getStart();
							elementsWritten++;
							((W) elem.getE1()).setTimeProgressMarker(true);
							transfer(elem);
							elem = this.outputQueue.peek();
						} else {
							elem = null;
						}
					}
				}
				if (lastSendObject != null) {
					// Set marker to time stamp of the last send object
					watermark = lastSendObject.clone();
					// if( po.getClass().getName().contains("JoinTIPO")) {
					// System.err.println("SET " + watermark);
					// }
				}
			}
		}
	}

	private PointInTime getMinTs() {
		synchronized (minTs) {
			PointInTime minimum = minTs.get(0);
			for (int i = 0; i < minTs.size(); i++) {
				PointInTime p = minTs.get(i);
				if (isDone.get(i)) {
					continue;
				}
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

	@SuppressWarnings("unchecked")
	public PointInTime calcMaxEndTs() {
		PointInTime maxTS = null;
		synchronized (this.outputQueue) {
			Iterator<SerializablePair<IStreamable, Integer>> iter = outputQueue.iterator();
			while (iter.hasNext()) {
				IStreamable e = iter.next().getE1();
				if (!e.isPunctuation()) {
					maxTS = PointInTime.max(maxTS, ((IStreamObject<ITimeInterval>) e).getMetadata().getEnd());
				}
			}
		}
		return maxTS;
	}

	@Override
	public PointInTime getMinTs(int inPort) {
		return minTs.get(inPort);
	}

	@Override
	public boolean isInOrder() {
		return inOrder;
	}

	@Override
	public void setInOrder(boolean isInOrder) {
		this.inOrder = isInOrder;
	}

	@Override
	public void dump() {
		System.out.println("Elements in Queue");
		List<SerializablePair<IStreamable, Integer>> out = new ArrayList<>(outputQueue);
		Collections.sort(out, outputQueueComp);
		Iterator<SerializablePair<IStreamable, Integer>> iter = out.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	@Override
	public long getElementsWritten() {
		return elementsWritten;
	}

	@Override
	public long getPunctuationsWritten() {
		return puncWritten;
	}

	@Override
	public long getPunctuationsSuppressed() {
		return puncSuppressed;
	}

	@Override
	public long getElementsRead() {
		return elementsRead;
	}

	@Override
	public long getPunctuationsRead() {
		return puncRead;
	}

	public void setOperator(IHasName po) {
		this.operator = po;
	}

}
