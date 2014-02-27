package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions;

abstract public class AbstractPartialAggregate<T> implements IPartialAggregate<T> {

	@Override
	public void clear() {
		
	}
	
	@Override
	abstract public IPartialAggregate<T> clone();

}
