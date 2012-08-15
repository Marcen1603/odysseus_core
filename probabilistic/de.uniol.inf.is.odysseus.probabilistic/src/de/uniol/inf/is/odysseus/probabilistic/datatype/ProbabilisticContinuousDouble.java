package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

public class ProbabilisticContinuousDouble implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5858308006884394418L;
	private final double mean;
	private final double sigma;

	public ProbabilisticContinuousDouble(double mean, double sigma) {
		this.mean = mean;
		this.sigma = sigma;
	}

	public double sigma() {
		return this.sigma;
	}

	public double mean() {
		return this.mean;
	}

	@Override
	public ProbabilisticContinuousDouble clone() {
		return new ProbabilisticContinuousDouble(this.mean, this.sigma);
	}
}
