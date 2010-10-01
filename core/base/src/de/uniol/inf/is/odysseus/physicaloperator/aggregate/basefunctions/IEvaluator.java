package de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions;


public interface IEvaluator<R> {
	public R evaluate(IPartialAggregate<R> p);
}
