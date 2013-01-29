package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticInteger extends AbstractProbabilisticValue<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126430661442462253L;

	public ProbabilisticInteger(final Integer value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticInteger(final Map<Integer, Double> values) {
		super(values);
	}

	public ProbabilisticInteger(ProbabilisticInteger other) {
		super(other);
	}

	public ProbabilisticInteger(final Integer[] values,
			final Double[] probabilities) {
		super(values, probabilities);
	}
}
