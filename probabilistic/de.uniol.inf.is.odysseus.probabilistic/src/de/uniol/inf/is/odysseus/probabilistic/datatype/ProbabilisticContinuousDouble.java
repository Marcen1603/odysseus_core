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

	public ProbabilisticContinuousDouble(final int distribution) {
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
		sb.append(this.distribution);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public ProbabilisticContinuousDouble clone() {
		return new ProbabilisticContinuousDouble(this);
	}

	public void setDistribution(final int distribution) {
		this.distribution = distribution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distribution;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (getClass() == obj.getClass()) {
			ProbabilisticContinuousDouble other = (ProbabilisticContinuousDouble) obj;
			if (distribution != other.distribution) {
				return true;
			} else {
				return false;
			}
		} else {
			if (obj.getClass() == Double.class) {
				return true;
			}
		}
		return false;
	}

}
