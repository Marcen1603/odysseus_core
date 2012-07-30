package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CountPartialAggregate<T> implements IPartialAggregate<T> {
	double count = 0;

	public CountPartialAggregate(double count) {
		this.count = count;
	}

	public CountPartialAggregate(CountPartialAggregate<T> countPartialAggregate) {
		this.count = countPartialAggregate.count;
	}

	public double getCount() {
		return count;
	}

	public void add(double probability) {
		count = count + (1 - probability);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("CountPartialAggregate (")
				.append(hashCode()).append(")").append(count);
		return ret.toString();
	}

	@Override
	public CountPartialAggregate<T> clone() {
		return new CountPartialAggregate<T>(this);
	}

}
