package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;


public interface IMerger<R> {
	IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew);
}
