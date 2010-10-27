package de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions;

public interface IAggregateFunction<R> extends IInitializer<R>, IMerger<R>,
		IEvaluator<R> {
	public String getName();
	@Override
	public int hashCode();
	@Override
	public boolean equals(Object obj);

}
