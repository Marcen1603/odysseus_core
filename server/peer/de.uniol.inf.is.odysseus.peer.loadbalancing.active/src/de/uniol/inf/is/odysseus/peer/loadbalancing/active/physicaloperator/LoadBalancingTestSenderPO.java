package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingTestSenderAO;

/**
 * A {@link LoadBalancingTestSenderPO} sends up all incoming tuples as they come
 * in and in addition sends {@link LoadBalancingPunctuation} in adjustable
 * intervals. This operator is for testing purpose to test the
 * load-balancing-synchronization.
 * 
 * When starting using this operator, it will wait {@value startSyncAfter}
 * tuples until a {@link LoadBalancingPunctuation} to start the synchronization
 * will be send. Then it will wait {@value stopSyncAfter} tuples until it will
 * send another {@link LoadBalancingPunctuation}. Then it will wait {@value
 * pauseBetween} tuples + {@value startSyncAfter} tuples until the next {@value
 * LoadBalancingPunctuation} will be send.
 * 
 * (startSyncAfter tuples waiting) -> (send {@link LoadBalancingPunctuation} to
 * start sync) -> (wait stopSyncAfter tuples) -> (send
 * {@link LoadBalancingPunctuation} to stop sync) -> (wait pauseBetween tuples)
 * -> (start from beginning)
 * 
 * @author Tobias Brandt
 * 
 * @param <T>
 * 
 */
public class LoadBalancingTestSenderPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {

	/**
	 * The version of this class for serialization.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5051804126354443735L;

	private static final Logger log = LoggerFactory
			.getLogger(LoadBalancingTestSenderPO.class);

	private static final String logMessageStart = "Sent LoadBalancingPunctuation to start synchronization after %d tuples.";
	private static final String logMessageStop = "Sent LoadBalancingPunctuation to stop synchronization after %d tuples.";

	private long counter;
	private long startSyncAfter;
	private long stopSyncAfter;
	private long pauseBetween;
	private boolean writeToLog;
	private boolean firstRound;

	/**
	 * Standard-constructor with standard configuration to start with and all
	 * adjustable values set to 10
	 */
	public LoadBalancingTestSenderPO() {
		super();
		counter = 0;
		firstRound = true;
	}

	/**
	 * Copy-constructor to copy an existing {@link LoadBalancingTestSenderPO}.
	 * Copies the internal state, too.
	 * 
	 * @param other
	 *            {@link LoadBalancingTestSenderPO} to be copied.
	 */
	public LoadBalancingTestSenderPO(LoadBalancingTestSenderPO<T> other) {
		super(other);
		this.counter = other.counter;
		this.startSyncAfter = other.startSyncAfter;
		this.stopSyncAfter = other.stopSyncAfter;
		this.pauseBetween = other.pauseBetween;
		this.writeToLog = other.writeToLog;
		this.firstRound = other.firstRound;
	}

	/**
	 * Constructor, to create a {@link LoadBalancingTestSenderPO} from a
	 * {@link LoadBalancingTestSenderAO}.
	 * 
	 * @param other
	 *            {@link LoadBalancingTestSenderAO} to create the
	 *            {@link LoadBalancingTestSenderPO} from.
	 */
	public LoadBalancingTestSenderPO(LoadBalancingTestSenderAO other) {
		super();
		this.counter = 0;
		this.startSyncAfter = other.getStartSyncAfter();
		this.stopSyncAfter = other.getStopAfter();
		this.pauseBetween = other.getPauseBetween();
		this.writeToLog = other.getWriteToLog();
		this.firstRound = true;
	}

	/**
	 * Clones this {@link LoadBalancingTestSenderPO} (with internal state and
	 * returns the clone.
	 */
	@Override
	public LoadBalancingTestSenderPO<T> clone() {
		return new LoadBalancingTestSenderPO<T>(this);
	}

	/**
	 * Counts the incoming tuples and sends them up. Sends
	 * {@link LoadBalancingPunctuation} in the adjusted rates to start or stop
	 * the synchronization.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(T object, int port) {
		this.transfer(object, port);

		counter++;

		long neededTuplesStart = 0;
		long neededTuplesStop = 0;
		if (firstRound) {
			neededTuplesStart = startSyncAfter;
			neededTuplesStop = neededTuplesStart + stopSyncAfter;
		} else {
			neededTuplesStart = pauseBetween + startSyncAfter;
			neededTuplesStop = neededTuplesStart + stopSyncAfter;
		}

		if (counter == neededTuplesStart) {
			// Send start sync punctuation
			sendSyncPunctuation(
					((IStreamObject<? extends ITimeInterval>) object)
							.getMetadata().getStart(), port, true, neededTuplesStart);

		} else if (counter == neededTuplesStop) {
			// Send stop sync punctuation
			sendSyncPunctuation(
					((IStreamObject<? extends ITimeInterval>) object)
							.getMetadata().getStart(), port, false, neededTuplesStop - neededTuplesStart);
			counter = 0;
			firstRound = false;
		}

	}

	/**
	 * Sends a {@link LoadBalancingPunctuation} to start or stop the sync.
	 * 
	 * @param time
	 *            Time of the punctuation
	 * @param port
	 *            Port to send the punctuation
	 * @param startSync
	 *            true, if the {@link LoadBalancingPunctuation} should start the
	 *            synchronization, false if the synchronization should be
	 *            stopped.
	 */
	private void sendSyncPunctuation(PointInTime time, int port,
			boolean startSync, long tuples) {
		LoadBalancingPunctuation startSyncPunctuation = new LoadBalancingPunctuation(
				time, startSync, !startSync);
		sendPunctuation(startSyncPunctuation, port);

		// Log it if wanted
		if (writeToLog) {
			String message = "";
			if (startSync)
				message = String.format(logMessageStart, tuples);
			else
				message = String.format(logMessageStop, tuples);
			LoadBalancingTestSenderPO.log.debug(message);
		}

	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		this.sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
}
