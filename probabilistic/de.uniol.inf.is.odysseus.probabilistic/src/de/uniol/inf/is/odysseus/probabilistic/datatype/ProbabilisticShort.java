package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticShort extends AbstractProbabilisticValue<Short> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -969773405115701795L;

	public ProbabilisticShort(final Short value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticShort(final Map<Short, Double> values) {
		super(values);
	}

	public ProbabilisticShort(ProbabilisticShort other) {
		super(other);
	}

	public ProbabilisticShort(final Short[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
