package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;


public interface Evaluator<R> {
	public R evaluate(PartialAggregate<R> p);
}
