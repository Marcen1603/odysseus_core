package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SumPartialAggregate<T> implements IPartialAggregate<T> {
	double sum = 0;

	public SumPartialAggregate(double value, double probability) {
		this.sum = value * probability;
	}

	public SumPartialAggregate(double sum) {
		this.sum = sum;
	}

	public SumPartialAggregate(SumPartialAggregate<T> sumPartialAggregate) {
		this.sum = sumPartialAggregate.sum;
	}

	public double getSum() {
		return sum;
	}

	public void add(double value, double probability) {
		sum += value * probability;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("SumPartialAggregate (")
				.append(hashCode()).append(")").append(sum);
		return ret.toString();
	}

	@Override
	public SumPartialAggregate<T> clone() {
		return new SumPartialAggregate<T>(this);
	}
}
