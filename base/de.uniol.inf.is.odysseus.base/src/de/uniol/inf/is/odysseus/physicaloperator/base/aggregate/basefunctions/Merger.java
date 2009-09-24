package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;


public interface Merger<R> {
	PartialAggregate<R> merge(PartialAggregate<R> p, R toMerge, boolean createNew);
}
