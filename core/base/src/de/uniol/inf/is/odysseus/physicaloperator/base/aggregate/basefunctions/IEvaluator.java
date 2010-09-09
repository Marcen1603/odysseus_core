package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;


public interface IEvaluator<R> {
	public R evaluate(IPartialAggregate<R> p);
}
