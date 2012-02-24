package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class RatePartialAggregate<T> implements IPartialAggregate<T> {
	int count = 0;
	final PointInTime start;
	PointInTime end;

	public RatePartialAggregate(ITimeInterval meta) {
		this.start = meta.getStart();
		this.end = meta.getEnd();
		this.count = 1;
	}

	public RatePartialAggregate(RatePartialAggregate<T> ratePartialAggregate) {
		this.count = ratePartialAggregate.count;
		this.start = ratePartialAggregate.start;
		this.end = ratePartialAggregate.end;
	}

	public int getCount() {
		return count;
	}

	public PointInTime getStart() {
		return start;
	}

	public PointInTime getEnd() {
		return end;
	}

	public void add() {
		this.count++;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("RatePartialAggregate (")
				.append(hashCode()).append(")").append(count).append("/")
				.append(end.getMainPoint() - start.getMainPoint());
		return ret.toString();
	}

	@Override
	public RatePartialAggregate<T> clone() {
		return new RatePartialAggregate<T>(this);
	}
}
