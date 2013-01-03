package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticContinuousDouble implements Serializable, IClone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 537104992550497486L;
	private int distribution;

	public ProbabilisticContinuousDouble(int distribution) {
		this.distribution = distribution;
	}

	public ProbabilisticContinuousDouble(
			final ProbabilisticContinuousDouble probabilisticContinuousDouble) {
		this.distribution = probabilisticContinuousDouble.distribution;
	}

	public int getDistribution() {
		return this.distribution;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(distribution);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public ProbabilisticContinuousDouble clone() {
		return new ProbabilisticContinuousDouble(this);
	}

	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}
}
