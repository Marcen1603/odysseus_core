package de.uniol.inf.is.odysseus.trajectory.util;

public abstract class AbstractSimpleFactory<E> {
	
	private E product;
	
	public E create() {
		if(this.product == null) {
			this.product = this.internalCreate();
		}
		return this.product;
	}

	protected abstract E internalCreate();
}
