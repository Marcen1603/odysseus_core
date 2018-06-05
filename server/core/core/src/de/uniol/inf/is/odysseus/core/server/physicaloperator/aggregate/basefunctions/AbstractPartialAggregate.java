package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions;

abstract public class AbstractPartialAggregate<T> implements IPartialAggregate<T> {

	private static final long serialVersionUID = -4079033367310602640L;
	
	@Override
	public void clear() {
		
	}
	
	@Override
	abstract public IPartialAggregate<T> clone();

}
