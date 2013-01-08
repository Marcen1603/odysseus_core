package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDouble extends AbstractProbabilisticValue<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3098205199340767119L;

	public ProbabilisticDouble(final Double value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticDouble(final Map<Double, Double> values) {
		super(values);
	}

	public ProbabilisticDouble(ProbabilisticDouble other) {
		super(other);
	}

	public ProbabilisticDouble(final Double[] values,
			final Double[] probabilities) {
		super(values, probabilities);
	}
}
