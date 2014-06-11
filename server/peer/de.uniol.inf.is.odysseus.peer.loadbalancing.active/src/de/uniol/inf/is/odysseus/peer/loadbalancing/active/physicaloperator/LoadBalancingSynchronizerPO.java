package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import java.util.Collection;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;

/**
 * A {@link LoadBalancingSynchronizerPO} transfers the elements only from the
 * first (earliest) port until on both ports (first and second/earliest and
 * latest) {@link LoadBalancingPunctuation}s marking the end of a load balancing
 * process received. If that happens, only elements from the second (latest)
 * port are transferred and the earliest port will be closed.
 * 
 * @author Michael Brand
 */
public class LoadBalancingSynchronizerPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {

	/**
	 * Maximum Number of Ports allowed.
	 */
	private final int MAX_NUMBER_OF_PORTS = 2;
	
	/**
	 * Minimal Number of Ports allowed (used to initialize transfer area).
	 */
	private final int MIN_NUMBER_OF_PORTS = 1;	
	
	/**
	 * The logger instance for this class.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(LoadBalancingSynchronizerPO.class);

	/**
	 * The enumeration of states for input ports based on the retrieving of
	 * {@link LoadBalancingPunctuation}s.
	 * 
	 * @author Michael Brand
	 * 
	 */
	private static enum InputPortState {

		/**
		 * Neither {@link LoadBalancingPunctuation} with
		 * {@link LoadBalancingPunctuation#marksStart()} nor with
		 * {@link LoadBalancingPunctuation#marksStop()} received yet.
		 */
		noneReceived(0) {

			@Override
			public String toString() {

				return "no markers received";

			}

		},

		/**
		 * {@link LoadBalancingPunctuation} with
		 * {@link LoadBalancingPunctuation#marksStart()} received.
		 */
		startReceived(1) {

			@Override
			public String toString() {

				return "start marker received";

			}

		},

		/**
		 * {@link LoadBalancingPunctuation} with
		 * {@link LoadBalancingPunctuation#marksStop()} received.
		 */
		stopReceived(2) {

			@Override
			public String toString() {

				return "stop marker received";

			}

		};

		/**
		 * The index of the input port state for ordering.
		 */
		private final int index;

		/**
		 * Creates a new input port state.
		 * 
		 * @param i
		 *            The index of the input port state for ordering.
		 */
		private InputPortState(int i) {

			this.index = i;

		}

		/**
		 * Gets the index of the input port state for ordering.
		 */
		public int getIndex() {

			return this.index;

		}

	};

	/**
	 * All listeners of this operator.
	 */
	private final Collection<Observer> listeners;

	/**
	 * The {@link ITransferArea} to keep order within the output stream.
	 */
	private final ITransferArea<T, T> transferArea;

	/**
	 * The {@link InputPortState}s for all input ports.
	 */
	private final Collection<IPair<Integer, InputPortState>> inputPortStates;

	/**
	 * The input port, which elements shall be transferred.
	 */
	private int transferPort;

	/**
	 * Creates a new {@link LoadBalancingSynchronizerPO}.
	 * 
	 * @param tArea
	 *            The {@link ITransferArea} to keep order within the output
	 *            stream.
	 */
	public LoadBalancingSynchronizerPO(ITransferArea<T, T> tArea) {

		super();

		this.listeners = Lists.newArrayList();
		this.transferArea = tArea;
		this.transferArea.init(this, MIN_NUMBER_OF_PORTS);
		this.inputPortStates = Lists.newArrayList();
		for (int port = 0; port < this.getSubscribedToSource().size(); port++) {

			this.inputPortStates.add(new Pair<Integer, InputPortState>(port,
					InputPortState.noneReceived));

		}
		this.transferPort = 0;

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
		this.transferArea = syncPO.transferArea.clone();
		this.inputPortStates = Lists.newArrayList(syncPO.inputPortStates);
		this.transferPort = syncPO.transferPort;

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

		Preconditions.checkArgument(this.getInputPortCount() < MAX_NUMBER_OF_PORTS,
				"The input port count of {} can not be extended!", MAX_NUMBER_OF_PORTS);

		transferArea.addNewInput(sub.getSinkInPort());
		LoadBalancingSynchronizerPO.log.debug(
				"Added new input port. Current input port count : {}",
				this.getInputPortCount());

	}

	@Override
	protected void sourceUnsubscribed(
			PhysicalSubscription<ISource<? extends T>> sub) {

		transferArea.removeInput(sub.getSinkInPort());

	}

