package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import java.util.Collection;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingSynchronizerAO;

/**
 * A {@link LoadBalancingSynchronizerPO} transfers the elements only from the
 * first (earliest) port until both ports retrieve same elements or a given
 * threshold-time elapses.
 * 
 * @author Michael Brand
 */
public class LoadBalancingSynchronizerPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(LoadBalancingSynchronizerPO.class);

	/**
	 * /** Maximum number of input ports.
	 */
	private static final int MAX_NUMBER_OF_PORTS = 2;

	/**
	 * The index of the input port reading the "old" data stream.
	 */
	private static final int old_port = 0;

	/**
	 * The index of the input port reading the "new" data stream.
	 */
	private static final int new_port = 1;

	/**
	 * The default threshold for the synchronization process.
	 */
	private static final TimeValueItem default_threshold = new TimeValueItem(
			10, TimeUnit.MINUTES);

	/**
	 * The threshold for the synchronization process.
	 */
	private TimeValueItem threshold = LoadBalancingSynchronizerPO.default_threshold;

	/**
	 * All listeners of this operator.
	 */
	private Collection<Observer> listeners;

	/**
	 * The input port, which elements shall be transferred.
	 */
	private int transferPort;

	/**
	 * The time [ms], marking the start of the synchronization.
	 */
	private long startTime;

	/**
	 * The last seen element on input port {@link #old_port}.
	 */
	private T lastSeenElementOnOldPort;

	/**
	 * The last calculated time shift [ms] between elements on the different
	 * input ports {@link #old_port} ans {@link #new_port}.
	 */
	private PointInTime lastSeenTimeShift;

	/**
	 * Marks the synchronization as finished.
	 */
	private boolean finished = false;

	/**
	 * Creates a new {@link LoadBalancingSynchronizerPO}.
	 */
	public LoadBalancingSynchronizerPO() {

		super();

		this.listeners = Lists.newArrayList();
		this.transferPort = LoadBalancingSynchronizerPO.old_port;

	}

	/**
	 * Creates a new {@link LoadBalancingSynchronizerPO}.
	 * 
	 * @param syncAO
	 *            The corresponding {@link LoadBalancingSynchronizerAO}.
	 */
	public LoadBalancingSynchronizerPO(LoadBalancingSynchronizerAO syncAO) {

		super();

		this.listeners = Lists.newArrayList();
		this.transferPort = LoadBalancingSynchronizerPO.old_port;
		this.threshold = syncAO.getThreshold();

	}

	/**
	 * Creates a new {@link LoadBalancingSynchronizerPO} as a copy of an
	 * existing one.
	 * 
	 * @param syncPO
	 *            The {@link LoadBalancingSynchronizerPO} to copy.
	 */
	public LoadBalancingSynchronizerPO(LoadBalancingSynchronizerPO<T> syncPO) {

		super(syncPO);

		this.listeners = Lists.newArrayList(syncPO.listeners);
		this.transferPort = syncPO.transferPort;
		this.threshold = syncPO.threshold;
		this.startTime = syncPO.startTime;
		this.lastSeenElementOnOldPort = syncPO.lastSeenElementOnOldPort;
		this.lastSeenTimeShift = syncPO.lastSeenTimeShift;

	}

	@Override
	public AbstractPipe<T, T> clone() {

		return new LoadBalancingSynchronizerPO<T>(this);

	}

	/**
	 * Adds a new listener.
	 * 
	 * @param listener
	 *            The listener for this operator.
	 */
	public void addListener(Observer listener) {

		Preconditions.checkNotNull(listener,
				"Listener for load balancing synchronizer must be not null!");

		synchronized (this.listeners) {

			if (!this.listeners.contains(listener)) {

				this.listeners.add(listener);
				LoadBalancingSynchronizerPO.log.debug("Added listener: {}",
						listener);

			}

		}

	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeListener(Observer listener) {

		synchronized (this.listeners) {

			this.listeners.remove(listener);
			LoadBalancingSynchronizerPO.log.debug("Removed listener: {}",
					listener);

		}

	}

	/**
	 * Notifies the listeners, that the synchronization is finished.
	 * 
	 * @param inputPortToRemove
	 *            The old input port to remove.
	 */
	private final void fireFinishEvent(int inputPortToRemove) {

		Preconditions.checkArgument(inputPortToRemove >= 0
				&& inputPortToRemove < this.getInputPortCount(),
				inputPortToRemove + " is not a valid input port!");

		synchronized (listeners) {

			for (Observer listener : this.listeners) {

				listener.update(null, inputPortToRemove);

			}
		}

	}

	@Override
	public OutputMode getOutputMode() {

		return OutputMode.INPUT;

	}

	@Override
	protected void newSourceSubscribed(
			PhysicalSubscription<ISource<? extends T>> sub) {

		Preconditions
				.checkArgument(
						this.getInputPortCount() > LoadBalancingSynchronizerPO.MAX_NUMBER_OF_PORTS,
						"Invalid amount of input ports: {}",
						LoadBalancingSynchronizerPO.MAX_NUMBER_OF_PORTS);

		this.startTime = System.currentTimeMillis();
		this.finished = false;

	}

	/**
	 * Finishes the synchronization by doing the following steps: <br />
	 * - switch {@link #transferPort} - call
	 * {@link PhysicalSubscription#setDone(boolean)} - call
	 * {@link #fireFinishEvent(int)}
	 */
	private void finishSynchroization() {

		final long duration = System.currentTimeMillis() - this.startTime;
		final int portToRemove = LoadBalancingSynchronizerPO.old_port;
		this.transferPort = LoadBalancingSynchronizerPO.new_port;
		this.getSubscribedToSource(portToRemove).setDone(true);
		this.fireFinishEvent(portToRemove);
		this.finished = true;
		LoadBalancingSynchronizerPO.log.info(
				"Synchronization done. Duration: {} milliseconds", duration);

	}

	@SuppressWarnings("unchecked")
	// cloning of object
	@Override
	protected void process_next(T object, int port) {

		if (port == this.transferPort) {

			this.transfer(object);
			LoadBalancingSynchronizerPO.log.debug(
					"Transfered {} from input port {}", object, port);

		}

		if (this.finished) {

			return;

		} else if (port == LoadBalancingSynchronizerPO.old_port) {

			this.lastSeenElementOnOldPort = (T) object.clone();

		} else if (port == LoadBalancingSynchronizerPO.new_port) {

			if (this.lastSeenElementOnOldPort == null) {

				LoadBalancingSynchronizerPO.log
						.warn("Got element on port {} without seeing any elements on port {}",
								port, LoadBalancingSynchronizerPO.old_port);

			} else if (object.equals(this.lastSeenElementOnOldPort) && object.getMetadata().equals(((T) object).getMetadata())) {

				// Ideal case
				// synchronized
				this.finishSynchroization();

			} else {

				final PointInTime oldTS = this.lastSeenElementOnOldPort
						.getMetadata().getStart();
				final PointInTime newTS = object.getMetadata().getStart();
				final PointInTime currentTimeShift = newTS.minus(oldTS);

				if (currentTimeShift.getMainPoint() < 0) {

					// new data stream got ahead
					LoadBalancingSynchronizerPO.log
							.warn("Data stream on port {} got ahead of data stream on port {}",
									port, LoadBalancingSynchronizerPO.old_port);
					this.finishSynchroization();

				} else { // currentTimeShift.getMainPoint() >= 0

					if (currentTimeShift.getMainPoint() > this.lastSeenTimeShift
							.getMainPoint()) {

						LoadBalancingSynchronizerPO.log
								.warn("Time shift between data streams on port {} and {} rises",
										LoadBalancingSynchronizerPO.old_port,
										port);

						// update elapsed time
						final long timeElapsed = this.threshold.getUnit()
								.convert(
										System.currentTimeMillis()
												- this.startTime,
										TimeUnit.MILLISECONDS);
						if (timeElapsed >= this.threshold.getTime()) {

							// threshold time elapsed
							LoadBalancingSynchronizerPO.log.warn(
									"Threshold of {} reached", this.threshold);
							this.finishSynchroization();

						}

					}

					this.lastSeenTimeShift = currentTimeShift;

				}

			}

		} else {

			LoadBalancingSynchronizerPO.log.error("Invalid input port: {}",
					port);

		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		this.sendPunctuation(punctuation);

	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof LoadBalancingSynchronizerPO)) {

			return false;

		} else {

			return true;

		}

	}

}