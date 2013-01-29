package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticFloat extends AbstractProbabilisticValue<Float> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9174297465866495711L;

	public ProbabilisticFloat(final Float value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticFloat(final Map<Float, Double> values) {
		super(values);
	}

	public ProbabilisticFloat(ProbabilisticFloat other) {
		super(other);
	}

	public ProbabilisticFloat(final Float[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