	@Override
	protected void process_open() throws OpenFailedException {

		transferArea.init(this, getSubscribedToSource().size());

	}

	@Override
	protected void process_next(T object, int port) {

		if (port == this.transferPort) {

			this.transferArea.transfer(object);
			this.transferArea.newElement(object, port);

		}

	}

	/**
	 * Processes {@link LoadBalancingPunctuation}s.
	 * 
	 * @param punctuation
	 *            The punctuation to process.
	 * @param port
	 *            The input port of <code>punctuation</code>.
	 */
	private void processLoadBalancingPunctuation(
			LoadBalancingPunctuation punctuation, int port) {

		Preconditions
				.checkNotNull(punctuation, "Punctuation must be not null!");
		Preconditions.checkArgument(
				port >= 0 && port < this.getInputPortCount(),
				"Invalid input port: {}", port);

		final Optional<IPair<Integer, InputPortState>> optOldPortStatePair = this
				.getInputPortState(port);
		Preconditions.checkArgument(optOldPortStatePair.isPresent(),
				"Unknown port: {}", port);
		final IPair<Integer, InputPortState> oldPortStatePair = optOldPortStatePair
				.get();
		final InputPortState oldInputPortState = oldPortStatePair.getE2();
		InputPortState newInputPortState = oldInputPortState;

		// Determine new state of input port
		if (punctuation.marksStart()) {

			newInputPortState = InputPortState.startReceived;

		} else if (punctuation.marksStop()) {

			newInputPortState = InputPortState.stopReceived;

		}

		// Validate the state change
		final Object[] errorMessage = {
				"The state change delivered by the punction is invalid: {} to {}!",
				oldInputPortState, newInputPortState };
		Preconditions.checkArgument(
				newInputPortState.getIndex() >= oldInputPortState.getIndex(),
				errorMessage);
		Preconditions.checkArgument(newInputPortState.getIndex()
				- oldInputPortState.getIndex() == 1, errorMessage);

		// Save the changed state
		final IPair<Integer, InputPortState> newPortStatePair = new Pair<Integer, InputPortState>(
				port, newInputPortState);
		this.inputPortStates.remove(oldPortStatePair);
		this.inputPortStates.add(newPortStatePair);
		LoadBalancingSynchronizerPO.log.debug(
				"Set state of port {} from to {}", new Object[] { port,
						oldInputPortState, newInputPortState });

		// Check load balancing state
		if (this.areAllInputPortStates(InputPortState.stopReceived)) {

			// LB done
			final int portToRemove = this.transferPort;
			this.transferPort = (this.transferPort + 1) % MAX_NUMBER_OF_PORTS;
			this.inputPortStates.clear();
			this.inputPortStates.add(new Pair<Integer, InputPortState>(
					this.transferPort, InputPortState.noneReceived));
			this.getSubscribedToSource(portToRemove).setDone(true);
			this.fireFinishEvent(portToRemove);

		}

	}

	/**
	 * Checks, if all input ports have a given state.
	 * 
	 * @param state
	 *            The given state.
	 * @return True, if all input ports have a given state.
	 */
	private boolean areAllInputPortStates(InputPortState state) {

		for (IPair<Integer, InputPortState> pair : this.inputPortStates) {

			if (!pair.getE2().equals(state)) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Gets the {@link IPair} of input port and {@link InputPortState} for a
	 * given input port.
	 * 
	 * @param port
	 *            The given input port.
	 * @return The first pair found, having <code>port</code> as input port or
	 *         {@link Optional#absent()}, if no pair was found.
	 */
	private Optional<IPair<Integer, InputPortState>> getInputPortState(int port) {

		for (IPair<Integer, InputPortState> pair : this.inputPortStates) {

			if (pair.getE1().intValue() == port) {

				return Optional.of(pair);

			}

		}

		return Optional.absent();

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		if (punctuation instanceof LoadBalancingPunctuation) {

			this.processLoadBalancingPunctuation(
					(LoadBalancingPunctuation) punctuation, port);

		}

		this.transferArea.newElement(punctuation, port);

	}

	@Override
	protected void process_close() {

		for (int i = 0; i < getSubscribedToSource().size(); i++) {

			transferArea.done(i);

		}
	}

	@Override
	protected void process_done(int port) {

		transferArea.done(port);

	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof UnionPO)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public long getElementsStored1() {

		return transferArea.size();

	}

}