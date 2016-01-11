package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions;

abstract public class AbstractPartialAggregate<T> implements IPartialAggregate<T> {

	private static final long serialVersionUID = -4079033367310602640L;

	private boolean dirty = false;
	
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	/**
	 * Is this object changed since last setDirty(false)
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	public void clear() {
		
	}
	
	@Override
	abstract public IPartialAggregate<T> clone();

}
