package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;

/**
 * A {@link LoadBalancingPunctuation} will be send to mark, that the current
 * stream is part of an active load balancing process. <br />
 * This means, that there are more than one stream in parallel, one being the
 * original and the others being in a synchronize process with the first one. <br />
 * A {@link LoadBalancingPunctuation} can either mark the start or the end of a
 * load balancing process. None of those markers must be set for all
 * punctuations.
 * 
 * @author Michael
 * 
 */
public class LoadBalancingPunctuation extends AbstractPunctuation {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * True, if the punctuation marks the start of the load balancing process.
	 */
	private final boolean start;

	/**
	 * True, if the punctuation marks the stop of the load balancing process.
	 */
	private final boolean stop;

	/**
	 * Creates a new {@link LoadBalancingPunctuation} with given information
	 * about marking start or end point of load balancing.
	 * 
	 * @param time
	 *            The current time.
	 * @param start
	 *            True, if the punctuation marks the start of the load balancing
	 *            process. If true, <code>stop</code> must be false.
	 * @param stop
	 *            True, if the punctuation marks the stop of the load balancing
	 *            process. If true, <code>start</code> must be false.
	 */
	public LoadBalancingPunctuation(long time, boolean start, boolean stop) {

		super(time);

		Preconditions
				.checkArgument(
						!(start && stop),
						"A LoadBalancingPunctuation can not mark the start and end of a loadbalancing process at the same!");

		this.start = start;
		this.stop = stop;

	}

	/**
	 * Creates a new {@link LoadBalancingPunctuation} marking neither start nor
	 * stop point of load balancing.
	 * 
	 * @param time
	 *            The current time.
	 */
	public LoadBalancingPunctuation(long time) {

		this(time, false, false);

	}

	/**
	 * Creates a new {@link LoadBalancingPunctuation} with given information
	 * about marking start or end point of load balancing.
	 * 
	 * @param pit
	 *            The current {@link PointInTime}.
	 * @param start
	 *            True, if the punctuation marks the start of the load balancing
	 *            process. If true, <code>stop</code> must be false.
	 * @param stop
	 *            True, if the punctuation marks the stop of the load balancing
	 *            process. If true, <code>start</code> must be false.
	 */
	public LoadBalancingPunctuation(PointInTime pit, boolean start, boolean stop) {

		super(pit);

		Preconditions
				.checkArgument(
						!(start && stop),
						"A LoadBalancingPunctuation can not mark the start and end of a loadbalancing process at the same!");

		this.start = start;
		this.stop = stop;

	}

	/**
	 * Creates a new {@link LoadBalancingPunctuation} marking neither start nor
	 * stop point of load balancing.
	 * 
	 * @param pit
	 *            The current {@link PointInTime}.
	 */
	public LoadBalancingPunctuation(PointInTime pit) {

		this(pit, false, false);

	}

	/**
	 * Creates a new {@link LoadBalancingPunctuation} as a copy of an existing
	 * one.
	 * 
	 * @param punctuation
	 *            The punctuation to copy.
	 */
	public LoadBalancingPunctuation(LoadBalancingPunctuation punctuation) {

		super(punctuation);

		this.start = punctuation.start;
		this.stop = punctuation.stop;

	}

	@Override
	public AbstractPunctuation clone() {

		return new LoadBalancingPunctuation(this);

	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {

		return new LoadBalancingPunctuation(newTime);

	}

	/**
	 * Checks, if the punctuation marks the start.
	 * 
	 * @return True, if the punctuation marks the start of the load balancing
	 *         process.
	 */
	public boolean marksStart() {

		return this.start;

	}

	/**
	 * Checks, if the punctuation marks the stop.
	 * 
	 * @return True, if the punctuation marks the stop of the load balancing
	 *         process.
	 */
	public boolean marksStop() {

		return this.stop;

	}

	@Override
	public String toString() {

		StringBuffer strBuffer = new StringBuffer(this.getClass()
				.getSimpleName() + " (" + hashCode() + ") | " + this.getTime());
		if (this.start)
			strBuffer.append(" | Load balancing start");
		else if (this.stop)
			strBuffer.append(" | Load balancing stop");

		return strBuffer.toString();

	}

}