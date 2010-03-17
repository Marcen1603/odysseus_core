package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions;

abstract public class AbstractAggregateFunction<R> implements IAggregateFunction<R> {

	String name = "";
	
	@Override
	public String getName() {
		return name;
	}
	
	protected AbstractAggregateFunction(String name) {
		this.name = name;
	}
	
}
