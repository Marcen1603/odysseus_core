package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ProbabilisticDouble implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1616011665709240661L;
	private final Pair<Double, Double>[] values;

	public ProbabilisticDouble(Pair<Double, Double> value) {
		this.values = new Pair[] { value };
	}

	public ProbabilisticDouble(Pair<Double, Double>[] values) {
		this.values = values;
	}

	public ProbabilisticDouble(Double[] values, Double[] probabilities) {
		int length = Math.min(values.length, probabilities.length);
		this.values = new Pair[length];
		for (int i = 0; i < length; i++) {
			this.values[i] = new Pair<Double, Double>(values[i],
					probabilities[i]);
		}
	}

	public Pair<Double, Double>[] values() {
		return values;
	}

	public ProbabilisticDouble clone() {
		return new ProbabilisticDouble(values.clone());
	}
}
