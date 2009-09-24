package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;


public interface Initializer<R> {
	PartialAggregate<R> init(R in);
}
