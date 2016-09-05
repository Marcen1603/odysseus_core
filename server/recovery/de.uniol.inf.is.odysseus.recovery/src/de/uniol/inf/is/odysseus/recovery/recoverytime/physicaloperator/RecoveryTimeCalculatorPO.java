package de.uniol.inf.is.odysseus.recovery.recoverytime.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;
import de.uniol.inf.is.odysseus.recovery.recoverytime.ITimeIntervalTrustRecoveryTime;

/**
 * Uses the {@link IRecoveryTime} meta attribute to calculate the recovery time.
 * <br />
 * <br />
 * Recovery time is defined as the time between the first element after a
 * restart and the first trustworthy element. Recovery time is measured in
 * system time as well as in application time.
 * 
 * @author Michael Brand
 */
public class RecoveryTimeCalculatorPO<Element extends IStreamObject<? extends ITimeIntervalTrustRecoveryTime>>
		extends AbstractPipe<Element, Element> {

	/**
	 * Threshold for trust values. Values below mark "wrong" elements during
	 * recovery time.
	 */
	private static final double trustThreshold = 1;

	/**
	 * This operator sets start points of recovery time as well as end points.
	 * Which to set is determined by the state.
	 * 
	 * @author Michael Brand
	 *
	 */
	private static enum State {

		/**
		 * Operator waits for element with a trust below
		 * {@link RecoveryTimeCalculatorPO#trustThreshold} in order to set the
		 * start of the recovery time.
		 */
		waitingForStart,

		/**
		 * Operator waits for element with a trust higher
		 * {@link RecoveryTimeCalculatorPO#trustThreshold} in order to set the
		 * end of the recovery time.
		 */
		waitingForEnd;

		/**
		 * Calculates the next state of the operator based on the current state.
		 */
		public static State next(State current) {
			// Alternating states
			switch (current) {
			case waitingForStart:
				return waitingForEnd;
			case waitingForEnd:
				return waitingForStart;
			default:
				return null;
			}
		}
	}

	/**
	 * This operator sets start points of recovery time as well as end points.
	 * Which to set is determined by the state.
	 */
	private State state = State.waitingForStart;

	/**
	 * Empty constructor.
	 */
	public RecoveryTimeCalculatorPO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public RecoveryTimeCalculatorPO(RecoveryTimeCalculatorPO<Element> other) {
		super(other);
		state = other.state;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Element object, int port) {
		final long systime = System.currentTimeMillis();
		final double trust = object.getMetadata().getTrust();
		final long appTime = object.getMetadata().getStart().getMainPoint();

		if (state == State.waitingForStart && trust < trustThreshold) {
			// Recovery time starts, when an element with lower trust arrives.
			object.getMetadata().setApplicationTimeStart(appTime);
			object.getMetadata().setSystemTimeStart(systime);
			state = State.next(state);
		} else if (state == State.waitingForEnd && trust >= trustThreshold) {
			// Recovery time ends, when an element with higher trust arrives.
			object.getMetadata().setApplicationTimeEnd(appTime);
			object.getMetadata().setSystemTimeEnd(systime);
			state = State.next(state);
		}

		transfer(object);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator other) {
		return other instanceof RecoveryTimeCalculatorPO;
	}

}