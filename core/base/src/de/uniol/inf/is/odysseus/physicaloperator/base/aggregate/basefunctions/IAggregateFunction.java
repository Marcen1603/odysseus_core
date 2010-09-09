package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;

public interface IAggregateFunction<R> extends IInitializer<R>, IMerger<R>,
		IEvaluator<R> {
	public String getName();
	public int hashCode();
	public boolean equals(Object obj);

}
