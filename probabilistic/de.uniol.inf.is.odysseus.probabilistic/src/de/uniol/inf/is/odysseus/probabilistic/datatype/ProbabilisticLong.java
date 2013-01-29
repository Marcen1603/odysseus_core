package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticLong extends AbstractProbabilisticValue<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8340232282600212118L;

	public ProbabilisticLong(final Long value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticLong(final Map<Long, Double> values) {
		super(values);
	}

	public ProbabilisticLong(ProbabilisticLong other) {
		super(other);
	}

	public ProbabilisticLong(final Long[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
