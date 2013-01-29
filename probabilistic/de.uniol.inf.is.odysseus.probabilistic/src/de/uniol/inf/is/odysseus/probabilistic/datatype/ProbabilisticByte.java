package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.util.Map;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticByte extends AbstractProbabilisticValue<Byte> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7352353470010419001L;

	public ProbabilisticByte(final Byte value, Double probability) {
		super(value, probability);
	}

	public ProbabilisticByte(final Map<Byte, Double> values) {
		super(values);
	}

	public ProbabilisticByte(ProbabilisticByte other) {
		super(other);
	}

	public ProbabilisticByte(final Byte[] values, final Double[] probabilities) {
		super(values, probabilities);
	}
}
