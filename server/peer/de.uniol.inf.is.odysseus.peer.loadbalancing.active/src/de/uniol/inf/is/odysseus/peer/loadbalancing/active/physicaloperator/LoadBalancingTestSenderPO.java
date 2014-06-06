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
 * 
 * @author Tobias Brandt
 * 
 * @param <T>
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

	private String logMessage = "Sent LoadBalancingPunctuation to start synchronization after %d tuples.";

	private long counter;
	private long startSyncAfter;
	private long stopSyncAfter;
	private long pauseBetween;
	private boolean firstRound;

	public LoadBalancingTestSenderPO() {
		counter = 0;
		startSyncAfter = 42;
		stopSyncAfter = 42;
		pauseBetween = 42;
		firstRound = true;
	}

	public LoadBalancingTestSenderPO(LoadBalancingTestSenderPO<T> other) {
		this.counter = other.counter;
		this.startSyncAfter = other.startSyncAfter;
		this.stopSyncAfter = other.stopSyncAfter;
		this.pauseBetween = other.pauseBetween;
		this.firstRound = other.firstRound;
	}

	public LoadBalancingTestSenderPO(LoadBalancingTestSenderAO other) {
		this.counter = 0;
		this.startSyncAfter = other.getStartSyncAfter();
		this.stopSyncAfter = other.getStopAfter();
		this.pauseBetween = other.getPauseBetween();
		this.firstRound = true;
	}

	@Override
	public LoadBalancingTestSenderPO<T> clone() {
		return new LoadBalancingTestSenderPO<T>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(T object, int port) {
		counter++;
		if (firstRound) {
			if (counter == startSyncAfter) {
				// Send start sync punctuation
				sendSyncPunctuation(
						((IStreamObject<? extends ITimeInterval>) object)
								.getMetadata().getStart(), port, true);
				LoadBalancingTestSenderPO.log.debug(String.format(logMessage,
						counter));
			} else if (counter == startSyncAfter + stopSyncAfter) {
				// Send stop sync punctuation
				sendSyncPunctuation(
						((IStreamObject<? extends ITimeInterval>) object)
								.getMetadata().getStart(), port, false);
				LoadBalancingTestSenderPO.log.debug(String.format(logMessage,
						counter));
				counter = 0;
				firstRound = false;
			}
		} else {
			if (counter == pauseBetween + startSyncAfter) {
				// Send start sync punctuation
				sendSyncPunctuation(
						((IStreamObject<? extends ITimeInterval>) object)
								.getMetadata().getStart(), port, true);
				LoadBalancingTestSenderPO.log.debug(String.format(logMessage,
						counter));
			} else if (counter == pauseBetween + startSyncAfter + stopSyncAfter) {
				// Send stop sync punctuation
				sendSyncPunctuation(
						((IStreamObject<? extends ITimeInterval>) object)
								.getMetadata().getStart(), port, false);
				LoadBalancingTestSenderPO.log.debug(String.format(logMessage,
						counter));
				counter = 0;
			}
		}
		
		this.transfer(object, port);
	}

	private void sendSyncPunctuation(PointInTime time, int port,
			boolean startSync) {
		LoadBalancingPunctuation startSyncPunctuation = new LoadBalancingPunctuation(
				time, startSync, !startSync);
		sendPunctuation(startSyncPunctuation, port);
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	public long getCounter() {
		return this.counter;
	}

	public boolean isFirstRound() {
		return this.firstRound;
	}

	protected void setCounter(long counter) {
		this.counter = counter;
	}

	protected void setIsFirstRound(boolean firstRound) {
		this.firstRound = firstRound;
	}
}
