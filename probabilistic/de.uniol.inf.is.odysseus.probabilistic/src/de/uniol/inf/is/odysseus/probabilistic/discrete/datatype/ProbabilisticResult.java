package de.uniol.inf.is.odysseus.probabilistic.discrete.datatype;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticResult {
	private Double probability = 0.0;
	private IClone value;

	public ProbabilisticResult(Double probability, IClone value) {
		this.probability = probability;
		this.value = value;
	}

	public ProbabilisticResult(ProbabilisticResult other) {
		this.probability = other.probability;
		this.value = other.value.clone();
	}

	public Double getProbability() {
		return this.probability;
	}

	public Object getValue() {
		return this.value;
	}

	@Override
	public ProbabilisticResult clone() {
		return new ProbabilisticResult(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.value).append(" (").append(this.probability).append(")");
		return sb.toString();
	}
}
