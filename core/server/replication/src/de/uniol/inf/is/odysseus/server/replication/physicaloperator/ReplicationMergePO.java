package de.uniol.inf.is.odysseus.server.replication.physicaloperator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;

/**
 * A {@link ReplicationMergePO} can be used to realize a
 * {@link ReplicationMergeAO}. <br />
 * The {@link ReplicationMergePO} uses a {@link PriorityQueue} and can handle
 * {@link IPunctuations}. <br />
 * It does only work for real replicates. That means this operator can not
 * handle scenarios in which an input stream delivers unequal elements compared
 * to the other input stream. Such a scenario can occur if an input stream is
 * the result of query part that has been recovered after a failure.
 *
 * @author Michael Brand
 */
public class ReplicationMergePO<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<T, T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReplicationMergePO.class);

	/**
	 * Delivers a timestamp of an {@link IStreamable} object.
	 *
	 * @param object
	 *            The object.
	 * @param searchForStartTS
	 *            true, if it is the start timestamp, which should be delivered;
	 *            false, if it is the end timestamp.
	 * @return {@link IPunctuation#getTime()} - if <code>object</code> is a
	 *         punctuation <br />
	 *         {@link ITimeInterval#getStart()} - if <code>object</code> is no
	 *         punctuation and <code>searchForStartTS == true</code> <br />
	 *         {@link ITimeInterval#getEnd()} - if <code>object</code> is no
	 *         punctuation and <code>searchForStartTS == false</code> <br />
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends IStreamObject<? extends ITimeInterval>> PointInTime getTS(IStreamable object,
			boolean searchForStartTS) {

		if (object.isPunctuation())
			return ((IPunctuation) object).getTime();
		else if (searchForStartTS)
			return ((T) object).getMetadata().getStart();
		else
			return ((T) object).getMetadata().getEnd();

	}

	/**
	 * The comparator for pairs of {@link IStreamable} objects and ports. <br />
	 * The start timestamps of the {@link IStreamable} objects will be compared.
	 */
	static transient Comparator<IPair<IStreamable, Integer>> comp = new Comparator<IPair<IStreamable, Integer>>() {

		@Override
		public int compare(IPair<IStreamable, Integer> left, IPair<IStreamable, Integer> right) {

			PointInTime ts_l = getTS(left.getE1(), true);
			PointInTime ts_r = getTS(right.getE1(), true);

			return ts_l.compareTo(ts_r);

		}

	};

	/**
	 * The {@link PriorityQueue} to keep in mind which elements have been seen
	 * at which port.
	 */
	protected PriorityQueue<IPair<IStreamable, Integer>> inputQueue;

	/**
	 * The youngest start timestamp of all seen objects.
	 */
	protected PointInTime youngestTS;

	// Is needed to sync youngestTS, since youngest TS may be null
	protected Object dummyForYTSSync = new Object();

	// Is needed to sync transfer
	protected Object dummyForTransferSync = new Object();

	/**
	 * Creates a new {@link ReplicationMergePO}.
	 */
	public ReplicationMergePO() {
		super();
		this.inputQueue = new PriorityQueue<IPair<IStreamable, Integer>>(10, comp);
		this.youngestTS = null;
	}

	/**
	 * Creates a new {@link ReplicationMergePO} as a copy of an existing one.
	 *
	 * @param mergePO
	 *            The {@link ReplicationMergePO} to be copied.
	 */
	public ReplicationMergePO(ReplicationMergePO<T> mergePO) {
		super(mergePO);
		this.inputQueue = new PriorityQueue<IPair<IStreamable, Integer>>(mergePO.inputQueue.size(), comp);
		this.inputQueue.addAll(mergePO.inputQueue);
		this.youngestTS = mergePO.youngestTS;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		synchronized (this.inputQueue) {
			this.inputQueue.clear();
		}
		synchronized (this.dummyForYTSSync) {
			this.youngestTS = null;
		}
	}

	@Override
	protected void process_close() {
		synchronized (this.inputQueue) {
			this.inputQueue.clear();
		}
		synchronized (this.dummyForYTSSync) {
			this.youngestTS = null;
		}
	}

	@Override
	protected void process_done() {
		if (this.isOpen()) {
			synchronized (this.inputQueue) {
				this.inputQueue.clear();
			}
			synchronized (this.dummyForYTSSync) {
				this.youngestTS = null;
			}
		}
	}

	@Override
	public boolean isDone() {
		synchronized (this.inputQueue) {
			if (!this.inputQueue.isEmpty())
				return false;
		}
		for (int port = 0; port < this.getInputPortCount(); port++) {
			if (!this.getSubscribedToSource(port).isDone())
				return false;
		}
		return true;
	}

	/**
	 * Checks, if the start timestamp of the object is younger or equal to the
	 * youngest seen and updates the youngest seen if necessary.
	 *
	 * @return True if the object is is younger or equal. False means that the
	 *         object can be rejected.
	 */
	protected boolean precheck(IStreamable object, int port) {
		final PointInTime ts = getTS(object, true);
		synchronized (this.dummyForYTSSync) {
			return youngestTS == null || ts.afterOrEquals(youngestTS);
		}
	}

	private void updateYoungestTS(IStreamable object) {
		final PointInTime ts = getTS(object, true);
		synchronized (this.dummyForYTSSync) {
			if (this.youngestTS == null || ts.after(this.youngestTS)) {
				this.youngestTS = ts;
				logger.debug("Set youngest timestamp to {}.", this.youngestTS);
			}
		}
	}

	/**
	 * Removes all elements from the {@link inputQueue} that have a start
	 * timestamp before <code>deadline</code>.
	 */
	protected void purgeElements(PointInTime deadline) {

		boolean continuePeeking = true;

		while (!this.inputQueue.isEmpty() && continuePeeking) {

			IPair<IStreamable, Integer> pair = this.inputQueue.peek();
			IStreamable elem = pair.getE1();
			PointInTime ts = getTS(elem, true);
			if (ts.before(deadline))
				this.inputQueue.poll();
			else
				continuePeeking = false;

		}

	}

	/**
	 * Transfers an object if it was not transfered before (which input port
	 * doesn't matter) or if <code>port</code> is the port with the most
	 * occurrences of <code>object</code>. <br />
	 * As objects can be equal with equal meta data this method determines, if
	 * the number of objects equal to <code>object</code> transfered from port
	 * <code>port</code> is the maximum count over all ports. Otherwise the
	 * object will not transfered. <br />
	 * This method also adds <code>object</code> to the input queue.
	 *
	 * @param object
	 *            The object to be merged
	 * @param port
	 *            The port on which the object was arriving.
	 */
	@SuppressWarnings("unchecked")
	protected void mergeElement(IStreamable object, int port) {
		// The maximal count of that object over all ports and on the arriving
		// port
		int foundMax = 0;
		int foundOwnPort = 0;
		Map<Integer, Integer> countToPortMap = Maps.newHashMap(); // key = port,
																	// value =
																	// count
		Iterator<IPair<IStreamable, Integer>> queueIter = this.inputQueue.iterator();
		while (queueIter.hasNext()) {
			IPair<IStreamable, Integer> pair = queueIter.next();
			IStreamable elem = pair.getE1();
			Integer elemPort = pair.getE2();

			if ((elem.isPunctuation() && !object.isPunctuation())
					|| (!elem.isPunctuation() && object.isPunctuation())) {
				// only one element is a punctuation
				continue;
			} else if (elem.isPunctuation() && object.isPunctuation()
					&& !((IPunctuation) elem).getTime().equals(((IPunctuation) object).getTime())) {
				// not the same timestamps of the punctuations
				continue;
			} else if (!elem.isPunctuation() && !object.isPunctuation()) {
				if(!((T) elem).equals(object)) {
					// not the same elements
					continue;
				} else {
					// same elements, what about time stamps?
					ITimeInterval ti1 = ((ITimeInterval) ((T) elem).getMetadata());
					ITimeInterval ti2 = ((ITimeInterval) ((T) object).getMetadata());
					if(!ti1.getStart().equals(ti2.getStart()) || !ti1.getEnd().equals(ti2.getEnd())) {
						// not the same time stamps
						continue;
					}
				}
			}
			if (countToPortMap.containsKey(elemPort)) {
				// port is already in the map
				countToPortMap.put(elemPort, countToPortMap.get(elemPort) + 1);
			} else {
				// port isn't in the map yet
				countToPortMap.put(elemPort, 1);
			}

			// Check for maximum
			if (countToPortMap.get(elemPort) > foundMax) {
				foundMax = countToPortMap.get(elemPort);
			}
			if (port == elemPort) {
				foundOwnPort = countToPortMap.get(elemPort);
			}
		}

		if (foundMax == 0 || foundMax == foundOwnPort) {
			/*
			 * First appearance of that object -> transfer Else would mean, that
			 * the object appeared on a different port earlier -> drop
			 */
			logger.debug("Transfering object {} from port {}.", object, port);
			transferOrSendPunctuation(object, 0);
		} else {
			// Transfer duplicates to other output port
			transferOrSendPunctuation(object, 1);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if (!this.precheck(object, port)) {
			// Transfer duplicates to other output port
			transfer(object, 1);
			return;
		}

		synchronized (this.inputQueue) {
			this.purgeElements(getTS(object, true));
			this.mergeElement(object, port);
			this.inputQueue.add(new Pair<IStreamable, Integer>((IStreamable) object.clone(), port));
		}

		updateYoungestTS(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (!this.precheck(punctuation, port)) {
			// Transfer duplicates to other output port
			sendPunctuation(punctuation, 1);
			return;
		}

		synchronized (this.inputQueue) {
			this.purgeElements(getTS(punctuation, true));
			this.mergeElement(punctuation, port);
			this.inputQueue.add(new Pair<IStreamable, Integer>(punctuation, port));
		}
	}

	@SuppressWarnings("unchecked")
	protected void transferOrSendPunctuation(IStreamable object, int port) {
		synchronized (this.dummyForTransferSync) {
			if (object.isPunctuation()) {
				sendPunctuation((IPunctuation) object, port);
			} else {
				transfer((T) object, port);
			}
		}
	}

}