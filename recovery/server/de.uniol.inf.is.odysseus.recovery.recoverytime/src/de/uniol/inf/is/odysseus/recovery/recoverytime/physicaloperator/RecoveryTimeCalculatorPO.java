package de.uniol.inf.is.odysseus.recovery.recoverytime.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;
import de.uniol.inf.is.odysseus.trust.ITrust;

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
public class RecoveryTimeCalculatorPO<Element extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<Element, IStreamObject<?>>  {

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

	// The current known values to write
	private long appTimeStart = -1;
	private long appTimeEnd = -1;
	private long sysTimeStart = -1;
	private long sysTimeEnd = -1;

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
		this.appTimeStart = other.appTimeStart;
		this.appTimeEnd = other.appTimeEnd;
		this.sysTimeStart = other.sysTimeStart;
		this.sysTimeEnd = other.sysTimeEnd;
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
		final long sysTime = System.currentTimeMillis();
		final double trust = ((ITrust) object.getMetadata()).getTrust();
		final long appTime = ((ITimeInterval) object.getMetadata()).getStart().getMainPoint();

		if (state == State.waitingForStart && trust < trustThreshold) {
			// Recovery time starts, when an element with lower trust arrives.
			appTimeStart = appTime;
			sysTimeStart = sysTime;
			appTimeEnd = -1;
			sysTimeEnd = -1;
			state = State.next(state);
		} else if (state == State.waitingForEnd && trust >= trustThreshold) {
			// Recovery time ends, when an element with higher trust arrives.
			appTimeEnd = appTime;
			sysTimeEnd = sysTime;
			state = State.next(state);
		}

		@SuppressWarnings("unchecked")
		Element copy = (Element) object.clone();
		((IRecoveryTime) copy.getMetadata()).setApplicationTimeStart(appTimeStart);
		((IRecoveryTime) copy.getMetadata()).setApplicationTimeEnd(appTimeEnd);
		((IRecoveryTime) copy.getMetadata()).setSystemTimeStart(sysTimeStart);
		((IRecoveryTime) copy.getMetadata()).setSystemTimeEnd(sysTimeEnd);
		transfer(copy);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator other) {
		return other instanceof RecoveryTimeCalculatorPO;
	}

}