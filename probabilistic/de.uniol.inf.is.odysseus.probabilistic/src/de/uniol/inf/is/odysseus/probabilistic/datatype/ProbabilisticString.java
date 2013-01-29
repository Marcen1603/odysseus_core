package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticString extends AbstractProbabilisticValue<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145484449655555135L;

	public ProbabilisticString(final String value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticString(final Map<String, Double> values) {
		super(values);
	}

	public ProbabilisticString(ProbabilisticString other) {
		super(other);
	}

	public ProbabilisticString(final String[] values,
			final Double[] probabilities) {
		super(values, probabilities);
	}
}
