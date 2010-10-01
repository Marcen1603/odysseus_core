package de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions;


public interface IInitializer<R> {
	IPartialAggregate<R> init(R in);
}
